(ns sns.sdk.progression-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.progression :as sp]
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
