(ns sns.ui.nexus
  "Nexus registry: the pure actions of `sns.ui.actions`, and the effects that
   perform their side-effects (state writes, HTTP, IndexedDB). Requiring this
   namespace registers everything."
  (:require
    [nexus.registry :as nxr]
    [sns.ui.actions :as a]
    [sns.ui.api :as api]
    [sns.ui.export :as export]
    [sns.ui.idb :as idb]
    [sns.ui.spies :as spies]
    [sns.ui.state :as state]))

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

(nxr/register-effect! :fx/merge-in
                      (fn [_ctx system path m]
                        (swap! system #(if (seq path) (update-in % path merge m) (merge % m)))))

;; Drag-and-drop reordering needs dragover/drop to suppress the browser's
;; default (open-as-link/navigate) handling to actually receive the drop.
(nxr/register-effect! :fx/prevent-default
                      (fn [_ctx _system dom-event]
                        (.preventDefault dom-event)))

(nxr/register-effect! :fx/push-route
                      (fn [_ctx _system page]
                        (.pushState js/history nil "" (state/page->hash page))))

(nxr/register-effect! :fx/replace-route
                      (fn [_ctx _system page]
                        (.replaceState js/history nil "" (state/page->hash page))))

(nxr/register-effect! :fx/boot
                      (fn [{:keys [dispatch]} _system]
                        (-> (js/Promise.all #js [(api/request {:url "/api/loot-types"})
                                                 (api/request {:url "/api/capabilities"})])
                            (.then #(dispatch [[:ui/booted (vec %)]]))
                            (.catch #(dispatch [[:ui/failed nil (api/->error %)]])))))

(nxr/register-effect! :fx/request
                      (fn [{:keys [dispatch]} system {:keys [req collections on-ok on-err]}]
                        (-> (if (and collections (:browser-storage? @system))
                              (-> (idb/read-collections collections)
                                  (.then #(api/request (assoc-in req [:body :state] %)))
                                  (.then (fn [resp]
                                           (-> (idb/apply-mutations! (:store/mutations resp))
                                               ;; stripped before it reaches app state, so an edited
                                               ;; view-model never echoes stale writes back
                                               (.then #(dissoc resp :store/mutations))))))
                              (api/request req))
                            (.then (fn [resp] (dispatch (mapv #(conj % resp) on-ok))))
                            (.catch (fn [e] (dispatch (mapv #(conj % (api/->error e)) on-err)))))))

;; Under `:browser` storage the history is read and written in IndexedDB
;; directly: the server would only hand it straight back.
(nxr/register-effect! :fx/load-history
                      (fn [{:keys [dispatch]} system]
                        (-> (if (:browser-storage? @system)
                              (.then (idb/read-collections [:history]) #(:history %))
                              (.then (api/request {:method :post :url "/api/history" :body {}}) #(:store/state %)))
                            (.then #(dispatch [[:ui/history-loaded %]]))
                            (.catch #(dispatch [[:ui/failed nil (api/->error %)]])))))

(nxr/register-effect! :fx/persist-history
                      (fn [{:keys [dispatch]} system id rows]
                        (let [mutations {(name id) rows}]
                          (-> (if (:browser-storage? @system)
                                (idb/apply-mutations! {:history mutations})
                                (api/request {:method :post :url "/api/history" :body {:mutations mutations}}))
                              (.catch #(dispatch [[:ui/failed nil (api/->error %)]]))))))

;; A failed fetch is forgotten, so the next hover tries again.
(nxr/register-effect! :fx/spies-load
                      (fn [{:keys [dispatch]} system page]
                        (swap! system assoc-in [:spies page] :loading)
                        (-> (js/fetch (spies/data-url page))
                            (.then #(.json %))
                            (.then #(dispatch [[:fx/assoc-in [:spies page]
                                                (spies/index page (js->clj % :keywordize-keys true))]]))
                            (.catch #(swap! system update :spies dissoc page)))))

(nxr/register-effect! :fx/export
                      (fn [{:keys [dispatch]} system]
                        (export/download!
                          (:browser-storage? @system)
                          (fn [err] (dispatch [[:ui/failed nil err]])))))

;; --- actions -----------------------------------------------------------------

(doseq [[k f] {:ui/booted                 a/booted
               :ui/failed                 a/failed
               :ui/route                  a/route
               :ui/select-page            a/select-page
               :ui/set-type-filter        a/set-type-filter
               :ui/generate               a/generate
               :ui/generate-on-enter      a/generate-on-enter
               :ui/generated              a/generated
               :loot/action               a/act
               :ui/acted                  a/acted
               :ui/report                 a/report
               :ui/reported               a/reported
               :ui/set-roll-input         a/set-roll-input
               :ui/roll                   a/roll
               :ui/roll-on-enter          a/roll-on-enter
               :ui/roll-die               a/roll-die
               :ui/rolled                 a/rolled
               :ui/set-input              a/set-input
               :ui/set-list-input         a/set-list-input
               :ui/remove-list-input      a/remove-list-input
               :ui/list-drag-start        a/list-drag-start
               :ui/list-drag-end          a/list-drag-end
               :ui/list-drag-drop         a/list-drag-drop
               :ui/toggle-edit            a/toggle-edit
               :ui/edit-result            a/edit-result
               :ui/add-result-metadata    a/add-result-metadata
               :ui/remove-result-metadata a/remove-result-metadata
               :ui/add-result-item        a/add-result-item
               :ui/add-result-var         a/add-result-var
               :ui/remove-result-var      a/remove-result-var
               :ui/remove-result-item     a/remove-result-item
               :ui/dismiss-error          a/dismiss-error
               :ui/history-loaded         a/history-loaded
               :ui/history-save           a/history-save
               :ui/history-restore        a/history-restore
               :ui/history-delete         a/history-delete
               :ui/history-clear          a/history-clear
               :ui/history-hover          a/history-hover
               :ui/history-lock           a/history-lock
               :ui/manual-loaded          a/manual-loaded
               :ui/manual-edit            a/manual-edit
               :ui/manual-commit          a/manual-commit
               :ui/manual-remove          a/manual-remove
               :ui/manual-remove-item     a/manual-remove-item
               :ui/manual-set-key         a/manual-set-key
               :ui/manual-add             a/manual-add
               :ui/spies-preview          a/spies-preview
               :ui/export                 a/export}]
  (nxr/register-action! k f))
