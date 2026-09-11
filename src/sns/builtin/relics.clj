(ns sns.builtin.relics
  "A stateful built-in loot type: relics that are generated, persisted, and
   ranked up over time. Each relic carries a mod whose vars hold their own
   ranks, and its template — with `{{#gte}}` for the parts that appear
   once a var is high enough — is rendered in the browser against them. This
   exercises the full Store + LootAction loop.

   Also the worked example of collection-based state: each relic is one entry in
   the `:relics` collection, keyed by its id, with its ranks a plain map, read
   through the `Store` and written by declaring `:store/mutations` on the
   view-model. The static `:mod` is looked up from the templates below by name
   rather than persisted at all."
  (:require
    [randy.core :as r]
    [sns.sdk.protocols :as p]
    [sns.sdk.rank :as rank]
    [sns.sdk.vars :as vars]))

(def ^:private loot-id :relics)
(def ^:private coll :relics)

(def ^:private templates
  "Starting relics. Each `:mod`'s vars rank up by themselves (see
   `sns.sdk.rank`); `:max-ranks` caps what the relic may spend across all of
   them."
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

(defn- read-relic
  "The stored relic, rehydrated for the view-model and ranking up. The `:mod` is
   static template data, so it comes from `templates` rather than the store; only
   the ranks the DM has taken are persisted."
  [store id]
  (when-let [{:keys [name ranks] :as relic} (get (p/read-collection store coll) id)]
    (assoc relic :id id :mod (:mod (template-by-name name)) :ranks (or ranks {}))))

(defn- relic-vars
  "The relic's vars at the ranks it has reached: the mod's declared vars,
   resolved, with the stored rank put back on each."
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

(defn- persisting
  "`view-model` carrying the write that stores `relic`. Declared rather than
   performed, so a view-model that fails validation stores nothing."
  [relic view-model]
  (assoc view-model :store/mutations
         {coll {(:id relic) (select-keys relic [:name :base :ranks])}}))

(defn- take-rank
  "Take one rank on `choice`, or nil when it is not an option the relic can
   still take. The ranks live in the store rather than on the vars, since the
   mod itself is static template data re-read on every action."
  [rng relic choice]
  (when (some #{choice} (rank/available (relic-vars rng relic) (-> relic :mod :max-ranks)))
    (update-in relic [:ranks choice] (fnil inc 1))))

(defn generator
  [_plugin]
  (reify
    p/LootGenerator
    (loot-spec [_]
      {:id loot-id :label "Relic"})
    (generate [_ {:keys [rng]}]
      (let [template (r/sample rng templates)
            relic    (assoc template :id (str (random-uuid)) :ranks {})]
        (persisting relic (view-model rng relic))))
    p/LootAction
    (handle-action [_ {:keys [store rng]} action {:keys [relic-id choice]}]
      (case action
        :rank-up
        (let [relic (or (read-relic store relic-id)
                        (throw (ex-info "Unknown relic" {:relic-id relic-id})))]
          (if-let [updated (take-rank rng relic choice)]
            (persisting updated (view-model rng updated))
            ;; choice unavailable (or the relic is capped out): re-show as it is
            (view-model rng relic)))))))
