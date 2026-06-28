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
  (let [spec (current-spec state)]
    [:div.app
     [:header.topbar
      [:div.brand [:span.brand__mark "✦"] [:span.brand__name "sns-companion"]]
      [:p.topbar__tag "loot at the table"]]
     [:div.stage
      (render/picker state)
      [:main.workbench
       (when (:error state)
         [:p.notice.notice--error (:error state)])
       (when spec
         [:section.summon
          [:p.summon__eyebrow (:label spec)]
          (render/input-form spec (:inputs state))
          [:button.generate {:on {:click [[:ui/generate]]}}
           (if (:loading? state) "Summoning…" (str "Generate " (:label spec)))]])
       (render/result (:result state))
       (when (and (nil? (:result state)) (nil? spec))
         [:div.empty
          [:p.empty__line "Choose a discipline, or roll the hoard."]])]]]))

(defn- render! [state]
  (r/render (js/document.getElementById "app") (view state)))

(defn init! []
  (r/set-dispatch! (fn [event-data actions] (nxr/dispatch state/store event-data actions)))
  (add-watch state/store ::render (fn [_ _ _ state] (render! state)))
  (nxr/dispatch state/store {} [[:fx/load-loot-types]])
  (render! @state/store))
