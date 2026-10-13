(ns sns.ui.link
  "Turns markdown `[text](url)` links into anchors, so the same text links in
   the UI and in Discord."
  (:require
    [clojure.string :as str]))

(def ^:private pattern #"\[([^\]]+)\]\((https?://[^)\s]+)\)")

(defn linkify
  "`s` split into strings and `[:a …]` elements, as a seq so replicant flattens
   it into its parent."
  [s]
  (loop [s s out []]
    (if-let [[m text href] (re-find pattern s)]
      (let [i (str/index-of s m)]
        (recur (subs s (+ i (count m)))
               (cond-> out
                       (pos? i) (conj (subs s 0 i))
                       :always  (conj [:a {:href href :target "_blank" :rel "noopener noreferrer"} text]))))
      (seq (cond-> out (seq s) (conj s))))))
