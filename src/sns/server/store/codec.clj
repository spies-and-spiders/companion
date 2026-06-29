(ns sns.server.store.codec
  "Lossless (de)serialisation of store documents. Uses transit+json so Clojure
   values — notably the keywords in upgrade-graph mods (`:select :choice`, option
   `:id`s, …) — round-trip intact. Plain JSON would stringify them, which silently
   breaks stateful loot (the chosen option no longer matches on the way back)."
  (:require
    [sns.server.transit :as transit]))

(defn encode
  "Serialise a document to a transit+json string."
  [doc]
  (transit/write-str doc))

(defn decode
  "Deserialise a transit+json string back to its Clojure value, nil for blank."
  [s]
  (when (seq s) (transit/read-str s)))
