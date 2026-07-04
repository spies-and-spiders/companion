(ns sns.server.social-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [randy.core :as r]
    [sns.server.social :as social]
    [sns.server.store.memory :as memory]
    [sns.social :as pure]
    [sns.spi.protocols :as p]))

(deftest group-bonus-weighted-average
  (testing "the documented example: top two bonuses counted twice"
    (is (= 3 (pure/group-bonus [7 3 0 -2]))))
  (testing "a lone character's bonus is their own"
    (is (= 5 (pure/group-bonus [5]))))
  (testing "an empty party has no bonus"
    (is (= 0 (pure/group-bonus []))))
  (testing "rounds down, including below zero"
    (is (= 1 (pure/group-bonus [2 1 1])))     ; 7/5
    (is (= -1 (pure/group-bonus [-1 0 -2]))))) ; -4/5

(deftest tracker-flow
  (let [store (memory/create)]
    (testing "an empty tracker snapshots to no characters and +0 bonuses"
      (is (= {:characters [] :deception 0 :persuasion 0} (social/snapshot store))))
    (testing "upsert adds a present character (bonuses arrive as strings)"
      (let [s (social/upsert! store {:name "Alice" :deception "7" :persuasion "3"})]
        (is (= [{:name "Alice" :deception 7 :persuasion 3 :present? true}]
               (:characters s)))
        (is (= 7 (:deception s)))))
    (testing "a second character shifts the group bonus"
      (let [s (social/upsert! store {:name "Bob" :deception 1 :persuasion -2})]
        ;; deception [7 1] -> (7+7+1+1)/4 = 4
        (is (= 4 (:deception s)))
        (is (= 0 (:persuasion s)))))
    (testing "state persists under the __-prefixed internal collection"
      (is (= #{"Alice" "Bob"} (set (keys (p/fetch store :__social "characters"))))))
    (testing "unticking excludes a character from the averages"
      (let [s (social/toggle! store "Bob")]
        (is (= 7 (:deception s)))
        (is (= [true false] (mapv :present? (:characters s))))))
    (testing "rolling reports 1d20 + the group bonus for the chosen skill"
      (let [{:keys [roll]} (social/roll store @r/default-rng :deception)]
        (is (<= 1 (:die roll) 20))
        (is (= 7 (:bonus roll)))
        (is (= (+ (:die roll) 7) (:total roll)))))
    (testing "removing deletes the character"
      (is (= ["Alice"] (mapv :name (:characters (social/remove! store "Bob"))))))
    (testing "a blank name is rejected"
      (is (thrown? Exception (social/upsert! store {:name "  " :deception 1}))))
    (testing "an unknown skill is rejected"
      (is (thrown? Exception (social/roll store @r/default-rng :stealth))))))
