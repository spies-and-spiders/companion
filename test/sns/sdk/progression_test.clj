(ns sns.sdk.progression-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [randy.rng :as rng]
    [sns.sdk.progression :as sp]
    [sns.server.progression :as progression]
    [sns.server.render :as render]))

(deftest built-in-ops-mutate-state
  (testing "each op transforms the accumulator, never the rendered text"
    (is (= {:state {:ab 3}} (sp/apply-op {:state {:ab 1}} :inc {:ab 2})))
    (is (= {:state {:tags ["a"]}} (sp/apply-op {:state {}} :conj {:tags "a"})))
    (is (= {:state {:cover true}} (sp/apply-op {:state {}} :enable [:cover])))
    (is (= {:template "t"} (sp/apply-op {} :assoc-template "t")))))

(deftest unknown-op-is-rejected
  (testing "a typo'd op fails loudly rather than silently doing nothing"
    (is (thrown? Exception (sp/apply-op {:state {}} :inx {:ab 1}))))
  (testing "the error names what is available"
    (is (= (sp/known-ops)
           (-> (try (sp/apply-op {} :nope nil) (catch Exception e (ex-data e)))
               :known)))
    (is (contains? (set (sp/known-ops)) :inc))))

(deftest plugin-op-extends-upgrade-graphs
  (testing "a defmethod'd op works inside an ordinary graph, no custom Progression"
    (defmethod sp/apply-op ::multiply [acc _ m]
      (update acc :state #(merge-with * % m)))
    (try
      (let [base {:state    {:ab 3}
                  :template "+{{ab}} AB"
                  :upgrades {:select  :choice
                             :options [{:id ::doubled ::multiply {:ab 2}}]}}]
        (is (= "+6 AB"
               (:effect (progression/derive-mod render/render base [{:id ::doubled}])))))
      (finally
        (remove-method sp/apply-op ::multiply)))))

(deftest roll-option-persists-rolled-values
  (testing "inclusive bounds, recorded so the effect re-derives deterministically"
    (let [rng (reify rng/RandomNumberGenerator
                (next-int [_ lo _] lo))]
      (is (= {:id :elemental :rolled {:dmg 1}}
             (sp/roll-option rng {:id :elemental :roll {:dmg [1 6]}})))
      (is (= {:id :plain} (sp/roll-option rng {:id :plain}))))))
