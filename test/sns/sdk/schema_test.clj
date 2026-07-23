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
    (is (schema/validate ::schema/view-model {:loot/title "Divine Dust"}))))

(deftest upgrade-graph-schema
  (testing "the mutually-recursive upgrade graph validates to arbitrary depth"
    (is (schema/validate ::schema/mod
                         {:state    {:ab 1}
                          :template "+{{ab}} AB"
                          :upgrades {:select  :choice
                                     :options [{:id :precise :repeatable true :inc {:ab 1}}
                                               {:id       :nested
                                                :upgrades {:select  :random
                                                           :options [{:id :deep :set {:ab 5}}]}}]}}))))

(deftest path-schema
  (testing "a persisted path carries ids and optional rolled values"
    (is (schema/validate ::schema/path
                         [{:id :precise} {:id :elemental :rolled {:dmg 4}}]))))

(deftest loot-spec-schema
  (testing "a loot-spec may flag itself as a utility"
    (is (schema/validate ::schema/loot-spec
                         {:id :social :label "Group Social" :stateful? true :utility? true})))
  (testing "a loot-spec may override the generate-button label"
    (is (schema/validate ::schema/loot-spec
                         {:id :social :label "Group Social" :generate-label "Add / update character"}))))

(deftest config-schema
  (testing "config with mixed plugin types validates"
    (is (schema/validate ::schema/config
                         {:storage    {:backend :mysql :url "jdbc:mariadb://localhost:3306/sns"}
                          :plugins    [{:type :data :id :uniques :source "data/uniques.edn"}
                                       {:type :cli :id :weather :command ["python3" "gen.py"]}
                                       {:type :builtin :id :relics :entrypoint 'sns.builtin.relics/generator}]
                          :loot-table [{:id :uniques :weight 30}]})))
  (testing "a :builtin plugin needs no :entrypoint"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :builtin :id :relics}]})))
  (testing "a :jar plugin may name a :class instead of an :entrypoint"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :jar :id :x :jar "p.jar" :class "my.Loot"}]}))
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :jar :id :x :jar "p.jar"}]}))))
  (testing "a :data plugin may carry an :inline spec instead of a :source"
    (is (schema/validate ::schema/config
                         {:plugins [{:type   :data
                                     :id     :omens
                                     :inline {:label "Omen" :items [{:text "a crow"}] :title "{{text}}"}}]}))
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :data :id :omens}]}))))
  (testing "decoding a JSON config coerces an :inline spec's keyword positions"
    (let [decoded (schema/decode ::schema/config
                                 {:plugins [{:type   "data"
                                             :id     "omens"
                                             :inline {:label    "Omen"
                                                      :inputs   [{:id "who" :label "Who" :type "text"}]
                                                      :items    [{:text "a crow"}]
                                                      :sections [{:each "items"
                                                                  :item {:body "{{text}}" :metadata "tags"}}]}}]})
          plugin  (-> decoded :plugins first)]
      (is (= :data (:type plugin)))
      (is (= :items (-> plugin :inline :sections first :each)))
      (is (= :tags (-> plugin :inline :sections first :item :metadata)))
      (is (= [:who :text] (-> plugin :inline :inputs first ((juxt :id :type)))))
      (testing "and the decoded config validates"
        (is (schema/validate ::schema/config decoded)))))
  (testing "any plugin type may be marked :hidden?"
    (is (schema/validate ::schema/config
                         {:plugins [{:type :data :id :tarot :source "tarot.edn" :hidden? true}
                                    {:type :cli :id :w :command ["gen"] :hidden? true}
                                    {:type :jar :id :x :jar "p.jar" :class "my.Loot" :hidden? true}
                                    {:type :builtin :id :relics :hidden? true}]})))
  (testing "an unknown plugin type is rejected"
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :wat :id :x}]})))))
