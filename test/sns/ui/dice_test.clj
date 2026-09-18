(ns sns.ui.dice-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.ui.dice :as dice]))

(deftest die-size
  (testing "the ladder up to the first pool of d12s"
    (is (= ["1d4" "1d6" "1d8" "1d10" "1d12" "3d4" "1d6+2d4" "2d6+1d4" "3d6" "1d8+2d6"]
           (map dice/die-size (range 1 11)))))
  (testing "a pool of d12s splits one at a time, then climbs again"
    (is (= ["3d12" "2d12+3d4" "1d12+6d4" "9d4" "1d6+8d4"]
           (map dice/die-size (range 18 23)))))
  (testing "a rung reached as a string, as an edited var arrives"
    (is (= "3d4" (dice/die-size "6"))))
  (testing "anything that is not a rung renders as nothing"
    (is (every? nil? (map dice/die-size [0 -1 2.5 "" "big" nil])))))
