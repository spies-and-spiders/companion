(ns sns.sdk.progression-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.progression :as sp]
    [sns.sdk.protocols :as p]
    [sns.server.progression :as progression]))

(deftest built-in-ops-mutate-vars
  (testing "each op takes vars and returns vars, addressing one by the id its
            template interpolates"
    (is (= {:ab {:value 3}} (sp/apply-op {:ab {:value 1}} :inc {:ab 2})))
    (is (= {:tags {:value ["a"]}} (sp/apply-op {} :conj {:tags "a"})))
    (is (= {:cover {:value true}} (sp/apply-op {} :enable [:cover]))))
  (testing "an op may introduce a var the mod never declared"
    (is (= {:new {:value 2}} (sp/apply-op {} :inc {:new 2}))))
  (testing "other vars' metadata survives the op"
    (is (= {:ab {:value 3 :label "Ab" :options ["x"]}}
           (sp/apply-op {:ab {:value 1 :label "Ab" :options ["x"]}} :inc {:ab 2})))))

(deftest unknown-op-is-rejected
  (testing "a typo'd op fails loudly rather than silently doing nothing"
    (is (thrown? Exception (sp/apply-op {} :inx {:ab 1}))))
  (testing "the error names what is available"
    (is (= (sp/known-ops)
           (-> (try (sp/apply-op {} :nope nil) (catch Exception e (ex-data e)))
               :known)))
    (is (contains? (set (sp/known-ops)) :inc))))

(def ^:private two-var-mod
  {:vars     {:harm 1 :range 10}
   :template "+{{harm}} harm within {{range}}ft"
   :upgrades {:select  :choice
              :options [{:id :harm :inc {:harm 1}}
                        {:id :range :inc {:range 10}}]}})

(defn- stepped
  "The vars after taking `ids` one at a time, the way a plugin that keeps its
   vars as the state does it: look each option up at the path so far, then apply
   only that option's ops to the vars it already has."
  [mod ids]
  (let [prog (progression/progression nil)]
    (first (reduce (fn [[vars path] id]
                     (let [option (->> (:options (p/level-options prog mod path))
                                       (filter #(= id (:id %)))
                                       first)]
                       [(sp/apply-ops nil vars option) (conj path {:id id})]))
                   [(:vars mod) []]
                   ids))))

(deftest one-option-moves-vars-by-one-upgrade
  (testing "each step adds exactly its own increment, however many came before —
            regression: replaying a whole path over already-stepped vars
            compounds, so the third pick is worth three"
    (is (= {:harm 2 :range 10} (update-vals (stepped two-var-mod [:harm]) :value)))
    (is (= {:harm 3 :range 10} (update-vals (stepped two-var-mod [:harm :harm]) :value)))
    (is (= {:harm 4 :range 10} (update-vals (stepped two-var-mod [:harm :harm :harm]) :value))))
  (testing "and touches only the var its own ops name"
    (is (= {:harm 3 :range 20}
           (update-vals (stepped two-var-mod [:harm :harm :range]) :value))))
  (testing "stepping agrees with replaying the same path from declared vars"
    (let [path [{:id :harm} {:id :range} {:id :harm}]]
      (is (= (update-vals (progression/derive-vars nil two-var-mod path) :value)
             (update-vals (stepped two-var-mod [:harm :range :harm]) :value))))))

(deftest plugin-op-extends-upgrade-graphs
  (testing "a defmethod'd op works inside an ordinary graph, no custom Progression"
    (defmethod sp/apply-op ::multiply [vars _ m]
      (sp/update-values vars m *))
    (try
      (let [base {:vars     {:ab 3}
                  :template "+{{ab}} AB"
                  :upgrades {:select  :choice
                             :options [{:id ::doubled ::multiply {:ab 2}}]}}]
        (is (= 6 (-> (progression/derive-vars nil base [{:id ::doubled}]) :ab :value))))
      (finally
        (remove-method sp/apply-op ::multiply)))))
