(ns sns.ui.idb
  "IndexedDB persistence for `:browser` storage: one record per
   `[collection key]`, so new collections need no schema change. Stored as EDN
   text, so keywords and sets survive."
  (:require
    [clojure.edn :as edn]))

(def ^:private db-name "sns-state")
(def ^:private store-name "kv")

(defn- request->promise [request]
  (js/Promise.
    (fn [resolve reject]
      (set! (.-onsuccess request) (fn [_] (resolve (.-result request))))
      (set! (.-onerror request) (fn [_] (reject (.-error request)))))))

(defn- migrate!
  "Move records from older per-collection stores into `kv`, within the upgrade
   transaction `tx`."
  [db ^js tx]
  (doseq [old (array-seq (.-objectStoreNames db))
          :when (not= store-name old)
          :let [src (.objectStore tx old)
                kv  (.objectStore tx store-name)
                ks  (.getAllKeys src)]]
    (set! (.-onsuccess ks)
          (fn [_]
            (let [vs (.getAll src)]
              (set! (.-onsuccess vs)
                    (fn [_]
                      (doseq [[k v] (map vector (array-seq (.-result ks)) (array-seq (.-result vs)))]
                        (.put kv v #js [old (pr-str k)]))
                      (.deleteObjectStore db old))))))))

(defn- open-at [version]
  (js/Promise.
    (fn [resolve reject]
      (let [req (if version (.open js/indexedDB db-name version) (.open js/indexedDB db-name))]
        (set! (.-onupgradeneeded req)
              (fn [_]
                (let [db (.-result req)]
                  (when-not (.contains (.-objectStoreNames db) store-name)
                    (.createObjectStore db store-name))
                  (migrate! db (.-transaction req)))))
        (set! (.-onblocked req)
              (fn [_] (reject (js/Error. "Browser storage is being upgraded: close the app's other tabs and try again."))))
        (set! (.-onsuccess req)
              (fn [_]
                (let [db (.-result req)]
                  ;; Never block another tab's upgrade.
                  (set! (.-onversionchange db) (fn [_] (.close db)))
                  (resolve db))))
        (set! (.-onerror req) (fn [_] (reject (.-error req))))))))

(defn- open
  "The database, upgraded to hold `kv` if it doesn't."
  []
  (-> (open-at nil)
      (.then (fn [db]
               (if (.contains (.-objectStoreNames db) store-name)
                 db
                 (let [next-version (inc (.-version db))]
                   (.close db)
                   (open-at next-version)))))))

(defn- with-store
  "Run `(f object-store)` in a `mode` transaction, resolving to its result once
   committed."
  [mode f]
  (-> (open)
      (.then (fn [db]
               (let [tx     (.transaction db store-name mode)
                     result (f (.objectStore tx store-name))
                     done   (js/Promise. (fn [resolve reject]
                                           (set! (.-oncomplete tx) resolve)
                                           (set! (.-onerror tx) #(reject (.-error tx)))))]
                 (-> (js/Promise.all #js [result done])
                     (.then (fn [[r]] r))
                     (.finally #(.close db))))))))

(defn- read-records
  "`{<collection> {<key> <value>}}` from the records `range` selects (all of
   them when nil)."
  [store range]
  (-> (js/Promise.all #js [(request->promise (.getAllKeys store range))
                           (request->promise (.getAll store range))])
      (.then (fn [[ks vs]]
               (reduce (fn [acc [[coll k] v]]
                         (assoc-in acc [(keyword coll) (edn/read-string k)] (edn/read-string v)))
                       {}
                       (map vector ks vs))))))

(defn- collection-range [coll]
  (let [n (name coll)]
    ;; Every [n k] sorts between [n] and [n []], as arrays sort after strings.
    (.bound js/IDBKeyRange #js [n] #js [n #js []])))

(defn read-collections
  "Resolve to `{<collection> {<key> <value>}}`, with unwritten collections as `{}`."
  [collections]
  (if (empty? collections)
    (js/Promise.resolve {})
    (with-store "readonly"
      (fn [store]
        (-> (js/Promise.all (into-array (map #(read-records store (collection-range %)) collections)))
            (.then #(apply merge (zipmap collections (repeat {})) %)))))))

(defn apply-mutations!
  "Apply `{<collection> {<key> <value>}}`, a nil value deleting its key."
  [mutations]
  (if (empty? mutations)
    (js/Promise.resolve nil)
    (with-store "readwrite"
      (fn [store]
        (doseq [[coll changes] mutations
                [k v] changes
                :let [key #js [(name coll) (pr-str k)]]]
          (if (nil? v)
            (.delete store key)
            (.put store (pr-str v) key)))
        (js/Promise.resolve nil)))))

(defn export-state []
  (with-store "readonly" #(read-records % nil)))
