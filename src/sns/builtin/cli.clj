(ns sns.builtin.cli
  "Adapter for `:cli` plugins: shell out to an external command, writing the
   request context as JSON to stdin and reading a *friendly* JSON view-model from
   stdout. Lets DMs write loot generators in any language. The friendly <->
   view-model mapping and output validation live in `sns.builtin.plugin-io`,
   shared with the `:ffi` adapter.

   Generation writes `{:inputs}` to stdin; an action writes `{:action :params}` —
   the presence of `action` tells the script which it is."
  (:require
    [clojure.java.shell :as shell]
    [sns.builtin.plugin-io :as io]
    [sns.sdk.protocols :as p]))

(defn- run
  "Run `command` with `ctx` written as JSON on stdin, returning the friendly
   stdout JSON mapped to a view-model. A non-zero exit is treated as an error."
  [id command ctx]
  (let [{:keys [exit out err]} (apply shell/sh (concat command [:in (io/encode-request ctx)]))]
    (when-not (zero? exit)
      (throw (ex-info "CLI plugin failed" {:id id :exit exit :err err})))
    (io/read-output id out)))

(defn generator
  "Build a `LootGenerator`/`LootAction` that runs `command` (a vector of program
   + args). `utility?` marks a session tool rather than loot (grouped separately
   in the UI, barred from the :loot-table)."
  ([id command label] (generator id command label false))
  ([id command label utility?]
   (reify
     p/LootGenerator
     (loot-spec [_]
       (cond-> {:id id :label (or label (name id))}
               utility? (assoc :utility? true)))
     (generate [_ {:keys [inputs]}]
       (run id command {:inputs inputs}))
     p/LootAction
     (handle-action [_ _ action params]
       (run id command {:action action :params params})))))
