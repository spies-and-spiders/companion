(ns sns.server.social
  (:require
    [clojure.string :as str]
    [randy.rng :as rng]
    [sns.spi.protocols :as p]))

(def ^:private collection :__social)
(def ^:private doc-id "characters")

(defn group-bonus [bonuses]
  (if (empty? bonuses)
    0
    (let [top-two (take 2 (sort > bonuses))
          total   (+ (reduce + bonuses) (reduce + top-two))]
      (long (Math/floor (/ total (+ (count bonuses) (count top-two))))))))

(defn- parse-bonus
  "Bonuses arrive as strings from the UI form; accept numbers too. Blank or
   unparseable values count as +0."
  [v]
  (or (cond
        (number? v) (long v)
        (string? v) (parse-long (str/trim v)))
      0))

(defn- present-bonuses [characters skill]
  (->> (vals characters)
       (filter :present?)
       (mapv skill)))

(defn snapshot [store]
  (let [characters (p/fetch store collection doc-id)]
    {:characters (->> characters
                      (sort-by key)
                      (mapv (fn [[char-name c]]
                              (-> (select-keys c [:deception :persuasion :present?])
                                  (assoc :name char-name)))))
     :deception  (group-bonus (present-bonuses characters :deception))
     :persuasion (group-bonus (present-bonuses characters :persuasion))}))

(defn upsert! [store {char-name :name :keys [deception persuasion]}]
  (let [char-name (some-> char-name str/trim not-empty)]
    (when-not char-name
      (throw (ex-info "Character name is required" {})))
    (p/update! store collection doc-id
               #(assoc % char-name {:deception  (parse-bonus deception)
                                    :persuasion (parse-bonus persuasion)
                                    :present?   true}))
    (snapshot store)))

(defn toggle!
  "Tick/untick a character's presence at the session. Returns the new snapshot."
  [store char-name]
  (p/update! store collection doc-id
             (fn [characters]
               (cond-> characters
                       (contains? characters char-name)
                       (update-in [char-name :present?] not))))
  (snapshot store))

(defn remove! [store char-name]
  (p/update! store collection doc-id #(dissoc % char-name))
  (snapshot store))

(defn roll
  "Roll 1d20 + the group bonus for `skill` (:deception or :persuasion). Returns
   the snapshot with the result under :roll."
  [store rng skill]
  (let [skill (keyword skill)]
    (when-not (contains? #{:deception :persuasion} skill)
      (throw (ex-info "Unknown skill" {:skill skill})))
    (let [characters (p/fetch store collection doc-id)
          bonus      (group-bonus (present-bonuses characters skill))
          die        (rng/next-int rng 1 21)]
      (assoc (snapshot store)
             :roll {:skill skill :die die :bonus bonus :total (+ die bonus)}))))
