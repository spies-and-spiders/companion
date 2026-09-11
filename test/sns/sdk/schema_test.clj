(ns sns.sdk.schema-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.schema :as schema]))

(deftest view-model-schema
  (testing "a fully-populated view-model is accepted"
    (is (schema/validate ::schema/view-model
                         {:loot/title    "Pacifist's Vow"
                          :loot/subtitle "Unique · armour"
                          :loot/sections [{:section/heading "Mods"
                                           :section/items   [{:item/body     "+1 AB"
                                                              :item/metadata ["accuracy"]}]}]
                          :loot/actions  [{:action/label "Level up"
                                           :action/event [:loot/action {:id :relics}]}]})))
  (testing "title must be a string"
    (is (not (schema/validate ::schema/view-model {:loot/title 42}))))
  (testing "a minimal view-model needs only a title"
    (is (schema/validate ::schema/view-model {:loot/title "Divine Dust"})))
  (testing "an item carries its template plus the vars it interpolates, keyed by
            the name the template refers to them by"
    (is (schema/validate ::schema/view-model
                         {:loot/title    "Reliquary"
                          :loot/sections [{:section/items
                                           [{:item/body "+1 {{ x }} absorption"
                                             :item/vars {:x {:value   "fire"
                                                             :label   "Damage types"
                                                             :random  :damage-types
                                                             :options ["fire" "cold" "acid"]}}}]}]})))
  (testing "a var needs a value, and vars are keyed by keyword"
    (is (not (schema/validate ::schema/item-vars {:x {}})))
    (is (not (schema/validate ::schema/item-vars {"x" {:value 1}}))))
  (testing "title/subtitle interpolate :loot/vars the same way"
    (is (schema/validate ::schema/view-model
                         {:loot/title "{{ name }}"
                          :loot/vars  {:name {:value "Reliquary"}}})))
  (testing "opaque plugin state round-trips with the view-model"
    (is (schema/validate ::schema/view-model
                         {:loot/title "Soul"
                          :loot/state {:origin 3}}))))

(deftest mod-schema
  (testing "a mod that ranks up: declared vars, the template, what it may spend"
    (is (schema/validate ::schema/mod
                         {:vars      {:ab 1 :range {:value 30 :step 15 :max 3}}
                          :template  "+{{ab}} AB within {{range}}ft"
                          :max-ranks 4}))))

(deftest item-var-carries-its-rank
  (testing "a var's rank rides on the var, so it round-trips with the item"
    (is (schema/validate ::schema/item-vars
                         {:ab {:value 1 :type :int :step 2 :max 5 :rank 3}})))
  (testing "ranks are 1-based, so there is no rank zero"
    (is (not (schema/validate ::schema/item-vars {:ab {:value 1 :rank 0}})))))

(deftest loot-spec-schema
  (testing "a loot-spec may flag itself as a utility"
    (is (schema/validate ::schema/loot-spec
                         {:id :social :label "Group Social" :utility? true})))
  (testing "a loot-spec may override the generate-button label"
    (is (schema/validate ::schema/loot-spec
                         {:id :social :label "Group Social" :generate-label "Add / update character"}))))

(deftest config-schema
  (testing "config with mixed plugin types validates"
    (is (schema/validate ::schema/config
                         {:storage    {:backend :file :file {:dir "./state"}}
                          :plugins    [{:type :data :id :uniques :data {:source "data/uniques.edn"}}
                                       {:type :cli :id :weather :cli {:command ["python3" "gen.py"]}}
                                       {:type :builtin :id :relics :builtin {:entrypoint 'sns.builtin.relics/generator}}]
                          :loot-table [{:id :uniques :weight 30}]})))
  (testing "a :builtin plugin needs no :builtin config"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :builtin :id :relics}]})))
  (testing "a :jar plugin may name a :class instead of an :entrypoint"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :jar :id :x :jar {:path "p.jar" :class "my.Loot"}}]}))
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :jar :id :x :jar {:path "p.jar"}}]}))))
  (testing "a :data plugin may carry an :inline spec instead of a :source"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :data
                                     :id   :omens
                                     :data {:inline {:label "Omen" :items [{:text "a crow"}] :title "{{text}}"}}}]}))
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :data :id :omens :data {}}]}))))
  (testing "decoding a JSON config coerces an :inline spec's keyword positions"
    (let [decoded (schema/decode ::schema/config
                                 {:plugins [{:type "data"
                                             :id   "omens"
                                             :data {:inline {:label    "Omen"
                                                             :inputs   [{:id "who" :label "Who" :type "text"}]
                                                             :items    [{:text "a crow"}]
                                                             :sections [{:each "items"
                                                                         :item {:body "{{text}}" :metadata "tags"}}]}}}]})
          plugin  (-> decoded :plugins first)]
      (is (= :data (:type plugin)))
      (is (= :items (-> plugin :data :inline :sections first :each)))
      (is (= :tags (-> plugin :data :inline :sections first :item :metadata)))
      (is (= [:who :text] (-> plugin :data :inline :inputs first ((juxt :id :type)))))
      (testing "and the decoded config validates"
        (is (schema/validate ::schema/config decoded)))))
  (testing "any plugin type may be marked :hidden?"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :data :id :tarot :data {:source "tarot.edn"} :hidden? true}
                                    {:type :cli :id :w :cli {:command ["gen"]} :hidden? true}
                                    {:type :jar :id :x :jar {:path "p.jar" :class "my.Loot"} :hidden? true}
                                    {:type :builtin :id :relics :hidden? true}]})))
  (testing "an unknown plugin type is rejected"
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :wat :id :x}]})))))
