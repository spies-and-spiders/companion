(ns sns.server.store
  "Facade over the storage backends. `from-config` selects one from the config
   `:storage` map. `:browser` has no server-side store at all — its state lives
   in the DM's browser and arrives with each request, so the engine builds a
   request-scoped store instead."
  (:require
    [sns.server.store.edn :as edn-store]))

(defn browser?
  [config]
  (= :browser (get-in config [:storage :backend])))

(defn from-config
  "Build a `Store` from the config `:storage` map, or nil for `:browser`.
   Defaults to the in-memory backend when no storage is configured (handy for
   tests and headless dev)."
  [{:keys [backend] :as storage}]
  (case backend
    :browser            nil
    (:memory :file nil) (edn-store/create storage)
    (throw (ex-info "Unknown storage backend" {:backend backend}))))
