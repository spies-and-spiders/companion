(ns sns.ui.link-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.ui.link :as link]))

(defn- a [text href]
  [:a {:href href :target "_blank" :rel "noopener noreferrer"} text])

(deftest linkify
  (testing "text without links is left whole"
    (is (= ["Plain +1 sword."] (link/linkify "Plain +1 sword."))))
  (testing "links are split out of the surrounding text"
    (is (= ["See " (a "Vorpal" "https://x.test/v") " and " (a "Keen" "http://x.test/k") "."]
           (link/linkify "See [Vorpal](https://x.test/v) and [Keen](http://x.test/k)."))))
  (testing "a link alone is just the anchor"
    (is (= [(a "Vorpal" "https://x.test/v")] (link/linkify "[Vorpal](https://x.test/v)"))))
  (testing "non-http schemes stay as text"
    (is (= ["[x](javascript:alert(1))"] (link/linkify "[x](javascript:alert(1))")))))
