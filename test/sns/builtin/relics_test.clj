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

(defn- path-length
  "How many upgrade steps the stored relic has."
  [store id]
  (count (get-in (p/read-collection store :relics) [id :path])))

(defn- relic-id
  "Pull the relic id out of a view-model's first level-up action."
  [view-model]
  (-> view-model :loot/actions first :action/event second :params :relic-id))

(deftest generate-then-level-up
  (let [s   (edn-store/create {:backend :memory})
        eng (engine/create config {:store s})
        vm  (engine/generate eng :relics)
        id  (relic-id vm)]
    (testing "generation persists a level-1 relic with a derived effect"
      (is (= 1 (count (p/read-collection s :relics)))
          "exactly one relic stored")
      (is (re-find #"level 1" (:loot/subtitle vm)))
      (is (some? (-> vm :loot/sections first :section/items first :item/body))))
    (testing "a generated relic surfaces its upgrade choices as actions"
      (is (seq (:loot/actions vm))))
    (testing "levelling up with a choice persists a path step and re-derives"
      (let [choice (-> vm :loot/actions first :action/event second :params :choice)
            vm'    (engine/handle-action eng :relics :level-up {:relic-id id :choice choice} nil)]
        (is (re-find #"level 2" (:loot/subtitle vm')))
        (is (= 1 (path-length s id))
            "one step recorded in the persisted path")))
    (testing "the persisted effect is reproducible from state across a fresh engine"
      (let [eng2 (engine/create config {:store s})
            again (engine/handle-action eng2 :relics :level-up {:relic-id id} nil)]
        ;; no choice supplied -> re-shows current state without advancing
        (is (= 1 (path-length s id)))
        (is (re-find #"level 2" (:loot/subtitle again)))))))

(deftest level-up-through-file-store
  ;; Regression: the file store must round-trip the relic's keyword values, or
  ;; level-up can't match the chosen option and silently re-shows the unchanged
  ;; item. Uses the file backend that the default config ships with.
  (let [dir (str (io/file (System/getProperty "java.io.tmpdir")
                          (str "sns-relics-" (System/nanoTime))))
        s   (edn-store/create {:backend :file :file {:dir dir}})]
    (try
      (let [eng    (engine/create config {:store s})
            vm     (engine/generate eng :relics)
            id     (relic-id vm)
            choice (-> vm :loot/actions first :action/event second :params :choice)
            vm'    (engine/handle-action eng :relics :level-up {:relic-id id :choice choice} nil)]
        (testing "the choice options survive persistence (still a :choice node)"
          (is (some? choice) "generated relic offers a named choice"))
        (testing "levelling up actually advances after a store round-trip"
          (is (re-find #"level 2" (:loot/subtitle vm')))
          (is (= 1 (path-length s id)))))
      (finally
        (run! io/delete-file (reverse (file-seq (io/file dir))))))))

(deftest repeatable-upgrade-accumulates
  (let [s   (edn-store/create {:backend :memory})
        eng (engine/create config {:store s})
        vm0 (engine/generate eng :relics)
        id  (relic-id vm0)
        ;; drive three upgrades, each time taking whichever choice the current
        ;; view-model offers first
        final (reduce (fn [vm _]
                        (let [{:keys [choice]} (-> vm :loot/actions first :action/event second :params)]
                          (engine/handle-action eng :relics :level-up
                                                {:relic-id id :choice choice} nil)))
                      vm0
                      (range 3))]
    (testing "three upgrades produce a level-4 relic"
      (is (re-find #"level 4" (:loot/subtitle final)))
      (is (= 3 (path-length s id))))))
