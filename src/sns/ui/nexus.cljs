(ns sns.ui.nexus
  "Nexus registry: pure actions return effects; effects perform side-effects
   (state writes, HTTP). Requiring this namespace registers everything."
  (:require
    [clojure.string :as str]
    [nexus.registry :as nxr]
    [sns.ui.api :as api]
    [sns.ui.export :as export]
    [sns.ui.idb :as idb]
    [sns.ui.spies :as spies]
    [sns.ui.state :as state]
    [sns.ui.template :as template]))

(nxr/register-system->state! deref)

;; --- placeholders (resolve DOM values at dispatch time) ----------------------

(nxr/register-placeholder! :event.target/value
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-target .-value)))

(nxr/register-placeholder! :event.target/checked
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-target .-checked)))

(nxr/register-placeholder! :event/key
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-key)))

(nxr/register-placeholder! :event/alt?
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-altKey)))

(nxr/register-placeholder! :event/raw
                           (fn [{:replicant/keys [dom-event]}]
                             dom-event))

;; --- effects (impure) --------------------------------------------------------

(nxr/register-effect! :fx/assoc-in
                      (fn [_ctx system path v]
                        (swap! system assoc-in path v)))

;; Drag-and-drop reordering needs dragover/drop to suppress the browser's
;; default (open-as-link/navigate) handling to actually receive the drop.
(nxr/register-effect! :fx/prevent-default
                      (fn [_ctx _system dom-event]
                        (.preventDefault dom-event)))

