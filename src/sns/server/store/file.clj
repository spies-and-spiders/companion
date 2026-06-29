(ns sns.server.store.file
  "File-backed `Store`: each collection is persisted to `<dir>/<collection>.json`
   (default dir `./state`) as an `{id doc}` map. Needs no external process —
   handy for a single DM who wants durable state without running a SQL server."
  (:require
    [clojure.java.io :as io]
    [jsonista.core :as j]
    [sns.spi.protocols :as p]))

(def ^:private mapper j/keyword-keys-object-mapper)

(defn- id->key [id]
  (keyword (if (keyword? id) (name id) (str id))))

(defn- coll-file [dir coll]
  (io/file dir (str (name coll) ".json")))

(defn- read-coll [^java.io.File file]
  (if (.exists file)
    (j/read-value (slurp file) mapper)
    {}))

(defn- write-coll! [file m]
  (io/make-parents file)
  (spit file (j/write-value-as-string m)))

(defn create
  "A `Store` that persists each collection to `<dir>/<collection>.json`. Reads and
   writes are guarded by a per-store lock so `update!` is atomic; built for a
   single writer, not concurrent processes."
  ([] (create "./state"))
  ([dir]
   (let [lock (Object.)]
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
