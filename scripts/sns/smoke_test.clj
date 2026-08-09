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
    (java.time Duration)))

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

(defn- run-plugin-suite! [base-url ffi-available?]
  (expect-200! (request base-url :get "/api/capabilities" nil) "capabilities")
  (exercise-generate! base-url :divine-dust)
  (exercise-generate! base-url :uniques)
  (exercise-generate! base-url :rings)
  (exercise-generate! base-url :weather {:chill-factor "1.5"})
  (exercise-statefully! base-url :relics)
  (if ffi-available?
    (exercise-statefully! base-url :ffi-loot)
    (println "  (skipping :ffi-loot - no shared library built on this runner)")))

(defn- run-backend! [{:keys [bin-path lib-path port storage-backend] :as opts}]
  (println "=== backend:" storage-backend "===")
  (let [base-url (str "http://127.0.0.1:" port)
        state-dir (when (= :file storage-backend) (temp-dir! "sns-smoke-state"))
        config-path (write-config! (assoc opts :state-dir state-dir))
        proc (start-server! bin-path config-path)]
    (try
      (wait-for-ready! base-url proc)
      (run-plugin-suite! base-url (some? lib-path))
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
      (run-backend! {:bin-path bin-path :lib-path lib-path :port 18090 :storage-backend :memory}))
    (println "Smoke test passed.")
    (System/exit 0)
    (catch Exception e
      (println "SMOKE TEST FAILED:" (ex-message e))
      (let [data (ex-data e)]
        (when (seq data) (prn data)))
      (System/exit 1))))
