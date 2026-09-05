(ns sns.smoke-test
  "Boots a built native-image binary and drives its HTTP API for real, across
   every plugin type (:builtin, :data, :cli, :ffi) and both persistent storage
   backends (:file, :memory).

   Exists because bugs like the `bigdec` string->reflection crash (fixed in
   dd6fc1a) are invisible to both the test suite (runs on the JVM, where
   reflection just works) and the `make reflection` guard (`sns.server.reflection-check`,
   which only sees reflection warnings whose call site is lexically inside an
   `sns.*` namespace — not reflection buried inside a `clojure.core` fn like
   `bigdec` itself, which only fires for certain runtime argument types). The
   only thing that reliably catches a native-image-only failure is running the
   actual compiled binary.

   Usage: clojure -M:smoke <path-to-native-binary>
   (`make smoke BIN=<path>`, or `make smoke` for the default local build path.)"
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.string :as str])
  (:import
    (java.net ConnectException URI)
    (java.net.http HttpClient HttpRequest HttpRequest$BodyPublishers HttpResponse$BodyHandlers)
    (java.nio.file Files)
    (java.nio.file.attribute FileAttribute)
    (java.time Duration)
    (java.util.zip ZipInputStream)))

(def ^:private client (HttpClient/newHttpClient))

(defn- request [base-url method path edn-body]
  (let [builder (-> (HttpRequest/newBuilder (URI/create (str base-url path)))
                    (.header "accept" "application/edn")
                    (.timeout (Duration/ofSeconds 10)))]
    (case method
      :get  (.GET builder)
      :post (-> builder
                (.header "content-type" "application/edn")
                (.POST (HttpRequest$BodyPublishers/ofString (pr-str edn-body)))))
    (let [resp (.send client (.build builder) (HttpResponse$BodyHandlers/ofString))
          body (.body resp)]
      {:status (.statusCode resp)
       :body   (when-not (str/blank? body) (edn/read-string body))})))

(defn- request-bytes
  "A raw GET, for responses that are not EDN (the export ZIP)."
  [base-url path]
  (let [req  (-> (HttpRequest/newBuilder (URI/create (str base-url path)))
                 (.timeout (Duration/ofSeconds 10))
                 (.GET)
                 (.build))
        resp (.send client req (HttpResponse$BodyHandlers/ofByteArray))]
    {:status (.statusCode resp) :body (.body resp)}))

(defn- fail!
  "Throws rather than exiting directly, so `finally` blocks up the stack (e.g.
   `run-backend!`'s server teardown) still run - System/exit tears down the JVM
   immediately, orphaning the child server process and leaving it holding the
   inherited stdout pipe open forever."
  [msg data]
  (throw (ex-info msg (or data {}))))

(defn- expect-200! [{:keys [status body] :as resp} what]
  (when (not= 200 status) (fail! (str what " returned HTTP " status) resp))
  body)

(defn- lib-extension []
  (let [os (str/lower-case (System/getProperty "os.name"))]
    (cond
      (str/includes? os "mac") "dylib"
      (str/includes? os "win") "dll"
      :else                    "so")))

(defn- build-ffi-plugin!
  "Compiles examples/ffi-plugin/loot.go to a C-ABI shared library, returning its
   path, or nil when this runner has no working C toolchain for cgo (mirrors
   the same fallback test/sns/builtin/ffi_test.clj uses on Windows)."
  [out-path]
  (println "Building FFI example plugin (Go) ->" out-path)
  ;; go build's cgo preprocessing (needed for -buildmode=c-shared) is silently
  ;; skipped when CGO_ENABLED=0, which some environments default to - without
  ;; this the failure is the deeply misleading "no Go source files".
  (let [env (assoc (into {} (System/getenv)) "CGO_ENABLED" "1")
        {:keys [exit err]} (shell/sh "go" "build" "-buildmode=c-shared"
                                     "-o" out-path "examples/ffi-plugin/loot.go"
                                     :env env)]
    (if (zero? exit)
      out-path
      (do
        (println "  skipping :ffi plugin - failed to build the example (no C toolchain?):" err)
        nil))))

(defn- temp-dir! [prefix]
  (str (Files/createTempDirectory prefix (make-array FileAttribute 0))))

(defn- python-command
  "`python3` everywhere except plain Windows setups, where it's usually just
   `python` on PATH."
  []
  (if (try (zero? (:exit (shell/sh "python3" "--version"))) (catch Exception _ false))
    "python3"
    "python"))

