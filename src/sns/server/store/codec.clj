(ns sns.server.store.codec
  "Lossless, human-readable (de)serialisation of store documents using EDN.
   Clojure values — notably the keywords in upgrade-graph mods (`:select
   :choice`, option `:id`s, …) — round-trip intact, so stateful loot keeps
   matching the chosen option on the way back. EDN (not JSON) because JSON
   stringifies keyword *values* and can't restore them without a per-document
   schema; EDN needs none and stays readable on disk / in the DB."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io])
  (:import
    (java.io PushbackReader)))

(defn encode
  "Serialise a document to an EDN string."
  [doc]
  (pr-str doc))

(defn decode
  "Deserialise an EDN string back to its Clojure value, nil for blank."
  [s]
  (when (seq s) (edn/read-string s)))

(defn decode-from
  "Deserialise EDN from an `io/reader`-able source (File, InputStream, …)
   without buffering it into a string, nil for empty."
  [source]
  (with-open [r (PushbackReader. (io/reader source))]
    (edn/read {:eof nil} r)))
