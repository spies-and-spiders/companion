(ns sns.server.store.mysql
  "`Store` backed by any MySQL-compatible SQL server over JDBC (the app only uses
   the MySQL wire protocol, so Dolt, MySQL, MariaDB, etc. are interchangeable; see
   docs/storage.md). The server runs as a separate process, so it is *not*
   embedded.

   The `Store` SPI is schema-agnostic, so documents are persisted generically in
   one `documents(collection, id, doc)` table rather than per-loot-type tables.
   `doc` is EDN-encoded (so keyword values round-trip, and the column stays
   readable) and stored as `LONGTEXT` — the column isn't queried via SQL JSON
   functions, and plain text avoids the server reformatting the payload."
  (:require
    [next.jdbc :as jdbc]
    [sns.server.store.codec :as codec]
    [sns.spi.protocols :as p]))

(defn- ->doc [s]
  (codec/decode s))

(defn- ensure-schema! [ds]
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS documents (
                        collection VARCHAR(255) NOT NULL,
                        id         VARCHAR(255) NOT NULL,
                        doc        LONGTEXT     NOT NULL,
                        PRIMARY KEY (collection, id))"]))

(defn create
  "A `Store` backed by a MySQL-compatible server reachable at JDBC `url`
   (e.g. \"jdbc:mariadb://localhost:3306/sns\"). Documents are stored generically
   in a `documents(collection, id, doc LONGTEXT)` table; `coll`/`id` are coerced
   to strings and `doc` is EDN-encoded."
  [url]
  (let [ds (jdbc/get-datasource {:jdbcUrl url})]
    (ensure-schema! ds)
    (reify p/Store
      (fetch [_ coll id]
        (-> (jdbc/execute-one!
              ds ["SELECT doc FROM documents WHERE collection = ? AND id = ?"
                  (name coll) (str id)])
            :documents/doc
            ->doc))
      (query [_ coll q]
        (->> (jdbc/execute!
               ds ["SELECT doc FROM documents WHERE collection = ?" (name coll)])
             (map (comp ->doc :documents/doc))
             (filterv (fn [doc] (every? (fn [[k v]] (= v (get doc k))) q)))))
      (put! [_ coll id doc]
        (jdbc/execute!
          ds ["INSERT INTO documents (collection, id, doc) VALUES (?, ?, ?)
               ON DUPLICATE KEY UPDATE doc = VALUES(doc)"
              (name coll) (str id) (codec/encode doc)])
        doc)
      (update! [_ coll id f]
        (jdbc/with-transaction [tx ds]
          (let [current (-> (jdbc/execute-one!
                              tx ["SELECT doc FROM documents
                                   WHERE collection = ? AND id = ? FOR UPDATE"
                                  (name coll) (str id)])
                            :documents/doc
                            ->doc)
                updated (f current)]
            (jdbc/execute!
              tx ["INSERT INTO documents (collection, id, doc) VALUES (?, ?, ?)
                   ON DUPLICATE KEY UPDATE doc = VALUES(doc)"
                  (name coll) (str id) (codec/encode updated)])
            updated))))))
