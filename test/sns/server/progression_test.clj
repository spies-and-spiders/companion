(ns sns.server.progression-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.progression :as progression]))

(def ^:private base
  {:vars     {:ab 1 :cover false :fire false}
   :template "+{{ab}} AB{{#if cover}}, ignoring half cover{{/if}}.{{#if fire}} Deal 6 extra fire damage.{{/if}}"
   :upgrades {:select  :choice
              :options [{:id :precise :inc {:ab 1}}
                        {:id       :wide
                         :enable   [:cover]
                         :upgrades {:select  :choice
                                    :options [{:id :keen :repeatable false :inc {:ab 2}}]}}
                        {:id :elemental :repeatable false :enable [:fire]}]}})

(defn- derived
  "The mod's vars at `path`, as `{var-id value}`."
  ([path] (derived base path))
  ([mod path]
   (update-vals (progression/derive-vars nil mod path) :value)))

(deftest base-declares-its-starting-vars
  (is (= {:ab 1 :cover false :fire false} (derived []))))

(deftest repeated-inc-accumulates
  (testing "a repeatable option chosen N times accumulates onto the var"
    (is (= {:ab 4 :cover false :fire false}
           (derived [{:id :precise} {:id :precise} {:id :precise}])))))

(deftest flag-then-bump-composes
  (testing "an option can switch a template's {{#if}} flag; later ops still land"
    (is (= {:ab 3 :cover true :fire false}
           (derived [{:id :wide} {:id :keen}])))))

(deftest path-re-derives-deterministically
  (testing "a path reproduces the same vars every time"
    (let [path [{:id :elemental}]]
      (is (= {:ab 1 :cover false :fire true} (derived path)))
      (is (= (derived path) (derived path))))))

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
  {:vars     {}
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
    (is (= 2 (:ab (derived [{:id :elemental} {:id :precise}])))))
  (testing "the node only goes terminal once every option there is consumed"
    (is (nil? (progression/options-at single-shot [{:id :only}])))))

(def ^:private capped
  {:vars     {:lvl 0}
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
  (is (thrown? Exception (derived [{:id :nope}]))))

(def ^:private ordering
  {:vars     {:ab 0 :flag false}
   :template "{{ab}}{{#if flag}}!{{/if}}"
   :upgrades {:select  :choice
              :options [{:id     :everything
                         :inc    {:ab 4}
                         :dec    {:ab 1}
                         :enable [:flag]}]}})

(deftest ops-apply-in-a-fixed-order
  (testing "every op on an option applies, independent of map ordering"
    (is (= {:ab 3 :flag true} (derived ordering [{:id :everything}])))))

(deftest option-keys-that-are-not-ops-are-ignored
  (testing "graph keys never dispatch as mutations"
    (is (= {} (derived single-shot [{:id :only}])))))
