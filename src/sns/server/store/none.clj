(ns sns.server.store.none
  "No-op `Store` for DMs who run only stateless loot types. Nothing is ever
   persisted: reads find nothing and writes are discarded."
  (:require
    [sns.spi.protocols :as p]))

(defrecord NoneStore []
  p/Store
  (setup! [_])
  (fetch [_ _coll _id] nil)
  (query [_ _coll _q] [])
  (put! [_ _coll _id doc] doc)
  (update! [_ _coll _id f] (f nil)))

(defn create
  "A `Store` that stores nothing. `fetch` is always nil, `query` always empty;
   `put!`/`update!` honour the return contract but discard the document."
  []
  (->NoneStore))
