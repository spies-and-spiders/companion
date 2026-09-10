(ns sns.sdk.protocols-test
  "The Java <-> Clojure bridge. A JVM-language plugin builds `Models` records
   rather than namespaced maps, so anything those records cannot carry is
   silently lost — these tests pin that they carry the schemas in full, in both
   directions (out to a generator's result, back in to a `Reporter` or to the
   view-model an action is handed)."
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.sdk.schema :as schema])
  (:import
    (sns.sdk Models$Action Models$Field Models$Item Models$ItemVar
             Models$LootSpec Models$ManualState Models$Section Models$ViewModel)))

(def ^:private view-model->clj #'p/view-model->clj)
(def ^:private clj->view-model #'p/clj->view-model)
(def ^:private loot-spec->clj #'p/loot-spec->clj)

(def ^:private java-view-model
  "Every optional corner of `::view-model`, built the way a Java plugin would."
  (Models$ViewModel.
    "Ember {{ n }}" "Unique"
    [(Models$Section. "Mods"
                      [(Models$Item. "Ring" "+{{ dmg }} fire" ["rare"]
                                     {"dmg" (Models$ItemVar. 3 "int" "Damage" "uniform"
                                                             {"lo" 1 "hi" 6} [1 2 3] false)})])]
    [(Models$Action. "Level up" "level-up" {"by" 1})]
    {"n" (Models$ItemVar. 2 "int")}
    ["yard" "congenial"]
    {"tier" 2}
    {"relics" {"Quincy" {"flares" 3} "Viktor" nil}}))

(deftest java-view-model-carries-the-whole-schema
  (let [vm (view-model->clj :ember java-view-model)]
    (is (schema/validate ::schema/view-model vm))
    (testing "the fields a Models.ViewModel used to drop"
      (is (= {:n {:value 2 :type :int}} (:loot/vars vm)))
      (is (= ["yard" "congenial"] (:loot/words vm)))
      (is (= {"tier" 2} (:loot/state vm)) "opaque, so it travels untouched")
      (is (= {:relics {"Quincy" {"flares" 3} "Viktor" nil}} (:store/mutations vm))
          "collection names keywordise; the keys within a collection do not"))
    (testing "an item's vars, with every optional component"
      (is (= {:dmg {:value   3
                    :type    :int
                    :label   "Damage"
                    :random  :uniform
                    :args    {:lo 1 :hi 6}
                    :options [1 2 3]}}
             (-> vm :loot/sections first :section/items first :item/vars))))
    (testing "an action becomes an event routed back to this plugin"
      (is (= [:loot/action {:id :ember :action :level-up :params {"by" 1}}]
             (-> vm :loot/actions first :action/event))))))

(deftest view-model-round-trips-through-clojure
  (testing "Java -> Clojure -> Java lands on the same record, so what a Reporter
            (or an action's :view-model) receives is what the generator returned"
    (is (= java-view-model (clj->view-model (view-model->clj :ember java-view-model))))))

(deftest java-loot-spec-carries-the-whole-schema
  (let [spec (loot-spec->clj
               (Models$LootSpec. "relics" "Relic"
                                 [(Models$Field. "who" "Who" "text" nil nil true)]
                                 true "Add character" ["relics" "tally"]
                                 (Models$ManualState. "Character" true
                                                      [(Models$Field. "chance" "Chance" "int")])
                                 "always"))]
    (is (schema/validate ::schema/loot-spec spec))
    (is (= {:id                :relics
            :label             "Relic"
            :utility?          true
            :generate-label    "Add character"
            :store/collections [:relics :tally]
            :store/manual      {:key-label "Character"
                                :list?     true
                                :fields    [{:id :chance :label "Chance" :type :int}]}
            :history           :always
            :inputs            [{:id :who :label "Who" :type :text :list? true}]}
           spec))))

(deftest omitted-components-stay-absent
  (testing "a minimal record produces no empty keys, so the spec and view-model
            validate as the sparse maps they are"
    (is (= {:loot/title "Bare"} (view-model->clj :x (Models$ViewModel. "Bare"))))
    (is (= {:id :x :label "X"} (loot-spec->clj (Models$LootSpec. "x" "X"))))))
