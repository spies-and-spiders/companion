(ns sns.ui.state
  "The single application-state atom. Nexus treats this as its `system`.")

(defonce store
  (atom {:loot-types   []    ; specs from the backend (drives picker + forms)
         :selected     nil   ; currently chosen loot-type id
         :inputs       {}    ; collected input-form values
         :result       nil   ; the latest view-model
         :error        nil
         :loading?     false
         :report?      false ; whether a reporter (e.g. Discord) is configured
         :report-label nil   ; button label supplied by the backend reporter
         :report-status nil}))  ; nil | :sending | :sent
