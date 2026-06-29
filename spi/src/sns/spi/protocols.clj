(ns sns.spi.protocols
  "The extension points third-party plugins implement. Kept dependency-light so
   external JAR plugins can depend on this module without pulling in the app.")

(defprotocol LootGenerator
  "A loot type. Implementations are resolved from config by the registry."
  (loot-spec [this]
    "Static, data-only description of this loot type. Conforms to
     `sns.spi.schema/loot-spec`, e.g.
     {:id :relics :label \"Relic\" :stateful? true
      :inputs [{:id :character :label \"Character\" :type :enum :options [...]}]}")
  (generate [this ctx]
    "Produce loot. Returns a view-model (`sns.spi.schema/view-model`).
     `ctx` is `{:rng :store :render :inputs :config :session}` — see the engine.
     Stateful types read/write via `(:store ctx)`."))

(defprotocol LootAction
  "Optional. Stateful follow-up operations on previously generated loot
   (e.g. levelling a relic up). Surfaced to the UI via view-model `:loot/actions`."
  (action-spec [this]
    "Data-only description of the actions this generator supports.")
  (handle-action [this ctx action params]
    "Apply `action` (a keyword) with `params`. Returns an updated view-model."))

(defprotocol Progression
  "How a single mod evolves. The default implementation interprets the
   upgrade-graph DSL (`sns.spi.schema/mod`); plugins may supply bespoke logic."
  (current-state [this mod path]
    "Derive `mod` at the progression described by `path`
     (a vector of `{:id ... :rolled {...}}`). Returns the final, rendered mod.")
  (level-options [this mod path]
    "Return the upgrade options available as the next step from `path`."))

(defprotocol Reporter
  "Optional. Sends a generated loot view-model to an external destination (e.g. a
   Discord webhook). Config-driven via `:reporting`, mirroring `Store`; when no
   reporter is configured the UI hides its report button."
  (report-label [this]
    "Human label for the report button, e.g. \"Send to Discord\".")
  (report! [this view-model]
    "Send `view-model` (`sns.spi.schema/view-model`) to the destination. Returns
     a result map (e.g. {:ok true}); throws ex-info on failure."))

(defprotocol Store
  "Persistence abstraction. Built-in backends: a MySQL-compatible SQL server, a
   JSON file per collection, or in-memory. `coll` is a collection/table keyword,
   `id` a document key."
  (fetch [this coll id]
    "Return the document at `id`, or nil.")
  (query [this coll q]
    "Return documents in `coll` matching query map `q`.")
  (put! [this coll id doc]
    "Insert or replace the document at `id`. Returns `doc`.")
  (update! [this coll id f]
    "Atomically apply `f` to the document at `id`. Returns the new document."))
