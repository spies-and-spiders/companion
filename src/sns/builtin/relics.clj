(ns sns.builtin.relics
  "A stateful built-in loot type: relics that are generated, persisted, and
   levelled up over time. Each relic carries an upgrade-graph mod and a persisted
   path of choices; its variables are always derived from that path, and its
   template, with `{{#if}}` for the parts an upgrade switches on, is rendered in
   the browser against them. This exercises the full Store + Progression +
   LootAction loop.

   Also the worked example of collection-based state: each relic is one entry in
   the `:relics` collection, keyed by its id, with the path a plain vector, read
   through the `Store` and written by declaring `:store/mutations` on the
   view-model. The static upgrade-graph `:mod` is looked up from the templates
   below by name rather than persisted at all."
  (:require
    [randy.core :as r]
    [sns.sdk.protocols :as p]))

(def ^:private loot-id :relics)
(def ^:private coll :relics)

(def ^:private templates
  "Starting relics. Each `:mod` is an upgrade-graph (see the schema)."
  [{:name "Aegis of the Vow"
    :base "armour"
    :mod  {:vars     {:ab 1 :fire false}
           :template "+{{ab}} AB with effects that cannot deal damage.{{#if fire}} Deals 6 fire damage on hit.{{/if}}"
           :upgrades {:select  :choice
                      :options [{:id :precise :inc {:ab 1}}
                                {:id :elemental :repeatable false :enable [:fire]}]}}}
   {:name "Wanderer's Compass"
    :base "trinket"
    :mod  {:vars     {:range 30 :swift false}
           :template "Teleport up to {{range}} feet as a {{#if swift}}free{{else}}bonus{{/if}} action."
           :upgrades {:select  :choice
                      :options [{:id :far :inc {:range 15}}
                                {:id :swift :repeatable false :enable [:swift]}]}}}])

(def ^:private template-by-name
  (into {} (map (juxt :name identity)) templates))

(defn- read-relic
  "The stored relic, rehydrated for the view-model and upgrade logic. The `:mod`
   graph is static template data, so it comes from `templates` rather than the
   store."
  [store id]
  (when-let [{:keys [name] :as relic} (get (p/read-collection store coll) id)]
    (assoc relic :id id :mod (:mod (template-by-name name)))))

(defn- option->action [relic-id {:keys [id]}]
  {:action/label (str "Upgrade: " (name id))
   :action/event [:loot/action {:id     loot-id
                                :action :level-up
                                :params {:relic-id relic-id :choice id}}]})

(defn- view-model [progression {:keys [id name base mod path]}]
  (let [vars    (p/current-state progression mod path)
        options (p/level-options progression mod path)
        actions (when options
                  (if (= :choice (:select options))
                    (mapv #(option->action id %) (:options options))
                    [{:action/label "Level up"
                      :action/event [:loot/action {:id     loot-id
                                                   :action :level-up
                                                   :params {:relic-id id}}]}]))]
    (cond-> {:loot/title    name
             :loot/subtitle (str "Relic · " base " · level " (inc (count path)))
             :loot/sections [{:section/heading "Effect"
                              :section/items   [(cond-> {:item/body (:template mod)}
                                                        (seq vars) (assoc :item/vars vars))]}]}
            (seq actions) (assoc :loot/actions actions))))

(defn- take-step
  "Apply one upgrade step to `relic`, choosing `choice` (an option id) or, for a
   :random node, sampling. Returns the updated relic, or nil when a choice is
   required but none was supplied."
  [rng progression relic choice]
  (let [{:keys [select options] :as node} (p/level-options progression (:mod relic) (:path relic))]
    (when node
      (when-let [option (cond
                          choice (some #(when (= choice (:id %)) %) options)
                          (= :random select) (r/sample rng options)
                          (= :all select) (first options) ; :all handled as sequential steps
                          :else nil)]
        (update relic :path conj {:id (:id option)})))))

(defn- persisting
  "`view-model` carrying the write that stores `relic`. Declared rather than
   performed, so a view-model that fails validation stores nothing."
  [relic view-model]
  (assoc view-model :store/mutations
         {coll {(:id relic) (select-keys relic [:name :base :path])}}))

(defn generator
  [_plugin]
  (reify
    p/LootGenerator
    (loot-spec [_]
      {:id loot-id :label "Relic"})
    (generate [_ {:keys [rng progression]}]
      (let [template (r/sample rng templates)
            relic    (assoc template :id (str (random-uuid)) :path [])]
        (persisting relic (view-model progression relic))))
    p/LootAction
    (handle-action [_ {:keys [store rng progression]} action {:keys [relic-id choice]}]
      (case action
        :level-up
        (let [relic (or (read-relic store relic-id)
                        (throw (ex-info "Unknown relic" {:relic-id relic-id})))]
          (if-let [updated (take-step rng progression relic choice)]
            (persisting updated (view-model progression updated))
            ;; choice required but not supplied (or terminal): re-show current state
            (view-model progression relic)))))))
