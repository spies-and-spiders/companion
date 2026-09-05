(ns sns.ui.nexus
  "Nexus registry: pure actions return effects; effects perform side-effects
   (state writes, HTTP). Requiring this namespace registers everything."
  (:require
    [clojure.string :as str]
    [nexus.registry :as nxr]
    [sns.ui.api :as api]
    [sns.ui.export :as export]
    [sns.ui.idb :as idb]
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
                                     (fn [{:keys [report? report-label browser-storage?]}]
                                       (dispatch [[:fx/assoc-in [:report?] (boolean report?)]
                                                  [:fx/assoc-in [:report-label] report-label]
                                                  [:fx/assoc-in [:browser-storage?] (boolean browser-storage?)]]))
                                     (fn [_err] nil))))

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

(defn- result-effect [{:keys [dispatch]} system collections req]
  (dispatch [[:fx/assoc-in [:loading?] true] [:fx/assoc-in [:error] nil]
             [:fx/assoc-in [:report-status] nil]])
  (stateful-request
    system collections req
    (fn [vm] (dispatch [[:fx/assoc-in [:result] vm]
                        [:fx/assoc-in [:editing?] false]
                        [:fx/assoc-in [:loading?] false]]))
               (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]
                                    [:fx/assoc-in [:loading?] false]]))))

(nxr/register-effect! :fx/report
                      (fn [{:keys [dispatch]} system]
                        ;; finished text, not templates — the backend has no renderer
                        (when-let [vm (some-> (:result @system) template/render-view-model)]
                          (dispatch [[:fx/assoc-in [:report-status] :sending]
                                     [:fx/assoc-in [:error] nil]])
                          (api/request {:method :post :url "/api/report" :body {:view-model vm}}
                                       (fn [_ok] (dispatch [[:fx/assoc-in [:report-status] :sent]]))
                                       (fn [err] (dispatch [[:fx/assoc-in [:report-status] nil]
                                                            [:fx/assoc-in [:error] (:error err)]]))))))

