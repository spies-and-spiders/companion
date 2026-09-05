(ns sns.builtin.social
  "The group Deception & Persuasion tracker, as a plugin over a manual state
   collection. The DM keeps a row per character — two bonuses and whether
   they're at the table — and the plugin's one action rolls 1d20 plus the
   group bonus over whoever is present."
  (:require
    [randy.rng :as rng]
    [sns.sdk.protocols :as p]))

(def ^:private skills
  (array-map :deception "Deception" :persuasion "Persuasion"))

(defn- tidy
  "Keeps fractions exactly as they are, but drops the pointless decimal on whole
   values so they render as +7 rather than +7.0."
  [n]
  (cond-> n (zero? (rem n 1)) long))

(defn group-bonus
  "The party's bonus for a skill: the mean over everyone present, with the top
   two counted twice so a strong speaker pulls the group up."
  [bonuses]
  (if (empty? bonuses)
    0
    (let [top-two (take 2 (sort > bonuses))
          total   (+ (reduce + bonuses) (reduce + top-two))]
      (tidy (/ (double total) (+ (count bonuses) (count top-two)))))))

(defn- present-bonuses [characters skill]
  (into [] (comp (filter :present?) (map skill) (map #(or % 0))) (vals characters)))

(defn- bonus-str [n]
  (str (when-not (neg? n) "+") n))

(defn- roll-item [{:keys [skill die bonus total]}]
  {:item/title (str (get skills skill) " check: " total)
   :item/body  (str "Rolled " die " " (bonus-str bonus)
                    (case (long die) 1 " · natural 1!" 20 " · natural 20!" ""))})

(defn- view
  "The tracker's view-model: the two group bonuses, a roll button each, and the
   last roll when there is one. The roster itself is the manual-state editor's
   to render, so it is deliberately absent here."
  [id characters roll]
  (let [bonus   #(group-bonus (present-bonuses characters %))
        present (count (filter :present? (vals characters)))]
    (cond-> {:loot/title    "Group Deception & Persuasion"
             :loot/subtitle (str present "/" (count characters) " present")
             :loot/sections [{:section/heading "Group bonuses"
                              :section/items   (mapv (fn [[skill label]]
                                                       {:item/title label
                                                        :item/body  (str "1d20 " (bonus-str (bonus skill)))})
                                                     skills)}]
             :loot/actions  (mapv (fn [[skill label]]
                                    {:action/label (str "Roll " label)
                                     :action/event [:loot/action {:id     id
                                                                  :action :roll
                                                                  :params {:skill skill}}]})
                                  skills)}
            roll (update :loot/sections conj {:section/heading "Last roll"
                                              :section/items   [(roll-item roll)]}))))

(defn generator
  "Build the tracker from its `:builtin` plugin config entry. The characters live
   in one manual-state collection named after the plugin's `:id`."
  [{:keys [id label]}]
  (let [id   (or id :social)
        spec {:id             id
              :label          (or label "Group Social")
              :utility?       true
              :generate-label "Show group bonuses"
              :store/manual   {:key-label "Character"
                               :fields    [{:id      :deception
                                            :label   "Deception"
                                            :type    :decimal
                                            :default 0}
                                           {:id      :persuasion
                                            :label   "Persuasion"
                                            :type    :decimal
                                            :default 0}
                                           {:id      :present?
                                            :label   "Present"
                                            :type    :bool
                                            :default true}]}}]
    (reify
      p/LootGenerator
      (loot-spec [_] spec)
      (generate [_ {:keys [store]}]
        (view id (p/read-collection store id) nil))
      p/LootAction
      (handle-action [_ {:keys [store rng]} action {:keys [skill]}]
        (when-not (= :roll action)
          (throw (ex-info "Unknown action" {:action action})))
        (let [skill (keyword skill)]
          (when-not (contains? skills skill)
            (throw (ex-info "Unknown skill" {:skill skill :known (vec (keys skills))})))
          (let [characters (p/read-collection store id)
                die        (rng/next-int rng 1 21)
                bonus      (group-bonus (present-bonuses characters skill))]
            (view id characters {:skill skill :die die :bonus bonus :total (+ die bonus)})))))))
