(ns sns.server.reporter-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.engine :as engine]
    [sns.server.reporter :as reporter]))

(deftest from-config-selects-backend
  (testing "nil when reporting is not configured"
    (is (nil? (reporter/from-config nil))))
  (testing "a discord reporter when configured"
    (let [r (reporter/from-config {:backend :discord :webhook-url "https://example/x"})]
      (is (satisfies? p/Reporter r))
      (is (= "Send to Discord" (p/report-label r)))))
  (testing "an unknown backend is rejected"
    (is (thrown? Exception (reporter/from-config {:backend :carrier-pigeon})))))

(defn- recording-reporter [sink]
  (reify p/Reporter
    (report-label [_] "Send")
    (report! [_ vm] (reset! sink vm) nil)))

(def ^:private base-config
  {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}]
   :loot-table [{:id :divine-dust :weight 1}]})

(deftest capabilities-reflect-reporter
  (testing "no reporter -> no report capability"
    (is (= {:social-storage? true} (engine/capabilities (engine/create base-config)))))
  (testing "with a reporter -> report? and label surfaced"
    (let [eng (engine/create base-config {:reporter (recording-reporter (atom nil))})]
      (is (= {:social-storage? true :report? true :report-label "Send"}
             (engine/capabilities eng))))))

(deftest report-sends-validated-view-model
  (let [sink (atom nil)
        eng  (engine/create base-config {:reporter (recording-reporter sink)})]
    (testing "a valid view-model is forwarded to the reporter"
      (engine/report! eng {:loot/title "Dust"})
      (is (= {:loot/title "Dust"} @sink)))
    (testing "an invalid view-model is rejected before reaching the reporter"
      (reset! sink nil)
      (is (thrown? Exception (engine/report! eng {:not "a view-model"})))
      (is (nil? @sink)))))

(deftest report-without-reporter-throws
  (is (thrown? Exception (engine/report! (engine/create base-config) {:loot/title "x"}))))
