(ns sns.server.engine
  "The generation pipeline: owns the registry, store and rng, and emits
   validated view-models."
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
  "The config's `:words`, or the built-in list plus `:extra-words`."
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
  "Install config's `:randoms` as presets. Run after plugins are built, so
   config wins on a name clash."
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

(defn- pages [config]
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
  "Build a loot engine from validated `config`. `deps` may override `:store`,
   `:reporter` and `:rng`."
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
     (install-randoms! (:randoms config))
     {:config        config
      :registry      registry
      :entries       (into {} (map (juxt :id identity)) (:tools config))
      :words         (build-words config)
      :store         store
      :reporter      (or reporter (reporter/from-config (:reporting config)))
      :rng           rng
      ;; Missing weights default to 1. A ranged table rolls the die instead.
      :loot-sampler  (when (and (seq table) (not (ranged? table)))
                       (r/alias-method-sampler (mapv :id table)
                                               (mapv #(or (:weight %) 1) table)))
      :loot-die-size size
      :loot-ranges   (when (seq table) (loot-ranges table size))})))

(defn- ctx [engine inputs]
  (-> (select-keys engine [:rng :store :config :registry])
      (assoc :inputs inputs)))

(defn- ->decimal
  "BigDecimal rather than a double, so a value like 1.3 stays exact."
  [v]
  (cond
    (string? v) (try (BigDecimal. ^String (str/trim v)) (catch NumberFormatException _ v))
    (number? v) (bigdec v)
    :else v))

(defn- ->position
  "A `:section` (`\"1\"`) or `:item` (`\"1:2\"`) pick, as the browser sends
   it, into `{:section 1}` or `{:section 1 :item 2}`."
  [type v]
  (let [[s i] (when (string? v) (map parse-long (str/split (str/trim v) #":")))]
    (cond
      (and s (= :section type)) {:section s}
      (and s i)                 {:section s :item i}
      :else                     v)))

(defn- coerce
  "Parse a form string into the field's declared type. Unparseable values pass
   through for the generator to reject."
  [type v]
  (case type
    :int (if (string? v) (or (parse-long (str/trim v)) v) v)
    :decimal (->decimal v)
    (:section :item) (->position type v)
    v))

(defn- apply-input-defaults
  "Coerce each input `loot-spec` declares, with blanks (missing, or \"\" like
   the UI's `—` enum option) falling back to the field's `:default`. A `:list?`
   field always resolves to a vector."
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
                ;; So the UI always knows which state to fetch.
                (update :store/collections #(or (not-empty %) [id]))
                (assoc :id id :label (label id entry)))
            section (assoc :section section))))

(defn loot-specs
  "Every loot type's spec, in config order. Includes hidden types, which the UI
   still renders when rolled."
  [{:keys [registry] :as engine}]
  (mapv #(tool-spec engine %) (keys registry)))

(defn- coerce-entry
  "A manual-state row narrowed to the declared fields and coerced like an input."
  [fields entry]
  (reduce (fn [acc {:keys [id type default]}]
            (let [v (get entry id)]
              (assoc acc id (if (or (nil? v) (= "" v)) default (coerce type v)))))
          {}
          fields))

(defn manual-state
  "Apply `mutations` (`{<key> <row>}`, nil retracting) to loot type `id`'s
   `:store/manual` collection, then read it. Returns `{:store/state <collection>}`,
   plus the applied `:store/mutations` if any."
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
  "Holds the result history, one row per loot type."
  :history)

(defn history
  "Like `manual-state`, for the result history. `mutations` is
   `{<loot-type name> <rows>}`, with rows `{:at <ms> :view-model <vm>}` newest
   first and nil clearing that type."
  [{:keys [store]} mutations]
  (when (seq mutations)
    (when-some [blank (some #(when (str/blank? (str %)) %) (keys mutations))]
      (throw (ex-info "A history key cannot be blank" {:key blank})))
    (edn-store/mutate! store {history-collection mutations}))
  (cond-> {:store/state (p/read-collection store history-collection)}
          (seq mutations) (assoc :store/mutations {history-collection mutations})))

(defn- persist!
  "Apply the writes `view-model` declares. Only call once it's validated."
  [store view-model]
  (when-let [mutations (:store/mutations view-model)]
    (edn-store/mutate! store mutations))
  view-model)

(defn with-state
  "Under `:browser` storage, an engine whose store holds the `state` the client
   sent. Other backends ignore `state`."
  [{:keys [config] :as engine} state]
  (cond-> engine
          (store/browser? config) (assoc :store (edn-store/->MemoryStore (atom (or state {}))))))

(defn- address-actions
  "Route every `:loot/action` on `vm` back to tool `id`."
  [id vm]
  (cond-> vm
          (seq (:loot/actions vm))
          (update :loot/actions (partial mapv (fn [{[k params] :action/event :as a}]
                                                (cond-> a
                                                        (= :loot/action k) (assoc :action/event [k (assoc params :id id)])))))))

(defn- with-words
  "Name the result with two words, keeping `carry` across a follow-up action."
  [{:keys [words]} carry vm]
  (if-some [w (or (:loot/words vm) carry (when (seq words) (vec (r/sample-without-replacement 2 words))))]
    (assoc vm :loot/words w)
    vm))

(defn- or-error
  "Call `f`, turning a throw into an error view-model: the exception's
   `:view-model` if it has one, else one built from the exception."
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
  "Roll the loot table and generate every loot type the roll lands on: `n` if
   given, else sampled by weight or rolled on the loot die. `inputs` is keyed by
   loot type. Returns `{:results [{:id :view-model} ...]}`, with every result's
   writes merged at the top."
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
  "Config-derived settings for the UI. `:sections` lists every tool id in config
   order, and `:loot-table` each loot type's inclusive die ranges."
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
  "Validate `view-model` and send it to the configured reporter."
  [{:keys [reporter]} view-model]
  (when-not reporter
    (throw (ex-info "Reporting is not configured" {})))
  (p/report! reporter (schema/assert! ::schema/view-model view-model)))

(defn- action-fields
  "The `:action/inputs` of the action on `view-model` that dispatches `action`
   with `params`."
  [view-model action params]
  (some (fn [{:action/keys [event inputs]}]
          (let [[k m] event]
            (when (and (= :loot/action k) (= action (:action m)) (= params (:params m)))
              inputs)))
        (:loot/actions view-model)))

(defn handle-action
  "Dispatch `action` to loot type `id`, returning an updated, validated
   view-model. `view-model` is what the UI had on screen, or nil; it's not
   validated, since the DM may be mid-edit. `inputs` are the values entered for
   the action's `:action/inputs`."
  ([engine id action params view-model] (handle-action engine id action params view-model {}))
  ([{:keys [registry rng] :as engine} id action params view-model inputs]
   (let [generator (or (get registry id)
                       (throw (ex-info "Unknown loot type" {:id id})))
         inputs    (apply-input-defaults {:inputs (action-fields view-model action params)} inputs)]
     (when-not (satisfies? p/Action generator)
       (throw (ex-info "Loot type does not support actions" {:id id})))
     (randoms/with-rng rng
       (->> (or-error #(p/handle-action generator (assoc (ctx engine inputs) :view-model view-model) action params))
            (address-actions id)
            (with-words engine (:loot/words view-model))
            (vars/resolve-view-model rng)
            (schema/assert! ::schema/view-model)
            (persist! (:store engine)))))))
