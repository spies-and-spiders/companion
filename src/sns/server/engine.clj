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
    [sns.spi.schema :as schema]))

(defn create
  "Build a loot engine from validated `config`. `deps` supplies overridable
   collaborators: `:store`, `:reporter`, `:render`, `:session`, `:rng`. `:render`,
   the derived `:progression`, `:store`, and `:reporter` default to the built-in
   (swappable) impls."
  ([config] (create config {}))
  ([config {:keys [store reporter render session rng]}]
   (let [table (:loot-table config)
         render (or render render/render)]
     {:config       config
      :registry     (registry/build config)
      :store        (or store (store/from-config (:storage config)))
      :reporter     (or reporter (reporter/from-config (:reporting config)))
      :render       render
      :progression  (progression/progression render)
      :session      session
      :rng          (or rng @r/default-rng)
      ;; Missing weights default to 1, so a table without weights is sampled
      ;; uniformly (and partial weights mix evenly-weighted entries in).
      :loot-sampler (when (seq table)
                      (r/alias-method-sampler (mapv :id table)
                                              (mapv #(or (:weight %) 1) table)))})))

(defn- ctx
  "Assemble the per-request context handed to a generator."
  [engine inputs]
  (-> (select-keys engine [:rng :store :render :progression :config :session])
      (assoc :inputs inputs)))

(defn loot-specs
  "Data-only specs for every registered loot type (drives the UI picker/forms)."
  [{:keys [registry]}]
  (mapv p/loot-spec (vals registry)))

(defn generate
  "Generate loot of type `id` with `inputs`, returning a validated view-model."
  ([engine id] (generate engine id {}))
  ([{:keys [registry] :as engine} id inputs]
   (let [generator (or (get registry id)
                       (throw (ex-info "Unknown loot type" {:id id :known (keys registry)})))]
     (->> (ctx engine inputs)
          (p/generate generator)
          (schema/assert! ::schema/view-model)))))

(defn roll
  "Roll the top-level loot table and generate the chosen loot type. Returns the
   chosen `:id` alongside its `:view-model` so callers (e.g. the UI) can reflect
   which discipline was rolled."
  ([engine] (roll engine {}))
  ([{:keys [loot-sampler] :as engine} inputs]
   (when-not loot-sampler
     (throw (ex-info "No loot-table configured" {})))
   (let [id (loot-sampler)]
     {:id id :view-model (generate engine id inputs)})))

(defn capabilities
  "UI-facing flags describing optional features enabled by config (drives, e.g.,
   whether the report button is shown)."
  [{:keys [reporter]}]
  (cond-> {}
          reporter (assoc :report? true
                          :report-label (p/report-label reporter))))

(defn report
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
