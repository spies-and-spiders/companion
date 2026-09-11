(ns sns.builtin.plugin-io
  "Shared request/output plumbing for external plugins (`:cli` over stdio, `:ffi`
   over a C ABI, `:wasm` over WASI stdio). All three exchange the *same* JSON as
   a `:jar` plugin exchanges objects: the namespaced view-model
   (`sns.sdk.schema/view-model`). Namespaced keys survive JSON as they are
   (`\"loot/title\"` reads back as `:loot/title`); `decode` then coerces the
   enum and keyword *values* an author writes as strings.

   Actions round-trip: a returned `action` becomes a `:loot/action` event the UI
   dispatches back to the engine, which re-invokes the same plugin with an
   `action`/`params` request (rather than `inputs`), so an external plugin can
   drive stateful follow-ups (e.g. ranking up) entirely in its own language.
   The action request also carries the current, possibly DM-edited `view-model`,
   exactly as `LootAction/handle-action` receives it — that is what carries
   `:loot/state` back, and what makes the displayed values the source of truth.

   State round-trips the same way. An external plugin never reaches the store —
   the engine reads the collections it declared and sends them as `state`, and
   the `store/mutations` it returns are applied on this side. So a plugin that
   wants persistent state writes JSON, not EDN, and never learns which backend is
   configured."
  (:require
    [jsonista.core :as j]
    [sns.sdk.protocols :as p]
    [sns.sdk.randoms :as randoms]
    [sns.sdk.schema :as schema]
    [sns.sdk.vars :as vars]))

(def ^:private mapper j/keyword-keys-object-mapper)

(defn- ->action
  "Wire an author's `{:label :action :params}` to a view-model action whose event
   routes back to plugin `id` via `handle-action`. External plugins do not write
   `:action/event` themselves: the browser dispatches it as-is, and `id` is
   assigned in config — the same command can be registered under several — so the
   adapter is what knows it. The SDK's `Models.Action` gives `:jar` authors the
   same three fields."
  [id {:keys [label action params]}]
  {:action/label label
   :action/event [:loot/action {:id     id
                                :action (keyword action)
                                :params (or params {})}]})

(defn- ->mutations
  "Every JSON object key arrives keywordised. A collection name is a keyword on
   this side, but the key of an entry within it is a plain string, so those go
   back — otherwise a row written by a plugin would never match one written by
   the manual-state editor."
  [mutations]
  (update-vals mutations #(update-keys % name)))

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

(defn action-request
  "The request body for an action call. `view-model` is the result the UI had on
   screen, DM edits included; a plugin rebuilds its item from that rather than
   from a copy frozen into `params`."
  [ctx action params]
  (cond-> {:action action
           :params params}
          (:view-model ctx) (assoc :view-model (:view-model ctx))))

(defn encode-request
  "Serialise a request context (the map handed to the plugin) to JSON."
  [ctx]
  (j/write-value-as-string ctx))

(defn read-output
  "Parse a plugin's JSON output, draw any vars it declared rather than resolved
   (the engine draws them for every plugin, but validation here comes first, so
   they must be finished by then), validate it against `::plugin-output`, and map
   the two parts that are not the author's to write: actions get the event
   routing them back to plugin `id`, and mutation keys the author wrote as JSON
   strings are put back. Validating before mapping means a contract breach throws
   in the author's own keys."
  [id json]
  (let [out (->> (j/read-value json mapper)
                 (schema/decode ::schema/plugin-output)
                 (vars/resolve-view-model randoms/*rng*)
                 (schema/assert! ::schema/plugin-output))]
    (cond-> out
            (seq (:loot/actions out))    (update :loot/actions #(mapv (partial ->action id) %))
            (seq (:store/mutations out)) (update :store/mutations ->mutations))))
