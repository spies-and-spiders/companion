(ns sns.server.progression
  "Default `Progression`: interprets the upgrade-graph DSL. Upgrades transform
   structured *state*; the effect text is derived (rendered once) from the
   accumulated state. This is what makes choosing the same option N times
   well-defined — see the architecture plan."
  (:require
    [randy.rng :as rng]
    [sns.spi.protocols :as p]))

(defn roll-option
  "Build the persisted path-step for taking `option`, rolling any `:roll` specs
   with `rng` (inclusive bounds). The rolled values are recorded under `:rolled`
   so the resulting effect re-derives deterministically."
  [rng-impl {:keys [id roll]}]
  (cond-> {:id id}
          roll (assoc :rolled (update-vals roll (fn [[lo hi]]
                                                  (rng/next-int rng-impl lo (inc hi)))))))

(defn- find-option [upgrades id]
  (->> (:options upgrades)
       (filter #(= id (:id %)))
       first))

(defn- next-upgrades
  "The graph node reached after taking `option`: its child `:upgrades` if it has
   them, otherwise the same node again. A one-shot (non-repeatable, child-less)
   option is applied in place and later filtered out of the node's available
   options by `options-at`; it never closes off the node on its own, so the
   node's other upgrades (e.g. a repeatable sibling) stay reachable."
  [current option]
  (or (:upgrades option) current))

(defn- apply-option
  "Apply one option's mutation ops (plus any persisted `:rolled` values) to the
   accumulator `{:state :template}`. Ordered so a set/template establishes the
   base, rolled values override, then numeric/text accumulation applies."
  [acc {set-op      :set
        inc-op      :inc
        dec-op      :dec
        append-op   :append
        conj-op     :conj
        template-op :assoc-template
        enable-op   :enable
        disable-op  :disable}
   rolled]
  (cond-> acc
          template-op (assoc :template template-op)
          set-op      (update :state merge set-op)
          rolled      (update :state merge rolled)
          inc-op      (update :state #(merge-with + % inc-op))
          dec-op      (update :state #(merge-with - % dec-op))
          append-op   (update :state #(merge-with str % append-op))
          conj-op     (update :state (fn [s]
                                       (reduce-kv (fn [s k v]
                                                    (update s k (fnil conj []) v))
                                                  s conj-op)))
          enable-op   (update :state #(reduce (fn [s k] (assoc s k true)) % enable-op))
          disable-op  (update :state #(reduce (fn [s k] (assoc s k false)) % disable-op))))

(defn derive-mod
  "Fold the chosen `path` over `base` mod and render. Returns `base` with its
   final `:state`, active `:template`, and the rendered `:effect`. `render` is
   the swappable `(fn [template state] -> string)`."
  [render base path]
  (let [{:keys [state template]}
        (reduce (fn [{:keys [upgrades] :as acc} {:keys [id rolled]}]
                  (let [option (or (find-option upgrades id)
                                   (throw (ex-info "Unknown upgrade option"
                                                   {:id id :available (mapv :id (:options upgrades))})))]
                    (-> (apply-option acc option rolled)
                        (assoc :upgrades (next-upgrades upgrades option)))))
                (-> (select-keys base [:state :template])
                    (assoc :upgrades (:upgrades base)))
                path)]
    (-> (dissoc base :upgrades)
        (assoc :state state
               :template template
               :effect (render template state)))))

(defn- available-options
  "Drop non-repeatable options already taken at the current node, so a one-shot
   upgrade is consumed without closing off the node's other upgrades."
  [upgrades taken]
  (update upgrades :options
          (fn [options]
            (filterv (fn [{:keys [id repeatable]}]
                       (or repeatable (not (contains? taken id))))
                     options))))

(defn options-at
  "Walk `path` and return the upgrade options available as the next step, or nil
   if the mod has reached a terminal node (descended into a child node with no
   options, or consumed every remaining option at the current node). One-shot
   options taken at the current node are filtered out; a repeatable sibling keeps
   the node open."
  [base path]
  (loop [upgrades (:upgrades base)
         taken    #{}
         [step & more] path]
    (if (and step upgrades)
      (let [option (find-option upgrades (:id step))]
        (if (:upgrades option)
          (recur (:upgrades option) #{} more)
          (recur upgrades
                 (cond-> taken (not (:repeatable option)) (conj (:id step)))
                 more)))
      (when upgrades
        (let [available (available-options upgrades taken)]
          (when (seq (:options available))
            available))))))

(defn progression
  "Construct the default Progression, injecting the swappable `render` fn."
  [render]
  (reify p/Progression
    (current-state [_ mod path] (derive-mod render mod path))
    (level-options [_ mod path] (options-at mod path))))
