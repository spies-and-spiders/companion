(ns sns.ui.render
  "The three generic, data-driven renderers. They emit stable class names; the
   stylesheet does the rest. A new loot type renders with zero UI code as long
   as its spec/view-model conform to the schema."
  (:require
    [clojure.string :as str]))

;; --- input-form (from a loot-spec's :inputs) ---------------------------------

(defn- enum-field
  "An enum renders as a combobox rather than a `<select>`: a text input backed by
   a `<datalist>`, so the browser's own picker filters as the DM types. Options
   come from config and can be long (every character, every damage type), where a
   select means scrolling. Free text is the trade — a value that isn't an option
   is flagged, not blocked, since only the plugin knows what it will accept.
   Clearing the field is how you get back to `—` (blank falls back to :default).
   `dom-id` must be unique on the page — a list field renders one per row."
  [dom-id value options action]
  (let [known? (contains? (into #{} (map str) options) (str value))]
    (list
      [:input.field__control
       {:type  "text"
        :list  dom-id
        :class (when-not (or (str/blank? (str value)) known?) "field__control--unknown")
        :value (str value)
        :on    {:input [action]}}]
      [:datalist {:id dom-id}
       (for [opt options]
         [:option {:value (str opt)}])])))

(defn- control
  "The bare input for one field value — shared between a scalar field and a
   single row of a list field. `on-change` is the action's fixed leading args
   (e.g. `[:ui/set-input id]`, or `[:ui/set-list-input id idx]` for a list row);
   the DOM value placeholder is appended to complete the dispatch."
  [dom-id value {:keys [type options]} on-change]
  (case type
    :enum (enum-field dom-id value options (conj on-change [:event.target/value]))
    :bool [:input.field__control
           {:type    "checkbox"
            :checked (boolean value)
            :on      {:change [(conj on-change [:event.target/checked])]}}]
    (:int :decimal)
    [:input.field__control
     {:type  "number"
      :step  (when (= :decimal type) "any") ; the default step of 1 rejects 1.3
      :value (str value)
      :on    {:input [(conj on-change [:event.target/value])]}}]
    [:input.field__control
     {:type  "text"
      :value (str value)
      :on    {:input [(conj on-change [:event.target/value])]}}]))

;; --- list (`:list?`) fields ---------------------------------------------------
;; A list field's value is a vector of entries. Non-boolean types get a
;; trailing blank row that, once filled in, reveals another — the list grows as
;; you type. A boolean has no "blank" state to fill in, so it grows via an
;; explicit Add button instead. Either way, dropping a value below its default
;; count still submits a vector.

(defn- list-row
  "One stored entry: its control, a remove button, and — for reorderable types —
   a drag handle plus drop target covering the whole row."
  [id idx value f draggable?]
  [:div.list-row
   {:replicant/key idx
    :on            (when draggable?
                     {:dragover [[:fx/prevent-default [:event/raw]]]
                      :drop     [[:fx/prevent-default [:event/raw]] [:ui/list-drag-drop id idx]]})}
   (when draggable?
     [:span.list-row__handle {:draggable "true" :on {:dragstart [[:ui/list-drag-start id idx]]}} "⠿"])
   (control (str "field-options-" (name id) "-" idx) value f [:ui/set-list-input id idx])
   [:button.list-row__remove
    {:type "button" :on {:click [[:ui/remove-list-input id idx]]}}
    "✕"]])

(defn- list-field [inputs {:keys [id type] :as f}]
  (let [values (vec (get inputs id))
        bool?  (= :bool type)]
    [:div.list-field
     (for [[idx v] (map-indexed vector values)]
       (list-row id idx v f (not bool?)))
     (if bool?
       [:button.list-field__add
        {:type "button" :on {:click [[:ui/set-list-input id (count values) false]]}}
        "+ Add"]
       ;; a fresh, unsaved row — typing into it appends rather than overwrites
       (when (or (empty? values) (not (str/blank? (str (peek values)))))
         [:div.list-row.list-row--extra {:replicant/key (count values)}
          (control (str "field-options-" (name id) "-" (count values)) nil f
                   [:ui/set-list-input id (count values)])]))]))

(defn- field [inputs {:keys [id label type list?] :as f}]
  (if list?
    [:div.field {:replicant/key id}
     [:span.field__label label]
     (list-field inputs f)]
    [:label.field {:replicant/key id
                   :class         (when (= :bool type) "field--bool")}
     [:span.field__label label]
     (control (str "field-options-" (name id)) (get inputs id) f [:ui/set-input id])]))

(defn input-form
  "Render a spec's declared inputs, or nil when there are none."
  [spec inputs]
  (when (seq (:inputs spec))
    ;; Enter anywhere in the form generates, matching the button.
    [:div.fields {:on {:keydown [[:ui/generate-on-enter [:event/key]]]}}
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

;; A randomised value's own control, separate from the body/title text it's
;; baked into — so editing it doesn't mean retyping the surrounding prose (and a
;; plugin round-tripping the edit later reads a value, not parsed prose). Reuses
;; `enum-field`/plain-text `control` from the input-form renderer above, so a
;; preset's `:options` become the same combobox a loot-spec enum input uses.
(defn- edit-var [path {:keys [id label value options]}]
  (let [dom-id (str "item-var-" (str/join "-" (map #(if (keyword? %) (name %) %) path)))]
    [:label.edit {:replicant/key (str path)}
     [:span.edit__label (or label (name id))]
     (control dom-id value
              {:type (if (seq options) :enum :text) :options options}
              [:ui/edit-result (conj path :value)])]))

(defn- edit-item [si ii {:item/keys [title body metadata vars]}]
  [:li.entry.entry--edit {:replicant/key ii}
   (edit-field "Item title" [:loot/sections si :section/items ii :item/title] title false)
   (edit-field "Body" [:loot/sections si :section/items ii :item/body] body true)
   (edit-metadata [:loot/sections si :section/items ii :item/metadata] metadata)
   (when (seq vars)
     [:div.entry__vars
      (map-indexed
        (fn [vi v] (edit-var [:loot/sections si :section/items ii :item/vars vi] v))
        vars)])])

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
     :step  (when (= "number" type) "any") ; bonuses may be fractional (e.g. 10.5)
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

;; A hidden type only ever appears as a transient row (see `picker`), so it is
;; styled to read as one: present for now, not part of the standing rail.
(defn- modifier [{:keys [hidden?]}]
  (when hidden? "discipline--transient"))

(defn- type-button [active? event glyph label modifier]
  [:button.discipline {:class [(when active? "discipline--active") modifier]
                       :on    {:click [event]}}
   [:span.discipline__glyph glyph]
   [:span.discipline__name label]])

(defn- type-list [class types selected glyph]
  [:ul.rail__list {:class class}
   (for [{:keys [id label] :as spec} types]
     [:li {:replicant/key id}
      (type-button (= id selected) [:ui/select-type id] glyph label (modifier spec))])])

(defn- matcher
  "A case-insensitive substring predicate over labels; matches everything when
   the query is blank."
  [type-filter]
  (let [q (str/lower-case (str/trim (str type-filter)))]
    (fn [label]
      (or (str/blank? q)
          (str/includes? (str/lower-case (str label)) q)))))

(defn picker [{:keys [loot-types selected roll-n page type-filter]}]
  (let [loot-selected (when (= :loot page) selected)
        match?        (matcher type-filter)
        ;; A hidden type is meant to be reached only by rolling the loot-table,
        ;; so it stays off the rail — except while it is the type on screen,
        ;; where it appears (in its config position) so the rail keeps showing
        ;; what the workbench holds.
        visible       (filterv #(and (or (not (:hidden? %)) (= loot-selected (:id %)))
                                     (match? (:label %)))
                               loot-types)
        utilities     (filterv :utility? visible)
        disciplines   (filterv (complement :utility?) visible)
        social?       (match? "Group Social")]
    [:nav.rail
     [:div.roll-group
      [:input.roll__input
       {:type        "number"
        :min         "1"
        :max         "100"
        :placeholder "d100"
        :value       (str roll-n)
        :on          {:input   [[:ui/set-roll-input [:event.target/value]]]
                      :keydown [[:ui/roll-on-enter [:event/key]]]}}]
      [:button.roll {:on {:click [[:ui/roll]]}}
       (if (str/blank? (str roll-n)) "Roll Loot" (str "Roll " roll-n))]]
     [:p.rail__hint "Enter 1–100 to roll on the table, or leave blank for random."]
     [:input.rail__search
      {:type        "search"
       :placeholder "Search plugins…"
       :value       (str type-filter)
       :on          {:input [[:ui/set-type-filter [:event.target/value]]]}}]
     [:p.rail__eyebrow "Loot Types"]
     (type-list "rail__list--loot" disciplines loot-selected "◆")
     [:p.rail__eyebrow.rail__eyebrow--utilities "Utilities"]
     [:ul.rail__list.rail__list--utils
      ;; the group tracker is part of the app, not a plugin — always present
      (when social?
        [:li {:replicant/key "__social"}
         (type-button (= :social page) [:ui/open-social] "✦" "Group Social" nil)])
      (for [{:keys [id label] :as spec} utilities]
        [:li {:replicant/key id}
         (type-button (= id loot-selected) [:ui/select-type id] "✦" label (modifier spec))])]]))
