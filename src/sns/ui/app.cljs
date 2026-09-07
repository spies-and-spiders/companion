(ns sns.ui.app
  "Composition root: wires nexus dispatch into replicant, renders on state
   change, and loads the loot-type specs on init."
  (:require
    [nexus.registry :as nxr]
    [replicant.dom :as r]
    [sns.ui.nexus]
    [sns.ui.render :as render]
    [sns.ui.state :as state]))

(defn- current-spec [{:keys [loot-types selected]}]
  (some #(when (= selected (:id %)) %) loot-types))

(defn- view [state]
  (let [spec         (current-spec state)
        history-mode (or (:history spec) (:history-mode state))
        rows         (get (:history state) (some-> (:selected state) name))
        ;; What is on the bench but in no history entry: a hand-edit, or a
        ;; result generated under a mode that does not store on its own.
        unsaved?     (and (:result state)
                          (not-any? #(= (:result state) (:view-model %)) rows))]
    [:div.app
     [:header.topbar
      [:div.brand [:span.brand__mark "✦"] [:span.brand__name "sns-companion"]]]
     [:div.stage
      (render/picker state)
      [:main.workbench
       (when (:error state)
         [:p.notice.notice--error (:error state)])
       (render/manual-editor spec (:manual state) (:manual-key state))
       (when spec
         [:section.summon
          [:p.summon__eyebrow (:label spec)]
          (render/input-form spec (:inputs state))
          [:button.generate {:on {:click [[:ui/generate]]}}
           (if (:loading? state)
             "Summoning…"
             (or (:generate-label spec) (str "Generate " (:label spec))))]])
       (if (:editing? state)
         (render/result-editor (:result state))
         (render/result (:result state)))
       (when (:result state)
         [:div.result-actions
          [:button.action-btn
           {:on {:click [[:ui/toggle-edit]]}}
           (if (:editing? state) "Done editing" "Edit item")]
          (when (or (= :button history-mode)
                    (and (= :always history-mode) unsaved?))
            [:button.action-btn {:on {:click [[:ui/history-save]]}} "Save to history"])
          (when (:report? state)
            [:button.report__btn
             {:disabled (= :sending (:report-status state))
              :on       {:click [[:ui/report]]}}
             (case (:report-status state)
               :sending "Sending…"
               :sent    "Sent ✓"
               (or (:report-label state) "Send"))])])
       (render/history (:selected state) rows (:history-hover state))
       (when (and (nil? (:result state)) (nil? spec))
         [:div.empty
          [:p.empty__line "Choose a loot type, or make a loot roll."]])]]]))

(defn- render! [state]
  (r/render (js/document.getElementById "app") (view state)))

(defn init! []
  (r/set-dispatch! (fn [event-data actions] (nxr/dispatch state/store event-data actions)))
  (add-watch state/store ::render (fn [_ _ _ state] (render! state)))
  (nxr/dispatch state/store {} [[:fx/load-loot-types] [:fx/load-capabilities]])
  (render! @state/store))
