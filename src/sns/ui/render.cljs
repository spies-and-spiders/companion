(ns sns.ui.render
  "Generic, data-driven views emitting stable class names for the stylesheet, so
   a loot type needs no UI code of its own."
  (:require
    [clojure.string :as str]
    [sns.sdk.rank :as rank]
    [sns.ui.actions :as actions]
    [sns.ui.link :as link]
    [sns.ui.spies :as spies]
    [sns.ui.state :as state]
    [sns.ui.template :as template]))

;; --- input-form (from a loot-spec's :inputs) ---------------------------------

(defn- enum-field
  "A combobox, so long option lists filter as the DM types. Unknown values are
   flagged rather than blocked, since only the plugin knows what it accepts.
   `dom-id` must be unique on the page."
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

(defn- position-select
  "A `:section`/`:item` picker over `positions`, grouping items by section."
  [value type positions on-change]
  (let [option (fn [{v :value :keys [label]}] [:option {:value v :selected (= v (str value))} label])]
    [:select.field__control {:on {:change [(conj on-change [:event.target/value])]}}
     [:option {:value "" :selected (str/blank? (str value))} "—"]
     (if (= :section type)
       (map option positions)
       (for [{:keys [label items]} positions]
         [:optgroup {:label label} (map option items)]))]))

(defn- control
  "The input for one field value. `on-change` is an action, to which the DOM
   value is appended."
  [dom-id value {:keys [type options positions]} on-change]
  (case type
    (:section :item) (position-select value type positions on-change)
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
;; Lists grow as a trailing blank row is filled in, or for booleans, which have
;; no blank state, via an Add button.

(defn- dom-id [& parts]
  (str/join "-" (map #(if (keyword? %) (name %) %) (flatten parts))))

(defn- list-row
  "An entry's control and remove button, plus drag-and-drop when `draggable?`."
  [path id idx value f draggable?]
  [:div.list-row
   {:replicant/key idx
    :on            (when draggable?
                     {:dragover [[:fx/prevent-default [:event/raw]]]
                      :drop     [[:fx/prevent-default [:event/raw]] [:ui/list-drag-drop path id idx]]})}
   (when draggable?
     [:span.list-row__handle {:draggable "true" :on {:dragstart [[:ui/list-drag-start path id idx]]}} "⠿"])
   (control (dom-id "field-options" path id idx) value f [:ui/set-list-input path id idx])
   [:button.list-row__remove
    {:type "button" :on {:click [[:ui/remove-list-input path id idx]]}}
    "✕"]])

(defn- list-field [path inputs {:keys [id type] :as f}]
  (let [values (vec (get inputs id))
        bool?  (= :bool type)]
    [:div.list-field
     (for [[idx v] (map-indexed vector values)]
       (list-row path id idx v f (not bool?)))
     (if bool?
       [:button.list-field__add
        {:type "button" :on {:click [[:ui/set-list-input path id (count values) false]]}}
        "+ Add"]
       (when (or (empty? values) (not (str/blank? (str (peek values)))))
         [:div.list-row.list-row--extra {:replicant/key (count values)}
          (control (dom-id "field-options" path id (count values)) nil f
                   [:ui/set-list-input path id (count values)])]))]))

(defn- field [path inputs {:keys [id label type list?] :as f}]
  (if list?
    [:div.field {:replicant/key id}
     [:span.field__label label]
     (list-field path inputs f)]
    [:label.field {:replicant/key id
                   :class         (when (= :bool type) "field--bool")}
     [:span.field__label label]
     (control (dom-id "field-options" path id) (get inputs id) f [:ui/set-input path id])]))

(defn input-form
  "Render a spec's declared inputs, or nil when there are none."
  [{plugin :id :as spec} inputs]
  (when (seq (:inputs spec))
    [:div.fields {:on {:keydown [[:ui/generate-on-enter plugin [:event/key]]]}}
     (for [f (:inputs spec)]
       (field [:tools plugin :inputs] inputs f))]))

;; --- view-model renderer (the signature surface) -----------------------------

(defn- linked
  "Text with its links, spies.tools ones previewing their entry on hover."
  [spies s]
  (for [x (link/linkify s)]
    (if-let [[page n] (and spies (vector? x) (spies/target (get-in x [1 :href])))]
      [:span.spies {:on {:mouseenter [[:ui/spies-preview page]]}}
       x
       (when-some [e (get-in spies [page n])]
         (spies/card page e))]
      x)))

(defn- marked
  "Key the node on its text `s`, flagging it if `changed?`, so each change
   re-mounts it and replays the highlight."
  [changed? s [tag & more]]
  (into [tag {:replicant/key s :class (when (changed? s) "changed")}] more))

;; An item's vars shadow the `:loot/vars` shared by every item. An item whose
;; body renders blank, like one gated on a higher rank, is hidden.
(defn- entry [spies changed? loot-vars {:item/keys [title body metadata vars]}]
  (let [vars  (merge loot-vars vars)
        title (some-> title (template/render vars))
        body  (template/render body vars)]
    (when-not (str/blank? body)
      [:li.entry
       (when title (marked changed? title [:h4.entry__title (linked spies title)]))
       (marked changed? body [:div.entry__body (linked spies body)])
       (when (seq metadata)
         [:ul.tags (map-indexed (fn [i t] [:li.tag {:replicant/key (str i t) :class (when (changed? t) "changed")} t]) metadata)])])))

(defn- block [spies changed? loot-vars {:section/keys [heading items]}]
  (when-let [entries (seq (keep (partial entry spies changed? loot-vars) items))]
    [:section.block
     (when heading [:h3.block__heading heading])
     [:ul.entries entries]]))

(defn- clip [s]
  (let [s (str/trim s)]
    (if (> (count s) 60) (str (subs s 0 59) "…") s)))

(defn- positions
  "The sections and items of `vm` that render, as `:section`/`:item` options."
  [vm]
  (for [[s {:section/keys [heading items]}] (map-indexed vector (:loot/sections vm))
        :let [shown (keep-indexed
                      (fn [i {:item/keys [title body vars]}]
                        (let [vars  (merge (:loot/vars vm) vars)
                              title (some-> title (template/render vars))
                              body  (template/render body vars)]
                          (when-not (str/blank? body)
                            {:value (str s ":" i)
                             :label (clip (if (str/blank? title) body title))})))
                      items)]
        :when (seq shown)]
    {:value (str s)
     :label (or heading (str "Section " (inc s)))
     :items shown}))

(defn- action
  "An action's button, with a form for any `:action/inputs`, whose values are
   keyed by `idx`."
  [vm action-inputs idx {:action/keys [label event inputs]}]
  (if (seq inputs)
    (let [[_ {:keys [id] :as params}] event
          picks (positions vm)]
      [:div.action-form
       [:div.fields {:on {:keydown [[:ui/act-on-enter params idx [:event/key]]]}}
        (for [f inputs]
          (field [:tools id :action-inputs idx] (get action-inputs idx) (assoc f :positions picks)))]
       [:button.action {:on {:click [(conj event idx)]}} label]])
    [:button.action {:on {:click [event]}} label]))

(defn- words-badge
  "The result's words, revealed on hover."
  [words]
  (when (seq words)
    [:span.words {:title (str/join " " words)}
     [:span.words__mark "W"]
     [:span.words__text (str/join " " words)]]))

(defn- changes
  "A predicate for whether rendered text is new since `previous`."
  [previous]
  (if previous
    (let [seen (into #{} (filter string?) (tree-seq coll? seq (template/render-view-model previous)))]
      (fn [s] (not (contains? seen s))))
    (constantly false)))

(defn result
  "Render a view-model, keyed on its title so a new result replays its
   animation. Text that wasn't in `previous` is highlighted."
  ([vm] (result vm nil nil nil))
  ([vm spies action-inputs previous]
   (when vm
     (let [changed? (changes previous)
           vars     (:loot/vars vm)]
       [:article.sigil {:replicant/key (:loot/title vm)}
        [:div.sigil__frame
         (words-badge (:loot/words vm))
         (when-let [subtitle (some-> (:loot/subtitle vm) (template/render vars))]
           (marked changed? subtitle [:p.sigil__eyebrow subtitle]))
         (let [title (template/render (:loot/title vm) vars)]
           (marked changed? title [:h2.sigil__title title]))
         [:div.sigil__body
          (keep (partial block spies changed? vars) (:loot/sections vm))]
         (when (seq (:loot/actions vm))
           [:div.sigil__actions
            (map-indexed (partial action vm action-inputs) (:loot/actions vm))])]]))))

(defn error-overlay
  "An error view-model, shown over the card until clicked away."
  [plugin vm]
  (when vm
    [:div.error-overlay {:on {:click [[:ui/dismiss-error plugin]]}}
     (result vm)]))

;; --- result editor --------------------------------------------------------------
;; Edits write straight into the result view-model.

(defn- edit-field [plugin label path v area?]
  [:label.edit {:replicant/key (str path)}
   [:span.edit__label label]
   (if area?
     [:textarea.edit__control
      {:on {:input [[:ui/edit-result plugin path :text [:event.target/value]]]}}
      (str v)]
     [:input.edit__control
      {:type  "text"
       :value (str v)
       :on    {:input [[:ui/edit-result plugin path :text [:event.target/value]]]}}])])

(defn- edit-metadata [plugin path metadata draft]
  [:div.edit {:replicant/key (str path)}
   [:span.edit__label "Metadata (Enter to add)"]
   (when (seq metadata)
     [:ul.tags.tags--edit
      (map-indexed (fn [i t]
                     [:li.tag {:replicant/key i}
                      [:button.tag__text
                       {:type  "button"
                        :title "Edit"
                        :on    {:click [[:fx/focus-sibling-input [:event/raw]]
                                        [:ui/edit-result-metadata plugin path i]]}}
                       t]
                      [:button.tag__remove
                       {:type  "button"
                        :title "Remove"
                        :on    {:click [[:ui/remove-result-metadata plugin path i]]}}
                       "✕"]])
                   metadata)])
   [:input.edit__control
    {:type  "text"
     :value (str draft)
     :on    {:input   [[:fx/assoc-in (into [:tools plugin :drafts] path) [:event.target/value]]]
             :keydown [[:ui/add-result-metadata plugin path [:event/key]]]}}]])

(defn- var-label [id label]
  (or label (-> (name id) (str/replace #"[-_]" " ") str/capitalize)))

(defn- var-type
  "The control a var edits with. Ints edit as decimals too, so 2 can become 2.5."
  [type]
  (cond
    (= :bool type)          :bool
    (#{:int :decimal} type) :decimal
    :else                   :text))

(defn- select-field
  "A `<select>` over a drawn var's vocabulary. Not a combobox like `enum-field`,
   whose filtering would hide every option but the current value. A value
   outside the vocabulary is added, so it isn't lost."
  [value options action]
  (let [v      (str value)
        values (map str options)
        values (if (some #{v} values) values (cons v values))]
    [:select.field__control
     {:value v
      :on    {:change [(conj action [:event.target/value])]}}
     (for [opt values]
       [:option (cond-> {:value opt} (= opt v) (assoc :selected true)) opt])]))

(defn- ranked-to
  "The ranked value, when it differs from the declared one."
  [{:keys [value] :as v}]
  (let [stepped (rank/stepped v)]
    (when (not= value stepped)
      [:span.edit__derived "= " (str stepped)])))

(defn- rank-field
  [plugin path {:keys [rank] mx :max}]
  [:label.edit.edit--rank
   [:span.edit__label "Rank"]
   [:input.field__control
    (cond-> {:type  "number"
             :min   1
             :step  1
             :value (str (or rank 1))
             :on    {:input [[:ui/edit-result plugin (conj path :rank) :rank [:event.target/value]]]}}
            mx (assoc :max mx))]])

(defn- step-field
  [plugin path {:keys [value step]}]
  [:label.edit.edit--rank
   [:span.edit__label "Step"]
   [:input.field__control
    {:type        "number"
     :step        "any"
     :placeholder (str value)
     :value       (str step)
     :on          {:input [[:ui/edit-result plugin (conj path :step) :decimal [:event.target/value]]]}}]])

(defn- edit-var [plugin path id {:keys [label value options type] :as v}]
  (let [action [:ui/edit-result plugin (conj path :value) type]]
    [:div.edit-var {:replicant/key (str path)
                    :class         (when (rank/upgradeable? v) "edit-var--ranked")}
     [:label.edit
      [:span.edit__label (var-label id label) (ranked-to v)]
      (if (seq options)
        (select-field value options action)
        (control nil value {:type (var-type type)} action))]
     (when (rank/upgradeable? v)
       (list (step-field plugin path v)
             (rank-field plugin path v)))
     [:button.list-row__remove
      {:type  "button"
       :title "Remove var"
       :on    {:click [[:ui/remove-result-var plugin path]]}}
      "✕"]]))

(defn- editable-vars [vars]
  (remove (comp :context? val) vars))

(defn- var-grid
  [plugin class base-path vars]
  (when-let [editable (seq (editable-vars vars))]
    [:div {:class class}
     (for [[id v] editable]
       (edit-var plugin (conj base-path id) id v))]))

(defn- new-var-field [plugin base-path {var-name :name :keys [type]}]
  (let [draft-path (into [:tools plugin :drafts] base-path)]
    [:div.edit
     [:span.edit__label "New variable (Enter to add)"]
     [:div.new-var
      [:input.edit__control
       {:type        "text"
        :placeholder "variable name"
        :value       (str var-name)
        :on          {:input   [[:fx/assoc-in (conj draft-path :name) [:event.target/value]]]
                      :keydown [[:ui/add-result-var plugin base-path [:event/key]]]}}]
      [:select.field__control
       {:title "Type"
        :value (name (or type :decimal))
        :on    {:change [[:fx/assoc-in (conj draft-path :type) [:event.target/value]]]}}
       (for [[v label] [["decimal" "Number"] ["text" "Text"] ["bool" "True/false"]]]
         [:option (cond-> {:value v} (= v (name (or type :decimal))) (assoc :selected true)) label])]]]))

(defn- edit-text
  "An item's templates, folded behind a rendered preview, since vars are the
   common edit."
  [plugin drafts si ii {:item/keys [title body metadata]} preview]
  (let [vars-path [:loot/sections si :section/items ii :item/vars]]
    [:details.fold {:replicant/key (str "text-" si "-" ii)}
     [:summary.fold__summary
      [:span.fold__preview preview]
      [:span.fold__hint "text"]
      ;; Inside the summary, so prevent it toggling the fold.
      [:button.list-row__remove
       {:type  "button"
        :title "Remove item"
        :on    {:click [[:fx/prevent-default [:event/raw]] [:ui/remove-result-item plugin si ii]]}}
       "✕"]]
     [:div.fold__body
      (edit-field plugin "Item title" [:loot/sections si :section/items ii :item/title] title false)
      (edit-field plugin "Body" [:loot/sections si :section/items ii :item/body] body true)
      (let [path [:loot/sections si :section/items ii :item/metadata]]
        (edit-metadata plugin path metadata (get-in drafts path)))
      (new-var-field plugin vars-path (get-in drafts vars-path))]]))

(defn- edit-item [plugin drafts loot-vars si ii {:item/keys [title body vars] :as item}]
  (let [all     (merge loot-vars vars)
        rendered #(some-> % (template/render all) str str/trim not-empty)
        preview (or (not-empty (str (some-> (rendered title) (str ": ")) (rendered body))) "New item")]
    [:li.entry.entry--edit {:replicant/key ii}
     (var-grid plugin "entry__vars" [:loot/sections si :section/items ii :item/vars] vars)
     (edit-text plugin drafts si ii item preview)]))

(defn- edit-block [plugin drafts loot-vars si {:section/keys [heading items secret?]}]
  [:section.block.block--edit {:replicant/key si}
   [:div.block__head
    [:input.block__heading.block__heading--edit
     {:type  "text"
      :value (str heading)
      :on    {:input [[:ui/edit-result plugin [:loot/sections si :section/heading] :text
                       [:event.target/value]]]}}]
    [:label.field.field--bool.block__secret {:title "Left out of reports"}
     [:span.field__label "Secret"]
     (control nil secret? {:type :bool}
              [:ui/edit-result plugin [:loot/sections si :section/secret?] :bool])]]
   [:ul.entries (map-indexed (fn [ii item] (edit-item plugin drafts loot-vars si ii item)) items)]
   [:button.list-field__add
    {:type "button"
     :on   {:click [[:ui/add-result-item plugin si]]}}
    "+ Add item"]])

(defn result-editor
  "Render a result view-model as an editable form, leaving its actions as they
   are."
  [plugin vm drafts]
  (when vm
    (let [loot-vars (:loot/vars vm)]
      [:article.sigil.sigil--edit {:replicant/key "result-editor"}
       [:div.sigil__frame
        [:h2.sigil__title (template/render (:loot/title vm) loot-vars)]
        [:details.fold {:replicant/key "text-loot"}
         [:summary.fold__summary
          [:span.fold__preview (:loot/title vm)]
          [:span.fold__hint "title, subtitle & shared vars"]]
         [:div.fold__body
          (edit-field plugin "Title" [:loot/title] (:loot/title vm) false)
          (edit-field plugin "Subtitle" [:loot/subtitle] (:loot/subtitle vm) false)
          (new-var-field plugin [:loot/vars] (:loot/vars drafts))]]
        (when-let [grid (var-grid plugin "entry__vars entry__vars--shared" [:loot/vars] loot-vars)]
          (list [:h3.block__heading "Shared values"] grid))
        [:div.sigil__body
         (map-indexed (partial edit-block plugin drafts loot-vars) (:loot/sections vm))]]])))

;; --- manually-managed state (a spec's `:store/manual` table) ------------------
;; Edits are local until a `change` bubbles up to the row, which commits it.

(defn- manual-cell [plugin k path f value]
  [:label.manual__cell {:replicant/key (:id f)
                        :class         (when (= :bool (:type f)) "manual__cell--bool")}
   [:span.field__label (:label f)]
   (control (apply dom-id "manual" plugin k path)
            value f [:ui/manual-edit plugin k (vec path)])])

(defn- manual-fields [plugin k prefix fields row]
  (for [f fields]
    (manual-cell plugin k (conj prefix (:id f)) f (get row (:id f)))))

(defn- manual-item
  "One record of a `:list?` row. An `idx` past the end is the blank record that
   appends when filled in."
  [plugin k fields idx row extra?]
  [:li.manual__item {:replicant/key idx :class (when extra? "manual__item--extra")}
   (manual-fields plugin k [idx] fields row)
   ;; Kept, inert, on the blank record so the columns line up.
   [:button.manual__remove
    {:type     "button"
     :class    (when extra? "manual__remove--placeholder")
     :disabled extra?
     :on       (when-not extra? {:click [[:ui/manual-remove-item plugin k idx]]})}
    "✕"]])

(defn- manual-row [plugin {:keys [fields list?]} k value]
  [:li.manual__row {:replicant/key k :on {:change [[:ui/manual-commit plugin k]]}}
   [:div.manual__head
    [:span.manual__name k]
    [:button.manual__remove {:type "button" :on {:click [[:ui/manual-remove plugin k]]}} "Remove"]]
   (if list?
     [:ul.manual__items
      (concat (map-indexed (fn [idx row] (manual-item plugin k fields idx row false)) value)
              [(manual-item plugin k fields (count value) nil true)])]
     [:div.manual__fields (manual-fields plugin k [] fields value)])])

(defn manual-editor
  "The spec's `:store/manual` table, if it declares one."
  [{plugin :id :as spec} manual manual-key]
  (when-let [{:keys [key-label] :as declaration} (:store/manual spec)]
    (let [key-label (or key-label "Entry")]
      [:section.manual
       [:p.summon__eyebrow key-label]
       [:ul.manual__rows
        (for [[k v] (sort-by key manual)]
          (manual-row plugin declaration k v))
        [:li.manual__row.manual__row--new {:replicant/key "__add"}
         [:input.field__control
          {:type        "text"
           :placeholder (str "Add " (str/lower-case key-label) "…")
           :value       (str manual-key)
           :on          {:input  [[:ui/manual-set-key plugin [:event.target/value]]]
                         :change [[:ui/manual-add plugin]]}}]]]])))

;; --- page picker ---------------------------------------------------------------

;; Hidden types only appear while on screen, so are styled as transient.
(defn- modifier [{:keys [hidden?]}]
  (when hidden? "discipline--transient"))

(defn- page-button [active? id glyph label modifier]
  [:button.discipline {:class [(when active? "discipline--active") modifier]
                       :on    {:click [[:ui/select-page id]]}}
   [:span.discipline__glyph glyph]
   [:span.discipline__name label]])

(defn- type-list [rows page]
  [:ul.rail__list
   (for [{:keys [id label glyph] :as row} rows]
     [:li {:replicant/key id}
      (page-button (= id page) id glyph label (modifier row))])])

(def ^:private default-section "Loot")

(defn- by-section
  "`rows` grouped by section, Loot first then alphabetically, each in `order`."
  [order rows]
  (let [position (zipmap order (range))]
    (->> (sort-by (comp position :id) rows)
         (group-by #(or (:section %) default-section))
         (sort-by (fn [[section]] [(not= default-section section) section])))))

(defn- matcher [type-filter]
  (let [q (str/lower-case (str/trim (str type-filter)))]
    (fn [label]
      (or (str/blank? q)
          (str/includes? (str/lower-case (str label)) q)))))

(defn picker [{:keys [pages sections page type-filter browser-storage?] :as state}]
  (let [match?  (matcher type-filter)
        ;; A page matches on its own label or any of its tools'.
        pages   (for [{:keys [label tools] :as p} pages
                      :when (some match? (cons label (map #(:label (state/spec state %)) tools)))]
                  (assoc p :glyph "❖"))
        ;; Hidden types only appear while they're the page on screen.
        plugins (for [spec (:loot-types state)
                      :when (and (or (not (:hidden? spec)) (= page (:id spec)))
                                 (match? (:label spec)))]
                  (assoc spec :glyph "◆"))]
    [:nav.rail
     (when (seq (:loot-table state))
       [:button.rail__table {:class (when (= state/loot-table-page page) "rail__table--active")
                             :on    {:click [[:ui/select-page state/loot-table-page]]}}
        "Loot table"])
     [:input.rail__search
      {:type        "search"
       :placeholder "Search tools…"
       :value       (str type-filter)
       :on          {:input [[:ui/set-type-filter [:event.target/value]]]}}]
     (for [[section rows] (by-section sections (concat pages plugins))]
       [:div.rail__section {:replicant/key section}
        [:p.rail__eyebrow section]
        (type-list rows page)])
     [:button.rail__export {:on {:click [[:ui/export]]}}
      "Download state"]
     [:p.rail__hint
      (if browser-storage?
        "Download a copy of your local state to use it for a local deployment, or just as a backup."
        "A ZIP of all application state data.")]]))

;; --- loot table ----------------------------------------------------------------

(defn- spans [ranges]
  (or (not-empty (str/join ", " (for [[from to] ranges] (if (= from to) (str from) (str from "–" to)))))
      "—"))

(defn loot-table
  "The loot table, highlighting the entries the roll box's number lands on."
  [{:keys [loot-table loot-die-size roll-n] :as state}]
  (let [n    (actions/roll-number roll-n)
        hit? (fn [{:keys [ranges]}] (and n (some (fn [[from to]] (<= from n to)) ranges)))]
    [:section.loot-table
     [:p.summon__eyebrow "Loot table"]
     [:div.roll-group
      [:input.roll__input
       {:type        "number"
        :min         "1"
        :max         (str loot-die-size)
        :placeholder (str "d" loot-die-size)
        :value       (str roll-n)
        :on          {:input   [[:ui/set-roll-input [:event.target/value]]]
                      :keydown [[:ui/roll-on-enter [:event/key]]]}}]
      [:button.action-btn {:on {:click [[:ui/roll-die]]}} (str "Roll d" loot-die-size)]
      (when (and n (<= 1 n loot-die-size))
        [:button.action-btn {:on {:click [[:ui/roll]]}} "Generate"])]
     [:ul.loot-table__list
      (for [{:keys [id ranges] :as entry} loot-table]
        [:li.loot-table__row {:replicant/key id
                              :class         (when (hit? entry) "loot-table__row--hit")}
         [:span.loot-table__span (spans ranges)]
         [:span.loot-table__name (or (:label (state/spec state id)) (name id))]
         [:button.action-btn {:on {:click [[:ui/select-page id] [:ui/generate id]]}} "Generate"]])]]))

;; --- result history -----------------------------------------------------------

(defn- history-label [{:loot/keys [title vars]}]
  (or (not-empty (str/trim (str (template/render title vars)))) "Untitled"))

(defn- history-body
  "The result's first item body, which tells apart results sharing a title."
  [{:loot/keys [vars sections]}]
  (let [{:item/keys [body] :as item} (first (mapcat :section/items sections))]
    (some-> body (template/render (merge vars (:item/vars item))) str str/trim not-empty)))

(defn- history-preview
  "The hovered result in full, without actions. A pinned preview takes the
   pointer, so its text can be selected."
  [vm locked?]
  (when vm
    [:div.history__preview
     (when locked?
       {:class "history__preview--locked"
        :on    {:mouseleave [[:ui/history-lock [:event/alt?]]]}})
     (result (dissoc vm :loot/actions))]))

(defn history
  "Loot type `plugin`'s stored results, newest first. Click to restore, hover to
   preview, and hold Alt to pin the preview."
  [plugin entries hovered locked?]
  (when (seq entries)
    [:section.history
     [:div.history__head
      [:p.summon__eyebrow "History"]
      [:button.action-btn {:on {:click [[:ui/history-clear plugin]]}} "Clear"]]
     [:ul.history__list
      (map-indexed
        (fn [idx {:keys [at view-model]}]
          [:li.history__row {:replicant/key (str at "-" idx)
                             :on            {:mouseenter [[:ui/history-hover plugin idx [:event/alt?]]]
                                             :mouseleave [[:ui/history-hover plugin nil]]}}
           [:button.history__entry {:on {:click [[:ui/history-restore plugin idx]]}}
            [:span.history__time (.toLocaleString (js/Date. at))]
            [:span.history__name (history-label view-model)]
            ;; Always rendered, to hold the grid columns.
            [:span.history__body (history-body view-model)]
            [:span.history__words (str/join " " (:loot/words view-model))]]
           [:button.history__remove {:type "button"
                                     :on   {:click [[:ui/history-delete plugin idx]]}}
            "✕"]])
        entries)]
     (history-preview (when hovered (:view-model (nth (vec entries) hovered nil))) locked?)]))
