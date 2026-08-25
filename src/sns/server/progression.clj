(ns sns.server.progression
  "Default `Progression`: interprets the upgrade-graph DSL. Upgrades transform a
   mod's *variables*: the effect text stays the mod's template, rendered in the
   browser against those variables, so text that varies with an upgrade is a
   `{{#if flag}}` the graph switches with `:enable`/`:disable`. This is what
   makes choosing the same option N times well-defined: the vars are re-derived
   by replaying the path from the mod's **declared** starting values. Replaying
   it over vars that already reflect it counts every upgrade twice, so a plugin
   carrying resolved vars forward steps them with
   `sns.sdk.progression/apply-ops` as each option is chosen.

   The graph walk lives here; the *ops* an option may carry are the open
   vocabulary in `sns.sdk.progression`, so a plugin extends what an upgrade can
   do without replacing this interpreter."
  (:require
    [sns.sdk.progression :as sp]
    [sns.sdk.protocols :as p]
    [sns.sdk.vars :as vars]))

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

(defn derive-vars
  "Fold the chosen `path` over `base` mod's variables. Returns the final
   resolved `sns.sdk.schema/item-vars`.

   `base`'s vars may be *declared* (a literal, or a `{:random …}` spec) or
   already resolved; either way they are resolved once, with `rng`, before the
   path is folded — so an upgrade's `:inc` lands on the drawn value."
  [rng base path]
  (loop [vars     (vars/resolve-vars rng (:vars base))
         upgrades (:upgrades base)
         [step & more] path]
    (if step
      (let [option (or (find-option upgrades (:id step))
                       (throw (ex-info "Unknown upgrade option"
                                       {:id        (:id step)
                                        :available (mapv :id (:options upgrades))})))]
        (recur (sp/apply-ops rng vars option) (next-upgrades upgrades option) more))
      vars)))

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
  "Construct the default Progression, bound to the request's `rng` (needed to
   resolve a mod's declared `{:random …}` vars on first derivation)."
  [rng]
  (reify p/Progression
    (current-state [_ mod path] (derive-vars rng mod path))
    (level-options [_ mod path] (options-at mod path))))
