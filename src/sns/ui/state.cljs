(ns sns.ui.state
  "The single application-state atom. Nexus treats this as its `system`.")

(defonce store
  (atom {:loot-types    []    ; specs from the backend (drives picker + forms)
         :selected      nil   ; currently chosen loot-type id
         :type-filter   ""    ; picker search box (filters loot types + utilities)
         :roll-n        ""     ; optional d100 roll (blank = random)
         :inputs        {}    ; collected input-form values
         :drag          nil   ; in-progress list-input reorder: {:field :from}
         :result        nil   ; the latest view-model
         :results       {}    ; last view-model per loot-type id (session-only)
         :history       {}    ; the store's history collection: id name -> [{:at :view-model}]
         :history-mode  :button ; when generated results join the history
         :history-hover nil   ; index of the history row under the cursor
         :error         nil
         :loading?      false
         :report?       false ; whether a reporter (e.g. Discord) is configured
         :report-label  nil   ; button label supplied by the backend reporter
         :report-status nil   ; nil | :sending | :sent
         :editing?      false ; manually editing the current result in place
         :manual        nil   ; the selected type's manual-state collection
         :manual-key    ""    ; the key being typed into the blank "add" row
         :browser-storage? false})) ; state lives in IndexedDB and travels with each request
