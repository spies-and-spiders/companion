(ns sns.ui.state
  "The single application-state atom. Nexus treats this as its `system`.")

(defonce store
  (atom {:loot-types    []    ; specs from the backend (drives picker + forms)
         :selected      nil   ; currently chosen loot-type id
         :roll-n        ""     ; optional d100 roll (blank = random)
         :inputs        {}    ; collected input-form values
         :result        nil   ; the latest view-model
         :error         nil
         :loading?      false
         :report?       false ; whether a reporter (e.g. Discord) is configured
         :report-label  nil   ; button label supplied by the backend reporter
         :report-status nil   ; nil | :sending | :sent
         :editing?      false ; manually editing the current result in place
         :page          :loot ; :loot | :social (the always-on group tracker)
         :social        nil   ; tracker snapshot (from the backend, or local)
         :social-local? false ; no server storage: tracker lives in the browser
         :social-form   {}})) ; the add/update-character form fields
