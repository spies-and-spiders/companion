(ns sns.ui.nexus
  "Nexus registry: pure actions return effects; effects perform side-effects
   (state writes, HTTP). Requiring this namespace registers everything."
  (:require
    [clojure.string :as str]
    [nexus.registry :as nxr]
    [sns.ui.api :as api]))

(nxr/register-system->state! deref)

;; --- placeholders (resolve DOM values at dispatch time) ----------------------

(nxr/register-placeholder! :event.target/value
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-target .-value)))

(nxr/register-placeholder! :event.target/checked
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-target .-checked)))

;; --- effects (impure) --------------------------------------------------------

(nxr/register-effect! :fx/assoc-in
                      (fn [_ctx system path v]
                        (swap! system assoc-in path v)))

(nxr/register-effect! :fx/load-loot-types
                      (fn [{:keys [dispatch]} _system]
                        (api/request {:url "/api/loot-types"}
                                     (fn [types] (dispatch [[:fx/assoc-in [:loot-types] types]]))
                                     (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]])))))

(nxr/register-effect! :fx/load-capabilities
                      (fn [{:keys [dispatch]} _system]
                        (api/request {:url "/api/capabilities"}
                                     (fn [{:keys [report? report-label]}]
                                       (dispatch [[:fx/assoc-in [:report?] (boolean report?)]
                                                  [:fx/assoc-in [:report-label] report-label]]))
                                     (fn [_err] nil))))

(defn- result-effect [{:keys [dispatch]} req]
  (dispatch [[:fx/assoc-in [:loading?] true] [:fx/assoc-in [:error] nil]
             [:fx/assoc-in [:report-status] nil]])
  (api/request req
               (fn [vm] (dispatch [[:fx/assoc-in [:result] vm]
                                   [:fx/assoc-in [:editing?] false]
                                   [:fx/assoc-in [:loading?] false]]))
               (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]
                                    [:fx/assoc-in [:loading?] false]]))))

(nxr/register-effect! :fx/report
                      (fn [{:keys [dispatch]} system]
                        (when-let [vm (:result @system)]
                          (dispatch [[:fx/assoc-in [:report-status] :sending]
                                     [:fx/assoc-in [:error] nil]])
                          (api/request {:method :post :url "/api/report" :body {:view-model vm}}
                                       (fn [_ok] (dispatch [[:fx/assoc-in [:report-status] :sent]]))
                                       (fn [err] (dispatch [[:fx/assoc-in [:report-status] nil]
                                                            [:fx/assoc-in [:error] (:error err)]]))))))

(nxr/register-effect! :fx/generate
                      (fn [ctx _system id inputs]
                        (result-effect ctx {:method :post :url "/api/generate" :body {:id id :inputs inputs}})))

(nxr/register-effect! :fx/roll
                      (fn [{:keys [dispatch]} _system inputs n]
                        (dispatch [[:fx/assoc-in [:loading?] true] [:fx/assoc-in [:error] nil]
                                   [:fx/assoc-in [:report-status] nil]])
                        (api/request {:method :post
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
                      (fn [ctx _system id action params]
                        (result-effect ctx {:method :post :url "/api/action" :body {:id id :action action :params params}})))

;; --- actions (pure: state -> effects) ----------------------------------------

(nxr/register-action! :ui/select-type
                      (fn [_state id]
                        [[:fx/assoc-in [:selected] id]
                         [:fx/assoc-in [:inputs] {}]
                         [:fx/assoc-in [:result] nil]
                         [:fx/assoc-in [:editing?] false]]))

(nxr/register-action! :ui/set-input
                      (fn [_state field value]
                        [[:fx/assoc-in [:inputs field] value]]))

(nxr/register-action! :ui/generate
                      (fn [state]
                        [[:fx/generate (:selected state) (:inputs state)]]))

(nxr/register-action! :ui/set-roll-input
                      (fn [_state value]
                        [[:fx/assoc-in [:roll-n] value]]))

(nxr/register-action! :ui/roll
                      (fn [{:keys [inputs roll-n]}]
                        ;; A blank field rolls randomly; a number rolls that d100
                        ;; result against the table's allocation (validated server-side).
                        (let [n (when-not (str/blank? roll-n)
                                  (let [parsed (js/parseInt roll-n 10)]
                                    (when-not (js/isNaN parsed) parsed)))]
                          [[:fx/roll inputs n]])))

(nxr/register-action! :ui/report
                      (fn [_state]
                        [[:fx/report]]))

(nxr/register-action! :ui/toggle-edit
                      (fn [state]
                        ;; clear any stale "Sent ✓" so an edited item reads as unsent
                        [[:fx/assoc-in [:editing?] (not (:editing? state))]
                         [:fx/assoc-in [:report-status] nil]]))

(nxr/register-action! :ui/edit-result
                      (fn [_state path value]
                        [[:fx/assoc-in (into [:result] path) value]
                         [:fx/assoc-in [:report-status] nil]]))

(nxr/register-action! :ui/edit-result-tags
                      (fn [_state path value]
                        [[:fx/assoc-in (into [:result] path)
                          (->> (str/split (or value "") #",")
                               (map str/trim)
                               (remove str/blank?)
                               vec)]
                         [:fx/assoc-in [:report-status] nil]]))

;; Dispatched directly from a view-model's :action/event vector.
(nxr/register-action! :loot/action
                      (fn [_state {:keys [id action params]}]
                        [[:fx/action id action params]]))
