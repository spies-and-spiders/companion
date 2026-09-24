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
         :errors        {}    ; loot-type id -> an error view-model shown over its bench
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
         :spies         {}    ; spies.tools page -> its entries by lowercased name, or :loading
         :browser-storage? false ; state lives in IndexedDB and travels with each request
         :loot-die-size 100    ; sides on the loot die (from the backend's config)
         :loot-table    []    ; [{:id :ranges [[from to] ...]}] — where each loot type sits on the loot die
         :rolled        []}))  ; loot-type ids the last loot-table roll landed on

(def loot-table-page
  "The page id of the loot-table view, namespaced apart from any tool id."
  :sns/loot-table)

(def rolled-page
  "The page showing a roll that landed on several types no other page holds."
  :sns/rolled)

(defn spec [{:keys [loot-types]} id]
  (some #(when (= id (:id %)) %) loot-types))

(defn previewed
  "The index of loot type `id`'s history row on preview: the pinned row while Alt
   is held, otherwise the hovered one."
  [{:keys [history-hover history-lock]} id]
  (let [[row-id idx] (or history-lock history-hover)]
    (when (= id row-id) idx)))

(defn- all-pages
  "The configured pages, a page of its own for every loot type, and the page of
   the last roll."
  [state]
  (concat (:pages state)
          (map (fn [{:keys [id]}] {:id id :tools [id]}) (:loot-types state))
          [{:id rolled-page :tools (:rolled state)}]))

(defn page-tools [state page]
  (some #(when (= page (:id %)) (:tools %)) (all-pages state)))

(defn page-for
  "Where loot types `ids` are shown together: the page on screen if it holds
   them all already, a lone type's own page, the first configured page holding
   them all, or else the page of the last roll."
  [state ids]
  (let [holds? (fn [page] (every? (set (page-tools state page)) ids))]
    (cond
      (holds? (:page state)) (:page state)
      (= 1 (count ids))      (first ids)
      :else                  (or (some #(when (holds? (:id %)) (:id %)) (:pages state))
                                 rolled-page))))
