(ns sns.sdk.protocols
  "The extension points third-party plugins implement. Kept dependency-light so
   external JAR plugins can depend on this module without pulling in the app."
  (:import
    (java.util HashMap)
    (sns.sdk Models$Action Models$Field Models$Item
             Models$LootSpec Models$Section Models$ViewModel)))

(defprotocol LootGenerator
  "A loot type. Implementations are resolved from config by the registry."
  (loot-spec [this]
    "Static, data-only description of this loot type. Conforms to
     `sns.sdk.schema/loot-spec`, e.g.
     {:id :relics :label \"Relic\" :stateful? true
      :inputs [{:id :character :label \"Character\" :type :enum :options [...]}]}")
  (generate [this ctx]
    "Produce loot. Returns a view-model (`sns.sdk.schema/view-model`).
     `ctx` is `{:rng :store :render :inputs :config :session}` — see the engine.
     Stateful types read/write via `(:store ctx)`."))

(defprotocol LootAction
  "Optional. Stateful follow-up operations on previously generated loot
   (e.g. levelling a relic up). Surfaced to the UI via view-model `:loot/actions`."
  (handle-action [this ctx action params]
    "Apply `action` (a keyword) with `params`. Returns an updated view-model."))

(defprotocol Progression
  "How a single mod evolves. The default implementation interprets the
   upgrade-graph DSL (`sns.sdk.schema/mod`); plugins may supply bespoke logic."
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
    "Send `view-model` (`sns.sdk.schema/view-model`) to the destination; throws ex-info on failure."))

(defprotocol Store
  "Persistence abstraction. Built-in backends: a MySQL-compatible SQL server, a
   JSON file per collection, or in-memory. `coll` is a collection/table keyword,
   `id` a document key."
  (setup! [this]
    "Prepare the backend for use (e.g. create a schema, ensure a directory
     exists). Called once at startup, before any `fetch`/`query`/`put!`/
     `update!`; construction itself must stay side-effect-free.")
  (fetch [this coll id]
    "Return the document at `id`, or nil.")
  (query [this coll q]
    "Return documents in `coll` matching query map `q`.")
  (put! [this coll id doc]
    "Insert or replace the document at `id`. Returns `doc`.")
  (update! [this coll id f]
    "Atomically apply `f` to the document at `id`. Returns the new document."))

(defn- loot-id
  "The loot-type id a generator declares, used to route a view-model's actions
   back to it. `nil` when `x` is not also a `LootGenerator`."
  [x]
  (when (instance? sns.sdk.LootGenerator x)
    (keyword (.id (.lootSpec ^sns.sdk.LootGenerator x)))))

;; --- Models -> Clojure data ---

(defn- field->clj [^Models$Field f]
  (cond-> {:id    (keyword (.id f))
           :label (.label f)
           :type  (keyword (.type f))}
          (some? (.defaultValue f)) (assoc :default (.defaultValue f))
          (seq (.options f))        (assoc :options (vec (.options f)))))

(defn- loot-spec->clj [^Models$LootSpec ls]
  (cond-> {:id    (keyword (.id ls))
           :label (.label ls)}
          (.stateful ls)     (assoc :stateful? true)
          (.utility ls)      (assoc :utility? true)
          (seq (.inputs ls)) (assoc :inputs (mapv field->clj (.inputs ls)))))

(defn- item->clj [^Models$Item i]
  (cond-> {:item/body (.body i)}
          (.title i)          (assoc :item/title (.title i))
          (seq (.metadata i)) (assoc :item/metadata (vec (.metadata i)))))

(defn- section->clj [^Models$Section s]
  (cond-> {:section/items (mapv item->clj (.items s))}
          (.heading s) (assoc :section/heading (.heading s))))

(defn- action->clj [loot-id ^Models$Action a]
  {:action/label (.label a)
   :action/event [:loot/action {:id     loot-id
                                :action (keyword (.action a))
                                :params (into {} (.params a))}]})

(defn- view-model->clj [loot-id ^Models$ViewModel vm]
  (cond-> {:loot/title (.title vm)}
          (.subtitle vm)      (assoc :loot/subtitle (.subtitle vm))
          (seq (.sections vm)) (assoc :loot/sections (mapv section->clj (.sections vm)))
          (seq (.actions vm))  (assoc :loot/actions (mapv #(action->clj loot-id %) (.actions vm)))))

(defn- clj->item [{:item/keys [title body metadata]}]
  (Models$Item. title body metadata))

(defn- clj->section [{:section/keys [heading items]}]
  (Models$Section. heading (mapv clj->item items)))

(defn- clj->action [{:action/keys [label event]}]
  (let [{:keys [action params]} (second event)]
    (Models$Action. label (some-> action name) params)))

(defn- clj->view-model [{:loot/keys [title subtitle sections actions]}]
  (Models$ViewModel. title subtitle
                     (when sections (mapv clj->section sections))
                     (when actions (mapv clj->action actions))))

(defn- clj->java-map [m]
  (reduce-kv
    (fn [^HashMap hm k v]
      (doto hm
        (.put (name k) v)))
    (HashMap.)
    m))

(extend-type sns.sdk.LootGenerator
  LootGenerator
  (loot-spec [this] (loot-spec->clj (.lootSpec this)))
  (generate [this ctx] (->> (clj->java-map ctx)
                            (.generate this)
                            (view-model->clj (loot-id this)))))

(extend-type sns.sdk.LootAction
  LootAction
  (handle-action [this ctx action params]
    (view-model->clj (loot-id this) (.handleAction this (clj->java-map ctx) (name action) (clj->java-map params)))))

(extend-type sns.sdk.Progression
  Progression
  (current-state [this mod path] (.currentState this mod path))
  (level-options [this mod path] (.levelOptions this mod path)))

(extend-type sns.sdk.Reporter
  Reporter
  (report-label [this] (.reportLabel this))
  (report! [this view-model]
    (.report this (clj->view-model view-model))))

(extend-type sns.sdk.Store
  Store
  (setup! [this] (.setup this))
  (fetch [this coll id] (.fetch this coll id))
  (query [this coll q] (.query this coll q))
  (put! [this coll id doc] (.put this coll id doc))
  (update! [this coll id f] (.update this coll id f)))
