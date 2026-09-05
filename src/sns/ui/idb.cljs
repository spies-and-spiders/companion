(ns sns.ui.idb
  "IndexedDB persistence for `:browser` storage. One object store per collection,
   holding one record per key, so the shape matches the server's `:file` backend
   and an export unzips straight into it.

   Values are stored as EDN text rather than structured clones, so keywords and
   sets survive the round trip exactly as they do in a file."
  (:require
    [clojure.edn :as edn]))

(def ^:private db-name "sns-state")

(defn- request->promise [request]
  (js/Promise.
    (fn [resolve reject]
      (set! (.-onsuccess request) (fn [_] (resolve (.-result request))))
      (set! (.-onerror request) (fn [_] (reject (.-error request)))))))

(defn- open-at
  "Open the database, at `version` when given — the only time object stores may
   be created, so `create` names are made then."
  [version create]
  (js/Promise.
    (fn [resolve reject]
      (let [req (if version
                  (.open js/indexedDB db-name version)
                  (.open js/indexedDB db-name))]
        (set! (.-onupgradeneeded req)
              (fn [_]
                (let [db (.-result req)]
                  (doseq [n create
                          :when (not (.contains (.-objectStoreNames db) n))]
                    (.createObjectStore db n)))))
        (set! (.-onsuccess req) (fn [_] (resolve (.-result req))))
        (set! (.-onerror req) (fn [_] (reject (.-error req))))))))

(defn- store-names [db]
  (set (array-seq (.-objectStoreNames db))))

(defn- with-stores
  "Open the database with every name in `names` present, bumping the version only
   when some are missing — opening at a lower version than exists is an error, so
   the current version is always read first."
  [names]
  (-> (open-at nil nil)
      (.then (fn [db]
               (let [missing (remove (store-names db) names)]
                 (if (empty? missing)
                   db
                   (let [next-version (inc (.-version db))]
                     (.close db)
                     (open-at next-version names))))))))

(defn- read-from
  "Read `names` (all known to exist) out of an open `db`."
  [db names]
  (if (empty? names)
    (js/Promise.resolve {})
    (let [tx (.transaction db (clj->js names) "readonly")]
      (-> (js/Promise.all
            (clj->js
              (for [n names
                    :let [store (.objectStore tx n)]]
                (js/Promise.all
                  #js [(request->promise (.getAllKeys store))
                       (request->promise (.getAll store))]))))
          (.then (fn [results]
                   (into {}
                         (map (fn [n result]
                                [(keyword n)
                                 (zipmap (array-seq (aget result 0))
                                         (map edn/read-string (array-seq (aget result 1))))])
                              names
                              (array-seq results)))))))))

(defn read-collections
  "Resolve to `{<collection> {<key> <value>}}` for `collections`. A collection
   that has never been written reads as `{}`, matching the server."
  [collections]
  (if (empty? collections)
    (js/Promise.resolve {})
    (-> (open-at nil nil)
        (.then (fn [db]
                 (let [have (store-names db)
                       present (filterv #(have (name %)) collections)]
                   (-> (read-from db (mapv name present))
                       (.then (fn [read]
                                (.close db)
                                ;; Absent collections still answer, so a plugin
                                ;; never sees a missing key where it expects {}.
                                (merge (zipmap collections (repeat {})) read))))))))))

(defn apply-mutations!
  "Apply `{<collection> {<key> <value>}}`, deleting the keys whose value is nil.
   Resolves once the transaction commits."
  [mutations]
  (if (empty? mutations)
    (js/Promise.resolve nil)
    (let [names (mapv name (keys mutations))]
      (-> (with-stores names)
          (.then (fn [db]
                   (js/Promise.
                     (fn [resolve reject]
                       (let [tx (.transaction db (clj->js names) "readwrite")]
                         (set! (.-oncomplete tx) (fn [_] (.close db) (resolve nil)))
                         (set! (.-onerror tx) (fn [_] (.close db) (reject (.-error tx))))
                         (doseq [[coll changes] mutations
                                 :let [store (.objectStore tx (name coll))]
                                 [k v] changes]
                           (if (nil? v)
                             (.delete store k)
                             (.put store (pr-str v) k))))))))))))

(defn export-state
  "Every collection currently held, for the export button — object store names
   are the collection names, so this needs no declaration from the loot types."
  []
  (-> (open-at nil nil)
      (.then (fn [db]
               (let [names (vec (store-names db))]
                 (-> (read-from db names)
                     (.then (fn [state] (.close db) state))))))))
