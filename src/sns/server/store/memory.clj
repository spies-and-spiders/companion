(ns sns.server.store.memory
  "In-memory `Store`, for tests and headless dev. State lives in an atom and is
   lost when the process exits."
  (:require
    [sns.sdk.protocols :as p]))

(defrecord MemoryStore [state]
  p/Store
  (setup! [_])
  (fetch [_ coll id] (get-in @state [coll id]))
  (query [_ coll q]
    (->> (vals (get @state coll))
         (filterv (fn [doc] (every? (fn [[k v]] (= v (get doc k))) q)))))
  (put! [_ coll id doc] (swap! state assoc-in [coll id] doc) doc)
  (update! [_ coll id f]
    (-> (swap! state update-in [coll id] f)
        (get-in [coll id]))))

(defn create
  "An atom-backed `Store`. `query` matches documents whose fields equal every
   entry in the query map."
  ([] (create {}))
  ([init] (->MemoryStore (atom init))))
