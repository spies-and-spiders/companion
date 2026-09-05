(ns sns.builtin.social-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [randy.core :as r]
    [sns.builtin.social :as social]
    [sns.sdk.protocols :as p]
    [sns.server.store.edn :as edn-store]))

(deftest group-bonus-weighted-average
  (testing "the documented example: top two bonuses counted twice"
    (is (= 3 (social/group-bonus [7 3 0 -2]))))
  (testing "a lone character's bonus is their own"
    (is (= 5 (social/group-bonus [5]))))
  (testing "an empty party has no bonus"
    (is (= 0 (social/group-bonus []))))
  (testing "never rounds, including below zero"
    (is (= 1.4 (social/group-bonus [2 1 1])))
    (is (= -0.8 (social/group-bonus [-1 0 -2]))))
  (testing "fractional bonuses are averaged as-is"
    (is (= 10.5 (social/group-bonus [10.5])))
    (is (= 7.75 (social/group-bonus [10.5 5])))) ; (10.5+5+10.5+5)/4
  (testing "whole results keep their integral type"
    (is (= 4 (social/group-bonus [3.5 4.5])))))

(defn- bonuses
  "The two group bonuses as the view-model renders them."
  [vm]
  (->> (get-in vm [:loot/sections 0 :section/items])
       (map (juxt :item/title :item/body))
       (into {})))

(deftest tracker-view
  (let [store     (doto (edn-store/create {:backend :memory}) p/setup!)
        generator (social/generator {:id :social})
        ctx       {:store store :rng @r/default-rng}]
    (testing "an empty tracker has nobody present and no bonus"
      (let [vm (p/generate generator ctx)]
        (is (= "0/0 present" (:loot/subtitle vm)))
        (is (= {"Deception" "1d20 +0" "Persuasion" "1d20 +0"} (bonuses vm)))))
    (p/mutate! store {:social {"Alice" {:deception 7 :persuasion 3 :present? true}
                               "Bob"   {:deception 1 :persuasion -2 :present? true}}})
    (testing "the group bonus counts the top two twice"
      (let [vm (p/generate generator ctx)]
        (is (= "2/2 present" (:loot/subtitle vm)))
        ;; deception [7 1] -> (7+1+7+1)/4 = 4; persuasion [3 -2] -> (3-2+3-2)/4 = 0.5
        (is (= {"Deception" "1d20 +4" "Persuasion" "1d20 +0.5"} (bonuses vm)))))
    (testing "an absent character is excluded from the averages"
      (p/mutate! store {:social {"Bob" {:deception 1 :persuasion -2 :present? false}}})
      (let [vm (p/generate generator ctx)]
        (is (= "1/2 present" (:loot/subtitle vm)))
        (is (= "1d20 +7" (get (bonuses vm) "Deception")))))
    (testing "rolling adds a section reporting 1d20 + the group bonus"
      (let [vm    (p/handle-action generator ctx :roll {:skill :deception})
            title (get-in vm [:loot/sections 1 :section/items 0 :item/title])
            die   (parse-long (re-find #"\d+" (get-in vm [:loot/sections 1 :section/items 0 :item/body])))]
        (is (= "Last roll" (get-in vm [:loot/sections 1 :section/heading])))
        (is (<= 1 die 20))
        (is (= (str "Deception check: " (+ die 7)) title))
        (testing "and leaves both roll buttons in place"
          (is (= 2 (count (:loot/actions vm)))))))
    (testing "an unknown skill or action is rejected"
      (is (thrown? Exception (p/handle-action generator ctx :roll {:skill :stealth})))
      (is (thrown? Exception (p/handle-action generator ctx :fumble {:skill :deception}))))))
