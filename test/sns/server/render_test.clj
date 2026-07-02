(ns sns.server.render-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.render :as render]))

(deftest interpolates-state
  (is (= "+3 AB" (render/render "+{{ab}} AB" {:ab 3}))))

(deftest echoes-unresolved-variables
  (testing "missing variables round-trip so partially-applied templates survive"
    (is (= "+{{ab}} AB" (render/render "+{{ab}} AB" {})))))

(deftest cosmetic-filters
  (testing "times"
    (is (= "once" (render/render "{{n|times}}" {:n 1})))
    (is (= "5 times" (render/render "{{n|times}}" {:n 5}))))
  (testing "ordinal"
    (is (= "1st" (render/render "{{n|ordinal}}" {:n 1})))
    (is (= "2nd" (render/render "{{n|ordinal}}" {:n 2})))
    (is (= "11th" (render/render "{{n|ordinal}}" {:n 11}))))
  (testing "percentage"
    (is (= "33.3" (render/render "{{n|percentage}}" {:n 1/3}))))
  (testing "dice"
    (is (= "1d4" (render/render "{{n|dice}}" {:n 1})))
    (is (= "1d12" (render/render "{{n|dice}}" {:n 5})))))
