(ns sns.ui.actions
  "Pure actions: state in, effects out. A response comes back as an action of
   its own, so what to do with it is decided here too, never in an effect."
  (:require
    [clojure.string :as str]
    [sns.ui.state :as state]
    [sns.ui.template :as template]))

;; --- helpers -------------------------------------------------------------------

(defn- tool-fx
  "Merge `m` into loot type `id`'s slot under `:tools`."
  [id m]
  [:fx/merge-in [:tools id] m])

(defn- without [v idx]
  (into (subvec v 0 idx) (subvec v (inc idx))))

(defn collections-for
  "The store collections a request needs. Known loot type -> exactly what it
   declared; a table roll -> every collection the loot table's types declared,
   since the server picks the type."
  [state id]
  (if id
    (or (:store/collections (state/spec state id)) [])
    (into [] (comp (mapcat #(:store/collections (state/spec state (:id %)))) (distinct))
          (:loot-table state))))

(defn- request-fx
  "A request whose response (or `{:error}`) is appended to each of the `on-ok`
   (or `on-err`) actions. With `collections`, it is stateful: under `:browser`
   storage those travel with it and the writes that come back are applied."
  [req & {:keys [collections on-ok on-err]}]
  [:fx/request (cond-> {:req req :on-ok on-ok :on-err on-err}
                       collections (assoc :collections collections))])

(defn failed
  "A request went wrong: say so, and stop whatever loot type `id` was waiting."
  [_state id {:keys [error]}]
  (cond-> [[:fx/assoc-in [:error] (or error "Something went wrong")]]
          id (conj (tool-fx id {:loading? false :report-status nil}))))

;; --- result history ------------------------------------------------------------
;; One row per loot type in the `:history` collection, so it lives wherever the
;; DM's `:storage` config puts every other collection.

;; ponytail: oldest entries fall off at this depth. Paging if a DM ever wants a
;; history deeper than a session's worth of loot.
(def ^:private history-limit 100)

(defn history-mode [state id]
  (or (:history (state/spec state id)) (:history-mode state)))

(defn- rows-for [state id]
  (vec (get (:history state) (name id))))

(defn- write-history-fx
  "Show `rows` as loot type `id`'s history straight away and persist them."
  [id rows]
  [[:fx/assoc-in [:history (name id)] rows]
   [:fx/persist-history id rows]])

(defn- with-entry [rows vm]
  (vec (take history-limit (cons {:at (js/Date.now) :view-model vm} rows))))

(defn history-fx
  "Effects filing `vm` in loot type `id`'s history, when the mode in force says
   `trigger` is what stores it. An error result never is."
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
  "Effects putting `vm` on loot type `id`'s bench, `saved?` saying whether it is
   already in the history. An error goes over the bench instead, leaving what is
   on it for when the error is dismissed."
  [id vm saved?]
  (if (:loot/error? vm)
    [(tool-fx id {:error vm})]
    [(tool-fx id {:result vm :error nil :editing? false :drafts nil :report-status nil :saved? saved?})]))

(defn history-restore [state id idx]
  (when-let [vm (get-in (rows-for state id) [idx :view-model])]
    (bench-fx id vm true)))

(defn history-delete [state id idx]
  (let [rows (rows-for state id)]
    (when (< idx (count rows))
      (cond-> (write-history-fx id (without rows idx))
              (= (get-in rows [idx :view-model]) (get-in state [:tools id :result]))
              (conj (tool-fx id {:saved? false}))))))

;; A nil row retracts the key, so clearing leaves nothing behind in the store.
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
  "A freshly made `vm` for loot type `id`: filed in its history when `trigger`
   stores it, and put on its bench."
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

;; Dispatched directly from a view-model's :action/event vector. Sends the
;; current (possibly DM-edited) result alongside the action's own static
;; params, so the plugin can see edits made since generation.
(defn act [state {:keys [id action params]}]
  [(tool-fx id {:loading? true :report-status nil})
   [:fx/assoc-in [:error] nil]
   (request-fx {:method :post                                                                                 :url "/api/action"
                :body   {:id id :action action :params params :view-model (get-in state [:tools id :result])}}
               :collections (collections-for state id)
               :on-ok [[:ui/acted id]]
               :on-err [[:ui/failed id]])])

(defn acted
  "An action reworks the item already on the bench, so it earns a history entry
   only when it actually changed something."
  [state id vm]
  (let [{:keys [result saved?]} (get-in state [:tools id])]
    (conj (if (= vm result)
            (bench-fx id vm saved?)
            (arrived-fx state id vm :always))
          (tool-fx id {:loading? false}))))

(defn report [state id]
  ;; finished text, not templates — the backend has no renderer
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
  "Read loot type `id`'s manually-managed collection, first applying `mutations`
   (`{<key> <row>}`, a nil row removing the key); nil just reads."
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
  "Put `page` on screen, reading the manual tables of the types it holds so what
   is shown is what the store has."
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
  "The URL hash changed under us (back, forward, or typed in)."
  [state page]
  (show-page state (if (state/known-page? state page) page (state/default-page state))))

(defn booted
  "The specs and capabilities have both arrived: take them in, and show the page
   the URL names, else the loot table."
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
  "The loot-die result typed into the roll box, or nil when it holds none."
  [roll-n]
  (some-> roll-n str str/trim parse-long))

(defn roll
  "A typed result rolls that side of the loot die; a blank box has the server
   roll it."
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
  "Every type the roll landed on, each put on its bench, and the page showing
   them all."
  [state {:keys [results]}]
  (let [ids    (mapv :id results)
        rolled (assoc state :rolled ids)]
    (-> [[:fx/assoc-in [:rolled] ids]]
        (into (mapcat (fn [{:keys [id view-model]}] (arrived-fx state id view-model :always))) results)
        (into (select-page rolled (state/page-for rolled ids))))))

;; --- input form ----------------------------------------------------------------

(defn set-input [_state id field value]
  [[:fx/assoc-in [:tools id :inputs field] value]])

;; A list field's value is always a vector. `idx` beyond the current end
;; appends (that's how the UI's trailing blank row, or a bool field's "+ Add",
;; grows the list); any other `idx` overwrites in place.
(defn set-list-input [state id field idx value]
  (let [current (vec (get-in state [:tools id :inputs field]))]
    [[:fx/assoc-in [:tools id :inputs field]
      (if (< idx (count current))
        (assoc current idx value)
        (conj current value))]]))

(defn remove-list-input [state id field idx]
  (let [current (vec (get-in state [:tools id :inputs field]))]
    (when (< idx (count current))
      [[:fx/assoc-in [:tools id :inputs field] (without current idx)]])))

(defn list-drag-start [_state id field idx]
  [[:fx/assoc-in [:drag] {:plugin id :field field :from idx}]])

(defn list-drag-end [_state]
  [[:fx/assoc-in [:drag] nil]])

(defn- move
  "Move the element at `from` so it ends up at index `to` in the result.
   Removing first shortens the vector by one, so inserting at `to` in that
   shorter vector already lands the element at `to` in the final one — no
   further index adjustment needed."
  [v from to]
  (let [item   (nth v from)
        others (without v from)]
    (into (conj (subvec others 0 to) item) (subvec others to))))

(defn list-drag-drop [{:keys [drag] :as state} id field to-idx]
  (let [current (vec (get-in state [:tools id :inputs field]))
        from    (:from drag)]
    (when (and drag (= [id field] [(:plugin drag) (:field drag)]) (not= from to-idx)
               (< from (count current)) (< to-idx (count current)))
      [[:fx/assoc-in [:tools id :inputs field] (move current from to-idx)]
       [:fx/assoc-in [:drag] nil]])))

;; --- result editor -------------------------------------------------------------

(defn- edited
  "What any edit to loot type `id`'s result means: it is no longer what was sent
   or saved."
  [id]
  (tool-fx id {:report-status nil :saved? false}))

(defn toggle-edit [state id]
  ;; clear any stale "Sent ✓" so an edited item reads as unsent
  [(tool-fx id {:editing? (not (get-in state [:tools id :editing?])) :report-status nil})])

(defn- retype
  "Put an edited value back into the type the plugin declared (`:type` on
   `sns.sdk.schema/item-var`), since every input hands back a string. Blank or
   mid-typing (`-`, `1e`) becomes nil: it renders as nothing, and a rank still
   steps from it.

   `:rank` is not a declared type but the rank control's own: whole ranks only,
   where a value may well want a decimal typed into it."
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

(defn add-result-item [state id si]
  (let [path [:tools id :result :loot/sections si :section/items]]
    [[:fx/assoc-in path (conj (vec (get-in state path)) {:item/body ""})]
     (edited id)]))

;; A var defined by hand starts blank, joining the grid for its value.
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
       ;; drafts are keyed by index, so they'd shift onto the wrong items
       [:fx/assoc-in [:tools id :drafts :loot/sections si] nil]
       (edited id)])))

(defn dismiss-error [_state id]
  [(tool-fx id {:error nil})])

;; --- manually-managed state (the `:store/manual` editor) ---------------------
;; Edits land in local state as they are typed and commit on `change` (blur, or
;; a checkbox toggling), so a row costs one request rather than one per
;; keystroke. Each commit sends the whole row and re-reads the collection.

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

;; A new key starts empty — the server fills its fields in from their declared
;; defaults, so the browser needs to know nothing about them.
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
