(ns sns.server.store.file-test
  (:require
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.store.file :as file]))

(defn- temp-dir []
  (str (io/file (System/getProperty "java.io.tmpdir")
                (str "sns-store-" (System/nanoTime)))))

(deftest round-trips
  (let [dir (temp-dir)]
    (try
      (let [s (file/create dir)]
        (testing "fetch of a missing key is nil"
          (is (nil? (p/fetch s :relics "x"))))
        (testing "put then fetch (survives a fresh store on the same dir)"
          (p/put! s :relics "x" {:name "X" :level 1})
          (is (= {:name "X" :level 1} (p/fetch s :relics "x")))
          (is (= {:name "X" :level 1}
                 (p/fetch (file/create dir) :relics "x")))
          (is (.exists (io/file dir "relics.edn"))))
        (testing "update! applies a function, defaulting nil"
          (is (= {:name "X" :level 2} (p/update! s :relics "x" #(update % :level inc))))
          (is (= 2 (:level (p/fetch s :relics "x")))))
        (testing "query matches by field equality, scoped to the collection"
          (p/put! s :relics "y" {:name "Y" :level 2})
          (p/put! s :other "z" {:name "Z" :level 2})
          (is (= #{"X" "Y"} (set (map :name (p/query s :relics {:level 2}))))))
        (testing "keyword values survive the round-trip (regression: stateful loot)"
          ;; mirrors a relic's upgrade-graph mod, whose keyword values plain JSON
          ;; would stringify, breaking option matching on level-up.
          (let [doc {:id "k" :mod {:upgrades {:select  :choice
                                              :options [{:id :precise} {:id :elemental}]}}}]
            (p/put! s :relics "k" doc)
            (is (= doc (p/fetch (file/create dir) :relics "k"))))))
      (finally
        (run! io/delete-file (reverse (file-seq (io/file dir))))))))