(defn- write-config! [{:keys [port storage-backend state-dir lib-path]}]
  (let [ffi-plugin {:type   :ffi       :id          :ffi-loot   :library lib-path
                    :symbol "generate" :free-symbol "loot_free"}
        config {:server     {:host "127.0.0.1" :port port}
                :storage    (cond-> {:backend storage-backend}
                                    (= :file storage-backend) (assoc :dir state-dir))
                :plugins    (cond-> [{:type :builtin :id :divine-dust}
                                     {:type :builtin :id :relics}
                                     {:type :builtin :id :social}
                                     {:type :data :id :uniques :source "data/uniques.edn"}
                                     {:type :data :id :rings :source "data/rings.edn"}
                                     ;; :chill-factor is unused by weather.py itself; it exists so
                                     ;; the smoke test can feed a numeric string through the
                                     ;; engine's :decimal input coercion (`->decimal` in
                                     ;; sns.server.engine) - the exact code path that broke in a
                                     ;; native image via reflection inside `bigdec` (dd6fc1a).
                                     {:type    :cli                                                       :id :weather :utility? true :label "Weather"
                                      :command [(python-command) "examples/cli-plugin/weather.py"]
                                      :inputs  [{:id :chill-factor :label "Chill Factor" :type :decimal}]}]
                                    lib-path (conj ffi-plugin))
                :loot-table (cond-> [{:id :divine-dust} {:id :relics} {:id :uniques} {:id :rings}]
                                    lib-path (conj {:id :ffi-loot}))}
        f (io/file (temp-dir! "sns-smoke-config") "config.edn")]
    (spit f (pr-str config))
    (str f)))

(defn- start-server! [bin-path config-path]
  (let [pb (doto (ProcessBuilder. [bin-path "--config" config-path])
             (.redirectErrorStream true)
             (.redirectOutput java.lang.ProcessBuilder$Redirect/INHERIT))]
    (.start pb)))

(defn- wait-for-ready! [base-url proc]
  (loop [attempts 0]
    (cond
      (not (.isAlive proc))
      (fail! "server process exited before becoming ready" {:exit-value (.exitValue proc)})

      (> attempts 100)
      (fail! "server did not become ready within 20s" {})

      :else
      (let [ready? (try (= 200 (:status (request base-url :get "/api/loot-types" nil)))
                        (catch ConnectException _ false))]
        (if ready?
          (println "Server ready at" base-url)
          (do (Thread/sleep 200) (recur (inc attempts))))))))

(defn- stop-server! [proc]
  (.destroy proc)
  (when-not (.waitFor proc 5 java.util.concurrent.TimeUnit/SECONDS)
    (.destroyForcibly proc)
    (.waitFor proc)))

(defn- exercise-generate!
  ([base-url id] (exercise-generate! base-url id {}))
  ([base-url id inputs]
   (println "  generate" id)
   (let [body (expect-200! (request base-url :post "/api/generate" {:id id :inputs inputs})
                           (str "generate " id))]
     (when-not (:loot/title body)
       (fail! (str "generate " id " returned no :loot/title") body))
     body)))

(defn- exercise-action! [base-url id action-event]
  (let [[_ params] action-event]
    (println "  action" id (:action params))
    (expect-200! (request base-url :post "/api/action" params) (str "action " id))))

(defn- exercise-statefully! [base-url id]
  (let [gen    (exercise-generate! base-url id)
        [event] (:loot/actions gen)]
    (if-not event
      (println "  (no action offered this roll for" id "- skipping action check)")
      (exercise-action! base-url id (:action/event event)))))

(defn- exercise-manual-state!
  "The `:store/manual` contract: an edited row is coerced, persisted, and read
   back by the plugin that declared it."
  [base-url]
  (println "  state :social")
  (let [written (expect-200! (request base-url :post "/api/state"
                                      {:id        :social
                                       :mutations {"Smoke" {:deception "7" :persuasion ""}}})
                             "state :social")
        row     (get (:store/state written) "Smoke")]
    (when-not (= {:deception 7M :persuasion 0 :present? true} row)
      (fail! "manual state was not coerced to the declared fields" {:row row}))
    (let [vm (exercise-generate! base-url :social)]
      (when-not (= "1/1 present" (:loot/subtitle vm))
        (fail! "the plugin did not read back its manual state" {:body vm}))
      (exercise-action! base-url :social (-> vm :loot/actions first :action/event)))
    (expect-200! (request base-url :post "/api/state" {:id :social :mutations {"Smoke" nil}})
                 "state :social retract")))

