(ns sns.server.store
  "Facade over the storage backends. Each engine lives in its own namespace under
   `sns.server.store` (`.mysql`, `.file`, `.memory`); `from-config` selects one
   from the config `:storage` map."
  (:require
    [sns.server.store.file :as file]
    [sns.server.store.memory :as memory]
    [sns.server.store.mysql :as mysql]
    [sns.server.store.none :as none]))

(defn from-config
  "Build a `Store` from the config `:storage` map. Defaults to in-memory when no
   storage is configured (handy for tests and headless dev)."
  [{:keys [backend url dir]}]
  (case backend
    :mysql (mysql/create (or url "jdbc:mariadb://localhost:3306/sns"))
    :file (file/create (or dir "./state"))
    :none (none/create)
    (:memory nil) (memory/create)
    (throw (ex-info "Unknown storage backend" {:backend backend}))))
