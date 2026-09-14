(ns sns.builtin.cli
  "Adapter for `:cli` plugins: shell out to an external command, writing the
   request context as JSON to stdin and reading a JSON view-model from stdout.
   Lets DMs write loot generators in any language. The request/output plumbing
   lives in `sns.builtin.plugin-io`, shared with the `:ffi` and `:wasm` adapters.

   Generation writes `{:inputs}` to stdin; an action writes
   `{:action :params :view-model}` — the presence of `action` tells the script
   which it is."
  (:require
    [clojure.java.shell :as shell]
    [clojure.string :as str]
    [sns.builtin.plugin-io :as io]
    [sns.sdk.protocols :as p]))

(defn- run
  "Run `command` with `ctx` written as JSON on stdin, returning its stdout parsed
   as a view-model. A non-zero exit is treated as an error,
   carrying the command's stderr in the message so the UI (which shows only the
   message) explains what the plugin actually objected to."
  [id command ctx]
  (let [{:keys [exit out err]} (apply shell/sh (concat command [:in (io/encode-request ctx)]))]
    (when-not (zero? exit)
      (throw (ex-info (cond-> "CLI plugin failed"
                              (not (str/blank? err)) (str ": " (str/trim err)))
                      {:id id :exit exit :err err})))
    (io/read-output id out)))

(defn generator
  "Build a `LootGenerator`/`LootAction` from a `:cli` plugin config entry, running
   its `:cli` `:command` (a vector of program + args). Having no loot-spec of its
   own, it is described entirely by its `:generator` config: the collections that
   declares are read and sent as `state`, and the `mutations` the script returns
   are applied by the engine."
  [{:keys [id] {:keys [command]} :cli :as plugin}]
  (let [colls (io/collections plugin)]
    (reify
      p/LootGenerator
      (loot-spec [_] {})
      (generate [_ ctx]
        (run id command (io/with-state ctx colls {:inputs (:inputs ctx)})))
      p/LootAction
      (handle-action [_ ctx action params]
        (run id command (io/with-state ctx colls (io/action-request ctx action params)))))))
