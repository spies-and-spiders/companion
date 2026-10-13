(ns sns.sdk.protocols
  "The extension points plugins implement. Dependency-light, so JAR plugins
   needn't pull in the app."
  (:import
    (java.util HashMap)
    (sns.sdk Models$Action Models$Field Models$Item Models$ItemVar
             Models$Spec Models$ManualState Models$Section Models$ViewModel)))

(defprotocol Generator
  "A loot type."
  (loot-spec [this]
    "A static `sns.sdk.schema/loot-spec`, e.g.
     {:inputs [{:id :character :label \"Character\" :type :enum :options [...]}]}")
  (generate [this ctx]
    "Return a `sns.sdk.schema/view-model`. `ctx` holds `:rng`, `:store`,
     `:config`, `:registry` and `:inputs`."))

(defprotocol Action
  "Optional follow-up operations on generated loot, like ranking a relic up,
   offered through a view-model's `:loot/actions`."
  (handle-action [this ctx action params]
    "Apply `action` (a keyword) with `params`, returning an updated view-model.
     `ctx` also carries `:view-model`, the result on screen with the DM's edits,
     or nil. Rebuild from it rather than from `params`, so the action works on
     what the DM sees. `:inputs` holds the values entered for the action's
     `:action/inputs`."))

(defprotocol Reporter
  "Sends a generated view-model somewhere external, like a Discord webhook.
   Configured under `:reporting`; without one, the UI hides its report button."
  (report-label [this]
    "The report button's label, e.g. \"Send to Discord\".")
  (report! [this view-model]
    "Send `view-model`, throwing ex-info on failure."))

(defprotocol Store
  "Read access to persisted state: named collections, each a map of key to
   value, reading as `{}` until written. Plugins write by returning
   `:store/mutations` on their view-model, which the engine applies once it
   validates."
  (setup! [this]
    "Called once at startup, before any other method, so construction can stay
     side-effect-free.")
  (read-collection [this coll-id]
    "The whole collection `coll-id` (a keyword), or `{}`."))

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

(defn- loot-spec->clj [^Models$Spec ls]
  (cond-> {}
          (.hidden ls)                 (assoc :hidden? true)
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
          (.context v)       (assoc :context? true)
          (some? (.step v))  (assoc :step (.step v))
          (some? (.max v))   (assoc :max (.max v))
          (some? (.rank v))  (assoc :rank (.rank v))))

(defn- item-vars->clj [vars]
  (when (seq vars)
    (into {} (map (fn [[k v]] [(keyword k) (item-var->clj v)])) vars)))

(defn- mutations->clj
  "Keywordise only collection names; row keys stay as written, matching the
   manual-state editor's."
  [mutations]
  (into {} (map (fn [[coll rows]] [(keyword coll) (into {} rows)])) mutations))

(defn- item->clj [^Models$Item i]
  (cond-> {:item/body (.body i)}
          (.title i)          (assoc :item/title (.title i))
          (seq (.metadata i)) (assoc :item/metadata (vec (.metadata i)))
          (seq (.vars i))     (assoc :item/vars (item-vars->clj (.vars i)))))

(defn- section->clj [^Models$Section s]
  (cond-> {:section/items (mapv item->clj (.items s))}
          (.heading s) (assoc :section/heading (.heading s))
          (.secret s)  (assoc :section/secret? true)))

(defn- action->clj [^Models$Action a]
  (cond-> {:action/label (.label a)
           :action/event [:loot/action {:action (keyword (.action a))
                                        :params (into {} (.params a))}]}
          (seq (.inputs a)) (assoc :action/inputs (mapv field->clj (.inputs a)))))

(defn- view-model->clj [^Models$ViewModel vm]
  (cond-> {:loot/title (.title vm)}
          (.subtitle vm)        (assoc :loot/subtitle (.subtitle vm))
          (seq (.vars vm))      (assoc :loot/vars (item-vars->clj (.vars vm)))
          (seq (.sections vm))  (assoc :loot/sections (mapv section->clj (.sections vm)))
          (seq (.actions vm))   (assoc :loot/actions (mapv action->clj (.actions vm)))
          (seq (.words vm))     (assoc :loot/words (vec (.words vm)))
          (some? (.state vm))   (assoc :loot/state (.state vm))
          (.error vm)           (assoc :loot/error? true)
          (seq (.mutations vm)) (assoc :store/mutations (mutations->clj (.mutations vm)))))

(defn- clj->item-var [{:keys [value type label random args options context? step rank] mx :max}]
  (Models$ItemVar. value (some-> type name) label (some-> random name)
                   (when (seq args) (update-keys args name))
                   (when (seq options) (vec options))
                   (boolean context?)
                   step mx rank))

(defn- clj->item-vars [vars]
  (when (seq vars)
    (into {} (map (fn [[k v]] [(name k) (clj->item-var v)])) vars)))

(defn- clj->item [{:item/keys [title body metadata vars]}]
  (Models$Item. title body metadata (clj->item-vars vars)))

(defn- clj->section [{:section/keys [heading items secret?]}]
  (Models$Section. heading (mapv clj->item items) (boolean secret?)))

(defn- clj->field [{:keys [id label type default options list?]}]
  (Models$Field. (name id) label (name type) default (when (seq options) (vec options)) (boolean list?)))

(defn- clj->action [{:action/keys [label event inputs]}]
  (let [{:keys [action params]} (second event)]
    (Models$Action. label (some-> action name) params (when (seq inputs) (mapv clj->field inputs)))))

(defn- clj->view-model [{:loot/keys  [title subtitle sections actions vars words state error?]
                         :store/keys [mutations]}]
  (Models$ViewModel. title subtitle
                     (when sections (mapv clj->section sections))
                     (when actions (mapv clj->action actions))
                     (clj->item-vars vars)
                     (when (seq words) (vec words))
                     state
                     (when (seq mutations) (update-keys mutations name))
                     (boolean error?)))

(defn- clj->java-map [m]
  (reduce-kv
    (fn [^HashMap hm k v]
      (doto hm
        (.put (name k) v)))
    (HashMap.)
    m))

(extend-type sns.sdk.Generator
  Generator
  (loot-spec [this] (loot-spec->clj (.lootSpec this)))
  (generate [this ctx] (->> (clj->java-map ctx)
                            (.generate this)
                            view-model->clj)))

(extend-type sns.sdk.Action
  Action
  (handle-action [this ctx action params]
    (let [ctx (cond-> ctx (:view-model ctx) (update :view-model clj->view-model))]
      (view-model->clj (.handleAction this (clj->java-map ctx) (name action) (clj->java-map params))))))

(extend-type sns.sdk.Reporter
  Reporter
  (report-label [this] (.reportLabel this))
  (report! [this view-model]
    (.report this (clj->view-model view-model))))

(extend-type sns.sdk.Store
  Store
  (setup! [this] (.setup this))
  (read-collection [this coll-id] (.readCollection this coll-id)))
