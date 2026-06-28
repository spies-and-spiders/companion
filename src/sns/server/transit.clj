(ns sns.server.transit
  "Transit (JSON flavour) encoding/decoding for the HTTP boundary."
  (:require
    [cognitect.transit :as transit])
  (:import
    (java.io ByteArrayInputStream ByteArrayOutputStream InputStream)))

(def content-type "application/transit+json")

(defn read-stream
  "Read a single transit value from an InputStream (e.g. a request body)."
  [^InputStream in]
  (when in
    (transit/read (transit/reader in :json))))

(defn write-str
  "Encode `data` to a transit+json string."
  [data]
  (let [out (ByteArrayOutputStream.)]
    (transit/write (transit/writer out :json) data)
    (.toString out "UTF-8")))

(defn read-str
  "Decode a transit+json string (handy for tests/clients)."
  [^String s]
  (read-stream (ByteArrayInputStream. (.getBytes s "UTF-8"))))
