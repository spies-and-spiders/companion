(ns sns.ui.app
  "Composition root: wires nexus dispatch into replicant, renders on state
   change, follows the URL hash, and boots from the backend on init."
  (:require
    [nexus.registry :as nxr]
    [replicant.dom :as r]
    [sns.ui.actions :as actions]
    [sns.ui.nexus]
    [sns.ui.render :as render]
    [sns.ui.state :as state]))

(defn- card
  "Everything for one loot type: its manual table, form, result and history."
  [state id]
  (when-let [spec (state/spec state id)]
    (let [{:keys [result editing? loading? report-status saved? error drafts inputs manual manual-key]}
          (get-in state [:tools id])
          history-mode (actions/history-mode state id)]
      [:div.card {:replicant/key id}
       (render/manual-editor spec manual manual-key)
       [:section.summon
        [:p.summon__eyebrow (:label spec)]
        (render/input-form spec inputs)
        [:button.generate {:on {:click [[:ui/generate id]]}}
         (if loading?
           "Summoning…"
           (or (:generate-label spec) (str "Generate " (:label spec))))]]
       (if editing?
         (render/result-editor id result drafts)
         (render/result result (:spies state)))
       (render/error-overlay id error)
       (when result
         [:div.result-actions
          [:button.action-btn
           {:on {:click [[:ui/toggle-edit id]]}}
           (if editing? "Done editing" "Edit item")]
          ;; under :always, what is on the bench but in no history entry: a
          ;; hand-edit, or a result an unchanged action left as it was
          (when (or (= :button history-mode)
                    (and (= :always history-mode) (not saved?)))
            [:button.action-btn {:on {:click [[:ui/history-save id]]}} "Save to history"])
          (when (:report? state)
            [:button.report__btn
             {:disabled (= :sending report-status)
              :on       {:click [[:ui/report id]]}}
             (case report-status
               :sending "Sending…"
               :sent    "Sent ✓"
               (or (:report-label state) "Send"))])])
       (render/history id
                       (get (:history state) (name id))
                       (state/previewed state id)
                       (= id (first (:history-lock state))))])))

(defn- view [state]
  (let [ids (state/page-tools state (:page state))]
    [:div.app
     [:header.topbar
      [:div.brand [:img.brand__mark {:src "/favicon.svg" :alt ""}] [:span.brand__name "S&S Companion"]]]
     [:div.stage
      (render/picker state)
      [:main.workbench
       (when (:error state)
         [:p.notice.notice--error (:error state)])
       (cond
         (= state/loot-table-page (:page state)) (render/loot-table state)
         (seq ids) [:div.cards (for [id ids] (card state id))]
         :else [:div.empty
                [:p.empty__line "Choose a page, or make a loot roll."]])]]]))

(defn- render! [state]
  (r/render (js/document.getElementById "app") (view state)))

(defn- dispatch! [actions]
  (nxr/dispatch state/store {} actions))

(defn init! []
  (r/set-dispatch! (fn [event-data actions] (nxr/dispatch state/store event-data actions)))
  (add-watch state/store ::render (fn [_ _ _ state] (render! state)))
  (let [lock! #(dispatch! [[:ui/history-lock %]])]
    (js/addEventListener "keydown" #(when (= "Alt" (.-key %)) (lock! true)))
    (js/addEventListener "keyup" #(when (and (= "Alt" (.-key %))
                                             (not (js/document.querySelector ".history__preview:hover")))
                                    (lock! false)))
    (js/addEventListener "blur" #(lock! false)))
  (js/addEventListener "popstate" #(dispatch! [[:ui/route (state/hash->page js/location.hash)]]))
  (swap! state/store assoc :page (state/hash->page js/location.hash))
  (dispatch! [[:fx/boot]])
  (render! @state/store))