(defn- exercise-export!
  "Download the state ZIP and read its entries back. Exercises java.util.zip in
   the native image, which is the only place that can prove it survived."
  [base-url]
  (let [{:keys [status body]} (request-bytes base-url "/api/export")]
    (when (not= 200 status) (fail! (str "export returned HTTP " status) {:status status}))
    (let [names (with-open [zip (ZipInputStream. (io/input-stream body))]
                  (loop [acc #{}]
                    (if-let [entry (.getNextEntry zip)]
                      (recur (conj acc (.getName entry)))
                      acc)))]
      (when-not (contains? names "relics.edn")
        (fail! "export ZIP is missing the relics collection" {:entries names}))
      (println "  export ->" (str/join ", " (sort names))))))

(defn- exercise-browser-storage!
  "The :browser contract: the server holds nothing, so state travels in and
   mutations come back out."
  [base-url]
  (let [gen        (expect-200! (request base-url :post "/api/generate" {:id :relics}) "generate :relics")
        [id relic] (first (get-in gen [:store/mutations :relics]))]
    (when-not id (fail! "browser storage returned no mutations to apply" {:body gen}))
    (println "  generate :relics -> mutations returned")
    (let [choice (-> gen :loot/actions first :action/event second :params :choice)
          acted  (expect-200! (request base-url :post "/api/action"
                                       {:id     :relics
                                        :action :level-up
                                        :params (cond-> {:relic-id id} choice (assoc :choice choice))
                                        :state  {:relics {id relic}}})
                              "action :relics :level-up")]
      (when-not (get-in acted [:store/mutations :relics id])
        (fail! "browser storage returned no mutation for the levelled relic" {:body acted}))
      (println "  action :relics :level-up -> state travelled both ways")))
  (let [rolled (expect-200! (request base-url :post "/api/roll" {}) "roll")]
    ;; A roll wraps the view-model; its writes must still sit at the top level.
    (when (contains? (:view-model rolled) :store/mutations)
      (fail! "roll left mutations buried inside :view-model" {:body rolled}))
    (println "  roll ->" (:id rolled)
             (if (:store/mutations rolled) "with mutations at the top level" "(stateless type)"))))

(defn- exercise-browser-manual-state!
  "A manual-state edit under `:browser`: the collection travels in, the coerced
   write comes back out for the client's IndexedDB."
  [base-url]
  (let [resp (expect-200! (request base-url :post "/api/state"
                                   {:id        :social
                                    :state     {:social {"Vex" {:deception 5 :persuasion 2 :present? true}}}
                                    :mutations {"Vex" {:deception 5 :persuasion 2 :present? false}}})
                          "state :social")]
    (when-not (false? (get-in resp [:store/mutations :social "Vex" :present?]))
      (fail! "manual-state edit did not come back as a mutation" {:body resp}))
    (println "  state :social -> manual edit travelled both ways")))

(defn- run-plugin-suite! [base-url ffi-available?]
  (expect-200! (request base-url :get "/api/capabilities" nil) "capabilities")
  (exercise-generate! base-url :divine-dust)
  (exercise-generate! base-url :uniques)
  (exercise-generate! base-url :rings)
  (exercise-generate! base-url :weather {:chill-factor "1.5"})
  (exercise-statefully! base-url :relics)
  (exercise-manual-state! base-url)
  (if ffi-available?
    (exercise-statefully! base-url :ffi-loot)
    (println "  (skipping :ffi-loot - no shared library built on this runner)")))

(defn- run-suite! [base-url ffi-available? storage-backend]
  (if (= :browser storage-backend)
    (do (expect-200! (request base-url :get "/api/capabilities" nil) "capabilities")
        (exercise-browser-storage! base-url)
        (exercise-browser-manual-state! base-url))
    (do (run-plugin-suite! base-url ffi-available?)
        (exercise-export! base-url))))

(defn- run-backend! [{:keys [bin-path lib-path port storage-backend] :as opts}]
  (println "=== backend:" storage-backend "===")
  (let [base-url (str "http://127.0.0.1:" port)
        state-dir (when (= :file storage-backend) (temp-dir! "sns-smoke-state"))
        config-path (write-config! (assoc opts :state-dir state-dir))
        proc (start-server! bin-path config-path)]
    (try
      (wait-for-ready! base-url proc)
      (run-suite! base-url (some? lib-path) storage-backend)
      (finally
        (stop-server! proc)))))

(defn -main [& [bin-path]]
  (when-not bin-path
    (println "Usage: clojure -M:smoke <path-to-native-binary>")
    (System/exit 2))
  ;; Every exit path funnels through here, so `finally` blocks further down the
  ;; stack (server teardown in run-backend!) always get to run before the JVM
  ;; does - see `fail!`.
  (try
    (when-not (.canExecute (io/file bin-path))
      (fail! (str bin-path " does not exist or is not executable") nil))
    (let [target (str (io/file (temp-dir! "sns-smoke-lib") (str "libloot." (lib-extension))))
          lib-path (build-ffi-plugin! target)]
      (run-backend! {:bin-path bin-path :lib-path lib-path :port 18089 :storage-backend :file})
      (run-backend! {:bin-path bin-path :lib-path lib-path :port 18090 :storage-backend :memory})
      (run-backend! {:bin-path bin-path :lib-path lib-path :port 18091 :storage-backend :browser}))
    (println "Smoke test passed.")
    (System/exit 0)
    (catch Exception e
      (println "SMOKE TEST FAILED:" (ex-message e))
      (let [data (ex-data e)]
        (when (seq data) (prn data)))
      (System/exit 1))))
