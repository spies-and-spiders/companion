(ns sns.ui.render
  "The three generic, data-driven renderers. They emit stable class names; the
   stylesheet does the rest. A new loot type renders with zero UI code as long
   as its spec/view-model conform to the schema.")

;; --- input-form (from a loot-spec's :inputs) ---------------------------------

(defn- field [inputs {:keys [id label type options]}]
  (let [value (get inputs id)]
    [:label.field {:replicant/key id}
     [:span.field__label label]
     (case type
       :enum [:select.field__control
              {:on {:change [[:ui/set-input id [:event.target/value]]]}}
              [:option {:value ""} "—"]
              (for [opt options]
                [:option {:value (str opt) :selected (= (str opt) (str value))}
                 (str opt)])]
       :bool [:input.field__control
              {:type    "checkbox"
               :checked (boolean value)
               :on      {:change [[:ui/set-input id [:event.target/checked]]]}}]
       :int  [:input.field__control
              {:type  "number"
               :value (str value)
               :on    {:input [[:ui/set-input id [:event.target/value]]]}}]
       [:input.field__control
        {:type  "text"
         :value (str value)
         :on    {:input [[:ui/set-input id [:event.target/value]]]}}])]))

(defn input-form
  "Render a spec's declared inputs, or nil when there are none."
  [spec inputs]
  (when (seq (:inputs spec))
    [:div.fields
     (for [f (:inputs spec)]
       (field inputs f))]))

;; --- view-model renderer (the signature surface) -----------------------------

(defn- entry [{:item/keys [title body tags]}]
  [:li.entry
   (when title [:h4.entry__title title])
   [:p.entry__body body]
   (when (seq tags)
     [:ul.tags (for [t tags] [:li.tag t])])])

(defn- block [{:section/keys [heading items]}]
  [:section.block
   (when heading [:h3.block__heading heading])
   [:ul.entries (map entry items)]])

(defn- action [{:action/keys [label event]}]
  [:button.action {:on {:click [event]}} label])

(defn result
  "Render a view-model. The whole article is keyed on the title so a fresh loot
   result re-mounts and replays the materialise animation."
  [vm]
  (when vm
    [:article.sigil {:replicant/key (:loot/title vm)}
     [:div.sigil__frame
      (when (:loot/subtitle vm)
        [:p.sigil__eyebrow (:loot/subtitle vm)])
      [:h2.sigil__title (:loot/title vm)]
      [:div.sigil__body
       (map block (:loot/sections vm))]
      (when (seq (:loot/actions vm))
        [:div.sigil__actions
         (map action (:loot/actions vm))])]]))

;; --- loot-type picker --------------------------------------------------------

(defn picker [{:keys [loot-types selected]}]
  [:nav.rail
   [:p.rail__eyebrow "Disciplines"]
   [:ul.rail__list
    (for [{:keys [id label]} loot-types]
      [:li {:replicant/key id}
       [:button.discipline {:class (when (= id selected) "discipline--active")
                            :on    {:click [[:ui/select-type id]]}}
        [:span.discipline__glyph "◆"]
        [:span.discipline__name label]]])]
   [:button.roll {:on {:click [[:ui/roll]]}}
    "Roll the hoard"]])
