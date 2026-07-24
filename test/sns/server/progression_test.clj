(ns sns.server.progression-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.progression :as progression]
    [sns.server.render :as render]))

(def ^:private base
  {:state    {:ab 1}
   :template "+{{ab}} AB with effects that cannot deal damage."
   :upgrades {:select  :choice
              :options [{:id :precise :inc {:ab 1}}
                        {:id             :wide
                         :assoc-template "+{{ab}} AB; ignore half cover."
                         :upgrades       {:select  :choice
                                          :options [{:id :keen :repeatable false :inc {:ab 2}}]}}
                        {:id             :elemental
                         :repeatable     false
                         :assoc-template "Deal 6 extra fire damage."}]}})

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

(deftest path-re-derives-deterministically
  (testing "a path reproduces the same effect every time"
    (let [path [{:id :elemental}]]
      (is (= "Deal 6 extra fire damage." (effect path)))
      (is (= (effect path) (effect path))))))

(deftest options-repeat-by-default
  (testing "an option with no :repeatable keeps being offered"
    (is (= [:precise :wide :elemental]
           (mapv :id (:options (progression/options-at base [{:id :precise} {:id :precise}])))))))

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
   :upgrades {:select :choice :options [{:id :only :repeatable false}]}})

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
    (is (= {:ab 2}
           (:state (progression/derive-mod render/render base
                                           [{:id :elemental}
                                            {:id :precise}])))))
  (testing "the node only goes terminal once every option there is consumed"
    (is (nil? (progression/options-at single-shot [{:id :only}])))))

(def ^:private capped
  {:state    {:lvl 0}
   :template "{{lvl}}"
   :upgrades {:select  :choice
              :options [{:id :bump :repeatable 3 :inc {:lvl 1}}
                        {:id :other :inc {:lvl 10}}]}})

(deftest numeric-repeatable-caps-a-node
  (testing "the option is offered until its cap, then drops out"
    (is (= [:bump :other]
           (mapv :id (:options (progression/options-at capped [{:id :bump} {:id :bump}])))))
    (testing "the uncapped sibling stays after the cap is reached"
      (is (= [:other]
             (mapv :id (:options (progression/options-at
                                   capped [{:id :bump} {:id :bump} {:id :bump}]))))))
    (testing "reaching the cap does not make the node terminal"
      (is (some? (progression/options-at capped [{:id :bump} {:id :bump} {:id :bump}]))))))

(deftest unknown-option-is-rejected
  (is (thrown? Exception (effect [{:id :nope}]))))

(def ^:private ordering
  {:state    {:ab 0 :log ""}
   :template "{{ab}}/{{log}}"
   :upgrades {:select  :choice
              :options [{:id     :everything
                         :inc    {:ab 4}
                         :dec    {:ab 1}
                         :append {:log "x"}}]}})

(deftest ops-apply-in-a-fixed-order
  (testing "every op on an option applies, independent of map ordering"
    (is (= "3/x"
           (:effect (progression/derive-mod render/render ordering
                                            [{:id :everything}]))))))

(deftest option-keys-that-are-not-ops-are-ignored
  (testing "graph keys never dispatch as mutations"
    (is (= {} (:state (progression/derive-mod render/render single-shot
                                              [{:id :only}]))))))
