(ns sns.server.engine
  "The single generation pipeline: owns the registry, randomness, and
   progression, and emits validated view-models. Templates travel to the
   browser unrendered, alongside the variables they interpolate."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [randy.core :as r]
    [sns.sdk.protocols :as p]
    [sns.sdk.randoms :as randoms]
    [sns.sdk.schema :as schema]
    [sns.server.progression :as progression]
    [sns.server.registry :as registry]
    [sns.server.reporter :as reporter]
    [sns.server.store :as store]
    [sns.server.store.edn :as edn-store])
  (:import
    (java.io PushbackReader)
    (java.util.random RandomGeneratorFactory)))

(def ^:private default-words
  (-> (io/resource "words.edn") io/reader PushbackReader. edn/read))

(defn- build-words
  "The vocabulary results are named from: the config's own list if it declares
   one, otherwise the built-in list plus whatever `:extra-words` adds."
  [{:keys [words extra-words]}]
  (cond
    (seq words) (vec words)
    (seq extra-words) (vec (into (set default-words) extra-words))
    :else default-words))

(defn- allocation
  "Turn a weighted loot table into cumulative 1-100 upper bounds, so an entered
   d100 roll resolves to a loot type. Weights are normalised to the total and
   scaled to 100, so the entered number is always on a 1-100 scale regardless of
   the raw weight total (and each type occupies a proportional slice). The final
   entry always reaches 100."
  [table]
  (let [total (transduce (map #(or (:weight %) 1)) + 0 table)]
    (:entries
      (reduce (fn [{:keys [acc entries]} {:keys [id weight]}]
                (let [acc (+ acc (or weight 1))]
                  {:acc     acc
                   :entries (conj entries {:id id :max (long (Math/ceil (* 100.0 (/ acc total))))})}))
              {:acc 0 :entries []}
              table))))

(defn- install-randoms!
  "Install each config-declared preset as a `randoms/preset` method, so a DM's
   `:randoms` and a plugin's own presets are the same mechanism. Config is
   applied after the plugins are built, so it wins on a name clash."
  [randoms]
  (doseq [[id values] randoms]
    (defmethod randoms/preset id [_ _] values)))

(defn- validate-table!
  "Every loot-table entry must reference a registered, rollable loot type;
   utilities are session tools, not loot, so they can't be rolled."
  [registry table]
  (doseq [{:keys [id]} table]
    (let [generator (or (get registry id)
                        (throw (ex-info "Loot-table references an unknown loot type"
                                        {:id id :known (vec (keys registry))})))]
      (when (:utility? (p/loot-spec generator))
        (throw (ex-info "Utilities cannot appear in the loot-table" {:id id}))))))

(defn create
  "Build a loot engine from validated `config`. `deps` supplies overridable
   collaborators: `:store`, `:reporter`, `:rng`. The derived `:progression`,
   `:store`, and `:reporter` default to the built-in (swappable) impls."
  ([config] (create config {}))
  ([config {:keys [store reporter rng]}]
   (let [table (:loot-table config)
         registry (registry/build config)
         rng (or rng (.create (RandomGeneratorFactory/of "L64X128MixRandom")))
         store (or store (store/from-config (:storage config)))]
     (some-> store p/setup!)
     (when (seq table)
       (validate-table! registry table))
     ;; Config-declared random presets, usable from any plugin's vars as
     ;; `{:random :<preset>}`.
     (install-randoms! (:randoms config))
     {:config          config
      :registry        registry
      :words           (build-words config)
      :store           store
      :reporter        (or reporter (reporter/from-config (:reporting config)))
      :progression     (progression/progression rng)
      :rng             rng
      ;; Missing weights default to 1, so a table without weights is sampled
      ;; uniformly (and partial weights mix evenly-weighted entries in).
      :loot-sampler    (when (seq table)
                         (r/alias-method-sampler (mapv :id table)
                                                 (mapv #(or (:weight %) 1) table)))
      ;; The same weights, laid out as a d100 lookup for number-driven rolls.
      :loot-allocation (when (seq table) (allocation table))})))

(defn- ctx
  "Assemble the per-request context handed to a generator."
  [engine inputs]
  (-> (select-keys engine [:rng :store :progression :config])
      (assoc :inputs inputs)))

(defn- ->decimal
  "BigDecimal rather than a double, so a value like 1.3 stays exact."
  [v]
  (cond
    (string? v) (try (BigDecimal. ^String (str/trim v)) (catch NumberFormatException _ v))
    (number? v) (bigdec v)
    :else v))

(defn- coerce
  "A numeric field arrives from the browser form as a string, so parse it — a
   plugin that declared an `:int` or a `:decimal` should be handed one. An
   unparseable value is passed through untouched, for the generator to reject in
   its own terms."
  [type v]
  (case type
    :int (if (string? v) (or (parse-long (str/trim v)) v) v)
    :decimal (->decimal v)
    v))

(defn- apply-input-defaults
  "Resolve each input declared in `loot-spec` against the supplied `inputs`. A
   field left blank (missing or an empty string, e.g. the UI's `—` enum option)
   falls back to the field's `:default`, or nil when it declares none — so a
   template sees the default rather than an empty string. Either way the value is
   coerced, so a `:default` written as a string reaches the generator in the same
   type an entered one does.

   A `:list?` field always resolves to a vector (its `:default`, or `[]` when
   unset and nothing was submitted), each element coerced individually — a
   generator sees a vector regardless of how many values were entered."
  [loot-spec inputs]
  (reduce (fn [acc {:keys [id default type list?]}]
            (let [v (get acc id)]
              (assoc acc id
                     (if list?
                       (mapv #(coerce type %) (if (seq v) v (or default [])))
                       (coerce type (if (or (nil? v) (= "" v)) default v))))))
          inputs
          (:inputs loot-spec)))

(defn loot-specs
  "Data-only specs for every registered loot type (drives the UI picker/forms),
   in the order the plugins appear in config. A plugin config entry marked
   `:hidden?` has that folded into its spec here rather than in the generator, so
   hiding works for every plugin type — including compiled `:jar` plugins that
   know nothing about it. Hidden types are still returned: the UI needs the spec
   (label, inputs) to render one that has been rolled off the loot-table."
  [{:keys [registry config]}]
  (let [hidden (into #{} (comp (filter :hidden?) (map :id)) (:plugins config))]
    (mapv (fn [generator]
            (let [spec (p/loot-spec generator)]
              (cond-> spec
                      ;; Defaulted here rather than in each generator, so the UI
                      ;; always receives an explicit list to fetch state for.
                      true (update :store/collections #(or (not-empty %) [(:id spec)]))
                      (hidden (:id spec)) (assoc :hidden? true))))
          (vals registry))))

(defn- coerce-entry
  "One row of a manual-state collection, narrowed to the declared fields and
   coerced to their types. A field left blank falls back to its `:default`, the
   same way an input does."
  [fields entry]
  (reduce (fn [acc {:keys [id type default]}]
            (let [v (get entry id)]
              (assoc acc id (if (or (nil? v) (= "" v)) default (coerce type v)))))
          {}
          fields))

(defn manual-state
  "Read — and first write, given `mutations` — the DM-owned collection loot type
   `id` declares with `:store/manual`. `mutations` is `{<key> <row>}`, a nil row
   retracting that key; rows are coerced to the declared fields. Returns
   `{:store/state <the whole collection>}`, carrying `:store/mutations` for what
   was actually applied when there was anything to apply."
  [{:keys [registry store]} id mutations]
  (let [generator (or (get registry id)
                      (throw (ex-info "Unknown loot type" {:id id})))
        spec      (p/loot-spec generator)
        {:keys [fields list?]} (or (:store/manual spec)
                                   (throw (ex-info "Loot type has no manual state" {:id id})))
        coll      (or (first (:store/collections spec)) id)
        applied   (when (seq mutations)
                    (when-some [blank (some #(when (str/blank? (str %)) %) (keys mutations))]
                      (throw (ex-info "A manual-state key cannot be blank" {:id id :key blank})))
                    (update-vals mutations
                                 #(cond
                                    (nil? %) nil
                                    list?    (mapv (partial coerce-entry fields) %)
                                    :else    (coerce-entry fields %))))]
    (when applied
      (edn-store/mutate! store {coll applied}))
    (cond-> {:store/state (p/read-collection store coll)}
            applied (assoc :store/mutations {coll applied}))))

(def history-collection
  "Where the result history lives: one row per loot type, in the store the
   config's `:storage` selects, like every other collection."
  :history)

(defn history
  "Read — and first write, given `mutations` — the result history. `mutations` is
   `{<loot-type name> <rows>}`, where rows are `{:at <ms> :view-model <vm>}`
   newest first and a nil row clears that type. Shaped like `manual-state`: the
   whole collection out, plus what was applied for a `:browser` client to store."
  [{:keys [store]} mutations]
  (when (seq mutations)
    (when-some [blank (some #(when (str/blank? (str %)) %) (keys mutations))]
      (throw (ex-info "A history key cannot be blank" {:key blank})))
    (edn-store/mutate! store {history-collection mutations}))
  (cond-> {:store/state (p/read-collection store history-collection)}
          (seq mutations) (assoc :store/mutations {history-collection mutations})))

(defn- persist!
  "Apply the writes `view-model` declares. Called only once it has validated, so
   a plugin that returns something unusable leaves the store untouched — the
   whole point of declaring writes rather than performing them."
  [store view-model]
  (when-let [mutations (:store/mutations view-model)]
    (edn-store/mutate! store mutations))
  view-model)

(defn with-state
  "Under `:browser` storage, an engine whose store holds the `state` the client
   sent. What a plugin writes travels back on its view-model, so nothing needs
   recording here. Every other backend keeps its own store and ignores `state`."
  [{:keys [config] :as engine} state]
  (cond-> engine
          (store/browser? config) (assoc :store (edn-store/->MemoryStore (atom (or state {}))))))

(defn- with-words
  "Two words naming this result, drawn once by the engine so a plugin never has
   to and every surface showing the result — UI, history, reporter — shows the
   same pair. `carry` keeps an existing pair across a follow-up action."
  [{:keys [words]} carry vm]
  (if-some [w (or (:loot/words vm) carry (when (seq words) (vec (r/sample-without-replacement 2 words))))]
    (assoc vm :loot/words w)
    vm))

(defn generate
  "Generate loot of type `id` with `inputs`, returning a validated view-model."
  ([engine id] (generate engine id {}))
  ([{:keys [registry rng] :as engine} id inputs]
   (let [generator (or (get registry id)
                       (throw (ex-info "Unknown loot type" {:id id :known (keys registry)})))
         inputs    (apply-input-defaults (p/loot-spec generator) inputs)]
     ;; Vars draw from the request's rng, wherever downstream the draw happens.
     (randoms/with-rng rng
       (->> (ctx engine inputs)
            (p/generate generator)
            (with-words engine nil)
            (schema/assert! ::schema/view-model)
            (persist! (:store engine)))))))

(defn- roll->id
  "Resolve the entered d100 roll `n` (1-100) to a loot type via the allocation."
  [loot-allocation n]
  (when-not (and (integer? n) (<= 1 n 100))
    (throw (ex-info "Roll must be an integer between 1 and 100" {:n n})))
  (some (fn [{:keys [id max]}] (when (<= n max) id)) loot-allocation))

(defn roll
  "Roll the top-level loot table and generate the chosen loot type. With no `n`,
   the table is sampled randomly by weight; given `n` (a 1-100 d100 result), the
   type is resolved from its allocation on the table. Returns the chosen `:id`
   alongside its `:view-model` so callers (e.g. the UI) can reflect which
   discipline was rolled."
  ([engine] (roll engine {} nil))
  ([engine inputs] (roll engine inputs nil))
  ([{:keys [loot-sampler loot-allocation] :as engine} inputs n]
   (when-not loot-sampler
     (throw (ex-info "No loot-table configured" {})))
   (let [id (if (some? n) (roll->id loot-allocation n) (loot-sampler))
         vm (generate engine id inputs)]
     ;; Writes ride at the top of the result, where a plain generate leaves
     ;; them, rather than buried inside the wrapper this adds.
     (cond-> {:id id :view-model (dissoc vm :store/mutations)}
             (:store/mutations vm) (assoc :store/mutations (:store/mutations vm))))))

(defn capabilities
  "UI-facing flags describing optional features enabled by config: whether the
   report button is shown, whether state lives in the browser (in which case the
   client ships it with each request and applies the writes that come back), and
   when generated results join the browser-side history (absent = the client's
   default, `:button`)."
  [{:keys [reporter config]}]
  (cond-> {:browser-storage? (store/browser? config)}
          (:history config) (assoc :history (:history config))
          reporter (assoc :report? true
                          :report-label (p/report-label reporter))))

(defn report!
  "Send a (validated) `view-model` to the configured reporter."
  [{:keys [reporter]} view-model]
  (when-not reporter
    (throw (ex-info "Reporting is not configured" {})))
  (p/report! reporter (schema/assert! ::schema/view-model view-model)))

(defn handle-action
  "Dispatch a stateful follow-up `action` (with `params`) to loot type `id`,
   returning an updated, validated view-model. `view-model` is the current
   (possibly DM-edited) view-model the UI had on screen when the action was
   triggered — not re-validated, since it may be mid-edit — so a generator can
   react to what's currently displayed (e.g. an edited `:item/vars` value)
   rather than only the `params` it declared for itself. Pass nil when there is
   nothing on screen to react to."
  [{:keys [registry rng] :as engine} id action params view-model]
  (let [generator (or (get registry id)
                      (throw (ex-info "Unknown loot type" {:id id})))]
    (when-not (satisfies? p/LootAction generator)
      (throw (ex-info "Loot type does not support actions" {:id id})))
    (randoms/with-rng rng
      (->> (p/handle-action generator (assoc (ctx engine nil) :view-model view-model) action params)
           (with-words engine (:loot/words view-model))
           (schema/assert! ::schema/view-model)
           (persist! (:store engine))))))
