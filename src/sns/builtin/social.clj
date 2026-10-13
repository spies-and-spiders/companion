(ns sns.builtin.social
  "A group Deception & Persuasion tracker. The DM keeps a row per character with
   their bonuses and whether they're present, and the plugin rolls 1d20 plus the
   group bonus."
  (:require
    [randy.rng :as rng]
    [sns.sdk.protocols :as p]))

(def ^:private skills
  (array-map :deception "Deception" :persuasion "Persuasion"))

(defn- tidy
  "Render whole numbers as +7 rather than +7.0."
  [n]
  (cond-> n (zero? (rem n 1)) long))

(defn group-bonus
  "The mean of `bonuses`, with the top two counted twice."
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
  "The group bonuses, their roll buttons, and the last roll if any. The roster
   is rendered by the manual-state editor."
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
  [{:keys [id]}]
  (let [id   (or id :social)
        spec {:generate-label "Show group bonuses"
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
      p/Generator
      (loot-spec [_] spec)
      (generate [_ {:keys [store]}]
        (view id (p/read-collection store id) nil))
      p/Action
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
