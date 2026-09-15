(ns sns.ui.app
  "Composition root: wires nexus dispatch into replicant, renders on state
   change, and loads the loot-type specs on init."
  (:require
    [nexus.registry :as nxr]
    [replicant.dom :as r]
    [sns.ui.nexus]
    [sns.ui.render :as render]
    [sns.ui.state :as state]))

(defn- card
  "Everything for one loot type: its manual table, form, result and history."
  [state id]
  (when-let [spec (state/spec state id)]
    (let [result       (get-in state [:results id])
          editing?     (get-in state [:editing id])
          history-mode (or (:history spec) (:history-mode state))
          rows         (get (:history state) (name id))
          ;; What is on the bench but in no history entry: a hand-edit, or a
          ;; result generated under a mode that does not store on its own.
          unsaved?     (and result (not-any? #(= result (:view-model %)) rows))]
      [:div.card {:replicant/key id}
       (render/manual-editor spec (get-in state [:manual id]) (get-in state [:manual-key id]))
       [:section.summon
        [:p.summon__eyebrow (:label spec)]
        (render/input-form spec (get-in state [:inputs id]))
        [:button.generate {:on {:click [[:ui/generate id]]}}
         (if (get-in state [:loading id])
           "Summoning…"
           (or (:generate-label spec) (str "Generate " (:label spec))))]]
       (if editing?
         (render/result-editor id result)
         (render/result result))
       (when result
         [:div.result-actions
          [:button.action-btn
           {:on {:click [[:ui/toggle-edit id]]}}
           (if editing? "Done editing" "Edit item")]
          (when (or (= :button history-mode)
                    (and (= :always history-mode) unsaved?))
            [:button.action-btn {:on {:click [[:ui/history-save id]]}} "Save to history"])
          (when (:report? state)
            [:button.report__btn
             {:disabled (= :sending (get-in state [:report-status id]))
              :on       {:click [[:ui/report id]]}}
             (case (get-in state [:report-status id])
               :sending "Sending…"
               :sent    "Sent ✓"
               (or (:report-label state) "Send"))])])
       (render/history id rows (state/previewed state id) (= id (first (:history-lock state))))])))

(defn- view [state]
  (let [ids (state/page-tools state (:page state))]
    [:div.app
     [:header.topbar
      [:div.brand [:span.brand__mark "✦"] [:span.brand__name "sns-companion"]]]
     [:div.stage
      (render/picker state)
      [:main.workbench
       (when (:error state)
         [:p.notice.notice--error (:error state)])
       (if (seq ids)
         [:div.cards (for [id ids] (card state id))]
         [:div.empty
          [:p.empty__line "Choose a page, or make a loot roll."]])]]]))

(defn- render! [state]
  (r/render (js/document.getElementById "app") (view state)))

(defn init! []
  (r/set-dispatch! (fn [event-data actions] (nxr/dispatch state/store event-data actions)))
  (add-watch state/store ::render (fn [_ _ _ state] (render! state)))
  (let [lock! #(nxr/dispatch state/store {} [[:ui/history-lock %]])]
    (js/addEventListener "keydown" #(when (= "Alt" (.-key %)) (lock! true)))
    (js/addEventListener "keyup" #(when (and (= "Alt" (.-key %))
                                             (not (js/document.querySelector ".history__preview:hover")))
                                    (lock! false)))
    (js/addEventListener "blur" #(lock! false)))
  (nxr/dispatch state/store {} [[:fx/load-loot-types] [:fx/load-capabilities]])
  (render! @state/store))
