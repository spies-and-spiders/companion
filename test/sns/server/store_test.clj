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
                                         (str "sns-cfg-" (System/nanoTime))))}))))
  (testing ":browser has no server-side store — its state arrives per request"
    (is (nil? (store/from-config {:backend :browser}))))
  (testing "an unknown backend is rejected rather than silently ignored"
    (is (thrown? Exception (store/from-config {:backend :mysql})))))

(deftest browser?-reads-the-config
  (is (true? (store/browser? {:storage {:backend :browser}})))
  (is (false? (store/browser? {:storage {:backend :file}})))
  (is (false? (store/browser? {}))))
