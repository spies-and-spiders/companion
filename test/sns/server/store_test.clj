(ns sns.server.store-test
  (:require
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.store :as store]))

(deftest from-config-selects-backend
  (testing "defaults to in-memory"
    (is (some? (store/from-config nil)))
    (is (some? (store/from-config {:backend :memory}))))
  (testing "the :none backend discards everything"
    (let [s (store/from-config {:backend :none})]
      (is (= {:a 1} (p/put! s :c "id" {:a 1})))
      (is (nil? (p/fetch s :c "id")))
      (is (= [] (p/query s :c {})))
      (is (= {:n 1} (p/update! s :c "id" (fnil #(update % :n inc) {:n 0}))))))
  (testing "file backend is built from config"
    (is (some? (store/from-config
                 {:backend :file
                  :dir     (str (io/file (System/getProperty "java.io.tmpdir")
                                         (str "sns-cfg-" (System/nanoTime))))})))))