(nxr/register-effect! :fx/load-loot-types
                      (fn [{:keys [dispatch]} _system]
                        (api/request {:url "/api/loot-types"}
                                     (fn [types] (dispatch [[:fx/assoc-in [:loot-types] types]]))
                                     (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

(nxr/register-effect! :fx/load-capabilities
                      (fn [{:keys [dispatch]} _system]
                        (api/request {:url "/api/capabilities"}
                                     (fn [{:keys [report? report-label browser-storage? history loot-die-size loot-table pages sections]}]
                                       (dispatch [[:fx/assoc-in [:pages] (vec pages)]
                                                  [:fx/assoc-in [:sections] (vec sections)]
                                                  [:fx/assoc-in [:report?] (boolean report?)]
                                                  [:fx/assoc-in [:report-label] report-label]
                                                  [:fx/assoc-in [:browser-storage?] (boolean browser-storage?)]
                                                  [:fx/assoc-in [:loot-die-size] (or loot-die-size 100)]
                                                  [:fx/assoc-in [:loot-table] (vec loot-table)]
                                                  [:fx/assoc-in [:history-mode] (or history :button)]
                                                  ;; only now is it known whether the
                                                  ;; history lives in this browser
                                                  [:fx/load-history]]))
                                     (fn [_err] nil))))

;; A failed fetch is forgotten, so the next hover tries again.
(nxr/register-effect! :fx/spies-load
                      (fn [{:keys [dispatch]} system page]
                        (swap! system assoc-in [:spies page] :loading)
                        (-> (js/fetch (spies/data-url page))
                            (.then #(.json %))
                            (.then #(dispatch [[:fx/assoc-in [:spies page]
                                                (spies/index page (js->clj % :keywordize-keys true))]]))
                            (.catch #(swap! system update :spies dissoc page)))))

;; --- result history ---------------------------------------------------------
;; One row per loot type in the `:history` collection, so it lives wherever the
;; DM's `:storage` config puts every other collection.

(def ^:private history-collection :history)

;; ponytail: oldest entries fall off at this depth. Paging if a DM ever wants a
;; history deeper than a session's worth of loot.
(def ^:private history-limit 100)

(defn- with-entry [rows vm]
  (vec (take history-limit (cons {:at (js/Date.now) :view-model vm} rows))))

(defn- history-mode [{:keys [loot-types history-mode]} id]
  (or (some #(when (= id (:id %)) (:history %)) loot-types) history-mode))

(defn- rows-for [state id]
  (get (:history state) (some-> id name)))

(defn- collections-for
  "The store collections a request needs. Known loot type -> exactly what it
   declared; a table roll -> every declared collection, since the server picks
   the type."
  [system id]
  (let [specs (:loot-types @system)]
    (if id
      (or (some #(when (= id (:id %)) (:store/collections %)) specs) [])
      (into [] (distinct (mapcat :store/collections specs))))))

(defn- stateful-request
  "Issue `req`, attaching the collections it needs from IndexedDB first and
   applying any writes that come back afterwards. With server-side storage this
   is a plain request — the server persists its own writes."
  [system collections req on-ok on-err]
  (if-not (:browser-storage? @system)
    (api/request req on-ok on-err)
    (-> (idb/read-collections collections)
        (.then (fn [state]
                 (api/request
                   (assoc-in req [:body :state] state)
                   (fn [resp]
                     (-> (idb/apply-mutations! (:store/mutations resp))
                         ;; Stripped before it reaches app state, so an edited
                         ;; view-model never echoes stale writes back.
                         (.then (fn [_] (on-ok (dissoc resp :store/mutations))))
                         (.catch (fn [e] (on-err {:error (str e)})))))
                   on-err)))
        (.catch (fn [e] (on-err {:error (str e)}))))))

(defn- history-request
  "Read the history collection back, having first applied `mutations` (nil just
   reads). Goes through `stateful-request` like every other stateful call, so a
   `:browser` store round-trips through IndexedDB and a server-side one does not."
  [{:keys [dispatch]} system mutations]
  (stateful-request
    system [history-collection]
    {:method :post :url "/api/history" :body (cond-> {} mutations (assoc :mutations mutations))}
    (fn [resp] (dispatch [[:fx/assoc-in [:history] (:store/state resp)]]))
    (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]]))))

(nxr/register-effect! :fx/load-history
                      (fn [ctx system]
                        (history-request ctx system nil)))

(nxr/register-effect! :fx/history
                      (fn [{:keys [dispatch] :as ctx} system id rows]
                        ;; on state before the write goes out, so the list and the
                        ;; "Save to history" button do not flicker for a round-trip
                        (dispatch [[:fx/assoc-in [:history (name id)] rows]])
                        (history-request ctx system {(name id) rows})))

(defn- history-fx
  "Effects recording `vm` against loot type `id`, when the mode in force says
   `trigger` is what stores it. An error result never is."
  [state id vm trigger]
  (when (and id vm (not (:loot/error? vm)) (= trigger (history-mode state id)))
    [[:fx/history id (with-entry (rows-for state id) vm)]]))

(defn- result-fx
  "Effects putting `vm` on loot type `id`'s bench. An error goes over the bench
   instead, leaving what is on it for when the error is dismissed."
  [id vm]
  (if (:loot/error? vm)
    [[:fx/assoc-in [:errors id] vm]]
    [[:fx/assoc-in [:results id] vm]
     [:fx/assoc-in [:errors id] nil]
     [:fx/assoc-in [:editing id] false]
     [:fx/assoc-in [:report-status id] nil]]))

(defn- result-effect
  "Run `req` and put the view-model it returns on loot type `id`'s bench, filing
   it in that type's history. `only-if-changed?` is for an action, which reworks
   the item already on the bench: it earns a history entry only when it actually
   changed something."
  [{:keys [dispatch]} system collections req id only-if-changed?]
  (dispatch [[:fx/assoc-in [:loading id] true] [:fx/assoc-in [:error] nil]
             [:fx/assoc-in [:report-status id] nil]])
  (stateful-request
    system collections req
    (fn [vm] (dispatch (-> (vec (when-not (and only-if-changed? (= vm (get-in @system [:results id])))
                                  (history-fx @system id vm :always)))
                           (into (result-fx id vm))
                           (conj [:fx/assoc-in [:loading id] false]))))
    (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]
                         [:fx/assoc-in [:loading id] false]]))))

(nxr/register-effect! :fx/report
                      (fn [{:keys [dispatch]} system id]
                        ;; finished text, not templates — the backend has no renderer
                        (when-let [vm (some-> (get-in @system [:results id]) template/render-view-model)]
                          (dispatch [[:fx/assoc-in [:report-status id] :sending]
                                     [:fx/assoc-in [:error] nil]])
                          (api/request {:method :post :url "/api/report" :body {:view-model vm}}
                                       (fn [_ok]
                                         (dispatch (into (vec (history-fx @system id (get-in @system [:results id])
                                                                          :on-report))
                                                         [[:fx/assoc-in [:report-status id] :sent]])))
                                       (fn [err] (dispatch [[:fx/assoc-in [:report-status id] nil]
                                                            [:fx/assoc-in [:error] (:error err)]]))))))

(nxr/register-effect! :fx/export
                      (fn [{:keys [dispatch]} system]
                        (export/download!
                          (:browser-storage? @system)
                          (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

(nxr/register-effect! :fx/generate
                      (fn [ctx system id inputs]
                        (result-effect ctx system (collections-for system id)
                                       {:method :post
                                        :url    "/api/generate"
                                        :body   {:id id :inputs inputs}}
                                       id false)))

(nxr/register-effect! :fx/roll
                      (fn [{:keys [dispatch]} system inputs n]
                        (dispatch [[:fx/assoc-in [:error] nil]])
                        (stateful-request
                          system (collections-for system nil)
                          {:method :post
                           :url    "/api/roll"
                           :body   (cond-> {:inputs inputs} (some? n) (assoc :n n))}
                          ;; roll returns every type the roll landed on, so we can
                          ;; jump to a page showing them all.
                          (fn [{:keys [results]}]
                            (let [ids (mapv :id results)]
                              (dispatch (-> [[:fx/assoc-in [:rolled] ids]]
                                            (into (mapcat (fn [{:keys [id view-model]}]
                                                            (concat (history-fx @system id view-model :always)
                                                                    (result-fx id view-model))))
                                                  results)
                                            (conj [:ui/select-page (state/page-for (assoc @system :rolled ids) ids)])))))
                          (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

(nxr/register-effect! :fx/action
                      (fn [ctx system id action params view-model]
                        (result-effect ctx system (collections-for system id)
                                       {:method :post
                                        :url    "/api/action"
                                        :body   {:id id :action action :params params :view-model view-model}}
                                       id true)))

;; A loot type's manually-managed collection: read when its page opens, and
;; re-read after every edit, so what is on screen is what the store holds.
;; `mutations` is `{<key> <row>}` (a nil row removes the key); nil just reads.
(nxr/register-effect! :fx/manual-state
                      (fn [{:keys [dispatch]} system id mutations]
                        (stateful-request
                          system (collections-for system id)
                          {:method :post
                           :url    "/api/state"
                           :body   (cond-> {:id id} mutations (assoc :mutations mutations))}
                          ;; ponytail: the reply replaces the whole local map, so
                          ;; typing into a second row while the first is in flight
                          ;; loses it. Per-row merging if that ever bites.
                          (fn [resp] (dispatch [[:fx/assoc-in [:manual id] (:store/state resp)]
                                                [:fx/assoc-in [:error] nil]]))
                          (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

;; --- actions (pure: state -> effects) ----------------------------------------

(defn- manual-spec
  "The `:store/manual` declaration of loot type `id`, or nil when it has none."
  [state id]
  (:store/manual (state/spec state id)))

(nxr/register-action! :ui/select-page
                      (fn [state page]
                        (into [[:fx/assoc-in [:page] page]
                               [:fx/assoc-in [:drag] nil]]
                              (comp (filter #(manual-spec state %))
                                    (map #(vector :fx/manual-state % nil)))
                              (state/page-tools state page))))

(nxr/register-action! :ui/set-input
                      (fn [_state id field value]
                        [[:fx/assoc-in [:inputs id field] value]]))

;; --- list (`:list?`) input fields ---------------------------------------------
;; A list field's value is always a vector. `idx` beyond the current end
;; appends (that's how the UI's trailing blank row, or a bool field's "+ Add",
;; grows the list); any other `idx` overwrites in place.

(nxr/register-action! :ui/set-list-input
                      (fn [state id field idx value]
                        (let [current (vec (get-in state [:inputs id field]))]
                          [[:fx/assoc-in [:inputs id field]
                            (if (< idx (count current))
                              (assoc current idx value)
                              (conj current value))]])))

(nxr/register-action! :ui/remove-list-input
                      (fn [state id field idx]
                        (let [current (vec (get-in state [:inputs id field]))]
                          (when (< idx (count current))
                            [[:fx/assoc-in [:inputs id field]
                              (into (subvec current 0 idx) (subvec current (inc idx)))]]))))

(nxr/register-action! :ui/list-drag-start
                      (fn [_state id field idx]
                        [[:fx/assoc-in [:drag] {:plugin id :field field :from idx}]]))

(nxr/register-action! :ui/list-drag-end
                      (fn [_state]
                        [[:fx/assoc-in [:drag] nil]]))

(defn- move
  "Move the element at `from` so it ends up at index `to` in the result.
   Removing first shortens the vector by one, so inserting at `to` in that
   shorter vector already lands the element at `to` in the final one — no
   further index adjustment needed."
  [v from to]
  (let [item    (nth v from)
        without (into (subvec v 0 from) (subvec v (inc from)))]
    (into (conj (subvec without 0 to) item) (subvec without to))))

(nxr/register-action! :ui/list-drag-drop
                      (fn [{:keys [drag] :as state} id field to-idx]
                        (let [current (vec (get-in state [:inputs id field]))
                              from    (:from drag)]
                          (when (and drag (= [id field] [(:plugin drag) (:field drag)]) (not= from to-idx)
                                     (< from (count current)) (< to-idx (count current)))
                            [[:fx/assoc-in [:inputs id field] (move current from to-idx)]
                             [:fx/assoc-in [:drag] nil]]))))

(nxr/register-action! :ui/set-type-filter
                      (fn [_state value]
                        [[:fx/assoc-in [:type-filter] value]]))

(defn- generate-fx [state id]
  [[:fx/generate id (get-in state [:inputs id])]])

(nxr/register-action! :ui/generate generate-fx)

;; Enter inside the input form generates, matching the button.
(nxr/register-action! :ui/generate-on-enter
                      (fn [state id key]
                        (when (= key "Enter")
                          (generate-fx state id))))

(nxr/register-action! :ui/set-roll-input
                      (fn [_state value]
                        [[:fx/assoc-in [:roll-n] value]]))

(defn- roll-fx [{:keys [inputs roll-n]}]
  ;; A blank field rolls randomly; a number rolls that die result against the
  ;; table's allocation (validated server-side).
  (let [n (when-not (str/blank? roll-n)
            (let [parsed (js/parseInt roll-n 10)]
              (when-not (js/isNaN parsed) parsed)))]
    [[:fx/roll inputs n]]))

(nxr/register-action! :ui/roll roll-fx)

(nxr/register-action! :ui/roll-die
                      (fn [{:keys [loot-die-size]}]
                        [[:fx/assoc-in [:roll-n] (str (inc (rand-int loot-die-size)))]]))

;; Enter inside the roll input rolls, matching the button.
(nxr/register-action! :ui/roll-on-enter
                      (fn [state key]
                        (when (= key "Enter")
                          (roll-fx state))))

(nxr/register-action! :ui/report
                      (fn [_state id]
                        [[:fx/report id]]))

(nxr/register-action! :ui/spies-preview
                      (fn [state page]
                        (when-not (get-in state [:spies page])
                          [[:fx/spies-load page]])))

(nxr/register-action! :ui/toggle-edit
                      (fn [state id]
                        ;; clear any stale "Sent ✓" so an edited item reads as unsent
                        [[:fx/assoc-in [:editing id] (not (get-in state [:editing id]))]
                         [:fx/assoc-in [:report-status id] nil]]))

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

(nxr/register-action! :ui/edit-result
                      (fn [_state id path type value]
                        [[:fx/assoc-in (into [:results id] path) (retype type value)]
                         [:fx/assoc-in [:report-status id] nil]]))

(nxr/register-action! :ui/edit-result-metadata
                      (fn [_state id path value]
                        [[:fx/assoc-in (into [:results id] path)
                          (->> (str/split (or value "") #",")
                               (map str/trim)
                               (remove str/blank?)
                               vec)]
                         [:fx/assoc-in [:report-status id] nil]]))

(nxr/register-action! :ui/add-result-item
                      (fn [state id si]
                        (let [path [:results id :loot/sections si :section/items]]
                          [[:fx/assoc-in path (conj (vec (get-in state path)) {:item/body ""})]
                           [:fx/assoc-in [:report-status id] nil]])))

(nxr/register-action! :ui/remove-result-item
                      (fn [state id si ii]
                        (let [path  [:results id :loot/sections si :section/items]
                              items (vec (get-in state path))]
                          (when (< ii (count items))
                            [[:fx/assoc-in path (into (subvec items 0 ii) (subvec items (inc ii)))]
                             [:fx/assoc-in [:report-status id] nil]]))))

;; Dispatched directly from a view-model's :action/event vector. Sends the
;; current (possibly DM-edited) result alongside the action's own static
;; params, so the plugin can see edits made since generation (issue #8).
(nxr/register-action! :loot/action
                      (fn [{:keys [results]} {:keys [id action params]}]
                        [[:fx/action id action params (get results id)]]))

;; The history list under the result: newest first, one entry per stored
;; view-model. Restoring one puts it back on the bench, where it can be edited,
;; actioned or reported like any freshly generated result.

(nxr/register-action! :ui/history-save
                      (fn [state id]
                        (when-let [result (get-in state [:results id])]
                          [[:fx/history id (with-entry (rows-for state id) result)]])))

(nxr/register-action! :ui/dismiss-error
                      (fn [_state id]
                        [[:fx/assoc-in [:errors id] nil]]))

(nxr/register-action! :ui/history-restore
                      (fn [state id idx]
                        (when-let [vm (get-in (rows-for state id) [idx :view-model])]
                          (result-fx id vm))))

(nxr/register-action! :ui/history-delete
                      (fn [state id idx]
                        (let [rows (vec (rows-for state id))]
                          (when (< idx (count rows))
                            [[:fx/history id
                              (into (subvec rows 0 idx) (subvec rows (inc idx)))]]))))

(nxr/register-action! :ui/history-hover
                      (fn [state id idx alt?]
                        (let [row (when idx [id idx])]
                          (cond-> [[:fx/assoc-in [:history-hover] row]]
                                  (and alt? row (not (:history-lock state)))
                                  (conj [:fx/assoc-in [:history-lock] row])))))

(nxr/register-action! :ui/history-lock
                      (fn [state lock?]
                        [[:fx/assoc-in [:history-lock]
                          (when lock? (or (:history-lock state) (:history-hover state)))]]))

;; A nil row retracts the key, so clearing leaves nothing behind in the store.
(nxr/register-action! :ui/history-clear
                      (fn [_state id]
                        [[:fx/history id nil]]))

(nxr/register-action! :ui/export
                      (fn [_state]
                        [[:fx/export]]))

;; --- manually-managed state (the `:store/manual` editor) ---------------------
;; Edits land in local state as they are typed and commit on `change` (blur, or
;; a checkbox toggling), so a row costs one request rather than one per
;; keystroke. Each commit sends the whole row and re-reads the collection.

(nxr/register-action! :ui/manual-edit
                      (fn [_state id k path value]
                        [[:fx/assoc-in (into [:manual id k] path) value]]))

(nxr/register-action! :ui/manual-commit
                      (fn [state id k]
                        [[:fx/manual-state id {k (get-in state [:manual id k])}]]))

(nxr/register-action! :ui/manual-remove
                      (fn [_state id k]
                        [[:fx/manual-state id {k nil}]]))

(nxr/register-action! :ui/manual-remove-item
                      (fn [state id k idx]
                        (let [rows (vec (get-in state [:manual id k]))]
                          (when (< idx (count rows))
                            [[:fx/manual-state id
                              {k (into (subvec rows 0 idx) (subvec rows (inc idx)))}]]))))

(nxr/register-action! :ui/manual-set-key
                      (fn [_state id value]
                        [[:fx/assoc-in [:manual-key id] value]]))

;; A new key starts empty — the server fills its fields in from their declared
;; defaults, so the browser needs to know nothing about them.
(nxr/register-action! :ui/manual-add
                      (fn [state id]
                        (let [k (str/trim (str (get-in state [:manual-key id])))]
                          (cond
                            (str/blank? k)                          nil
                            (contains? (get-in state [:manual id]) k) [[:fx/assoc-in [:manual-key id] ""]]
                            :else
                            [[:fx/manual-state id {k (if (:list? (manual-spec state id)) [] {})}]
                             [:fx/assoc-in [:manual-key id] ""]]))))
