(ns sns.ui.actions
  "Pure actions: state in, effects out. Responses arrive as actions too, so
   effects never decide anything."
  (:require
    [clojure.string :as str]
    [sns.ui.state :as state]
    [sns.ui.template :as template]))

;; --- helpers -------------------------------------------------------------------

(defn- tool-fx [id m]
  [:fx/merge-in [:tools id] m])

(defn- without [v idx]
  (into (subvec v 0 idx) (subvec v (inc idx))))

(defn collections-for
  "The collections loot type `id` declared, or for a table roll (nil `id`),
   those of every type on the loot table."
  [state id]
  (if id
    (or (:store/collections (state/spec state id)) [])
    (into [] (comp (mapcat #(:store/collections (state/spec state (:id %)))) (distinct))
          (:loot-table state))))

(defn- request-fx
  "A request whose response (or `{:error}`) is appended to each `on-ok` (or
   `on-err`) action. Under `:browser` storage, `collections` are sent with it."
  [req & {:keys [collections on-ok on-err]}]
  [:fx/request (cond-> {:req req :on-ok on-ok :on-err on-err}
                       collections (assoc :collections collections))])

(defn failed
  "Show the error, and stop loot type `id` loading."
  [_state id {:keys [error]}]
  (cond-> [[:fx/assoc-in [:error] (or error "Something went wrong")]]
          id (conj (tool-fx id {:loading? false :report-status nil}))))

;; --- result history ------------------------------------------------------------

;; ponytail: oldest entries fall off at this depth. Paging if a DM ever wants a
;; history deeper than a session's worth of loot.
(def ^:private history-limit 100)

(defn history-mode [state id]
  (or (:history (state/spec state id)) (:history-mode state)))

(defn- rows-for [state id]
  (vec (get (:history state) (name id))))

(defn- write-history-fx [id rows]
  [[:fx/assoc-in [:history (name id)] rows]
   [:fx/persist-history id rows]])

(defn- with-entry [rows vm]
  (vec (take history-limit (cons {:at (js/Date.now) :view-model vm} rows))))

(defn history-fx
  "File `vm` in loot type `id`'s history if its history mode is `trigger`.
   Errors are never filed."
  [state id vm trigger]
  (when (and vm (not (:loot/error? vm)) (= trigger (history-mode state id)))
    (write-history-fx id (with-entry (rows-for state id) vm))))

(defn history-loaded [_state history]
  [[:fx/assoc-in [:history] (or history {})]])

(defn history-save [state id]
  (when-let [result (get-in state [:tools id :result])]
    (conj (write-history-fx id (with-entry (rows-for state id) result))
          (tool-fx id {:saved? true}))))

(defn- bench-fx
  "Put `vm` on loot type `id`'s bench. An error is overlaid instead, keeping the
   bench for when it's dismissed."
  [id vm saved?]
  (if (:loot/error? vm)
    [(tool-fx id {:error vm})]
    [(tool-fx id {:result vm :previous nil :error nil :editing? false :drafts nil :report-status nil :saved? saved?})]))

(defn history-restore [state id idx]
  (when-let [vm (get-in (rows-for state id) [idx :view-model])]
    (bench-fx id vm true)))

(defn history-delete [state id idx]
  (let [rows (rows-for state id)]
    (when (< idx (count rows))
      (cond-> (write-history-fx id (without rows idx))
              (= (get-in rows [idx :view-model]) (get-in state [:tools id :result]))
              (conj (tool-fx id {:saved? false}))))))

(defn history-clear [_state id]
  (conj (write-history-fx id nil) (tool-fx id {:saved? false})))

(defn history-hover [state id idx alt?]
  (let [row (when idx [id idx])]
    (cond-> [[:fx/assoc-in [:history-hover] row]]
            (and alt? row (not (:history-lock state)))
            (conj [:fx/assoc-in [:history-lock] row]))))

(defn history-lock [state lock?]
  [[:fx/assoc-in [:history-lock] (when lock? (or (:history-lock state) (:history-hover state)))]])

;; --- results -------------------------------------------------------------------

(defn- arrived-fx
  "Bench a new `vm`, filing it in history if `trigger` calls for it."
  [state id vm trigger]
  (let [filed (history-fx state id vm trigger)]
    (into (vec filed) (bench-fx id vm (boolean (seq filed))))))

(defn generate [state id]
  [(tool-fx id {:loading? true :report-status nil})
   [:fx/assoc-in [:error] nil]
   (request-fx {:method :post                                               :url "/api/generate"
                :body   {:id id :inputs (get-in state [:tools id :inputs])}}
               :collections (collections-for state id)
               :on-ok [[:ui/generated id]]
               :on-err [[:ui/failed id]])])

(defn generated [state id vm]
  (conj (arrived-fx state id vm :always) (tool-fx id {:loading? false})))

(defn generate-on-enter [state id key]
  (when (= key "Enter")
    (generate state id)))

;; Dispatched from an `:action/event`, sending the on-screen result so the
;; plugin sees the DM's edits. `idx` locates the action's inputs, if it has any.
(defn act [state {:keys [id action params]} & [idx]]
  [(tool-fx id {:loading? true :report-status nil})
   [:fx/assoc-in [:error] nil]
   (request-fx {:method :post                                                                                 :url "/api/action"
                :body   (cond-> {:id id :action action :params params :view-model (get-in state [:tools id :result])}
                                idx (assoc :inputs (get-in state [:tools id :action-inputs idx])))}
               :collections (collections-for state id)
               :on-ok [[:ui/acted id]]
               :on-err [[:ui/failed id]])])

(defn act-on-enter [state event idx key]
  (when (= key "Enter")
    (act state event idx)))

(defn acted
  "Bench an action's result, filing it in history only if it changed. The
   replaced result is kept as `:previous` to highlight the difference."
  [state id vm]
  (let [{:keys [result saved?]} (get-in state [:tools id])]
    (conj (if (= vm result)
            (bench-fx id vm saved?)
            (conj (arrived-fx state id vm :always) (tool-fx id {:previous result})))
          (tool-fx id {:loading? false}))))

(defn report [state id]
  ;; The backend can't render templates.
  (when-let [vm (some-> (get-in state [:tools id :result]) template/render-view-model)]
    [(tool-fx id {:report-status :sending})
     [:fx/assoc-in [:error] nil]
     (request-fx {:method :post :url "/api/report" :body {:view-model vm}}
                 :on-ok [[:ui/reported id]]
                 :on-err [[:ui/failed id]])]))

(defn reported [state id _resp]
  (let [filed (history-fx state id (get-in state [:tools id :result]) :on-report)]
    (conj (vec filed)
          (tool-fx id (cond-> {:report-status :sent} (seq filed) (assoc :saved? true))))))

;; --- pages ---------------------------------------------------------------------

(defn- manual-fx
  "Apply `mutations` (`{<key> <row>}`, nil removing) to loot type `id`'s manual
   collection, then read it."
  [state id mutations]
  (request-fx {:method :post                                                    :url "/api/state"
               :body   (cond-> {:id id} mutations (assoc :mutations mutations))}
              :collections (collections-for state id)
              :on-ok [[:ui/manual-loaded id]]
              :on-err [[:ui/failed nil]]))

;; ponytail: the reply replaces the whole local map, so typing into a second row
;; while the first is in flight loses it. Per-row merging if that ever bites.
(defn manual-loaded [_state id resp]
  [[:fx/assoc-in [:tools id :manual] (:store/state resp)]
   [:fx/assoc-in [:error] nil]])

(defn show-page
  "Show `page`, refreshing its types' manual tables."
  [state page]
  (into [[:fx/assoc-in [:page] page]
         [:fx/assoc-in [:drag] nil]]
        (comp (filter #(:store/manual (state/spec state %)))
              (map #(manual-fx state % nil)))
        (state/page-tools state page)))

(defn select-page [state page]
  (cond-> (show-page state page)
          (not= page (:page state)) (conj [:fx/push-route page])))

(defn route
  "The URL hash changed (back, forward, or typed in)."
  [state page]
  (show-page state (if (state/known-page? state page) page (state/default-page state))))

(defn booted
  "Take in the specs and capabilities, and show the URL's page or the default."
  [state [types {:keys [report? report-label browser-storage? history loot-die-size loot-table pages sections]}]]
  (let [loaded {:loot-types       (vec types)
                :specs            (into {} (map (juxt :id identity)) types)
                :pages            (vec pages)
                :sections         (vec sections)
                :report?          (boolean report?)
                :report-label     report-label
                :browser-storage? (boolean browser-storage?)
                :loot-die-size    (or loot-die-size 100)
                :loot-table       (vec loot-table)
                :history-mode     (or history :button)}
        state (merge state loaded)
        page  (if (state/known-page? state (:page state)) (:page state) (state/default-page state))]
    (-> [[:fx/merge-in [] loaded]
         [:fx/load-history]
         [:fx/replace-route page]]
        (into (show-page state page)))))

;; --- loot table ----------------------------------------------------------------

(defn roll-number
  "The number typed into the roll box, or nil."
  [roll-n]
  (some-> roll-n str str/trim parse-long))

(defn roll
  "Roll the typed number, or have the server roll if the box is blank."
  [{:keys [tools roll-n] :as state}]
  (let [n (roll-number roll-n)]
    [[:fx/assoc-in [:error] nil]
     (request-fx {:method :post                                                                                    :url "/api/roll"
                  :body   (cond-> {:inputs (into {} (keep (fn [[id t]] (some->> (:inputs t) (vector id)))) tools)}
                                  n (assoc :n n))}
                 :collections (collections-for state nil)
                 :on-ok [[:ui/rolled]]
                 :on-err [[:ui/failed nil]])]))

(defn roll-on-enter [state key]
  (when (= key "Enter")
    (roll state)))

(defn roll-die [{:keys [loot-die-size]}]
  [[:fx/assoc-in [:roll-n] (str (inc (rand-int loot-die-size)))]])

(defn rolled
  "Bench every rolled result and show a page holding them all."
  [state {:keys [results]}]
  (let [ids    (mapv :id results)
        rolled (assoc state :rolled ids)]
    (-> [[:fx/assoc-in [:rolled] ids]]
        (into (mapcat (fn [{:keys [id view-model]}] (arrived-fx state id view-model :always))) results)
        (into (select-page rolled (state/page-for rolled ids))))))

;; --- input form ----------------------------------------------------------------
;; `path` locates the inputs map a form fills in: `[:tools id :inputs]` for a
;; loot type's generate form, `[:tools id :action-inputs idx]` for an action's.

(defn set-input [_state path field value]
  [[:fx/assoc-in (conj path field) value]])

;; An `idx` past the end appends.
(defn set-list-input [state path field idx value]
  (let [current (vec (get-in state (conj path field)))]
    [[:fx/assoc-in (conj path field)
      (if (< idx (count current))
        (assoc current idx value)
        (conj current value))]]))

(defn remove-list-input [state path field idx]
  (let [current (vec (get-in state (conj path field)))]
    (when (< idx (count current))
      [[:fx/assoc-in (conj path field) (without current idx)]])))

(defn list-drag-start [_state path field idx]
  [[:fx/assoc-in [:drag] {:path path :field field :from idx}]])

(defn list-drag-end [_state]
  [[:fx/assoc-in [:drag] nil]])

(defn- move
  "Move the element at `from` to index `to`."
  [v from to]
  (let [item   (nth v from)
        others (without v from)]
    (into (conj (subvec others 0 to) item) (subvec others to))))

(defn list-drag-drop [{:keys [drag] :as state} path field to-idx]
  (let [current (vec (get-in state (conj path field)))
        from    (:from drag)]
    (when (and drag (= [path field] [(:path drag) (:field drag)]) (not= from to-idx)
               (< from (count current)) (< to-idx (count current)))
      [[:fx/assoc-in (conj path field) (move current from to-idx)]
       [:fx/assoc-in [:drag] nil]])))

;; --- result editor -------------------------------------------------------------

(defn- edited
  "Mark the result as no longer what was sent or saved."
  [id]
  (tool-fx id {:report-status nil :saved? false}))

(defn toggle-edit [state id]
  [(tool-fx id {:editing? (not (get-in state [:tools id :editing?])) :report-status nil :previous nil})])

(defn- retype
  "Parse an input's string back into the var's declared type, or the rank
   control's whole numbers. Unparseable input like `-` becomes nil."
  [type value]
  (cond
    (not (string? value))   value
    (= :rank type)          (parse-long value)
    (#{:int :decimal} type) (parse-double value)
    :else                   value))

(defn edit-result [_state id path type value]
  [[:fx/assoc-in (into [:tools id :result] path) (retype type value)]
   (edited id)])

(defn add-result-metadata [state id path key]
  (let [draft-path (into [:tools id :drafts] path)
        tag        (some-> (get-in state draft-path) str/trim)
        path       (into [:tools id :result] path)]
    (when (and (= key "Enter") (seq tag))
      [[:fx/assoc-in path (conj (vec (get-in state path)) tag)]
       [:fx/assoc-in draft-path nil]
       (edited id)])))

(defn remove-result-metadata [state id path i]
  (let [path (into [:tools id :result] path)]
    [[:fx/assoc-in path (without (vec (get-in state path)) i)]
     (edited id)]))

(defn edit-result-metadata
  "Move tag `i` into the draft box, replacing it with any pending draft."
  [state id path i]
  (let [draft-path (into [:tools id :drafts] path)
        draft      (some-> (get-in state draft-path) str/trim)
        path       (into [:tools id :result] path)
        tags       (vec (get-in state path))]
    [[:fx/assoc-in path (if (seq draft) (assoc tags i draft) (without tags i))]
     [:fx/assoc-in draft-path (tags i)]
     (edited id)]))

(defn add-result-item [state id si]
  (let [path [:tools id :result :loot/sections si :section/items]]
    [[:fx/assoc-in path (conj (vec (get-in state path)) {:item/body ""})]
     (edited id)]))

(defn add-result-var [state id vars-path key]
  (let [draft-path (into [:tools id :drafts] vars-path)
        {var-name :name :keys [type]} (get-in state draft-path)
        var-name   (some-> var-name str/trim (str/replace #"\s+" "-"))
        path       (conj (into [:tools id :result] vars-path) (keyword var-name))]
    (when (and (= key "Enter") (seq var-name) (nil? (get-in state path)))
      [[:fx/assoc-in path (case type
                            "text" {:value ""}
                            "bool" {:value false :type :bool}
                            {:value 0 :type :decimal})]
       [:fx/assoc-in draft-path nil]
       (edited id)])))

(defn remove-result-var [state id var-path]
  (let [vars-path (into [:tools id :result] (pop var-path))]
    [[:fx/assoc-in vars-path (dissoc (get-in state vars-path) (peek var-path))]
     (edited id)]))

(defn remove-result-item [state id si ii]
  (let [path  [:tools id :result :loot/sections si :section/items]
        items (vec (get-in state path))]
    (when (< ii (count items))
      [[:fx/assoc-in path (without items ii)]
       ;; Drafts are keyed by index, so would shift onto the wrong items.
       [:fx/assoc-in [:tools id :drafts :loot/sections si] nil]
       (edited id)])))

(defn dismiss-error [_state id]
  [(tool-fx id {:error nil})])

;; --- manually-managed state (the `:store/manual` editor) ---------------------
;; Edits are local until `change` (blur, or a checkbox toggle), which commits the
;; whole row.

(defn manual-edit [_state id k path value]
  [[:fx/assoc-in (into [:tools id :manual k] path) value]])

(defn manual-commit [state id k]
  [(manual-fx state id {k (get-in state [:tools id :manual k])})])

(defn manual-remove [state id k]
  [(manual-fx state id {k nil})])

(defn manual-remove-item [state id k idx]
  (let [rows (vec (get-in state [:tools id :manual k]))]
    (when (< idx (count rows))
      [(manual-fx state id {k (without rows idx)})])))

(defn manual-set-key [_state id value]
  [[:fx/assoc-in [:tools id :manual-key] value]])

;; The server fills in the new row's defaults.
(defn manual-add [state id]
  (let [k (str/trim (str (get-in state [:tools id :manual-key])))]
    (cond
      (str/blank? k) nil
      (contains? (get-in state [:tools id :manual]) k) [[:fx/assoc-in [:tools id :manual-key] ""]]
      :else [(manual-fx state id {k (if (:list? (:store/manual (state/spec state id))) [] {})})
             [:fx/assoc-in [:tools id :manual-key] ""]])))

;; --- misc ------------------------------------------------------------------------

(defn set-type-filter [_state value]
  [[:fx/assoc-in [:type-filter] value]])

(defn set-roll-input [_state value]
  [[:fx/assoc-in [:roll-n] value]])

(defn spies-preview [state page]
  (when-not (get-in state [:spies page])
    [[:fx/spies-load page]]))

(defn export [_state]
  [[:fx/export]])
