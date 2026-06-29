(ns sns.server.http-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.engine :as engine]
    [sns.server.http :as http]
    [sns.server.store.memory :as memory]
    [sns.server.transit :as transit])
  (:import
    (java.io ByteArrayInputStream)))

(def ^:private config
  {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}
                {:type :builtin :id :relics :entrypoint 'sns.builtin.relics/generator}]
   :loot-table [{:id :divine-dust :weight 100}]})

(defn- post [app uri data]
  (app {:request-method :post
        :uri            uri
        :body           (ByteArrayInputStream. (.getBytes (transit/write-str data) "UTF-8"))}))

(defn- body [resp]
  (transit/read-str (:body resp)))

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

(deftest errors-return-transit
  (let [app (http/app (engine/create config {:store (memory/create)}))
        resp (post app "/api/generate" {:id :nonexistent})]
    (is (= 400 (:status resp)))
    (is (string? (:error (body resp))))))
