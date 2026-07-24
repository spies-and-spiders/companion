(ns sns.server.progression
  "Default `Progression`: interprets the upgrade-graph DSL. Upgrades transform
   structured *state*; the effect text is derived (rendered once) from the
   accumulated state. This is what makes choosing the same option N times
   well-defined — see the architecture plan.

   The graph walk lives here; the *ops* an option may carry are the open
   vocabulary in `sns.sdk.progression`, so a plugin extends what an upgrade can
   do without replacing this interpreter."
  (:require
    [sns.sdk.progression :as sp]
    [sns.sdk.protocols :as p]))

(def ^:private structural-keys
  "Keys on an option that describe the graph rather than mutate state, so they
   are never dispatched as ops. Everything else on an option is an op — which is
   what makes a typo'd op an error rather than a silent no-op."
  #{:id :repeatable :upgrades})

(def ^:private op-order
  "The built-in ops in application order: a template swap establishes the base,
   then the accumulating ops apply. Ops outside this list (a plugin's own) are
   applied afterwards in name order, so an option's ops resolve identically on
   every derivation regardless of map ordering."
  [:assoc-template :inc :dec :append :conj :enable :disable])

(defn- ordered
  "The keys of `ops`, sorted into `op-order` with unknown (plugin) ops last."
  [ops]
  (let [order (zipmap op-order (range))]
    (sort-by (fn [op] [(get order op (count op-order)) (name op)])
             (keys ops))))

(defn- apply-ops
  "Apply every op on `option` to the accumulator `acc`."
  [acc option]
  (let [ops (apply dissoc option structural-keys)]
    (reduce (fn [acc op] (sp/apply-op acc op (get ops op)))
            acc
            (ordered ops))))

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

(defn derive-mod
  "Fold the chosen `path` over `base` mod and render. Returns `base` with its
   final `:state`, active `:template`, and the rendered `:effect`. `render` is
   the swappable `(fn [template state] -> string)`."
  [render base path]
  (let [{:keys [state template]}
        (reduce (fn [{:keys [upgrades] :as acc} {:keys [id]}]
                  (let [option (or (find-option upgrades id)
                                   (throw (ex-info "Unknown upgrade option"
                                                   {:id id :available (mapv :id (:options upgrades))})))]
                    (-> (apply-ops acc option)
                        (assoc :upgrades (next-upgrades upgrades option)))))
                (-> (select-keys base [:state :template])
                    (assoc :upgrades (:upgrades base)))
                path)]
    (-> (dissoc base :upgrades)
        (assoc :state state
               :template template
               :effect (render template state)))))

(defn- cap
  "Max times an option may be taken at its node: a number is its own cap,
   `:repeatable false` caps at 1, `true`/absent is uncapped."
  [option]
  (let [r (get option :repeatable true)]
    (cond (number? r) r
          (false? r)  1
          :else       nil)))

(defn- available-options
  "Drop options that have hit their cap at the current node, so a one-shot (or
   N-shot) upgrade is consumed without closing off the node's other upgrades."
  [upgrades taken]
  (update upgrades :options
          (fn [options]
            (filterv (fn [{:keys [id] :as option}]
                       (let [c (cap option)]
                         (or (nil? c) (< (get taken id 0) c))))
                     options))))

(defn options-at
  "Walk `path` and return the upgrade options available as the next step, or nil
   if the mod has reached a terminal node (descended into a child node with no
   options, or consumed every remaining option at the current node). One-shot
   options taken at the current node are filtered out; a repeatable sibling keeps
   the node open."
  [base path]
  (loop [upgrades (:upgrades base)
         taken    {}
         [step & more] path]
    (if (and step upgrades)
      (let [option (find-option upgrades (:id step))]
        (if (:upgrades option)
          (recur (:upgrades option) {} more)
          (recur upgrades
                 (update taken (:id step) (fnil inc 0))
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
