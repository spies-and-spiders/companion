(ns sns.server.render
  "The default, swappable template renderer. Selmer interpolates a mod's state
   into its effect text. Only *cosmetic* filters live here (ordinal, percentage,
   times, dice) — level-scaling is handled upstream by the progression/state
   system, not by inline template filters as in campaign4. The one exception is
   `random`, which is a genuine per-render draw; it is registered by
   `sns.sdk.randoms` (required here so it is always available) against the rng
   the engine binds for the request."
  (:require
    [selmer.parser :as selmer]
    [selmer.util :as su]
    [sns.sdk.randoms]))

(defn- times-format [n]
  (case (long n)
    1 "once"
    2 "twice"
    3 "thrice"
    (str n " times")))

(defn- scale-dice
  "Render a magnitude `n` as exploding dice notation (ported from campaign4)."
  [n]
  (loop [n (long n)
         acc []]
    (let [modded (mod n 5)
          consume (if (zero? modded) 5 modded)
          remaining (- n consume)
          die (->> (inc consume)
                   (* 2)
                   (str "d"))]
      (if (pos? remaining)
        (->> (clojure.core/conj acc die)
             (recur remaining))
        (->> (clojure.core/conj acc die)
             frequencies
             (reduce-kv (fn [s die freq]
                          (if (seq s)
                            (str s " + " freq die)
                            (str freq die)))
                        ""))))))

(defn- percentage [n]
  (->> (double n)
       (* 100)
       (format "%.1f")))

(defn- ordinal [n]
  (let [n (long n)
        suffix (if (<= 11 (mod n 100) 13)
                 "th"
                 (case (int (mod n 10)) 1 "st" 2 "nd" 3 "rd" "th"))]
    (str n suffix)))

(declare render)

(defonce ^:private filters-registered
  (do
    (selmer/add-filter! :render-template (fn [tpl] (render tpl {})))
    (selmer/add-filter! :times times-format)
    (selmer/add-filter! :dice scale-dice)
    (selmer/add-filter! :percentage percentage)
    (selmer/add-filter! :ordinal ordinal)
    true))

(defn- regurgitate-missing [{:keys [tag-value]} _opts]
  (if tag-value (str "{{" tag-value "}}") ""))

(defn render
  "Render Selmer `template` against `context` (a map of state). Unresolved
   variables are echoed back so partially-applied templates round-trip."
  [template context]
  (assert filters-registered)
  (binding [su/*escape-variables*         false
            su/*missing-value-formatter*  regurgitate-missing]
    (selmer/render template
                   (assoc context :selmer.filter-parser/selmer-safe-filter true))))
