(ns sns.server.http-test
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.engine :as engine]
    [sns.server.http :as http]
    [sns.server.store.edn :as edn-store])
  (:import
    (java.io ByteArrayInputStream)
    (java.util.zip ZipInputStream)))

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
  (let [app (http/app (engine/create config {:store (edn-store/create {:backend :memory})}))
        resp (app {:request-method :get :uri "/api/loot-types"})]
    (is (= 200 (:status resp)))
    (is (= #{:divine-dust :relics} (set (map :id (body resp)))))))

(deftest generate-endpoint
  (let [app (http/app (engine/create config {:store (edn-store/create {:backend :memory})}))
        resp (post app "/api/generate" {:id :divine-dust})]
    (is (= 200 (:status resp)))
    (is (= "Divine Dust" (:loot/title (body resp))))))

(deftest action-endpoint-round-trips-state
  (let [app (http/app (engine/create config {:store (edn-store/create {:backend :memory})}))
        gen (body (post app "/api/generate" {:id :relics}))
        {:keys [id action params]} (-> gen :loot/actions first :action/event second)
        levelled (body (post app "/api/action" {:id id :action action :params params}))]
    (is (re-find #"level 2" (:loot/subtitle levelled)))))

(defn- recording-reporter [sink]
  (reify sns.sdk.protocols/Reporter
    (report-label [_] "Send to Discord")
    (report! [_ vm] (reset! sink vm) nil)))

(deftest capabilities-endpoint
  (testing "no reporter -> only the storage flag"
    (let [app  (http/app (engine/create config {:store (edn-store/create {:backend :memory})}))
          resp (app {:request-method :get :uri "/api/capabilities"})]
      (is (= 200 (:status resp)))
      (is (= {:browser-storage? false} (body resp)))))
  (testing "with a reporter -> report? surfaced"
    (let [app  (http/app (engine/create config {:store    (edn-store/create {:backend :memory})
                                                :reporter (recording-reporter (atom nil))}))
          resp (app {:request-method :get :uri "/api/capabilities"})]
      (is (= {:browser-storage? false
              :report?          true
              :report-label     "Send to Discord"} (body resp)))))
  (testing ":browser storage -> the client ships state with each request"
    (let [app  (http/app (engine/create (assoc config :storage {:backend :browser})))
          resp (app {:request-method :get :uri "/api/capabilities"})]
      (is (= {:browser-storage? true} (body resp))))))

(deftest report-endpoint
  (let [sink (atom nil)
        app  (http/app (engine/create config {:store    (edn-store/create {:backend :memory})
                                              :reporter (recording-reporter sink)}))
        resp (post app "/api/report" {:view-model {:loot/title "Dust"}})]
    (is (= 200 (:status resp)))
    (is (= {:loot/title "Dust"} @sink))))

(def ^:private social-config
  (update config :plugins conj {:type :builtin :id :social}))

(deftest state-endpoint
  (let [app (http/app (engine/create social-config {:store (edn-store/create {:backend :memory})}))]
    (testing "a manual collection starts empty"
      (let [resp (post app "/api/state" {:id :social})]
        (is (= 200 (:status resp)))
        (is (= {:store/state {}} (body resp)))))
    (testing "a written row is narrowed to the declared fields and coerced"
      (is (= {"Alice" {:deception 7M :persuasion 3M :present? true}}
             (:store/state (body (post app "/api/state"
                                       {:id        :social
                                        :mutations {"Alice" {:deception "7"  :persuasion "3"
                                                             :nickname  "Al"}}}))))))
    (testing "a blank field falls back to the declared default"
      (is (= {:deception 0 :persuasion 0 :present? true}
             (get (:store/state (body (post app "/api/state"
                                            {:id        :social
                                             :mutations {"Bob" {:deception ""}}})))
                  "Bob"))))
    (testing "the plugin reads what the editor wrote"
      (let [vm (body (post app "/api/generate" {:id :social}))]
        (is (= "2/2 present" (:loot/subtitle vm)))))
    (testing "a nil row retracts the key"
      (is (= ["Bob"] (keys (:store/state (body (post app "/api/state"
                                                     {:id :social :mutations {"Alice" nil}})))))))
    (testing "a blank key is a 400, as is a type with no manual state"
      (is (= 400 (:status (post app "/api/state" {:id :social :mutations {"  " {}}}))))
      (is (= 400 (:status (post app "/api/state" {:id :relics})))))))

(deftest state-endpoint-under-browser-storage
  ;; The same handler, with the collection travelling in and the writes back out.
  (let [app  (http/app (engine/create (assoc social-config :storage {:backend :browser})))
        resp (body (post app "/api/state" {:id        :social
                                           :state     {:social {"Vex" {:deception 5 :persuasion 2 :present? true}}}
                                           :mutations {"Vex" {:deception 5 :persuasion 2 :present? false}}}))]
    (is (= {:social {"Vex" {:deception 5M :persuasion 2M :present? false}}}
           (:store/mutations resp))
        "the edit comes back for the client to apply")
    (is (= false (get-in resp [:store/state "Vex" :present?])))))

(deftest errors-return-edn
  (let [app (http/app (engine/create config {:store (edn-store/create {:backend :memory})}))
        resp (post app "/api/generate" {:id :nonexistent})]
    (is (= 400 (:status resp)))
    (is (string? (:error (body resp))))))

(deftest browser-storage-round-trip
  ;; Under :browser the server holds nothing: the client sends the collections a
  ;; plugin uses, and gets back the writes to apply to its own IndexedDB.
  (let [app (http/app (engine/create (assoc config :storage {:backend :browser})))]
    (testing "a write comes back as mutations rather than being persisted"
      (let [vm    (body (post app "/api/generate" {:id :relics}))
            muts  (:store/mutations vm)
            [id r] (first (:relics muts))]
        (is (some? muts) "the response carries the plugin's writes")
        (is (= #{:relics} (set (keys muts))) "only the plugin's own collection")
        (is (string? id))
        (is (= [] (:path r)) "a fresh relic starts with an empty path")
        (testing "the server kept nothing — the same id is unknown without state"
          (let [resp (post app "/api/action" {:id     :relics
                                              :action :level-up
                                              :params {:relic-id id}})]
            (is (= 400 (:status resp)))
            (is (= "Unknown relic" (:error (body resp))))))
        (testing "sending the state back lets the action resolve and advance"
          (let [choice (-> vm :loot/actions first :action/event second :params :choice)
                vm'    (body (post app "/api/action" {:id     :relics
                                                      :action :level-up
                                                      :params {:relic-id id :choice choice}
                                                      :state  {:relics {id r}}}))]
            (is (re-find #"level 2" (:loot/subtitle vm')))
            (is (= 1 (count (get-in vm' [:store/mutations :relics id :path])))
                "the advanced path comes back for the client to store")))))
    (testing "a read-only request carries no mutation key"
      (let [vm (body (post app "/api/generate" {:id :divine-dust}))]
        (is (not (contains? vm :store/mutations)))))))

(deftest export-round-trips-into-a-file-store
  ;; The acceptance criterion for export: the ZIP unzips into a directory a
  ;; :file deployment can be pointed at, and carries on from exactly there.
  (let [store (edn-store/create {:backend :memory})
        app   (http/app (engine/create config {:store store}))]
    (p/mutate! store {:relics {"r1" {:name "Sunblade" :path [{:id :sharp}]}}
                      :social {"Vex" {:deception 5 :persuasion 2 :present? true}}})
    (let [resp (app {:request-method :get :uri "/api/export"})
          dir  (doto (io/file (System/getProperty "java.io.tmpdir")
                              (str "sns-export-" (System/nanoTime)))
                 .mkdirs)]
      (try
        (is (= 200 (:status resp)))
        (is (= "application/zip" (get-in resp [:headers "Content-Type"])))
        (testing "the archive holds one file per collection"
          (with-open [zip (ZipInputStream. (:body resp))]
            (loop [names #{}]
              (if-let [entry (.getNextEntry zip)]
                (let [name (.getName entry)]
                  (io/copy zip (io/file dir name))
                  (recur (conj names name)))
                (is (= #{"relics.edn" "social.edn"} names))))))
        (testing "a :file store over the unzipped directory sees the same state"
          (let [restored (doto (edn-store/create {:backend :file :dir (str dir)}) p/setup!)]
            (is (= (p/read-collection store :relics) (p/read-collection restored :relics)))
            (is (= (p/read-collection store :social) (p/read-collection restored :social)))
            (is (= [{:id :sharp}] (get-in (p/read-collection restored :relics) ["r1" :path]))
                "vectors and keywords survive the archive")))
        (finally
          (run! io/delete-file (reverse (file-seq dir))))))))
