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
   drive stateful follow-ups (e.g. levelling up) entirely in its own language.

   State round-trips the same way. An external plugin never reaches the store —
   the engine reads the collections it declared and sends them as `state`, and
   the `mutations` it returns are applied on this side. So a plugin that wants
   persistent state writes JSON, not EDN, and never learns which backend is
   configured."
  (:require
    [jsonista.core :as j]
    [sns.sdk.protocols :as p]
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

(defn- ->mutations
  "Every JSON object key arrives keywordised. A collection name is a keyword on
   this side, but the key of an entry within it is a plain string, so those go
   back — otherwise a row written by a plugin would never match one written by
   the manual-state editor."
  [mutations]
  (update-vals mutations #(update-keys % name)))

(defn ->view-model
  "Convert a friendly (un-namespaced) map into a view-model. Actions are wired
   back to plugin `id`, and declared `mutations` become the engine's to apply."
  [id {:keys [title subtitle sections actions mutations]}]
  (cond-> {:loot/title title}
          subtitle (assoc :loot/subtitle subtitle)
          (seq sections) (assoc :loot/sections (mapv ->section sections))
          (seq actions) (assoc :loot/actions (mapv #(->action id %) actions))
          (seq mutations) (assoc :store/mutations (->mutations mutations))))

(defn spec-storage
  "The `:store/...` keys an external plugin's config contributes to its
   loot-spec. Declared in config rather than by the plugin, exactly as `:inputs`
   and `:utility?` are, since it has no loot-spec of its own."
  [{:store/keys [collections manual]}]
  (cond-> {}
          (seq collections) (assoc :store/collections (vec collections))
          manual (assoc :store/manual manual)))

(defn collections
  "The collections to read and ship with each request, or nil when the plugin
   declared none. `:store/manual` implies the one named after the plugin's `:id`,
   matching what the loot-spec defaults to."
  [{:keys [id] :store/keys [collections manual]}]
  (or (not-empty (vec collections)) (when manual [id])))

(defn with-state
  "`request` plus the declared collections, read out of `ctx`'s store. Absent
   when the plugin declared none, so a stateless plugin costs no reads."
  [ctx colls request]
  (cond-> request
          (seq colls)
          (assoc :state (into {} (map (juxt identity #(p/read-collection (:store ctx) %))) colls))))

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
