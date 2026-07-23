(ns sns.server.store.memory-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.store.memory :as memory]))

(deftest round-trips
  (let [s (memory/create)]
    (testing "fetch of a missing key is nil"
      (is (nil? (p/fetch s :relics "x"))))
    (testing "put then fetch"
      (p/put! s :relics "x" {:name "X" :level 1})
      (is (= {:name "X" :level 1} (p/fetch s :relics "x"))))
    (testing "update! applies a function"
      (is (= {:name "X" :level 2} (p/update! s :relics "x" #(update % :level inc))))
      (is (= 2 (:level (p/fetch s :relics "x")))))
    (testing "query matches by field equality, scoped to the collection"
      (p/put! s :relics "y" {:name "Y" :level 2})
      (p/put! s :other "z" {:name "Z" :level 2})
      (is (= #{"X" "Y"} (set (map :name (p/query s :relics {:level 2}))))))))
