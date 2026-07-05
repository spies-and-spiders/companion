(ns sns.server.store.file
  "File-backed `Store`: each collection is persisted to `<dir>/<collection>.edn`
   (default dir `./state`) as an EDN-encoded `{id doc}` map. EDN (not plain JSON)
   so keyword values in docs survive the round-trip, while staying readable on
   disk. Needs no external process — handy for a single DM who wants durable
   state without a SQL server."
  (:require
    [clojure.java.io :as io]
    [sns.server.store.codec :as codec]
    [sns.spi.protocols :as p]))

(defn- id->key [id]
  (keyword (if (keyword? id) (name id) (str id))))

(defn- coll-file [dir coll]
  (io/file dir (str (name coll) ".edn")))

(defn- read-coll [^java.io.File file]
  (if (.exists file)
    (codec/decode-from file)
    {}))

(defn- write-coll! [file m]
  (io/make-parents file)
  (spit file (codec/encode m)))

(defn create
  "A `Store` that persists each collection to `<dir>/<collection>.edn`. Reads and
   writes are guarded by a per-store lock so `update!` is atomic; built for a
   single writer, not concurrent processes."
  ([] (create "./state"))
  ([dir]
   (let [lock (Object.)]
     (.mkdirs (io/file dir))
     (reify p/Store
       (fetch [_ coll id]
         (locking lock
           (get (read-coll (coll-file dir coll)) (id->key id))))
       (query [_ coll q]
         (locking lock
           (->> (vals (read-coll (coll-file dir coll)))
                (filterv (fn [doc] (every? (fn [[k v]] (= v (get doc k))) q))))))
       (put! [_ coll id doc]
         (locking lock
           (let [file (coll-file dir coll)]
             (write-coll! file (assoc (read-coll file) (id->key id) doc))
             doc)))
       (update! [_ coll id f]
         (locking lock
           (let [file    (coll-file dir coll)
                 k       (id->key id)
                 m       (read-coll file)
                 updated (f (get m k))]
             (write-coll! file (assoc m k updated))
             updated)))))))
