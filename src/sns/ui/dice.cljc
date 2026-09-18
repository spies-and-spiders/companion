(ns sns.ui.dice
  "The die ladder a `{{die-size n}}` template step counts along."
  (:require
    [clojure.string :as str]))

(def ^:private next-size {4 6, 6 8, 8 10, 10 12})

(defn- step
  "One rung up: the smallest die grows a size, or — with nothing left to grow —
   a d12 splits into three d4, which keeps the faces and reopens the ladder.
   Only d4s are left beside a splitting d12, so a pool of both is mid-split and
   keeps splitting; a d12 alongside anything else still has a die to grow."
  [dice]
  (let [dice (vec (sort dice))]
    (if (and (= 12 (peek dice)) (every? #{4 12} dice))
      (-> dice pop (conj 4 4 4))
      (update dice 0 next-size))))

(defn- notation [dice]
  (->> (frequencies dice)
       (sort-by key >)
       (map (fn [[faces n]] (str n "d" faces)))
       (str/join "+")))

(defn- rung
  "`x` as a 1-based rung, or nil if it is not one: a template may hand over a
   var mid-edit, holding a blank or prose."
  [x]
  (let [n (if (string? x) (parse-long (str/trim x)) x)]
    (when (and (number? n) (pos? n) (== n (long n)))
      (long n))))

(defn die-size
  "The dice at rung `x` of the ladder: 1 → 1d4, 5 → 1d12, 6 → 3d4, 7 → 1d6+2d4.
   Nil for anything that is not a rung, which a template renders as nothing."
  [x]
  (when-let [n (rung x)]
    (notation (nth (iterate step [4]) (dec n)))))
