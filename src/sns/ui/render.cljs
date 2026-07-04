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

(defn- entry [{:item/keys [title body metadata]}]
  [:li.entry
   (when title [:h4.entry__title title])
   [:p.entry__body body]
   (when (seq metadata)
     [:ul.tags (for [t metadata] [:li.tag t])])])

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

(defn- edit-metadata [path metadata]
  [:label.edit {:replicant/key (str path)}
   [:span.edit__label "Metadata (comma-separated)"]
   [:input.edit__control
    {:type  "text"
     :value (str/join ", " metadata)
     :on    {:input [[:ui/edit-result-metadata path [:event.target/value]]]}}]])

(defn- edit-item [si ii {:item/keys [title body metadata]}]
  [:li.entry.entry--edit {:replicant/key ii}
   (edit-field "Item title" [:loot/sections si :section/items ii :item/title] title false)
   (edit-field "Body" [:loot/sections si :section/items ii :item/body] body true)
   (edit-metadata [:loot/sections si :section/items ii :item/metadata] metadata)])

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

;; --- the Group Deception & Persuasion tracker (always-on, bespoke) -----------

(defn- bonus-str [n]
  (if (neg? n) (str n) (str "+" n)))

(defn- social-field [social-form field label type]
  [:label.field {:replicant/key field}
   [:span.field__label label]
   [:input.field__control
    {:type  type
     :value (str (get social-form field))
     :on    {:input [[:ui/set-social-input field [:event.target/value]]]}}]])

(defn- social-row [{char-name :name :keys [deception persuasion present?]}]
  [:li.social__row {:replicant/key char-name
                    :class         (when-not present? "social__row--absent")}
   [:label.social__tick
    [:input {:type    "checkbox"
             :checked (boolean present?)
             :on      {:change [[:ui/social-toggle char-name]]}}]]
   ;; clicking the details loads them into the form for editing
   [:button.social__details
    {:title "Edit this character"
     :on    {:click [[:ui/social-edit char-name deception persuasion]]}}
    [:span.social__name char-name]
    [:span.social__bonuses
     (str "Deception " (bonus-str deception) " · Persuasion " (bonus-str persuasion))]]
   [:button.social__remove {:on {:click [[:ui/social-remove char-name]]}} "Remove"]])

(defn social-page
  "The always-present group tracker: an add/update form, a row per character
   (tick beside their details), the two group bonuses, and the roll buttons."
  [{:keys [social social-form]}]
  (let [{:keys [characters deception persuasion roll]} social
        present-n (count (filter :present? characters))]
    [:section.social
     [:p.summon__eyebrow "Group Deception & Persuasion"]
     [:div.fields
      (social-field social-form :name "Character name" "text")
      (social-field social-form :deception "Deception bonus" "number")
      (social-field social-form :persuasion "Persuasion bonus" "number")]
     [:button.generate {:on {:click [[:ui/social-add]]}} "Add / update character"]
     (if (seq characters)
       [:ul.social__roster (map social-row characters)]
       [:p.social__empty "No characters yet — add each party member above. Untick anyone who misses a session."])
     [:div.social__summary
      [:span.social__bonus (str "Group Deception " (bonus-str deception))]
      [:span.social__bonus (str "Group Persuasion " (bonus-str persuasion))]
      (when (seq characters)
        [:span.social__present (str present-n "/" (count characters) " present")])]
     [:div.social__rolls
      [:button.action {:on {:click [[:ui/social-roll :deception]]}}
       (str "Roll Deception (1d20" (bonus-str deception) ")")]
      [:button.action {:on {:click [[:ui/social-roll :persuasion]]}}
       (str "Roll Persuasion (1d20" (bonus-str persuasion) ")")]]
     (when roll
       [:p.social__result {:replicant/key (str roll)}
        (str (if (= :deception (:skill roll)) "Deception" "Persuasion")
             " check: " (:total roll)
             " — rolled " (:die roll) " " (bonus-str (:bonus roll))
             (case (:die roll) 1 " · natural 1!" 20 " · natural 20!" ""))])]))

;; --- loot-type picker --------------------------------------------------------

(defn- type-button [active? event glyph label]
  [:button.discipline {:class (when active? "discipline--active")
                       :on    {:click [event]}}
   [:span.discipline__glyph glyph]
   [:span.discipline__name label]])

(defn- type-list [types selected glyph class]
  [:ul.rail__list
   {:class class}
   (for [{:keys [id label]} types]
     [:li {:replicant/key id}
      (type-button (= id selected) [:ui/select-type id] glyph label)])])

(defn picker [{:keys [loot-types selected roll-n page]}]
  (let [utilities     (filterv :utility? loot-types)
        disciplines   (filterv (complement :utility?) loot-types)
        loot-selected (when (= :loot page) selected)]
    [:nav.rail
     [:div.roll-group
      [:input.roll__input
       {:type        "number"
        :min         "1"
        :max         "100"
        :placeholder "d100"
        :value       (str roll-n)
        :on          {:input [[:ui/set-roll-input [:event.target/value]]]}}]
      [:button.roll {:on {:click [[:ui/roll]]}}
       (if (str/blank? (str roll-n)) "Roll Loot" (str "Roll " roll-n))]]
     [:p.rail__hint "Enter 1–100 to roll on the table, or leave blank for random."]
     [:p.rail__eyebrow "Disciplines"]
     (type-list disciplines loot-selected "◆" nil)
     [:p.rail__eyebrow.rail__eyebrow--utilities "Utilities"]
     ;; sized to content so the (scrollable) disciplines list keeps the room
     [:ul.rail__list.rail__list--utilities
      ;; the group tracker is part of the app, not a plugin — always present
      [:li {:replicant/key "__social"}
       (type-button (= :social page) [:ui/open-social] "✦" "Group Social")]
      (for [{:keys [id label]} utilities]
        [:li {:replicant/key id}
         (type-button (= id loot-selected) [:ui/select-type id] "✦" label)])]]))
