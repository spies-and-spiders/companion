(ns sns.builtin.relics
  "Relics that are generated, persisted and ranked up over time: a worked
   example of the Store and Action loop. Only each relic's name, base and ranks
   are stored; its mod is looked up from `templates` by name."
  (:require
    [randy.core :as r]
    [sns.sdk.protocols :as p]
    [sns.sdk.rank :as rank]
    [sns.sdk.vars :as vars]))

(def ^:private loot-id :relics)
(def ^:private coll :relics)

(def ^:private templates
  [{:name "Aegis of the Vow"
    :base "armour"
    :mod  {:vars      {:ab 1}
           :template  "+{{ab}} AB with effects that cannot deal damage.{{#gte ab 3}} Deals 6 fire damage on hit.{{/gte}}"
           :max-ranks 4}}
   {:name "Wanderer's Compass"
    :base "trinket"
    :mod  {:vars      {:range {:value 30 :step 15}}
           :template  "Teleport up to {{range}} feet as a {{#gte range 60}}free{{else}}bonus{{/gte}} action."
           :max-ranks 3}}])

(def ^:private template-by-name
  (into {} (map (juxt :name identity)) templates))

(defn- read-relic [store id]
  (when-let [{:keys [name ranks] :as relic} (get (p/read-collection store coll) id)]
    (assoc relic :id id :mod (:mod (template-by-name name)) :ranks (or ranks {}))))

(defn- relic-vars
  "The mod's vars, resolved, at the relic's stored ranks."
  [rng {:keys [mod ranks]}]
  (reduce-kv (fn [vars id rank] (cond-> vars (contains? vars id) (assoc-in [id :rank] rank)))
             (vars/resolve-vars rng (:vars mod))
             ranks))

(defn- option->action [relic-id id]
  {:action/label (str "Upgrade: " (vars/humanise-label id))
   :action/event [:loot/action {:id     loot-id
                                :action :rank-up
                                :params {:relic-id relic-id :choice id}}]})

(defn- view-model [rng {:keys [id name base mod] :as relic}]
  (let [vars    (relic-vars rng relic)
        options (rank/available vars (:max-ranks mod))]
    (cond-> {:loot/title    name
             :loot/subtitle (str "Relic · " base " · rank " (rank/mod-rank vars))
             :loot/sections [{:section/heading "Effect"
                              :section/items   [(cond-> {:item/body (:template mod)}
                                                        (seq vars) (assoc :item/vars vars))]}]}
            (seq options) (assoc :loot/actions (mapv #(option->action id %) options)))))

(defn- persisting [relic view-model]
  (assoc view-model :store/mutations
         {coll {(:id relic) (select-keys relic [:name :base :ranks])}}))

(defn- take-rank
  "Rank `choice` up once, or nil if the relic can't."
  [rng relic choice]
  (when (some #{choice} (rank/available (relic-vars rng relic) (-> relic :mod :max-ranks)))
    (update-in relic [:ranks choice] (fnil inc 1))))

(defn generator
  [_plugin]
  (reify
    p/Generator
    (loot-spec [_] {})
    (generate [_ {:keys [rng]}]
      (let [template (r/sample rng templates)
            relic    (assoc template :id (str (random-uuid)) :ranks {})]
        (persisting relic (view-model rng relic))))
    p/Action
    (handle-action [_ {:keys [store rng]} action {:keys [relic-id choice]}]
      (case action
        :rank-up
        (let [relic (or (read-relic store relic-id)
                        (throw (ex-info "Unknown relic" {:relic-id relic-id})))]
          (if-let [updated (take-rank rng relic choice)]
            (persisting updated (view-model rng updated))
            (view-model rng relic)))))))
