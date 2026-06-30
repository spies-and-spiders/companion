(ns sns.builtin.relics-test
  (:require
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [sns.server.engine :as engine]
    [sns.server.store.file :as file]
    [sns.server.store.memory :as memory]
    [sns.spi.protocols :as p]))

(def ^:private config
  {:plugins    [{:type :builtin :id :relics :entrypoint 'sns.builtin.relics/generator}]
   :loot-table [{:id :relics :weight 100}]})

(defn- relic-id
  "Pull the relic id out of a view-model's first level-up action."
  [view-model]
  (-> view-model :loot/actions first :action/event second :params :relic-id))

(deftest generate-then-level-up
  (let [s   (memory/create)
        eng (engine/create config {:store s})
        vm  (engine/generate eng :relics)
        id  (relic-id vm)]
    (testing "generation persists a level-1 relic with a derived effect"
      (is (= 1 (count (p/query s :relics {})))
          "exactly one relic stored")
      (is (re-find #"level 1" (:loot/subtitle vm)))
      (is (some? (-> vm :loot/sections first :section/items first :item/body))))
    (testing "a generated relic surfaces its upgrade choices as actions"
      (is (seq (:loot/actions vm))))
    (testing "levelling up with a choice persists a path step and re-derives"
      (let [choice (-> vm :loot/actions first :action/event second :params :choice)
            vm'    (engine/handle-action eng :relics :level-up {:relic-id id :choice choice})]
        (is (re-find #"level 2" (:loot/subtitle vm')))
        (is (= 1 (count (:path (p/fetch s :relics id))))
            "one step recorded in the persisted path")))
    (testing "the persisted effect is reproducible from state across a fresh engine"
      (let [eng2 (engine/create config {:store s})
            again (engine/handle-action eng2 :relics :level-up {:relic-id id})]
        ;; no choice supplied -> re-shows current state without advancing
        (is (= 1 (count (:path (p/fetch s :relics id)))))
        (is (re-find #"level 2" (:loot/subtitle again)))))))

(deftest level-up-through-file-store
  ;; Regression: the file (and mysql) store must round-trip the relic's keyword
  ;; values, or level-up can't match the chosen option and silently re-shows the
  ;; unchanged item. Uses the file backend that the default config ships with.
  (let [dir (str (io/file (System/getProperty "java.io.tmpdir")
                          (str "sns-relics-" (System/nanoTime))))]
    (try
      (let [s   (file/create dir)
            eng (engine/create config {:store s})
            vm  (engine/generate eng :relics)
            id  (relic-id vm)
            choice (-> vm :loot/actions first :action/event second :params :choice)
            vm' (engine/handle-action eng :relics :level-up {:relic-id id :choice choice})]
        (testing "the choice options survive persistence (still a :choice node)"
          (is (some? choice) "generated relic offers a named choice"))
        (testing "levelling up actually advances after a store round-trip"
          (is (re-find #"level 2" (:loot/subtitle vm')))
          (is (= 1 (count (:path (p/fetch s :relics id)))))))
      (finally
        (run! io/delete-file (reverse (file-seq (io/file dir))))))))

(deftest repeatable-upgrade-accumulates
  (let [s   (memory/create)
        eng (engine/create config {:store s})
        vm  (engine/generate eng :relics)
        id  (relic-id vm)
        ;; drive three upgrades down whichever first option each step offers
        final (reduce (fn [_ _]
                        (let [cur (p/fetch s :relics id)
                              opts (p/level-options (:progression eng) (:mod cur) (:path cur))]
                          (when opts
                            (engine/handle-action eng :relics :level-up
                                                  {:relic-id id
                                                   :choice   (-> opts :options first :id)}))))
                      nil
                      (range 3))]
    (testing "three upgrades produce a level-4 relic"
      (when final
        (is (re-find #"level 4" (:loot/subtitle final)))
        (is (= 3 (count (:path (p/fetch s :relics id)))))))))
