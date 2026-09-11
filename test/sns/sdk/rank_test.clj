(ns sns.sdk.rank-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [randy.core :as r]
    [sns.sdk.rank :as rank]
    [sns.sdk.vars :as vars]))

(defn- resolved
  "Declared vars as they reach an item."
  [declared]
  (vars/resolve-vars @r/default-rng declared))

(deftest a-var-declares-no-progression-of-its-own
  (testing "nothing is stamped on a var to set up its ranking: it travels as the
            plugin wrote it, and both sides read the same defaults"
    (is (= {:ab {:value 1 :type :int}} (resolved {:ab 1})))
    (is (= {:element {:value "fire"}} (resolved {:element "fire"})))))

(deftest rank-keys-survive-a-random-draw
  (testing "a drawn var's :step reaches the var, not the preset's draw args"
    (let [v (-> (resolved {:d {:random :literal :options [7] :step 2}}) :d)]
      (is (= 7 (:value v)))
      (is (= 2 (:step v)))
      (is (= {:options [7]} (:args v))))))

(deftest ranks-are-one-based
  (let [v {:value 1 :step 2}]
    (testing "a var at its first rank is its declared value"
      (is (= 1 (rank/stepped v)))
      (is (= 1 (rank/stepped (assoc v :rank 1)))))
    (testing "each rank above the first adds one step"
      (is (= 3 (rank/stepped (assoc v :rank 2))))
      (is (= 7 (rank/stepped (assoc v :rank 4)))))))

(deftest a-var-with-no-step-steps-by-its-own-value
  (testing "so a bare {:ab 1} ranks 1, 2, 3"
    (is (= 3 (rank/stepped {:value 1 :rank 3}))))
  (testing "and a declared step wins"
    (is (= 60 (rank/stepped {:value 30 :step 15 :rank 3})))))

(deftest stepping-leaves-anything-unsteppable-alone
  (testing "a non-numeric value is itself at any rank"
    (is (= "fire" (rank/stepped {:value "fire" :rank 3}))))
  (testing "a blank mid-edit value renders as nothing rather than throwing"
    (is (nil? (rank/stepped {:value nil :step 1 :rank 3}))))
  (testing "a rank field cleared mid-edit reads as the first rank, not zero"
    (is (= 1 (rank/stepped {:value 1 :step 2 :rank nil})))
    (is (= [:ab] (rank/available {:ab {:value 1 :rank nil}} nil)))
    (is (= 1 (rank/mod-rank {:ab {:value 1 :rank nil}})))))

(deftest upgradeable-is-numeric-and-uncapped
  (is (rank/upgradeable? {:value 1 :step 1}))
  (is (rank/upgradeable? {:value 1 :step 1 :max 3}))
  (testing "pinned to its first rank"
    (is (not (rank/upgradeable? {:value 1 :step 1 :max 1}))))
  (testing "not a number"
    (is (not (rank/upgradeable? {:value "fire"}))))
  (testing "an entry's own field, not something the plugin declared"
    (is (not (rank/upgradeable? {:value 1 :step 1 :context? true})))))

(def ^:private mod-vars
  (resolved {:ab 1 :range {:value 10 :step 5 :max 3} :element "fire"}))

(deftest available-lists-what-can-still-be-ranked
  (testing "every upgradeable var, while nothing is capped"
    (is (= #{:ab :range} (set (rank/available mod-vars nil)))))
  (testing "a var that has reached its own :max drops out"
    (is (= [:ab] (rank/available (assoc-in mod-vars [:range :rank] 3) nil))))
  (testing "the mod's cap counts ranks across all of its vars"
    (is (some? (rank/available (assoc-in mod-vars [:ab :rank] 3) 3)))
    (is (nil? (rank/available (assoc-in mod-vars [:ab :rank] 4) 3))))
  (testing "a mod with nothing to rank offers nothing"
    (is (nil? (rank/available (resolved {:element "fire"}) nil)))))

(deftest rank-up-takes-one-rank
  (testing "from the implicit first rank"
    (is (= 2 (-> (rank/rank-up mod-vars :ab) :ab :rank))))
  (testing "and from there"
    (is (= 3 (-> (rank/rank-up (rank/rank-up mod-vars :ab) :ab) :ab :rank))))
  (testing "touching only the var named"
    (is (= (dissoc mod-vars :ab) (dissoc (rank/rank-up mod-vars :ab) :ab)))))

(deftest mod-rank-counts-the-whole-mod
  (is (= 1 (rank/mod-rank mod-vars)))
  (is (= 2 (rank/mod-rank (rank/rank-up mod-vars :ab))))
  (is (= 3 (rank/mod-rank (-> mod-vars (rank/rank-up :ab) (rank/rank-up :range))))))