(nxr/register-effect! :fx/export
                      (fn [{:keys [dispatch]} system]
                        (export/download!
                          (:browser-storage? @system)
                          (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

(nxr/register-effect! :fx/generate
                      (fn [ctx system id inputs]
                        (result-effect ctx system (collections-for system id)
                                       {:method :post :url "/api/generate"
                                        :body   {:id id :inputs inputs}})))

(nxr/register-effect! :fx/roll
                      (fn [{:keys [dispatch]} system inputs n]
                        (dispatch [[:fx/assoc-in [:loading?] true] [:fx/assoc-in [:error] nil]
                                   [:fx/assoc-in [:report-status] nil]])
                        (stateful-request
                          system (collections-for system nil)
                          {:method :post
                           :url    "/api/roll"
                           :body   (cond-> {:inputs inputs} (some? n) (assoc :n n))}
                                     ;; roll returns {:id ... :view-model ...} so we can
                                     ;; jump the picker to the discipline that was rolled.
                                     (fn [{:keys [id view-model]}]
                                       (dispatch [[:fx/assoc-in [:selected] id]
                                                  [:fx/assoc-in [:inputs] {}]
                                                  [:fx/assoc-in [:result] view-model]
                                                  [:fx/assoc-in [:editing?] false]
                                                  [:fx/assoc-in [:loading?] false]]))
                                     (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]
                                                          [:fx/assoc-in [:loading?] false]])))))

(nxr/register-effect! :fx/action
                      (fn [ctx system id action params view-model]
                        (result-effect ctx system (collections-for system id)
                                       {:method :post
                                        :url    "/api/action"
                                        :body   {:id id :action action :params params :view-model view-model}})))

;; A loot type's manually-managed collection: read on selection, and re-read
;; after every edit, so what is on screen is what the store holds. `mutations`
;; is `{<key> <row>}` (a nil row removes the key); nil just reads.
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
                          (fn [resp] (dispatch [[:fx/assoc-in [:manual] (:store/state resp)]
                                                [:fx/assoc-in [:error] nil]]))
                          (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

;; --- actions (pure: state -> effects) ----------------------------------------

(defn- manual-spec
  "The `:store/manual` declaration of loot type `id`, or nil when it has none."
  [loot-types id]
  (some #(when (= id (:id %)) (:store/manual %)) loot-types))

(nxr/register-action! :ui/select-type
                      (fn [{:keys [loot-types]} id]
                        (cond-> [[:fx/assoc-in [:selected] id]
                                 [:fx/assoc-in [:inputs] {}]
                                 [:fx/assoc-in [:drag] nil]
                                 [:fx/assoc-in [:result] nil]
                                 [:fx/assoc-in [:manual] nil]
                                 [:fx/assoc-in [:manual-key] ""]
                                 [:fx/assoc-in [:editing?] false]]
                                (manual-spec loot-types id)
                                (conj [:fx/manual-state id nil]))))

(nxr/register-action! :ui/set-input
                      (fn [_state field value]
                        [[:fx/assoc-in [:inputs field] value]]))

;; --- list (`:list?`) input fields ---------------------------------------------
;; A list field's value is always a vector. `idx` beyond the current end
;; appends (that's how the UI's trailing blank row, or a bool field's "+ Add",
;; grows the list); any other `idx` overwrites in place.

(nxr/register-action! :ui/set-list-input
                      (fn [state field idx value]
                        (let [current (vec (get-in state [:inputs field]))]
                          [[:fx/assoc-in [:inputs field]
                            (if (< idx (count current))
                              (assoc current idx value)
                              (conj current value))]])))

(nxr/register-action! :ui/remove-list-input
                      (fn [state field idx]
                        (let [current (vec (get-in state [:inputs field]))]
                          (when (< idx (count current))
                            [[:fx/assoc-in [:inputs field]
                              (into (subvec current 0 idx) (subvec current (inc idx)))]]))))

(nxr/register-action! :ui/list-drag-start
                      (fn [_state field idx]
                        [[:fx/assoc-in [:drag] {:field field :from idx}]]))

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
                      (fn [{:keys [drag] :as state} field to-idx]
                        (let [current (vec (get-in state [:inputs field]))
                              from    (:from drag)]
                          (when (and drag (= field (:field drag)) (not= from to-idx)
                                     (< from (count current)) (< to-idx (count current)))
                            [[:fx/assoc-in [:inputs field] (move current from to-idx)]
                             [:fx/assoc-in [:drag] nil]]))))

(nxr/register-action! :ui/set-type-filter
                      (fn [_state value]
                        [[:fx/assoc-in [:type-filter] value]]))

(defn- generate-fx [{:keys [selected inputs]}]
  [[:fx/generate selected inputs]])

(nxr/register-action! :ui/generate generate-fx)

;; Enter inside the input form generates, matching the button.
(nxr/register-action! :ui/generate-on-enter
                      (fn [state key]
                        (when (and (= key "Enter") (:selected state))
                          (generate-fx state))))

(nxr/register-action! :ui/set-roll-input
                      (fn [_state value]
                        [[:fx/assoc-in [:roll-n] value]]))

(defn- roll-fx [{:keys [inputs roll-n]}]
  ;; A blank field rolls randomly; a number rolls that d100 result against the
  ;; table's allocation (validated server-side).
  (let [n (when-not (str/blank? roll-n)
            (let [parsed (js/parseInt roll-n 10)]
              (when-not (js/isNaN parsed) parsed)))]
    [[:fx/roll inputs n]]))

(nxr/register-action! :ui/roll roll-fx)

;; Enter inside the roll input rolls, matching the button.
(nxr/register-action! :ui/roll-on-enter
                      (fn [state key]
                        (when (= key "Enter")
                          (roll-fx state))))

(nxr/register-action! :ui/report
                      (fn [_state]
                        [[:fx/report]]))

(nxr/register-action! :ui/toggle-edit
                      (fn [state]
                        ;; clear any stale "Sent ✓" so an edited item reads as unsent
                        [[:fx/assoc-in [:editing?] (not (:editing? state))]
                         [:fx/assoc-in [:report-status] nil]]))

(defn- retype
  "Put an edited value back into the type the plugin declared (`:type` on
   `sns.sdk.schema/item-var`), since every input hands back a string. Blank or
   mid-typing (`-`, `1e`) becomes nil: it renders as nothing, and an op still
   accumulates onto it."
  [type value]
  (if (and (string? value) (#{:int :decimal} type))
    (parse-double value)
    value))

(nxr/register-action! :ui/edit-result
                      (fn [_state path type value]
                        [[:fx/assoc-in (into [:result] path) (retype type value)]
                         [:fx/assoc-in [:report-status] nil]]))

(nxr/register-action! :ui/edit-result-metadata
                      (fn [_state path value]
                        [[:fx/assoc-in (into [:result] path)
                          (->> (str/split (or value "") #",")
                               (map str/trim)
                               (remove str/blank?)
                               vec)]
                         [:fx/assoc-in [:report-status] nil]]))

;; Dispatched directly from a view-model's :action/event vector. Sends the
;; current (possibly DM-edited) :result alongside the action's own static
;; params, so the plugin can see edits made since generation (issue #8).
(nxr/register-action! :loot/action
                      (fn [{:keys [result]} {:keys [id action params]}]
                        [[:fx/action id action params result]]))

(nxr/register-action! :ui/export
                      (fn [_state]
                        [[:fx/export]]))

;; --- manually-managed state (the `:store/manual` editor) ---------------------
;; Edits land in local state as they are typed and commit on `change` (blur, or
;; a checkbox toggling), so a row costs one request rather than one per
;; keystroke. Each commit sends the whole row and re-reads the collection.

(nxr/register-action! :ui/manual-edit
                      (fn [_state k path value]
                        [[:fx/assoc-in (into [:manual k] path) value]]))

(nxr/register-action! :ui/manual-commit
                      (fn [{:keys [selected manual]} k]
                        [[:fx/manual-state selected {k (get manual k)}]]))

(nxr/register-action! :ui/manual-remove
                      (fn [{:keys [selected]} k]
                        [[:fx/manual-state selected {k nil}]]))

(nxr/register-action! :ui/manual-remove-item
                      (fn [{:keys [selected manual]} k idx]
                        (let [rows (vec (get manual k))]
                          (when (< idx (count rows))
                            [[:fx/manual-state selected
                              {k (into (subvec rows 0 idx) (subvec rows (inc idx)))}]]))))

(nxr/register-action! :ui/manual-set-key
                      (fn [_state value]
                        [[:fx/assoc-in [:manual-key] value]]))

;; A new key starts empty — the server fills its fields in from their declared
;; defaults, so the browser needs to know nothing about them.
(nxr/register-action! :ui/manual-add
                      (fn [{:keys [selected loot-types manual manual-key]}]
                        (let [k (str/trim (str manual-key))]
                          (cond
                            (str/blank? k)       nil
                            (contains? manual k) [[:fx/assoc-in [:manual-key] ""]]
                            :else
                            [[:fx/manual-state selected
                              {k (if (:list? (manual-spec loot-types selected)) [] {})}]
                             [:fx/assoc-in [:manual-key] ""]]))))
