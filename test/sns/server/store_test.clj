(ns sns.server.store-test
  (:require
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [sns.server.store :as store]))

(deftest from-config-selects-backend
  (testing "defaults to in-memory"
    (is (some? (store/from-config nil)))
    (is (some? (store/from-config {:backend :memory}))))
  (testing "file backend is built from config"
    (is (some? (store/from-config
                 {:backend :file
                  :dir     (str (io/file (System/getProperty "java.io.tmpdir")
                                         (str "sns-cfg-" (System/nanoTime))))})))))
