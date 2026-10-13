(ns sns.builtin.wasm
  "Adapter for `:wasm` plugins: runs a WASI command module on GraalWASM with the
   JSON request on stdin and reads a JSON view-model from stdout. See
   `sns.builtin.plugin-io`."
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
  ;; Shared, so modules compile once. Delayed so no Truffle state is baked into
  ;; the native image heap.
  (delay (.. (Engine/newBuilder (into-array String ["wasm"]))
             (option "engine.WarnInterpreterOnly" "false")
             (build))))

(defn- context
  "A fresh context per call, since WASI command modules run once and exit."
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
  "Run `src`'s `_start` on `req`. `proc_exit` surfaces as an exception; a
   non-zero status throws with stderr in the message, since that's all the UI
   shows."
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
  "Build a `Generator`/`Action` from a `:wasm` plugin config entry. The plugin's
   id is prepended to `:args` as the program name."
  [{:keys [id] {:keys [module args dirs]} :wasm :as plugin}]
  (let [src   (.. (Source/newBuilder "wasm" (File. ^String module))
                  (name (clojure.core/name id))
                  (build))
        argv  (into [(name id)] args)
        colls (io/collections plugin)]
    (reify
      p/Generator
      (loot-spec [_] {})
      (generate [_ ctx]
        (run id src argv dirs (io/with-state ctx colls {:inputs (:inputs ctx)})))
      p/Action
      (handle-action [_ ctx action params]
        (run id src argv dirs (io/with-state ctx colls (io/action-request ctx action params)))))))
