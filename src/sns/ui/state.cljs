(ns sns.ui.state
  "The single application-state atom — Nexus treats this as its `system` — and
   the page lookups every layer shares."
  (:require
    [clojure.string :as str]))

(defonce store
  (atom {:loot-types       []      ; specs from the backend, in config order
         :specs            {}      ; the same specs by id
         :pages            []      ; configured pages: {:id :label :section :tools [id ...]}
         :sections         []      ; every tool id in config order (orders the rail)
         :page             nil     ; id of the page on screen, mirrored in the URL hash
         :type-filter      ""      ; rail search box
         :roll-n           ""      ; optional loot-die roll (blank = random)
         ;; loot-type id -> {:inputs :result :error :loading? :editing? :drafts
         ;;                  :report-status :saved? :manual :manual-key}
         :tools            {}
         :drag             nil     ; in-progress list-input reorder: {:plugin :field :from}
         :history          {}      ; the store's history collection: id name -> [{:at :view-model}]
         :history-mode     :button ; when generated results join the history
         :history-hover    nil     ; [loot-type id, row index] of the history row under the cursor
         :history-lock     nil     ; the same, pinned on screen while Alt is held
         :error            nil
         :report?          false   ; whether a reporter (e.g. Discord) is configured
         :report-label     nil     ; button label supplied by the backend reporter
         :spies            {}      ; spies.tools page -> its entries by lowercased name, or :loading
         :browser-storage? false   ; state lives in IndexedDB and travels with each request
         :loot-die-size    100     ; sides on the loot die (from the backend's config)
         :loot-table       []      ; [{:id :ranges [[from to] ...]}] — where each loot type sits on the loot die
         :rolled           []}))   ; loot-type ids the last loot-table roll landed on

(def loot-table-page
  "The page id of the loot-table view, namespaced apart from any tool id."
  :sns/loot-table)

(def rolled-page
  "The page showing a roll that landed on several types no other page holds."
  :sns/rolled)

(defn spec [state id]
  (get-in state [:specs id]))

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

(defn default-page [state]
  (when (seq (:loot-table state)) loot-table-page))

(defn known-page?
  "Whether `page` can be shown: the loot table, or a page holding any tools."
  [state page]
  (if (= loot-table-page page)
    (boolean (default-page state))
    (boolean (seq (page-tools state page)))))

(defn page->hash [page]
  (str "#/" (some-> page str (subs 1))))

(defn hash->page [hash]
  (some-> (str/replace (str hash) #"^#/?" "") not-empty js/decodeURIComponent keyword))
