(ns sns.ui.state
  "The single application-state atom — Nexus treats this as its `system` — and
   the page lookups every layer shares.")

(defonce store
  (atom {:loot-types    []    ; specs from the backend (drives picker + forms)
         :pages         []    ; configured pages: {:id :label :section :tools [id ...]}
         :sections      []    ; every tool id in config order (orders the rail)
         :page          nil   ; id of the page on screen
         :type-filter   ""    ; rail search box
         :roll-n        ""     ; optional loot-die roll (blank = random)
         :inputs        {}    ; loot-type id -> collected input-form values
         :drag          nil   ; in-progress list-input reorder: {:plugin :field :from}
         :results       {}    ; loot-type id -> its view-model on the bench (session-only)
         :history       {}    ; the store's history collection: id name -> [{:at :view-model}]
         :history-mode  :button ; when generated results join the history
         :history-hover nil   ; [loot-type id, row index] of the history row under the cursor
         :history-lock  nil   ; the same, pinned on screen while Alt is held
         :error         nil
         :loading       {}    ; loot-type id -> generating?
         :report?       false ; whether a reporter (e.g. Discord) is configured
         :report-label  nil   ; button label supplied by the backend reporter
         :report-status {}    ; loot-type id -> nil | :sending | :sent
         :editing       {}    ; loot-type id -> manually editing its result in place?
         :manual        {}    ; loot-type id -> its manual-state collection
         :manual-key    {}    ; loot-type id -> the key being typed into the blank "add" row
         :browser-storage? false ; state lives in IndexedDB and travels with each request
         :loot-die-size 100}))  ; sides on the loot die (from the backend's config)

(defn spec [{:keys [loot-types]} id]
  (some #(when (= id (:id %)) %) loot-types))

(defn previewed
  "The index of loot type `id`'s history row on preview: the pinned row while Alt
   is held, otherwise the hovered one."
  [{:keys [history-hover history-lock]} id]
  (let [[row-id idx] (or history-lock history-hover)]
    (when (= id row-id) idx)))

(defn- all-pages
  "The configured pages, plus a page of its own for every loot type."
  [state]
  (concat (:pages state) (map (fn [{:keys [id]}] {:id id :tools [id]}) (:loot-types state))))

(defn page-tools [state page]
  (some #(when (= page (:id %)) (:tools %)) (all-pages state)))

(defn page-for
  "Where loot type `id` is shown: the page on screen if it is there already,
   otherwise its own page."
  [state id]
  (if (some #{id} (page-tools state (:page state)))
    (:page state)
    id))
