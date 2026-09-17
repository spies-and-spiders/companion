(ns sns.ui.link
  "Rendered template text to hiccup children, turning Discord-style `[text](url)`
   into anchors so a link written once shows in the UI and in a Discord report."
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
