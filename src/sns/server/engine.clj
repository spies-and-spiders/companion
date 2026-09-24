(ns sns.server.engine
  "The single generation pipeline: owns the registry and randomness, and emits
   validated view-models. Templates travel to the browser unrendered, alongside
   the variables they interpolate."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [randy.core :as r]
    [sns.sdk.protocols :as p]
    [sns.sdk.randoms :as randoms]
    [sns.sdk.schema :as schema]
    [sns.sdk.vars :as vars]
    [sns.server.registry :as registry]
    [sns.server.reporter :as reporter]
    [sns.server.store :as store]
    [sns.server.store.edn :as edn-store]
    [taoensso.telemere :as t])
  (:import
    (java.io PushbackReader)
    (java.util.random RandomGenerator RandomGeneratorFactory)))

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

(def ^:private default-loot-die-size 100)

(defn- loot-die-size [config]
  (or (:loot-die-size config) default-loot-die-size))

(defn- allocation [table size]
  (let [total (transduce (map #(or (:weight %) 1)) + 0 table)]
    (:entries
      (reduce (fn [{:keys [acc entries]} {:keys [id weight]}]
                (let [acc (+ acc (or weight 1))]
                  {:acc     acc
                   :entries (conj entries {:id id :max (long (Math/ceil (* (double size) (/ acc total))))})}))
              {:acc 0 :entries []}
              table))))

(defn- ranged? [table]
  (boolean (some :ranges table)))

(defn- loot-ranges
  "Each entry's inclusive `[from to]` ranges on the loot die: as configured, or
   laid out from the weights, where an entry too light for a side of its own
   gets none."
  [table size]
  (if (ranged? table)
    (mapv #(select-keys % [:id :ranges]) table)
    (let [entries (allocation table size)]
      (mapv (fn [prev {:keys [id max]}]
              {:id id :ranges (if (< prev max) [[(inc prev) max]] [])})
            (cons 0 (map :max entries))
            entries))))

(defn- install-randoms!
  "Install each config-declared preset as a `randoms/preset` method, so a DM's
   `:randoms` and a plugin's own presets are the same mechanism. Config is
   applied after the plugins are built, so it wins on a name clash."
  [randoms]
  (doseq [[id values] randoms]
    (defmethod randoms/preset id [_ _] values)))

(defn- validate-ranges!
  "A ranged table gives every entry its ranges, all of them on the die, and
   leaves no side of the die unassigned."
  [table size]
  (when-some [bare (seq (remove :ranges table))]
    (throw (ex-info "Every loot-table entry needs :ranges once one has them"
                    {:missing (mapv :id bare)})))
  (doseq [{:keys [id ranges]} table
          [from to :as rng] ranges]
    (when-not (<= 1 from to size)
      (throw (ex-info (str "Loot-table range must run upwards within 1-" size)
                      {:id id :range rng :loot-die-size size}))))
  (let [covered? (fn [n] (some (fn [[from to]] (<= from n to)) (mapcat :ranges table)))]
    (when-some [gaps (seq (remove covered? (range 1 (inc size))))]
      (throw (ex-info "Loot-table ranges leave sides of the loot die unassigned"
                      {:unassigned (vec gaps) :loot-die-size size})))))

(defn- validate-table!
  "Every loot-table entry must reference a registered loot type, and the table
   has to fit on the loot die, or some entry could never be rolled."
  [registry table size]
  (if (ranged? table)
    (validate-ranges! table size)
    (when (> (count table) size)
      (throw (ex-info "Loot-table has more entries than the loot die has sides; raise :loot-die-size"
                      {:entries       (count table)
                       :loot-die-size size}))))
  (doseq [{:keys [id]} table]
    (when-not (contains? registry id)
      (throw (ex-info "Loot-table references an unknown loot type"
                      {:id id :known (vec (keys registry))})))))

(defn- pages
  "The `:page` tools in config order."
  [config]
  (filter (comp #{:page} :type) (:tools config)))

(defn- validate-pages!
  "Every page lists registered plugins, each once. A page is not in the registry,
   so one page listing another is rejected like any other unknown id."
  [registry pages]
  (doseq [{:keys [id] {:keys [tools]} :page} pages]
    (when-some [unknown (seq (remove #(contains? registry %) tools))]
      (throw (ex-info "Page references an unknown plugin"
                      {:page id :unknown (vec unknown) :known (vec (keys registry))})))
    (when-not (apply distinct? tools)
      (throw (ex-info "Page lists a plugin more than once" {:page id :tools tools})))))

(defn create
  "Build a loot engine from validated `config`. `deps` supplies overridable
   collaborators: `:store`, `:reporter`, `:rng`. The derived `:store` and
   `:reporter` default to the built-in (swappable) impls."
  ([config] (create config {}))
  ([config {:keys [store reporter rng]}]
   (let [table (:loot-table config)
         size (loot-die-size config)
         registry (registry/build config)
         rng (or rng (.create (RandomGeneratorFactory/of "L64X128MixRandom")))
         store (or store (store/from-config (:storage config)))]
     (some-> store p/setup!)
     (when (seq table)
       (validate-table! registry table size))
     (validate-pages! registry (pages config))
     ;; Config-declared random presets, usable from any plugin's vars as
     ;; `{:random :<preset>}`.
     (install-randoms! (:randoms config))
     {:config        config
      :registry      registry
      :entries       (into {} (map (juxt :id identity)) (:tools config))
      :words         (build-words config)
      :store         store
      :reporter      (or reporter (reporter/from-config (:reporting config)))
      :rng           rng
      ;; Missing weights default to 1, so a table without weights is sampled
      ;; uniformly (and partial weights mix evenly-weighted entries in). A
      ;; ranged table has no weights: it rolls the die instead.
      :loot-sampler  (when (and (seq table) (not (ranged? table)))
                       (r/alias-method-sampler (mapv :id table)
                                               (mapv #(or (:weight %) 1) table)))
      :loot-die-size size
      :loot-ranges   (when (seq table) (loot-ranges table size))})))

(defn- ctx
  "Assemble the per-request context handed to a generator."
  [engine inputs]
  (-> (select-keys engine [:rng :store :config])
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

(defn- label
  "Tool `id`'s display label: its configured one, or one derived from the id."
  [id entry]
  (or (:label entry) (vars/humanise-label id)))

(defn- tool-spec
  "Everything known of loot type `id`: its generator's loot-spec with the config's
   `:generator` laid over it, under the config's `:id`, `:label` and `:section`.
   An id, label or section the generator reports for itself is ignored."
  [{:keys [registry entries]} id]
  (let [generator (or (get registry id)
                      (throw (ex-info "Unknown loot type" {:id id :known (keys registry)})))
        {:keys [section] :as entry} (entries id)]
    (cond-> (-> (p/loot-spec generator)
                (dissoc :id :label :section)
                (merge (:generator entry))
                ;; Defaulted here rather than in each generator, so the UI
                ;; always receives an explicit list to fetch state for.
                (update :store/collections #(or (not-empty %) [id]))
                (assoc :id id :label (label id entry)))
            section (assoc :section section))))

(defn loot-specs
  "Specs for every registered loot type (drives the UI picker/forms), in the
   order the plugins appear in config. Hidden types are still returned: the UI
   needs the spec (label, inputs) to render one that has been rolled off the
   loot-table."
  [{:keys [registry] :as engine}]
  (mapv #(tool-spec engine %) (keys registry)))

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
  [{:keys [store] :as engine} id mutations]
  (let [spec      (tool-spec engine id)
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

(defn- address-actions
  "Route every `:loot/action` on `vm` back to tool `id`, whatever id its
   generator believes it has."
  [id vm]
  (cond-> vm
          (seq (:loot/actions vm))
          (update :loot/actions (partial mapv (fn [{[k params] :action/event :as a}]
                                                (cond-> a
                                                        (= :loot/action k) (assoc :action/event [k (assoc params :id id)])))))))

(defn- with-words
  "Two words naming this result, drawn once by the engine so a plugin never has
   to and every surface showing the result — UI, history, reporter — shows the
   same pair. `carry` keeps an existing pair across a follow-up action."
  [{:keys [words]} carry vm]
  (if-some [w (or (:loot/words vm) carry (when (seq words) (vec (r/sample-without-replacement 2 words))))]
    (assoc vm :loot/words w)
    vm))

(defn- or-error
  "What calling plugin `f` returns, or, when it throws, the view-model attached
   as the exception's `:view-model`, else one built from the exception itself."
  [f]
  (try (f)
       (catch Exception ex
         (t/log! {:level :warn :id ::plugin-error :error ex} (ex-message ex))
         (assoc (or (:view-model (ex-data ex))
                    {:loot/title (str (ex-message ex)) :loot/subtitle (.getName (class ex))})
                :loot/error? true))))

(defn generate
  "Generate loot of type `id` with `inputs`, returning a validated view-model."
  ([engine id] (generate engine id {}))
  ([{:keys [registry rng] :as engine} id inputs]
   (let [inputs (apply-input-defaults (tool-spec engine id) inputs)]
     ;; Vars draw from the request's rng, wherever downstream the draw happens.
     (randoms/with-rng rng
       (->> (or-error #(p/generate (get registry id) (ctx engine inputs)))
            (address-actions id)
            (with-words engine nil)
            (vars/resolve-view-model rng)
            (schema/assert! ::schema/view-model)
            (persist! (:store engine)))))))

(defn- roll->ids
  "Every loot type whose ranges hold the entered roll `n` (1-`size`)."
  [loot-ranges size n]
  (when-not (and (integer? n) (<= 1 n size))
    (throw (ex-info (str "Roll must be an integer between 1 and " size) {:n n :loot-die-size size})))
  (into [] (keep (fn [{:keys [id ranges]}] (when (some (fn [[from to]] (<= from n to)) ranges) id)))
        loot-ranges))

(defn roll
  "Roll the top-level loot table and generate every loot type the roll lands on.
   With no `n`, a weighted table is sampled by weight and a ranged one rolls the
   loot die; given `n` (a 1-`:loot-die-size` result), the types are those whose
   ranges hold it. `inputs` is keyed by loot-type id, so each type generates with
   its own. Returns `{:results [{:id :view-model} ...]}` so callers (e.g. the UI)
   can reflect what was rolled, with every result's writes merged at the top."
  ([engine] (roll engine {} nil))
  ([engine inputs] (roll engine inputs nil))
  ([{:keys [loot-sampler loot-ranges loot-die-size rng] :as engine} inputs n]
   (when-not loot-ranges
     (throw (ex-info "No loot-table configured" {})))
   (let [ids       (cond
                     (some? n)    (roll->ids loot-ranges loot-die-size n)
                     loot-sampler [(loot-sampler)]
                     :else        (roll->ids loot-ranges loot-die-size
                                             (inc (.nextInt ^RandomGenerator rng (int loot-die-size)))))
         ;; ponytail: each type generates against the state the request arrived
         ;; with, so under :browser two types sharing a collection do not see
         ;; each other's writes. Thread the writes through if that ever matters.
         vms       (mapv #(generate engine % (get inputs % {})) ids)
         mutations (apply merge-with merge (keep :store/mutations vms))]
     (cond-> {:results (mapv (fn [id vm] {:id id :view-model (dissoc vm :store/mutations)}) ids vms)}
             mutations (assoc :store/mutations mutations)))))

(defn capabilities
  "UI-facing flags describing optional features enabled by config: whether the
   report button is shown, whether state lives in the browser (in which case the
   client ships it with each request and applies the writes that come back), and
   when generated results join the browser-side history (absent = the client's
   default, `:button`). `:sections` is every tool id in config order, which the
   UI lists the rail by, `:pages` the configured pages, and `:loot-table` the
   inclusive `[from to]` `:ranges` of the loot die each loot type is rolled on."
  [{:keys [reporter config loot-die-size loot-ranges]}]
  (cond-> {:browser-storage? (store/browser? config)
           :loot-die-size    loot-die-size
           :sections         (mapv :id (:tools config))}
          (:history config) (assoc :history (:history config))
          loot-ranges (assoc :loot-table loot-ranges)
          (seq (pages config)) (assoc :pages (mapv (fn [{:keys [page] :as p}]
                                                     (-> (select-keys p [:id :section])
                                                         (assoc :label (label (:id p) p) :tools (:tools page))))
                                                   (pages config)))
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
    (when-not (satisfies? p/Action generator)
      (throw (ex-info "Loot type does not support actions" {:id id})))
    (randoms/with-rng rng
      (->> (or-error #(p/handle-action generator (assoc (ctx engine nil) :view-model view-model) action params))
           (address-actions id)
           (with-words engine (:loot/words view-model))
           (vars/resolve-view-model rng)
           (schema/assert! ::schema/view-model)
           (persist! (:store engine))))))
