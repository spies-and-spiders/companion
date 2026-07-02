(ns sns.spi.schema-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.spi.schema :as schema]))

(deftest view-model-schema
  (testing "a fully-populated view-model is accepted"
    (is (schema/validate ::schema/view-model
                         {:loot/title    "Pacifist's Vow"
                          :loot/subtitle "Unique · armour"
                          :loot/sections [{:section/heading "Mods"
                                           :section/items   [{:item/body "+1 AB"
                                                              :item/tags ["accuracy"]}]}]
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

(deftest config-schema
  (testing "config with mixed plugin types validates"
    (is (schema/validate ::schema/config
                         {:storage    {:backend :mysql :url "jdbc:mariadb://localhost:3306/sns"}
                          :plugins    [{:type :data :id :uniques :source "data/uniques.edn"}
                                       {:type :cli :id :weather :command ["python3" "gen.py"]}
                                       {:type :builtin :id :relics :entrypoint 'sns.builtin.relics/generator}]
                          :loot-table [{:id :uniques :weight 30}]})))
  (testing "an unknown plugin type is rejected"
    (is (not (schema/validate ::schema/config
                              {:plugins [{:type :wat :id :x}]})))))
