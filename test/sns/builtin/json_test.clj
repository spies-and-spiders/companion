(ns sns.builtin.json-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.builtin.json :as json]
    [sns.server.render :as render]
    [sns.spi.schema :as schema]))

(def ^:private ctx {:render render/render :inputs {}})

(deftest single-draw-with-submods
  (testing "a single drawn entry renders title/subtitle and iterates its mods"
    (let [spec {:label    "Unique"
                :tables   {:uniques [{:name "Only One"                        :base "armour"
                                      :mods [{:effect "Effect A" :tags ["x"]}
                                             {:effect "Effect B"}]}]}
                :draw     {:from :uniques :take 1}
                :title    "{{name}}"
                :subtitle "Unique · {{base}}"
                :sections [{:heading "Mods"                           :each :mods
                            :item    {:body "{{effect}}" :tags :tags}}]}
          vm   (json/interpret spec ctx)]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Only One" (:loot/title vm)))
      (is (= "Unique · armour" (:loot/subtitle vm)))
      (is (= ["Effect A" "Effect B"]
             (map :item/body (-> vm :loot/sections first :section/items))))
      (testing "tags only present when the entry declares them"
        (is (= ["x"] (-> vm :loot/sections first :section/items first :item/tags)))
        (is (nil? (-> vm :loot/sections first :section/items second :item/tags)))))))

(deftest multi-draw-renders-each-entry
  (testing ":each :draw iterates the drawn entries"
    (let [spec {:label    "Rings"
                :tables   {:rings [{:name "A" :effect "ea"} {:name "B" :effect "eb"}
                                   {:name "C" :effect "ec"}]}
                :draw     {:from :rings :take 2}
                :title    "Two rings"
                :sections [{:heading "Rings"                                :each :draw
                            :item    {:title "{{name}}" :body "{{effect}}"}}]}
          vm   (json/interpret spec ctx)
          items (-> vm :loot/sections first :section/items)]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Two rings" (:loot/title vm)))
      (is (= 2 (count items)))
      (is (every? :item/title items)))))

(deftest enabled-filtering-and-loaded-resource
  (testing "the shipped uniques.edn loads and produces a valid view-model"
    (let [spec (json/load-spec "data/uniques.edn")
          gen  (json/generator :uniques spec)
          vm   (sns.spi.protocols/generate gen ctx)]
      (is (schema/validate ::schema/view-model vm))
      (is (seq (:loot/title vm))))))

(deftest inputs-available-to-templates
  (testing "input values are interpolable in templates"
    (let [spec {:label  "Greeting"
                :tables {:t [{:name "x"}]}
                :draw   {:from :t}
                :title  "Hail, {{who}}"}
          vm   (json/interpret spec (assoc ctx :inputs {:who "Thoros"}))]
      (is (= "Hail, Thoros" (:loot/title vm))))))
