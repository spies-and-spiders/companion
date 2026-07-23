(ns sns.builtin.data-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [randy.rng]
    [sns.builtin.data :as data]
    [sns.server.render :as render]
    [sns.spi.protocols]
    [sns.spi.schema :as schema]))

(def ^:private ctx {:render render/render :inputs {}})

(deftest single-draw-with-submods
  (testing "a single drawn entry renders title/subtitle and iterates its mods"
    (let [spec {:label    "Unique"
                :items    [{:name "Only One"
                            :base "armour"
                            :mods [{:effect "Effect A" :metadata ["x"]}
                                   {:effect "Effect B"}]}]
                :title    "{{name}}"
                :subtitle "Unique · {{base}}"
                :sections [{:heading "Mods"
                            :each    :mods
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

(deftest enabled-filtering-and-loaded-from-file
  (testing "the shipped uniques.edn loads and produces a valid view-model"
    (let [spec (data/load-spec "data/uniques.edn")
          gen  (data/generator :uniques spec)
          vm   (sns.spi.protocols/generate gen ctx)]
      (is (schema/validate ::schema/view-model vm))
      (is (seq (:loot/title vm))))))

(deftest json-spec-coerces-keyword-positions
  (testing "a .json spec decodes the positions the DSL reads as keywords"
    (let [spec (data/load-spec "test/resources/json-spec.json")]
      (is (= :mods (-> spec :sections first :each)))
      (is (= :tags (-> spec :sections first :item :metadata)))
      (is (= [:who :text] (-> spec :inputs first ((juxt :id :type)))))
      (testing "and renders as an EDN spec would"
        (let [vm (data/interpret spec (assoc ctx :inputs {:who "Thoros"}))]
          (is (schema/validate ::schema/view-model vm))
          (is (= "Only One for Thoros" (:loot/title vm)))
          (is (= [{:item/body "Effect A" :item/metadata ["accuracy"]}]
                 (-> vm :loot/sections first :section/items))))))))

(deftest utility-flag-surfaces-in-loot-spec
  (let [gen (data/generator :tools {:label "Tools" :utility? true :items [{:name "x"}] :title "t"})]
    (is (true? (:utility? (sns.spi.protocols/loot-spec gen))))))

(deftest honours-injected-rng
  (testing "draws use the rng threaded through the context, not the global default"
    (let [spec {:label "Pick" :items [{:name "A"} {:name "B"} {:name "C"}] :title "{{name}}"}
          ;; a stub rng that always returns index 2 -> "C"
          rng  (reify randy.rng/RandomNumberGenerator
                 (next-int [_ _] 2)
                 (next-int [_ _ _] 2))
          vm   (data/interpret spec (assoc ctx :rng rng))]
      (is (= "C" (:loot/title vm))))))

(deftest inputs-available-to-templates
  (testing "input values are interpolable in templates"
    (let [spec {:label "Greeting"
                :items [{:name "x"}]
                :title "Hail, {{who}}"}
          vm   (data/interpret spec (assoc ctx :inputs {:who "Thoros"}))]
      (is (= "Hail, Thoros" (:loot/title vm))))))
