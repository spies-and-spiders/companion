(ns sns.server.reporter
  "Selects the configured reporting backend."
  (:require
    [sns.server.reporter.discord :as discord]))

(defn from-config
  "Build a `Reporter` from the config `:reporting` map, or nil when absent."
  [{:keys [backend] :as cfg}]
  (when cfg
    (case backend
      :discord (discord/create (:discord cfg))
      (throw (ex-info "Unknown reporting backend" {:backend backend})))))
