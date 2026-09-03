(ns sns.server.store.request
  "Request-scoped `Store` for `:browser` storage, where the state lives in the
   DM's IndexedDB and the server holds nothing. Seeded with the collections the
   client sent, it records mutations rather than persisting them; the engine
   puts the recording on the response for the client to apply.

   Writes are still visible to later reads within the same request, so a plugin
   behaves identically here and against a durable store."
  (:require
    [sns.sdk.protocols :as p]
    [sns.server.store.edn :as edn-store]))

(defrecord RequestStore [inner recorded]
  p/Store
  (setup! [_])
  (read-collection [_ coll]
    (p/read-collection inner coll))
  (mutate! [_ mutations]
    (p/mutate! inner mutations)
    ;; Nested merge, so mutations to different keys of one collection accumulate
    ;; and a later write to the same key wins.
    (swap! recorded #(merge-with merge % mutations))
    nil))

(defn create
  "A store over the `state` the client supplied, shaped `{<collection> {<key>
   <value>}}`."
  [state]
  (->RequestStore (edn-store/->MemoryStore (atom (or state {}))) (atom {})))

(defn recorded-mutations
  "The mutations made during this request, for the client to apply. Nil for a
   store that persists its own writes, and nil when nothing was written — so a
   read-only request carries no mutation key."
  [store]
  (some-> (:recorded store) deref not-empty))
