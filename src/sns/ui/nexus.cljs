(ns sns.ui.nexus
  "Nexus registry: pure actions return effects; effects perform side-effects
   (state writes, HTTP). Requiring this namespace registers everything."
  (:require
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

(defn- result-effect [{:keys [dispatch]} req]
  (dispatch [[:fx/assoc-in [:loading?] true] [:fx/assoc-in [:error] nil]])
  (api/request req
               (fn [vm] (dispatch [[:fx/assoc-in [:result] vm]
                                   [:fx/assoc-in [:loading?] false]]))
               (fn [err] (dispatch [[:fx/assoc-in [:error] (:error err)]
                                    [:fx/assoc-in [:loading?] false]]))))

(nxr/register-effect! :fx/generate
                      (fn [ctx _system id inputs]
                        (result-effect ctx {:method :post :url "/api/generate" :body {:id id :inputs inputs}})))

(nxr/register-effect! :fx/roll
                      (fn [{:keys [dispatch]} _system inputs]
                        (dispatch [[:fx/assoc-in [:loading?] true] [:fx/assoc-in [:error] nil]])
                        (api/request {:method :post :url "/api/roll" :body {:inputs inputs}}
                                     ;; roll returns {:id ... :view-model ...} so we can
                                     ;; jump the picker to the discipline that was rolled.
                                     (fn [{:keys [id view-model]}]
                                       (dispatch [[:fx/assoc-in [:selected] id]
                                                  [:fx/assoc-in [:inputs] {}]
                                                  [:fx/assoc-in [:result] view-model]
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
                         [:fx/assoc-in [:result] nil]]))

(nxr/register-action! :ui/set-input
                      (fn [_state field value]
                        [[:fx/assoc-in [:inputs field] value]]))

(nxr/register-action! :ui/generate
                      (fn [state]
                        [[:fx/generate (:selected state) (:inputs state)]]))

(nxr/register-action! :ui/roll
                      (fn [state]
                        [[:fx/roll (:inputs state)]]))

;; Dispatched directly from a view-model's :action/event vector.
(nxr/register-action! :loot/action
                      (fn [_state {:keys [id action params]}]
                        [[:fx/action id action params]]))
