(ns sns.server.engine
  "The single generation pipeline: owns the registry, randomness, and (in later
   milestones) state and template rendering, and emits validated view-models."
  (:require
    [randy.core :as r]
    [sns.server.progression :as progression]
    [sns.server.registry :as registry]
    [sns.server.render :as render]
    [sns.server.reporter :as reporter]
    [sns.server.store :as store]
    [sns.spi.protocols :as p]
    [sns.spi.schema :as schema])
  (:import
    (java.util.random RandomGeneratorFactory)))

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
   collaborators: `:store`, `:reporter`, `:render`, `:session`, `:rng`. `:render`,
   the derived `:progression`, `:store`, and `:reporter` default to the built-in
   (swappable) impls."
  ([config] (create config {}))
  ([config {:keys [store reporter render session rng]}]
   (let [table (:loot-table config)
         registry (registry/build config)
         render (or render render/render)
         store (or store (store/from-config (:storage config)))]
     (p/setup! store)
     (when (seq table)
       (validate-table! registry table))
     {:config          config
      :registry        registry
      :store           store
      :reporter        (or reporter (reporter/from-config (:reporting config)))
      :render          render
      :progression     (progression/progression render)
      :session         session
      :rng             (or rng (.create (RandomGeneratorFactory/of "L64X128MixRandom")))
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
  (-> (select-keys engine [:rng :store :render :progression :config :session])
      (assoc :inputs inputs)))

(defn- apply-input-defaults
  "Resolve each input declared in `loot-spec` against the supplied `inputs`. A
   field left blank (missing or an empty string, e.g. the UI's `—` enum option)
   falls back to the field's `:default`, or nil when it declares none — so a
   template sees the default rather than an empty string."
  [loot-spec inputs]
  (reduce (fn [acc {:keys [id default]}]
            (let [v (get acc id)]
              (assoc acc id (if (or (nil? v) (= "" v)) default v))))
          inputs
          (:inputs loot-spec)))

(defn loot-specs
  "Data-only specs for every registered loot type (drives the UI picker/forms),
   in the order the plugins appear in config."
  [{:keys [registry]}]
  (mapv p/loot-spec (vals registry)))

(defn generate
  "Generate loot of type `id` with `inputs`, returning a validated view-model."
  ([engine id] (generate engine id {}))
  ([{:keys [registry] :as engine} id inputs]
   (let [generator (or (get registry id)
                       (throw (ex-info "Unknown loot type" {:id id :known (keys registry)})))
         inputs    (apply-input-defaults (p/loot-spec generator) inputs)]
     (->> (ctx engine inputs)
          (p/generate generator)
          (schema/assert! ::schema/view-model)))))

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
   (let [id (if (some? n) (roll->id loot-allocation n) (loot-sampler))]
     {:id id :view-model (generate engine id inputs)})))

(defn capabilities
  "UI-facing flags describing optional features enabled by config (drives, e.g.,
   whether the report button is shown, and whether the group tracker persists
   server-side or must live in the browser)."
  [{:keys [reporter config]}]
  (cond-> {:social-storage? (not= :none (get-in config [:storage :backend]))}
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
   returning an updated, validated view-model."
  [{:keys [registry] :as engine} id action params]
  (let [generator (or (get registry id)
                      (throw (ex-info "Unknown loot type" {:id id})))]
    (when-not (satisfies? p/LootAction generator)
      (throw (ex-info "Loot type does not support actions" {:id id})))
    (->> (p/handle-action generator (ctx engine nil) action params)
         (schema/assert! ::schema/view-model))))
