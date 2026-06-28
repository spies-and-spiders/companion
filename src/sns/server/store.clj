(ns sns.server.store
  "`Store` implementations. The default is `chrondb`, which talks to a chrondb
   server over its REST API (chrondb is AGPL and runs as a separate process, so
   it is *not* embedded). An `in-memory` store is provided for tests and dev."
  (:require
    [clojure.string :as str]
    [hato.client :as http]
    [jsonista.core :as j]
    [sns.spi.protocols :as p]))

;; --- in-memory ---------------------------------------------------------------

(defn in-memory
  "An atom-backed `Store`. `query` matches documents whose fields equal every
   entry in the query map."
  ([] (in-memory {}))
  ([init]
   (let [state (atom init)]
     (reify p/Store
       (fetch [_ coll id] (get-in @state [coll id]))
       (query [_ coll q]
         (->> (vals (get @state coll))
              (filterv (fn [doc] (every? (fn [[k v]] (= v (get doc k))) q)))))
       (put! [_ coll id doc] (swap! state assoc-in [coll id] doc) doc)
       (update! [_ coll id f]
         (-> (swap! state update-in [coll id] f)
             (get-in [coll id])))))))

;; --- chrondb over REST -------------------------------------------------------

(def ^:private mapper j/keyword-keys-object-mapper)
(def ^:private collection-field :_collection)

(defn- doc-key [coll id]
  (str (name coll) ":" id))

(defn- request [base-url method path opts]
  (http/request (merge {:method           method
                        :url              (str base-url "/api/v1" path)
                        :throw-exceptions false
                        :accept           :json}
                       opts)))

(defn- read-body [{:keys [status body]}]
  (when (and (= 200 status) (seq body))
    (j/read-value body mapper)))

(defn chrondb
  "A `Store` backed by a chrondb REST server at `base-url`
   (e.g. \"http://localhost:3000\"). Documents are tagged with their collection
   so `query` can be scoped; the tag is stripped on read."
  [base-url]
  (reify p/Store
    (fetch [_ coll id]
      (some-> (request base-url :get (str "/documents/" (doc-key coll id)) {})
              read-body
              (dissoc collection-field)))
    (put! [_ coll id doc]
      (request base-url :post (str "/documents/" (doc-key coll id))
               {:content-type :json
                :body         (-> (assoc doc collection-field (name coll))
                                  j/write-value-as-string)})
      doc)
    (update! [this coll id f]
      (let [updated (f (p/fetch this coll id))]
        (p/put! this coll id updated)
        updated))
    (query [_ coll q]
      (let [lucene (->> (assoc q collection-field (name coll))
                        (mapv (fn [[k v]] (str (name k) ":" v)))
                        (str/join " AND "))]
        (->> (request base-url :get "/search" {:query-params {:q lucene}})
             read-body
             (mapv #(dissoc % collection-field)))))))

;; --- construction from config ------------------------------------------------

(defn from-config
  "Build a `Store` from the config `:storage` map. Defaults to in-memory when no
   storage is configured (handy for tests and headless dev)."
  [{:keys [backend url]}]
  (case backend
    :chrondb (chrondb (or url "http://localhost:3000"))
    (:memory nil) (in-memory)
    (throw (ex-info "Unknown storage backend" {:backend backend}))))
