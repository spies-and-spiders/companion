(ns sns.server.reporter
  "Facade over reporting backends, mirroring `sns.server.store`. `from-config`
   selects a `Reporter` from the config `:reporting` map, or returns nil when
   reporting is not configured (the UI then hides its report button)."
  (:require
    [sns.server.reporter.discord :as discord]))

(defn from-config
  "Build a `Reporter` from the config `:reporting` map, or nil when absent."
  [{:keys [backend] :as cfg}]
  (when cfg
    (case backend
      :discord (discord/create cfg)
      (throw (ex-info "Unknown reporting backend" {:backend backend})))))
