(ns sns.social
  (:require
    [clojure.string :as str]))

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

(defn present-bonuses [characters skill]
  (->> (vals characters)
       (filter :present?)
       (mapv skill)))

(defn snapshot
  "The tracker's UI state: character rows (sorted by name) plus the two group
   bonuses over the present characters."
  [characters]
  {:characters (->> characters
                    (sort-by key)
                    (mapv (fn [[char-name c]]
                            (-> (select-keys c [:deception :persuasion :present?])
                                (assoc :name char-name)))))
   :deception  (group-bonus (present-bonuses characters :deception))
   :persuasion (group-bonus (present-bonuses characters :persuasion))})

(defn rows->characters
  "Invert a snapshot's :characters rows back into the characters map."
  [rows]
  (into {}
        (map (juxt :name #(select-keys % [:deception :persuasion :present?])))
        rows))

(defn normalise-character
  "Coerce a raw form/request character into a `[name character]` map entry (new
   and updated characters start present). Nil when the name is blank."
  [{char-name :name :keys [deception persuasion]}]
  (when-let [char-name (some-> char-name str/trim not-empty)]
    [char-name {:deception  (parse-bonus deception)
                :persuasion (parse-bonus persuasion)
                :present?   true}]))

(defn toggle [characters char-name]
  (cond-> characters
          (contains? characters char-name)
          (update-in [char-name :present?] not)))

(defn remove-character [characters char-name]
  (dissoc characters char-name))

(defn roll-result
  "The outcome of rolling `die` (a 1-20 result) for `skill` against the party."
  [characters skill die]
  (let [bonus (group-bonus (present-bonuses characters skill))]
    {:skill skill :die die :bonus bonus :total (+ die bonus)}))
