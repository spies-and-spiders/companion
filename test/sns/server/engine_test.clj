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
    (testing "roll picks from the loot-table, returning the chosen id and view-model"
      (is (= {:id         :divine-dust
              :view-model {:loot/title    "Divine Dust"
                           :loot/subtitle "A pinch of divine residue"}}
             (engine/roll eng))))
    (testing "an unknown loot type is rejected"
      (is (thrown? Exception (engine/generate eng :nonexistent))))))

(deftest loot-table-without-weights-is-uniform
  (testing "a loot-table entry may omit :weight (defaults to 1, sampled uniformly)"
    (let [eng (engine/create
                {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}]
                 :loot-table [{:id :divine-dust}]})]
      (is (= :divine-dust (:id (engine/roll eng)))))))

(deftest roll-by-number-resolves-via-allocation
  (let [eng (engine/create
              {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}
                            {:type :builtin :id :other :entrypoint 'sns.builtin.dust/generator}]
               :loot-table [{:id :divine-dust :weight 40} {:id :other :weight 60}]})]
    (testing "a number lands in the weighted slice it belongs to (1-40 vs 41-100)"
      (is (= :divine-dust (:id (engine/roll eng {} 1))))
      (is (= :divine-dust (:id (engine/roll eng {} 40))))
      (is (= :other (:id (engine/roll eng {} 41))))
      (is (= :other (:id (engine/roll eng {} 100)))))
    (testing "out-of-range and non-integer rolls are rejected"
      (is (thrown? Exception (engine/roll eng {} 0)))
      (is (thrown? Exception (engine/roll eng {} 101)))
      (is (thrown? Exception (engine/roll eng {} 4.5))))))

(deftest roll-by-number-normalises-arbitrary-weight-totals
  (testing "weights that don't sum to 100 are stretched onto the 1-100 scale"
    (let [eng (engine/create
                {:plugins    [{:type :builtin :id :a :entrypoint 'sns.builtin.dust/generator}
                              {:type :builtin :id :b :entrypoint 'sns.builtin.dust/generator}
                              {:type :builtin :id :c :entrypoint 'sns.builtin.dust/generator}]
                 :loot-table [{:id :a} {:id :b} {:id :c}]})]
      (is (= :a (:id (engine/roll eng {} 1))))
      (is (= :a (:id (engine/roll eng {} 34))))
      (is (= :b (:id (engine/roll eng {} 35))))
      (is (= :c (:id (engine/roll eng {} 100)))))))

(def ^:private utility-plugin
  ;; The command never runs during engine creation — only its loot-spec is read.
  {:type :cli :id :tools :command ["true"] :utility? true})

(deftest validates-loot-table-at-startup
  (testing "a utility in the loot-table is rejected"
    (is (thrown? Exception
                 (engine/create
                   {:plugins    [utility-plugin]
                    :loot-table [{:id :tools}]}))))
  (testing "a utility registers fine when kept off the table"
    (let [eng (engine/create {:plugins [utility-plugin]})]
      (is (true? (:utility? (first (engine/loot-specs eng)))))))
  (testing "an unknown loot-table id is rejected"
    (is (thrown? Exception
                 (engine/create
                   {:plugins    [{:type :builtin :id :divine-dust}]
                    :loot-table [{:id :nonexistent}]})))))

(deftest rejects-duplicate-ids
  (is (thrown? Exception
               (engine/create
                 {:plugins [{:type :builtin :id :dup :entrypoint 'sns.builtin.dust/generator}
                            {:type :builtin :id :dup :entrypoint 'sns.builtin.dust/generator}]}))))

(deftest loads-and-validates-resource-config
  (testing "a real filesystem config loads, validates, and drives the engine"
    ;; Load a committed, hermetic fixture rather than the git-ignored repo-root
    ;; config.edn (absent in CI). Override the store to keep the test in-memory.
    (let [eng (engine/create (config/load-config "test/resources/config.edn")
                             {:store (memory/create)})]
      (is (= "Divine Dust" (:loot/title (engine/generate eng :divine-dust))))
      (is (re-find #"Relic" (:loot/subtitle (engine/generate eng :relics)))))))

(deftest input-defaults-fill-blank-values
  (let [eng (engine/create
              {:plugins [{:type :data :id :potion :source "test/resources/enum-default.edn"}]})]
    (testing "a blank enum input falls back to its declared :default"
      (is (= "common potion" (:loot/title (engine/generate eng :potion {}))))
      (is (= "common potion" (:loot/title (engine/generate eng :potion {:rarity ""})))))
    (testing "a provided value overrides the default"
      (is (= "rare potion" (:loot/title (engine/generate eng :potion {:rarity "rare"})))))))
