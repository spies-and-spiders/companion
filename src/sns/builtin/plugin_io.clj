(ns sns.builtin.plugin-io
  "Request and output plumbing shared by the `:cli`, `:ffi` and `:wasm` adapters,
   which exchange the view-model as JSON (see `sns.sdk.schema/plugin-request`).

   A returned action becomes a `:loot/action` event that re-invokes the same
   plugin with an `action`/`params` request, carrying the on-screen
   `view-model`. Plugins never touch the store: they receive their declared
   collections as `state`, and return `store/mutations`."
  (:require
    [jsonista.core :as j]
    [sns.sdk.protocols :as p]
    [sns.sdk.randoms :as randoms]
    [sns.sdk.schema :as schema]
    [sns.sdk.vars :as vars]))

(def ^:private mapper j/keyword-keys-object-mapper)

(defn- ->action
  "Build the event routing an author's action back to plugin `id`, which only
   config knows."
  [id {:keys [label action params inputs]}]
  (cond-> {:action/label label
           :action/event [:loot/action {:id     id
                                        :action (keyword action)
                                        :params (or params {})}]}
          (seq inputs) (assoc :action/inputs inputs)))

(defn- ->mutations
  "Un-keywordise row keys, so they match the manual-state editor's."
  [mutations]
  (update-vals mutations #(update-keys % name)))

(defn collections
  "The collections to send with each request, or nil. `:store/manual` implies
   one named after the plugin's `:id`."
  [{:keys [id] {:store/keys [collections manual]} :generator}]
  (or (not-empty (vec collections)) (when manual [id])))

(defn with-state
  "`request` plus the declared collections, read from `ctx`'s store."
  [ctx colls request]
  (cond-> request
          (seq colls)
          (assoc :state (into {} (map (juxt identity #(p/read-collection (:store ctx) %))) colls))))

(defn action-request
  "The request body for an action call."
  [ctx action params]
  (cond-> {:action action
           :params params}
          (seq (:inputs ctx)) (assoc :inputs (:inputs ctx))
          (:view-model ctx) (assoc :view-model (:view-model ctx))))

(defn encode-request [ctx]
  (j/write-value-as-string ctx))

(defn read-output
  "Parse, resolve and validate a plugin's JSON output, then map its actions and
   mutations to the engine's shapes. Vars are resolved here because validation
   precedes the engine's own resolution."
  [id json]
  (let [out (->> (j/read-value json mapper)
                 (schema/decode ::schema/plugin-output)
                 (vars/resolve-view-model randoms/*rng*)
                 (schema/assert! ::schema/plugin-output))]
    (cond-> out
            (seq (:loot/actions out))    (update :loot/actions #(mapv (partial ->action id) %))
            (seq (:store/mutations out)) (update :store/mutations ->mutations))))
