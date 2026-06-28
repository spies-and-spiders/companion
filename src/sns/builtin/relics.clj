(ns sns.builtin.relics
  "A stateful built-in loot type: relics that are generated, persisted, and
   levelled up over time. Each relic carries an upgrade-graph mod and a persisted
   path of choices/rolls; its effect text is always derived from that path. This
   exercises the full Store + Progression + LootAction loop."
  (:require
    [randy.core :as r]
    [sns.server.progression :as progression]
    [sns.spi.protocols :as p]))

(def ^:private collection :relics)

(def ^:private templates
  "Starting relics. Each `:mod` is an upgrade-graph (see the schema)."
  [{:name "Aegis of the Vow"
    :base "armour"
    :mod  {:state    {:ab 1}
           :template "+{{ab}} AB with effects that cannot deal damage."
           :upgrades {:select  :choice
                      :options [{:id :precise :repeatable true :inc {:ab 1}}
                                {:id             :elemental
                                 :roll           {:dmg [1 6]}
                                 :assoc-template "+{{ab}} AB; deal {{dmg}} fire damage on hit."}]}}}
   {:name "Wanderer's Compass"
    :base "trinket"
    :mod  {:state    {:range 30}
           :template "Teleport up to {{range}} feet as a bonus action."
           :upgrades {:select  :choice
                      :options [{:id :far :repeatable true :inc {:range 15}}
                                {:id :swift :assoc-template "Teleport up to {{range}} feet as a free action."}]}}}])

(defn- option->action [relic-id {:keys [id]}]
  {:action/label (str "Upgrade: " (name id))
   :action/event [:loot/action {:id     collection
                                :action :level-up
                                :params {:relic-id relic-id :choice id}}]})

(defn- view-model [progression {:keys [id name base mod path]}]
  (let [{:keys [effect]} (p/current-state progression mod path)
        options (p/level-options progression mod path)
        actions (when options
                  (if (= :choice (:select options))
                    (mapv #(option->action id %) (:options options))
                    [{:action/label "Level up"
                      :action/event [:loot/action {:id     collection
                                                   :action :level-up
                                                   :params {:relic-id id}}]}]))]
    (cond-> {:loot/title    name
             :loot/subtitle (str "Relic · " base " · level " (inc (count path)))
             :loot/sections [{:section/heading "Effect"
                              :section/items   [{:item/body effect}]}]}
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
        (update relic :path conj (progression/roll-option rng option))))))

(defn generator
  [_plugin]
  (reify
    p/LootGenerator
    (loot-spec [_]
      {:id collection :label "Relic" :stateful? true})
    (generate [_ {:keys [rng store progression]}]
      (let [template (r/sample rng templates)
            relic    (assoc template :id (str (random-uuid)) :path [])]
        (p/put! store collection (:id relic) relic)
        (view-model progression relic)))

    p/LootAction
    (action-spec [_]
      {:level-up {:label "Level up" :params [:relic-id :choice]}})
    (handle-action [_ {:keys [store rng progression]} action {:keys [relic-id choice]}]
      (case action
        :level-up
        (let [relic (or (p/fetch store collection relic-id)
                        (throw (ex-info "Unknown relic" {:relic-id relic-id})))]
          (if-let [updated (take-step rng progression relic choice)]
            (do (p/put! store collection relic-id updated)
                (view-model progression updated))
            ;; choice required but not supplied (or terminal): re-show current state
            (view-model progression relic)))))))
