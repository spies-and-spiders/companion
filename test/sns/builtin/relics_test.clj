(ns sns.builtin.relics-test
  (:require
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.engine :as engine]
    [sns.server.store.edn :as edn-store]))

(def ^:private config
  {:plugins    [{:type :builtin :id :relics :builtin {:entrypoint 'sns.builtin.relics/generator}}]
   :loot-table [{:id :relics :weight 100}]})

(defn- ranks-taken
  "How many ranks the stored relic has taken across all of its vars."
  [store id]
  (->> (get-in (p/read-collection store :relics) [id :ranks])
       (transduce (map (comp dec val)) + 0)))

(defn- relic-id
  "Pull the relic id out of a view-model's first rank-up action."
  [view-model]
  (-> view-model :loot/actions first :action/event second :params :relic-id))

(deftest generate-then-rank-up
  (let [s   (edn-store/create {:backend :memory})
        eng (engine/create config {:store s})
        vm  (engine/generate eng :relics)
        id  (relic-id vm)]
    (testing "generation persists a rank-1 relic with a derived effect"
      (is (= 1 (count (p/read-collection s :relics)))
          "exactly one relic stored")
      (is (re-find #"rank 1" (:loot/subtitle vm)))
      (is (some? (-> vm :loot/sections first :section/items first :item/body))))
    (testing "a generated relic surfaces its upgrade choices as actions"
      (is (seq (:loot/actions vm))))
    (testing "ranking up with a choice persists the rank and re-derives"
      (let [choice (-> vm :loot/actions first :action/event second :params :choice)
            vm'    (engine/handle-action eng :relics :rank-up {:relic-id id :choice choice} nil)]
        (is (re-find #"rank 2" (:loot/subtitle vm')))
        (is (= 1 (ranks-taken s id))
            "one rank recorded in the persisted relic")))
    (testing "the persisted effect is reproducible from state across a fresh engine"
      (let [eng2 (engine/create config {:store s})
            again (engine/handle-action eng2 :relics :rank-up {:relic-id id} nil)]
        ;; no choice supplied -> re-shows current state without advancing
        (is (= 1 (ranks-taken s id)))
        (is (re-find #"rank 2" (:loot/subtitle again)))))))

(deftest rank-up-through-file-store
  ;; Regression: the file store must round-trip the relic's keyword values, or
  ;; rank-up can't match the chosen var and silently re-shows the unchanged
  ;; item. Uses the file backend that the default config ships with.
  (let [dir (str (io/file (System/getProperty "java.io.tmpdir")
                          (str "sns-relics-" (System/nanoTime))))
        s   (edn-store/create {:backend :file :file {:dir dir}})]
    (try
      (let [eng    (engine/create config {:store s})
            vm     (engine/generate eng :relics)
            id     (relic-id vm)
            choice (-> vm :loot/actions first :action/event second :params :choice)
            vm'    (engine/handle-action eng :relics :rank-up {:relic-id id :choice choice} nil)]
        (testing "the upgradeable var ids survive persistence"
          (is (some? choice) "generated relic offers a named choice"))
        (testing "ranking up actually advances after a store round-trip"
          (is (re-find #"rank 2" (:loot/subtitle vm')))
          (is (= 1 (ranks-taken s id)))))
      (finally
        (run! io/delete-file (reverse (file-seq (io/file dir))))))))

(deftest ranks-accumulate
  (let [s   (edn-store/create {:backend :memory})
        eng (engine/create config {:store s})
        vm0 (engine/generate eng :relics)
        id  (relic-id vm0)
        ;; drive three upgrades, each time taking whichever choice the current
        ;; view-model offers first
        final (reduce (fn [vm _]
                        (let [{:keys [choice]} (-> vm :loot/actions first :action/event second :params)]
                          (engine/handle-action eng :relics :rank-up
                                                {:relic-id id :choice choice} nil)))
                      vm0
                      (range 3))]
    (testing "three upgrades produce a rank-4 relic"
      (is (re-find #"rank 4" (:loot/subtitle final)))
      (is (= 3 (ranks-taken s id))))))
