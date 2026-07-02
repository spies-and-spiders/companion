(ns sns.server.progression-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.progression :as progression]
    [sns.server.render :as render]))

(def ^:private base
  {:state    {:ab 1}
   :template "+{{ab}} AB with effects that cannot deal damage."
   :upgrades {:select  :choice
              :options [{:id :precise :repeatable true :inc {:ab 1}}
                        {:id             :wide
                         :set            {:ab 1}
                         :assoc-template "+{{ab}} AB; ignore half cover."
                         :upgrades       {:select  :choice
                                          :options [{:id :keen :inc {:ab 2}}]}}
                        {:id :elemental :set {:dmg 0} :assoc-template "Deal {{dmg}} extra fire damage."}]}})

(defn- effect [path]
  (:effect (progression/derive-mod render/render base path)))

(deftest base-renders
  (is (= "+1 AB with effects that cannot deal damage." (effect []))))

(deftest repeated-inc-accumulates
  (testing "a repeatable option chosen N times accumulates onto state"
    (is (= "+4 AB with effects that cannot deal damage."
           (effect [{:id :precise} {:id :precise} {:id :precise}])))))

(deftest template-swap-then-bump-composes
  (testing "an option can rewrite the template; later ops render against it"
    (is (= "+3 AB; ignore half cover."
           (effect [{:id :wide} {:id :keen}])))))

(deftest random-path-re-derives-deterministically
  (testing "persisted :rolled values reproduce the same effect every time"
    (let [path [{:id :elemental :rolled {:dmg 4}}]]
      (is (= "Deal 4 extra fire damage." (effect path)))
      (is (= (effect path) (effect path))))))

(deftest options-at-respects-repeatable-and-descent
  (testing "repeatable option keeps offering itself"
    (is (= [:precise :wide :elemental]
           (mapv :id (:options (progression/options-at base [{:id :precise}]))))))
  (testing "descending into a child node offers the child's options"
    (is (= [:keen]
           (mapv :id (:options (progression/options-at base [{:id :wide}]))))))
  (testing "a terminal node offers nothing"
    (is (nil? (progression/options-at base [{:id :wide} {:id :keen}])))))

(def ^:private single-shot
  {:state    {}
   :template "x"
   :upgrades {:select :choice :options [{:id :only :set {}}]}})

(deftest one-shot-option-is-consumed-not-terminal
  (testing "taking a one-shot option removes only itself, leaving the siblings"
    (is (= [:precise :wide]
           (mapv :id (:options (progression/options-at base [{:id :elemental}]))))))
  (testing "the repeatable sibling stays reachable after the one-shot is taken"
    (is (= [:precise :wide]
           (mapv :id (:options (progression/options-at
                                 base [{:id :elemental} {:id :precise}])))))
    ;; regression: this path used to throw, since the one-shot made the node
    ;; terminal and the following :precise was then rejected as "unknown"
    (is (= {:ab 2 :dmg 0}
           (:state (progression/derive-mod render/render base
                                           [{:id :elemental :rolled {:dmg 0}}
                                            {:id :precise}])))))
  (testing "the node only goes terminal once every option there is consumed"
    (is (nil? (progression/options-at single-shot [{:id :only}])))))

(deftest unknown-option-is-rejected
  (is (thrown? Exception (effect [{:id :nope}]))))
