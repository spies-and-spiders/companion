(ns sns.sdk.protocols
  "The extension points third-party plugins implement. Kept dependency-light so
   external JAR plugins can depend on this module without pulling in the app."
  (:import
    (java.util HashMap)
    (sns.sdk Models$Action Models$Field Models$Item Models$ItemVar
             Models$LootSpec Models$ManualState Models$Section Models$ViewModel)))

(defprotocol LootGenerator
  "A loot type. Implementations are resolved from config by the registry."
  (loot-spec [this]
    "Static, data-only description of this loot type. Conforms to
     `sns.sdk.schema/loot-spec`, e.g.
     {:id :relics :label \"Relic\"
      :inputs [{:id :character :label \"Character\" :type :enum :options [...]}]}")
  (generate [this ctx]
    "Produce loot. Returns a view-model (`sns.sdk.schema/view-model`).
     `ctx` is `{:rng :store :progression :inputs :config}` — see the engine.
     Stateful types read/write via `(:store ctx)`."))

(defprotocol LootAction
  "Optional. Stateful follow-up operations on previously generated loot
   (e.g. levelling a relic up). Surfaced to the UI via view-model `:loot/actions`."
  (handle-action [this ctx action params]
    "Apply `action` (a keyword) with `params`. Returns an updated view-model.
     `ctx` additionally carries `:view-model` — the current, possibly
     DM-edited view-model the UI had on screen (nil if the caller didn't send
     one). Reconstruct your item from it rather than trusting a copy frozen
     into `params`, so the DM's edits are what the action operates on: the
     displayed values are the source of truth, and `:loot/state` carries only
     what the view-model cannot express (an upgrade `:path`, a stored id)."))

(defprotocol Progression
  "How a single mod evolves. The default implementation interprets the
   upgrade-graph DSL (`sns.sdk.schema/mod`); plugins may supply bespoke logic."
  (current-state [this mod path]
    "Derive `mod`'s variables at the progression described by `path` (a vector
     of `{:id ...}` steps). Returns the resolved `sns.sdk.schema/item-vars`,
     which the browser renders the mod's template against.")
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
  "Reads over persisted state, for stateful plugins. State is a set of named
   collections, each a map of key to value; a collection needs no declaration and
   reads as `{}` until something is written to it. Available to in-process
   plugins only — `:cli` and `:ffi` plugins persist their own state.

   Writing is declarative and not part of this protocol: put the changes on your
   view-model under `:store/mutations` and the engine applies them once the
   view-model has validated, so a plugin cannot leave state changed by a call
   that then fails. Declare the collections you use with `:store/collections` in
   your `loot-spec`; it defaults to a single collection named after the plugin's
   `:id`.

   Reads take and return plain data, so the same call works against a local store
   or one whose state lives in the DM's browser."
  (setup! [this]
    "Prepare the backend for use. Called once at startup, before any other
     method; construction itself must stay side-effect-free.")
  (read-collection [this coll-id]
    "The whole collection `coll-id` (a keyword) as a map, or `{}` when absent."))

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
          (.list f)                 (assoc :list? true)
          (seq (.options f))        (assoc :options (vec (.options f)))))

(defn- manual-state->clj [^Models$ManualState m]
  (cond-> {:fields (mapv field->clj (.fields m))}
          (.keyLabel m) (assoc :key-label (.keyLabel m))
          (.list m)     (assoc :list? true)))

(defn- loot-spec->clj [^Models$LootSpec ls]
  ;; No `:hidden?` — that is set on the plugin's config entry for every plugin
  ;; type and folded into the spec by the engine, never by the generator.
  (cond-> {:id    (keyword (.id ls))
           :label (.label ls)}
          (.utility ls)                (assoc :utility? true)
          (.generateLabel ls)          (assoc :generate-label (.generateLabel ls))
          (seq (.storeCollections ls)) (assoc :store/collections (mapv keyword (.storeCollections ls)))
          (.storeManual ls)            (assoc :store/manual (manual-state->clj (.storeManual ls)))
          (.history ls)                (assoc :history (keyword (.history ls)))
          (seq (.inputs ls))           (assoc :inputs (mapv field->clj (.inputs ls)))))

(defn- item-var->clj [^Models$ItemVar v]
  (cond-> {:value (.value v)}
          (.type v)          (assoc :type (keyword (.type v)))
          (.label v)         (assoc :label (.label v))
          (.random v)        (assoc :random (keyword (.random v)))
          (seq (.args v))    (assoc :args (update-keys (into {} (.args v)) keyword))
          (seq (.options v)) (assoc :options (vec (.options v)))
          (.context v)       (assoc :context? true)))

(defn- item-vars->clj
  "Vars keyed by the name their template refers to them by, so the keys
   keywordise while the values become `::item-var` maps."
  [vars]
  (when (seq vars)
    (into {} (map (fn [[k v]] [(keyword k) (item-var->clj v)])) vars)))

(defn- mutations->clj
  "`{<collection> {<key> <value>}}`. Only the collection name is a keyword on
   this side; the key of an entry within it stays as written, so a row a plugin
   writes matches one written by the manual-state editor."
  [mutations]
  (into {} (map (fn [[coll rows]] [(keyword coll) (into {} rows)])) mutations))

(defn- item->clj [^Models$Item i]
  (cond-> {:item/body (.body i)}
          (.title i)          (assoc :item/title (.title i))
          (seq (.metadata i)) (assoc :item/metadata (vec (.metadata i)))
          (seq (.vars i))     (assoc :item/vars (item-vars->clj (.vars i)))))

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
          (.subtitle vm)        (assoc :loot/subtitle (.subtitle vm))
          (seq (.vars vm))      (assoc :loot/vars (item-vars->clj (.vars vm)))
          (seq (.sections vm))  (assoc :loot/sections (mapv section->clj (.sections vm)))
          (seq (.actions vm))   (assoc :loot/actions (mapv #(action->clj loot-id %) (.actions vm)))
          (seq (.words vm))     (assoc :loot/words (vec (.words vm)))
          (some? (.state vm))   (assoc :loot/state (.state vm))
          (seq (.mutations vm)) (assoc :store/mutations (mutations->clj (.mutations vm)))))

(defn- clj->item-var [{:keys [value type label random args options context?]}]
  (Models$ItemVar. value (some-> type name) label (some-> random name)
                   (when (seq args) (update-keys args name))
                   (when (seq options) (vec options))
                   (boolean context?)))

(defn- clj->item-vars [vars]
  (when (seq vars)
    (into {} (map (fn [[k v]] [(name k) (clj->item-var v)])) vars)))

(defn- clj->item [{:item/keys [title body metadata vars]}]
  (Models$Item. title body metadata (clj->item-vars vars)))

(defn- clj->section [{:section/keys [heading items]}]
  (Models$Section. heading (mapv clj->item items)))

(defn- clj->action [{:action/keys [label event]}]
  (let [{:keys [action params]} (second event)]
    (Models$Action. label (some-> action name) params)))

(defn- clj->view-model [{:loot/keys  [title subtitle sections actions vars words state]
                         :store/keys [mutations]}]
  (Models$ViewModel. title subtitle
                     (when sections (mapv clj->section sections))
                     (when actions (mapv clj->action actions))
                     (clj->item-vars vars)
                     (when (seq words) (vec words))
                     state
                     (when (seq mutations) (update-keys mutations name))))

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
    ;; `:view-model` is the result the UI had on screen, DM edits included. It
    ;; goes over as a `Models$ViewModel` rather than raw Clojure data, so a Java
    ;; author reads the edited `Item.vars()` and `ViewModel.state()` back with
    ;; the same records they returned.
    (let [ctx (cond-> ctx (:view-model ctx) (update :view-model clj->view-model))]
      (view-model->clj (loot-id this)
                       (.handleAction this (clj->java-map ctx) (name action) (clj->java-map params))))))

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
  (read-collection [this coll-id] (.readCollection this coll-id)))
