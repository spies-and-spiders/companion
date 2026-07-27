(ns sns.builtin.plugin-io
  "Shared friendly <-> view-model mapping for external plugins (`:cli` over
   stdio, `:ffi` over a C ABI). The un-namespaced JSON authors work in
   ({\"title\",\"subtitle\",\"sections\",\"actions\"}; items with
   {\"title\",\"body\",\"metadata\"}) is validated against `::plugin-output` and
   mapped to the namespaced view-model here, so external authors never deal with
   `:loot/...` keys.

   Actions round-trip: a returned `action` becomes a `:loot/action` event the UI
   dispatches back to the engine, which re-invokes the same plugin with an
   `action`/`params` request (rather than `inputs`), so an external plugin can
   drive stateful follow-ups (e.g. levelling up) entirely in its own language."
  (:require
    [jsonista.core :as j]
    [sns.sdk.schema :as schema]))

(def ^:private mapper j/keyword-keys-object-mapper)

(defn- ->item [{:keys [title body metadata]}]
  (cond-> {:item/body body}
          title (assoc :item/title title)
          (seq metadata) (assoc :item/metadata (vec metadata))))

(defn- ->section [{:keys [heading items]}]
  (cond-> {:section/items (mapv ->item items)}
          heading (assoc :section/heading heading)))

(defn- ->action
  "Map a friendly `{:label :action :params}` to a namespaced view-model action
   whose event routes back to plugin `id` via `handle-action`."
  [id {:keys [label action params]}]
  {:action/label label
   :action/event [:loot/action {:id id :action (keyword action) :params (or params {})}]})

(defn ->view-model
  "Convert a friendly (un-namespaced) map into a view-model. Actions are wired
   back to plugin `id`."
  [id {:keys [title subtitle sections actions]}]
  (cond-> {:loot/title title}
          subtitle (assoc :loot/subtitle subtitle)
          (seq sections) (assoc :loot/sections (mapv ->section sections))
          (seq actions) (assoc :loot/actions (mapv #(->action id %) actions))))

(defn encode-request
  "Serialise a request context (the map handed to the plugin) to JSON."
  [ctx]
  (j/write-value-as-string ctx))

(defn read-output
  "Parse a plugin's friendly JSON output, validate it against `::plugin-output`,
   and map it to a view-model wired back to plugin `id`. A contract breach throws
   in the author's own keys, before mapping."
  [id json]
  (->view-model id (schema/assert! ::schema/plugin-output (j/read-value json mapper))))
