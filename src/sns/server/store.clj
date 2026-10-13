(ns sns.server.store
  "Selects the configured storage backend. `:browser` state arrives with each
   request, so the engine builds a request-scoped store for it instead."
  (:require
    [sns.server.store.edn :as edn-store]))

(defn browser?
  [config]
  (= :browser (get-in config [:storage :backend])))

(defn from-config
  "Build a `Store` from the config's `:storage`: nil for `:browser`, in-memory
   if unset."
  [{:keys [backend] :as storage}]
  (case backend
    :browser            nil
    (:memory :file nil) (edn-store/create storage)
    (throw (ex-info "Unknown storage backend" {:backend backend}))))
