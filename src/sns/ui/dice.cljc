(ns sns.ui.dice
  "The die ladder a `{{die-size n}}` template step counts along."
  (:require
    [clojure.string :as str]))

(def ^:private next-size {4 6, 6 8, 8 10, 10 12})

(defn- step
  "One rung up: grow the smallest die, or once only d12s and d4s remain, split
   a d12 into three d4s."
  [dice]
  (let [dice (vec (sort dice))]
    (if (and (= 12 (peek dice)) (every? #{4 12} dice))
      (-> dice pop (conj 4 4 4))
      (update dice 0 next-size))))

(defn- notation [dice]
  (->> (frequencies dice)
       (sort-by key >)
       (eduction (map (fn [[faces n]] (str n "d" faces))))
       (str/join "+")))

(defn- rung
  "`x` as a 1-based rung, or nil, as for a var that's blank mid-edit."
  [x]
  (let [n (if (string? x) (parse-long (str/trim x)) x)]
    (when (and (number? n) (pos? n) (== n (long n)))
      (long n))))

(defn die-size
  "The dice at rung `x` of the ladder: 1 → 1d4, 5 → 1d12, 6 → 3d4, 7 → 1d6+2d4.
   Nil for anything that isn't a rung."
  [x]
  (when-let [n (rung x)]
    (notation (nth (iterate step [4]) (dec n)))))
