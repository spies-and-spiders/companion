(ns sns.builtin.cli
  "Adapter for `:cli` plugins: runs a command with the JSON request on stdin and
   reads a JSON view-model from stdout. See `sns.builtin.plugin-io`."
  (:require
    [clojure.java.shell :as shell]
    [clojure.string :as str]
    [sns.builtin.plugin-io :as io]
    [sns.sdk.protocols :as p]))

(defn- run
  "Run `command` on `ctx`. A non-zero exit throws with stderr in the message,
   since that's all the UI shows."
  [id command ctx]
  (let [{:keys [exit out err]} (apply shell/sh (concat command [:in (io/encode-request ctx)]))]
    (when-not (zero? exit)
      (throw (ex-info (cond-> "CLI plugin failed"
                              (not (str/blank? err)) (str ": " (str/trim err)))
                      {:id id :exit exit :err err})))
    (io/read-output id out)))

(defn generator
  "Build a `Generator`/`Action` from a `:cli` plugin config entry. Its loot-spec
   comes entirely from its `:generator` config."
  [{:keys [id] {:keys [command]} :cli :as plugin}]
  (let [colls (io/collections plugin)]
    (reify
      p/Generator
      (loot-spec [_] {})
      (generate [_ ctx]
        (run id command (io/with-state ctx colls {:inputs (:inputs ctx)})))
      p/Action
      (handle-action [_ ctx action params]
        (run id command (io/with-state ctx colls (io/action-request ctx action params)))))))
