(ns sns.ui.actions-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [sns.ui.actions :as a]
    [sns.ui.state :as state]))

(defn- run
  "Apply the state effects `fx` to `state`, returning it with the other effects'
   names under `::fx`."
  [state fx]
  (reduce (fn [s [k & args]]
            (case k
              :fx/assoc-in (let [[path v] args] (assoc-in s path v))
              :fx/merge-in (let [[path m] args] (if (seq path) (update-in s path merge m) (merge s m)))
              (update s ::fx (fnil conj []) k)))
          (dissoc state ::fx)
          fx))

(def ^:private base
  (let [types [{:id :relics :store/collections [:relics]}
               {:id :social :store/collections [:social] :store/manual {:fields []}}
               {:id :weather :store/collections [:weather]}]]
    {:loot-types   types
     :specs        (into {} (map (juxt :id identity)) types)
     :pages        [{:id :session :tools [:weather :social]}]
     :loot-table   [{:id :relics :ranges [[1 60]]} {:id :weather :ranges [[40 100]]}]
     :history-mode :always
     :history      {}
     :tools        {}}))

(def ^:private vm {:loot/title "Aegis"})

(deftest page-for
  (testing "the page on screen when it holds every type"
    (is (= :session (state/page-for (assoc base :page :session) [:weather]))))
  (testing "a lone type's own page"
    (is (= :relics (state/page-for base [:relics]))))
  (testing "the first configured page holding them all"
    (is (= :session (state/page-for base [:social :weather]))))
  (testing "otherwise the page of the roll"
    (is (= state/rolled-page (state/page-for base [:relics :weather])))))

(deftest roll-collections
  (is (= [:relics :weather] (a/collections-for base nil))
      "only the loot table's types travel with a roll"))

(deftest generated
  (testing ":always files the result and marks it saved"
    (let [s (run base (a/generated base :relics vm))]
      (is (= vm (get-in s [:tools :relics :result])))
      (is (true? (get-in s [:tools :relics :saved?])))
      (is (= [vm] (map :view-model (get-in s [:history "relics"]))))
      (is (= [:fx/persist-history] (::fx s)))))
  (testing ":button leaves it unsaved"
    (let [button (assoc base :history-mode :button)
          s      (run button (a/generated button :relics vm))]
      (is (false? (get-in s [:tools :relics :saved?])))
      (is (empty? (:history s)))))
  (testing "an error goes over the bench, never into history"
    (let [before (assoc-in base [:tools :relics :result] vm)
          s      (run before (a/generated before :relics {:loot/title "Boom" :loot/error? true}))]
      (is (= vm (get-in s [:tools :relics :result])))
      (is (:loot/error? (get-in s [:tools :relics :error])))
      (is (empty? (:history s))))))

(deftest acted
  (let [s (run base (a/generated base :relics vm))]
    (testing "an action that changes nothing files nothing"
      (is (= 1 (count (get-in (run s (a/acted s :relics vm)) [:history "relics"])))))
    (testing "one that changes the item does"
      (is (= 2 (count (get-in (run s (a/acted s :relics (assoc vm :loot/subtitle "rank 2"))) [:history "relics"])))))))

(deftest saved
  (let [s (run base (a/generated base :relics vm))]
    (testing "an edit is no longer what was saved"
      (is (false? (get-in (run s (a/edit-result s :relics [:loot/title] :text "Aegis+")) [:tools :relics :saved?]))))
    (testing "deleting the result's own history row unsaves it"
      (is (false? (get-in (run s (a/history-delete s :relics 0)) [:tools :relics :saved?]))))
    (testing "restoring a row puts a saved result on the bench"
      (let [edited (run s (a/edit-result s :relics [:loot/title] :text "Aegis+"))]
        (is (true? (get-in (run edited (a/history-restore edited :relics 0)) [:tools :relics :saved?])))))))

(deftest reported
  (let [s (-> (assoc base :history-mode :on-report)
              (assoc-in [:tools :relics :result] vm))
        s (run s (a/reported s :relics nil))]
    (is (= :sent (get-in s [:tools :relics :report-status])))
    (is (= 1 (count (get-in s [:history "relics"]))))))

(deftest rolled
  (let [s (run base (a/rolled base {:results [{:id :relics :view-model vm}
                                              {:id :weather :view-model {:loot/title "Rain"}}]}))]
    (is (= state/rolled-page (:page s)))
    (is (= "Rain" (get-in s [:tools :weather :result :loot/title])))
    (is (some #{:fx/push-route} (::fx s)))))

(deftest routing
  (let [caps {:loot-table (:loot-table base) :pages (:pages base)}
        boot #(run (assoc base :page %) (a/booted (assoc base :page %) [(:loot-types base) caps]))]
    (testing "boot shows the page the URL names"
      (is (= :session (:page (boot :session)))))
    (testing "else the loot table"
      (is (= state/loot-table-page (:page (boot :nope))))
      (is (= state/loot-table-page (:page (boot nil))))))
  (testing "a page round-trips through the hash"
    (doseq [page [:relics state/loot-table-page]]
      (is (= page (state/hash->page (state/page->hash page))))))
  (testing "opening a page reads its manual tables"
    (is (= [:fx/request] (::fx (run base (a/show-page base :session)))))))

(deftest failed
  (is (= "Something went wrong" (:error (run base (a/failed base nil {:error nil}))))))
