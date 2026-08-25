(ns sns.ui.render
  "The three generic, data-driven renderers. They emit stable class names; the
   stylesheet does the rest. A new loot type renders with zero UI code as long
   as its spec/view-model conform to the schema."
  (:require
    [clojure.string :as str]
    [sns.ui.template :as template]))

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

;; `:item/title`/`:item/body` are templates; the values they interpolate travel
;; beside them as `:item/vars`, so a var edited in the browser re-renders here
;; with no round trip to the server. `:loot/vars` are ambient — every template
;; in the view-model can read them, which is how one drawn value is *shared*
;; between items rather than copied into each (copies desynchronise the moment
;; the DM edits one). An item's own vars shadow them.
(defn- entry [loot-vars {:item/keys [title body metadata vars]}]
  (let [vars (merge loot-vars vars)]
    [:li.entry
     (when title [:h4.entry__title (template/render title vars)])
     [:p.entry__body (template/render body vars)]
     (when (seq metadata)
       [:ul.tags (for [t metadata] [:li.tag t])])]))

(defn- block [loot-vars {:section/keys [heading items]}]
  [:section.block
   (when heading [:h3.block__heading heading])
   [:ul.entries (map (partial entry loot-vars) items)]])

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
        [:p.sigil__eyebrow (template/render (:loot/subtitle vm) (:loot/vars vm))])
      [:h2.sigil__title (template/render (:loot/title vm) (:loot/vars vm))]
      [:div.sigil__body
       (map (partial block (:loot/vars vm)) (:loot/sections vm))]
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
      {:on {:input [[:ui/edit-result path :text [:event.target/value]]]}}
      (str v)]
     [:input.edit__control
      {:type  "text"
       :value (str v)
       :on    {:input [[:ui/edit-result path :text [:event.target/value]]]}}])])

(defn- edit-metadata [path metadata]
  [:label.edit {:replicant/key (str path)}
   [:span.edit__label "Metadata (comma-separated)"]
   [:input.edit__control
    {:type  "text"
     :value (str/join ", " metadata)
     :on    {:input [[:ui/edit-result-metadata path [:event.target/value]]]}}]])

;; A var's own control, separate from the template that interpolates it — so
;; changing a value doesn't mean retyping the prose, and a plugin reads a value
;; back rather than parsing text. Reuses `enum-field`/plain-text `control` from
;; the input-form renderer above, so a preset's `:options` become the same
;; combobox a loot-spec enum input uses.
(defn- var-label
  "A var's field label. Derived here rather than sent: the id is already on the
   wire as the key, so a server-computed label would be a second copy of it —
   and how a name is *displayed* is this layer's business anyway. A plugin that
   wants something other than the id sets `:label`."
  [id label]
  (or label (-> (name id) (str/replace #"[-_]" " ") str/capitalize)))

(defn- var-type
  "The control a var edits with, driven by the declared `:type` — which holds
   still while the field is blank mid-edit. Numbers all edit as `:decimal`: a
   var holding 2 may want 2.5 typed into it, and `:int`'s step of 1 rejects
   that."
  [type options]
  (cond
    (seq options)           :enum
    (= :bool type)          :bool
    (#{:int :decimal} type) :decimal
    :else                   :text))

(defn- edit-var [path id {:keys [label value options type]}]
  (let [dom-id (str "item-var-" (str/join "-" (map #(if (keyword? %) (name %) %) path)))]
    [:label.edit {:replicant/key (str path)}
     [:span.edit__label (var-label id label)]
     (control dom-id value
              {:type (var-type type options) :options options}
              [:ui/edit-result (conj path :value) type])]))

(defn- editable-vars
  "The vars a DM may change: what the plugin *declared*, not the entry fields
   its templates happen to read (`:context?`)."
  [vars]
  (remove (comp :context? val) vars))

(defn- var-grid
  "The editable vars under `base-path`, several to a row. Values are what a DM
   changes mid-session, so they lead — the prose that interpolates them is the
   rarer edit and sits behind a disclosure."
  [class base-path vars]
  (when-let [editable (seq (editable-vars vars))]
    [:div {:class class}
     (for [[id v] editable]
       (edit-var (conj base-path id) id v))]))

;; The body field holds the template itself — `{{ x }}` where a value sits — so
;; the sentence and the values are edited independently and neither forces
;; retyping the other. What the DM types is what the plugin gets back.
(defn- edit-text
  "An item's prose, folded away behind a summary that previews it as rendered —
   which is what a DM reads to find the item, and the edit they seldom want."
  [si ii {:item/keys [title body metadata]} preview]
  [:details.fold {:replicant/key (str "text-" si "-" ii)}
   [:summary.fold__summary
    [:span.fold__preview preview]
    [:span.fold__hint "text"]]
   [:div.fold__body
    (edit-field "Item title" [:loot/sections si :section/items ii :item/title] title false)
    (edit-field "Body" [:loot/sections si :section/items ii :item/body] body true)
    (edit-metadata [:loot/sections si :section/items ii :item/metadata] metadata)]])

(defn- edit-item [loot-vars si ii {:item/keys [title body vars] :as item}]
  (let [all     (merge loot-vars vars)
        rendered #(some-> % (template/render all) str str/trim not-empty)
        preview (str (some-> (rendered title) (str ": ")) (rendered body))]
    [:li.entry.entry--edit {:replicant/key ii}
     (var-grid "entry__vars" [:loot/sections si :section/items ii :item/vars] vars)
     (edit-text si ii item preview)]))

(defn- edit-block [loot-vars si {:section/keys [heading items]}]
  [:section.block.block--edit {:replicant/key si}
   ;; Styled as the heading it is, so the sections stay legible as structure
   ;; while still editing in place.
   [:input.block__heading.block__heading--edit
    {:type  "text"
     :value (str heading)
     :on    {:input [[:ui/edit-result [:loot/sections si :section/heading] :text
                      [:event.target/value]]]}}]
   [:ul.entries (map-indexed (fn [ii item] (edit-item loot-vars si ii item)) items)]])

(defn result-editor
  "Render the result view-model as an editable form. Behavioural `:loot/actions`
   are intentionally not editable (and preserved untouched in state)."
  [vm]
  (when vm
    (let [loot-vars (:loot/vars vm)]
      [:article.sigil.sigil--edit {:replicant/key "result-editor"}
       [:div.sigil__frame
        ;; The rendered title, so the editor says which item is on the bench.
        [:h2.sigil__title (template/render (:loot/title vm) loot-vars)]
        [:details.fold {:replicant/key "text-loot"}
         [:summary.fold__summary
          ;; the *template*, since the heading above already shows it rendered
          [:span.fold__preview (:loot/title vm)]
          [:span.fold__hint "title & subtitle"]]
         [:div.fold__body
          (edit-field "Title" [:loot/title] (:loot/title vm) false)
          (edit-field "Subtitle" [:loot/subtitle] (:loot/subtitle vm) false)]]
        ;; Shared values, edited once: these are ambient to every template in the
        ;; view-model, so changing one here updates every item that reads it.
        (when-let [grid (var-grid "entry__vars entry__vars--shared" [:loot/vars] loot-vars)]
          (list [:h3.block__heading "Shared values"] grid))
        [:div.sigil__body
         (map-indexed (partial edit-block loot-vars) (:loot/sections vm))]]])))

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
