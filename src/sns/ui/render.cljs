(ns sns.ui.render
  "The three generic, data-driven renderers. They emit stable class names; the
   stylesheet does the rest. A new loot type renders with zero UI code as long
   as its spec/view-model conform to the schema."
  (:require
    [clojure.string :as str]))

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

;; --- result editor (manual tweaks before re-rendering / reporting) -----------
;; Edits write straight back into the result view-model at `path`, so the
;; read-only view and the reported payload always reflect the latest text.

(defn- edit-field [label path v area?]
  [:label.edit {:replicant/key (str path)}
   [:span.edit__label label]
   (if area?
     [:textarea.edit__control
      {:on {:input [[:ui/edit-result path [:event.target/value]]]}}
      (str v)]
     [:input.edit__control
      {:type  "text"
       :value (str v)
       :on    {:input [[:ui/edit-result path [:event.target/value]]]}}])])

(defn- edit-tags [path tags]
  [:label.edit {:replicant/key (str path)}
   [:span.edit__label "Tags (comma-separated)"]
   [:input.edit__control
    {:type  "text"
     :value (str/join ", " tags)
     :on    {:input [[:ui/edit-result-tags path [:event.target/value]]]}}]])

(defn- edit-item [si ii {:item/keys [title body tags]}]
  [:li.entry.entry--edit {:replicant/key ii}
   (edit-field "Item title" [:loot/sections si :section/items ii :item/title] title false)
   (edit-field "Body" [:loot/sections si :section/items ii :item/body] body true)
   (edit-tags [:loot/sections si :section/items ii :item/tags] tags)])

(defn- edit-block [si {:section/keys [heading items]}]
  [:section.block {:replicant/key si}
   (edit-field "Section heading" [:loot/sections si :section/heading] heading false)
   [:ul.entries (map-indexed (fn [ii item] (edit-item si ii item)) items)]])

(defn result-editor
  "Render the result view-model as an editable form. Behavioural `:loot/actions`
   are intentionally not editable (and preserved untouched in state)."
  [vm]
  (when vm
    [:article.sigil.sigil--edit {:replicant/key "result-editor"}
     [:div.sigil__frame
      (edit-field "Subtitle" [:loot/subtitle] (:loot/subtitle vm) false)
      (edit-field "Title" [:loot/title] (:loot/title vm) false)
      [:div.sigil__body
       (map-indexed edit-block (:loot/sections vm))]]]))

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
