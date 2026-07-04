(ns sns.server.http-test
  (:require
    [clojure.edn :as edn]
    [clojure.test :refer [deftest is testing]]
    [sns.server.engine :as engine]
    [sns.server.http :as http]
    [sns.server.store.memory :as memory]
    [sns.spi.protocols])
  (:import
    (java.io ByteArrayInputStream)))

(def ^:private config
  {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}
                {:type :builtin :id :relics :entrypoint 'sns.builtin.relics/generator}]
   :loot-table [{:id :divine-dust :weight 100}]})

(defn- post [app uri data]
  (app {:request-method :post
        :uri            uri
        :headers        {"content-type" "application/edn"
                         "accept"       "application/edn"}
        :body           (ByteArrayInputStream. (.getBytes (pr-str data) "UTF-8"))}))

(defn- body [resp]
  (let [b (:body resp)]
    (edn/read-string (if (string? b) b (slurp b)))))

(deftest loot-types-endpoint
  (let [app (http/app (engine/create config {:store (memory/create)}))
        resp (app {:request-method :get :uri "/api/loot-types"})]
    (is (= 200 (:status resp)))
    (is (= #{:divine-dust :relics} (set (map :id (body resp)))))))

(deftest generate-endpoint
  (let [app (http/app (engine/create config {:store (memory/create)}))
        resp (post app "/api/generate" {:id :divine-dust})]
    (is (= 200 (:status resp)))
    (is (= "Divine Dust" (:loot/title (body resp))))))

(deftest action-endpoint-round-trips-state
  (let [app (http/app (engine/create config {:store (memory/create)}))
        gen (body (post app "/api/generate" {:id :relics}))
        {:keys [id action params]} (-> gen :loot/actions first :action/event second)
        levelled (body (post app "/api/action" {:id id :action action :params params}))]
    (is (re-find #"level 2" (:loot/subtitle levelled)))))

(defn- recording-reporter [sink]
  (reify sns.spi.protocols/Reporter
    (report-label [_] "Send to Discord")
    (report! [_ vm] (reset! sink vm) {:ok true})))

(deftest capabilities-endpoint
  (testing "no reporter -> empty capabilities"
    (let [app  (http/app (engine/create config {:store (memory/create)}))
          resp (app {:request-method :get :uri "/api/capabilities"})]
      (is (= 200 (:status resp)))
      (is (= {} (body resp)))))
  (testing "with a reporter -> report? surfaced"
    (let [app  (http/app (engine/create config {:store    (memory/create)
                                                :reporter (recording-reporter (atom nil))}))
          resp (app {:request-method :get :uri "/api/capabilities"})]
      (is (= {:report? true :report-label "Send to Discord"} (body resp))))))

(deftest report-endpoint
  (let [sink (atom nil)
        app  (http/app (engine/create config {:store    (memory/create)
                                              :reporter (recording-reporter sink)}))
        resp (post app "/api/report" {:view-model {:loot/title "Dust"}})]
    (is (= 200 (:status resp)))
    (is (= {:ok true} (body resp)))
    (is (= {:loot/title "Dust"} @sink))))

(deftest social-endpoints
  (let [app (http/app (engine/create config {:store (memory/create)}))]
    (testing "the tracker is always available, regardless of configured plugins"
      (let [resp (app {:request-method :get :uri "/api/social"})]
        (is (= 200 (:status resp)))
        (is (= {:characters [] :deception 0 :persuasion 0} (body resp)))))
    (testing "the full character lifecycle round-trips"
      (is (= [{:name "Alice" :deception 7 :persuasion 3 :present? true}]
             (:characters (body (post app "/api/social/character"
                                      {:name "Alice" :deception "7" :persuasion "3"})))))
      (is (= [false] (mapv :present? (:characters (body (post app "/api/social/toggle" {:name "Alice"}))))))
      (let [{:keys [roll]} (body (post app "/api/social/roll" {:skill :persuasion}))]
        (is (= 0 (:bonus roll)))
        (is (= (:die roll) (:total roll))))
      (is (= [] (:characters (body (post app "/api/social/remove" {:name "Alice"}))))))
    (testing "a blank name is a 400"
      (is (= 400 (:status (post app "/api/social/character" {:name ""})))))))

(deftest errors-return-edn
  (let [app (http/app (engine/create config {:store (memory/create)}))
        resp (post app "/api/generate" {:id :nonexistent})]
    (is (= 400 (:status resp)))
    (is (string? (:error (body resp))))))
