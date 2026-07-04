(ns sns.builtin.data-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.builtin.data :as data]
    [sns.server.render :as render]
    [sns.spi.protocols]
    [sns.spi.schema :as schema]))

(def ^:private ctx {:render render/render :inputs {}})

(deftest single-draw-with-submods
  (testing "a single drawn entry renders title/subtitle and iterates its mods"
    (let [spec {:label    "Unique"
                :items    [{:name "Only One"                            :base "armour"
                            :mods [{:effect "Effect A" :metadata ["x"]}
                                   {:effect "Effect B"}]}]
                :title    "{{name}}"
                :subtitle "Unique · {{base}}"
                :sections [{:heading "Mods"                                   :each :mods
                            :item    {:body "{{effect}}" :metadata :metadata}}]}
          vm   (data/interpret spec ctx)]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Only One" (:loot/title vm)))
      (is (= "Unique · armour" (:loot/subtitle vm)))
      (is (= ["Effect A" "Effect B"]
             (map :item/body (-> vm :loot/sections first :section/items))))
      (testing "metadata only present when the entry declares them"
        (is (= ["x"] (-> vm :loot/sections first :section/items first :item/metadata)))
        (is (nil? (-> vm :loot/sections first :section/items second :item/metadata)))))))

(deftest multi-draw-renders-each-entry
  (testing ":each :items iterates the drawn entries"
    (let [spec {:label    "Rings"
                :items    [{:name "A" :effect "ea"} {:name "B" :effect "eb"}
                           {:name "C" :effect "ec"}]
                :take     2
                :title    "Two rings"
                :sections [{:heading "Rings"                                :each :items
                            :item    {:title "{{name}}" :body "{{effect}}"}}]}
          vm   (data/interpret spec ctx)
          items (-> vm :loot/sections first :section/items)]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Two rings" (:loot/title vm)))
      (is (= 2 (count items)))
      (is (every? :item/title items)))))

(deftest enabled-filtering-and-loaded-resource
  (testing "the shipped uniques.edn loads and produces a valid view-model"
    (let [spec (data/load-spec "data/uniques.edn")
          gen  (data/generator :uniques spec)
          vm   (sns.spi.protocols/generate gen ctx)]
      (is (schema/validate ::schema/view-model vm))
      (is (seq (:loot/title vm))))))

(deftest utility-flag-surfaces-in-loot-spec
  (let [gen (data/generator :tools {:label "Tools" :utility? true :items [{:name "x"}] :title "t"})]
    (is (true? (:utility? (sns.spi.protocols/loot-spec gen))))))

(deftest inputs-available-to-templates
  (testing "input values are interpolable in templates"
    (let [spec {:label "Greeting"
                :items [{:name "x"}]
                :title "Hail, {{who}}"}
          vm   (data/interpret spec (assoc ctx :inputs {:who "Thoros"}))]
      (is (= "Hail, Thoros" (:loot/title vm))))))
