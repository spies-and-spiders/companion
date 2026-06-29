(ns sns.server.engine-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.config :as config]
    [sns.server.engine :as engine]
    [sns.server.store.memory :as memory]))

(def ^:private test-config
  {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}]
   :loot-table [{:id :divine-dust :weight 100}]})

(deftest builds-registry-and-generates
  (let [eng (engine/create test-config)]
    (testing "loot-specs lists the registered type"
      (is (= [{:id :divine-dust :label "Divine Dust"}]
             (engine/loot-specs eng))))
    (testing "generate returns a validated view-model"
      (is (= {:loot/title    "Divine Dust"
              :loot/subtitle "A pinch of divine residue"}
             (engine/generate eng :divine-dust))))
    (testing "roll picks from the loot-table and generates"
      (is (= "Divine Dust" (:loot/title (engine/roll eng)))))
    (testing "an unknown loot type is rejected"
      (is (thrown? Exception (engine/generate eng :nonexistent))))))

(deftest rejects-duplicate-ids
  (is (thrown? Exception
               (engine/create
                 {:plugins [{:type :builtin :id :dup :entrypoint 'sns.builtin.dust/generator}
                            {:type :builtin :id :dup :entrypoint 'sns.builtin.dust/generator}]}))))

(deftest loads-and-validates-resource-config
  (testing "the shipped config.edn loads, validates, and drives the engine"
    ;; Override the store so the test never reaches the configured MySQL server.
    (let [eng (engine/create (config/load-config) {:store (memory/create)})]
      (is (= "Divine Dust" (:loot/title (engine/generate eng :divine-dust))))
      (is (re-find #"Relic" (:loot/subtitle (engine/generate eng :relics)))))))
