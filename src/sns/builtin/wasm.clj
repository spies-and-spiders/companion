(ns sns.builtin.wasm
  "Adapter for `:wasm` plugins: run a WASI command module on GraalWASM, writing
   the request context as JSON to its stdin and reading a JSON view-model from
   its stdout.

   The JSON is the view-model, shared with `:cli`/`:ffi` — see
   `sns.builtin.plugin-io`.

   Where `:jar` gives the cleanest integration but only on the JVM, this gives
   in-process, sandboxed, cross-language plugins that work in the native image
   too: no subprocess (`:cli`), no ABI hand-rolling or memory ownership rules
   (`:ffi`). The module sees only the directories `:dirs` maps for it.

   Native image needs the GraalWASM language on the image classpath; it is
   picked up automatically."
  (:require
    [clojure.string :as str]
    [sns.builtin.plugin-io :as io]
    [sns.sdk.protocols :as p])
  (:import
    (java.io ByteArrayInputStream ByteArrayOutputStream File)
    (java.nio.charset StandardCharsets)
    (org.graalvm.polyglot Context Engine PolyglotException Source)
    (org.graalvm.polyglot.io IOAccess)))

(defonce ^:private engine
  ;; One engine shared by every :wasm plugin, so a module is parsed and compiled
  ;; once and reused across calls. Deref'd on first use, never at native-image
  ;; build time (no Truffle state baked into the image heap).
  (delay (.. (Engine/newBuilder (into-array String ["wasm"]))
             (option "engine.WarnInterpreterOnly" "false")
             (build))))

(defn- context
  "A fresh context for one call. WASI command modules run once and exit, so each
   call gets its own; the engine behind them keeps the compiled code."
  ^Context [args dirs in out err]
  (let [b (doto (Context/newBuilder (into-array String ["wasm"]))
            (.engine @engine)
            (.option "wasm.Builtins" "wasi_snapshot_preview1")
            (.arguments "wasm" (into-array String args))
            (.in in)
            (.out out)
            (.err err))]
    (when (seq dirs)
      (.allowIO b IOAccess/ALL)
      (.option b "wasm.WasiMapDirs"
               (str/join "," (map (fn [[guest host]] (str (name guest) "::" host)) dirs))))
    (.build b)))

(defn- run
  "Instantiate `src` and call its `_start` with `req` on stdin, returning its
   stdout parsed as a view-model. A WASI module signals completion
   by calling `proc_exit`, which surfaces as an exit exception; a non-zero status
   is treated as an error, carrying the module's stderr in the message so the UI
   explains what the plugin actually objected to."
  [id ^Source src args dirs req]
  (let [out (ByteArrayOutputStream.)
        err (ByteArrayOutputStream.)
        ctx (context args dirs (ByteArrayInputStream. (.getBytes ^String (io/encode-request req) StandardCharsets/UTF_8)) out err)]
    (try
      (try
        (.execute (-> (.eval ctx src)
                      (.newInstance (object-array 0))
                      (.getMember "exports")
                      (.getMember "_start"))
                  (object-array 0))
        (catch PolyglotException e
          (when-not (and (.isExit e) (zero? (.getExitStatus e)))
            (let [stderr (str/trim (str err))]
              (throw (ex-info (cond-> "WASM plugin failed"
                                      (seq stderr) (str ": " stderr))
                              {:id id :exit (when (.isExit e) (.getExitStatus e)) :err stderr}
                              e))))))
      (io/read-output id (str out))
      (finally (.close ctx true)))))

(defn generator
  "Build a `LootGenerator`/`LootAction` from a `:wasm` plugin config entry,
   running the WASI module at `:module`. `:args` are the program arguments (the
   module's own name is prepended), and `:dirs` maps guest paths the module may
   read to host directories — a module with no `:dirs` gets no filesystem at all.
   `:utility?`, `:inputs` and `:store/...` mean exactly what they do for `:cli`."
  [{:keys [id label utility? history inputs] {:keys [module args dirs]} :wasm :as plugin}]
  (let [src   (.. (Source/newBuilder "wasm" (File. ^String module))
                  (name (clojure.core/name id))
                  (build))
        argv  (into [(name id)] args)
        spec  (merge (cond-> {:id id :label (or label (name id))}
                             utility? (assoc :utility? true)
                             history (assoc :history history)
                             (seq inputs) (assoc :inputs (vec inputs)))
                     (io/spec-storage plugin))
        colls (io/collections plugin)]
    (reify
      p/LootGenerator
      (loot-spec [_] spec)
      (generate [_ ctx]
        (run id src argv dirs (io/with-state ctx colls {:inputs (:inputs ctx)})))
      p/LootAction
      (handle-action [_ ctx action params]
        (run id src argv dirs (io/with-state ctx colls (io/action-request ctx action params)))))))
