(ns sns.sdk.schema
  "Malli schemas shared by the backend, plugins and frontend, in one registry so
   they can reference each other."
  (:require
    [malli.core :as m]
    [malli.error :as me]
    [malli.transform :as mt]
    [malli.util :as mu]))

(defn- tool [type & entries]
  (into [:map
         [:type [:= type]]
         [:id keyword?]
         [:label {:optional true} string?]
         ;; The UI rail section it's listed under ("Loot" when absent).
         [:section {:optional true} string?]]
        entries))

(defn- generator-tool [type & entries]
  (apply tool type [:generator {:optional true} ::loot-spec] entries))

(def schemas
  {;; --- loot specifications (drive the generic input forms) ---
   ::field [:map
            [:id keyword?]
            [:label string?]
            [:type [:enum :enum :int :decimal :text :bool]]
            [:default {:optional true} any?]
            [:list? {:optional true} boolean?]
            [:options {:optional true} [:sequential any?]]]

   ;; When a result is added to the browser's history: on every generation,
   ;; when reported, via the manual button, or never.
   ::history [:enum :always :on-report :button :never]

   ;; What a generator declares about itself. A plugin's `loot-spec` returns it,
   ;; and a tool's `:generator` config overrides it key by key. The engine adds
   ;; the tool's `:id`, `:label` and `:section`.
   ::loot-spec [:map
                 ;; Kept out of the UI picker, so only reachable by rolling the
                 ;; :loot-table, from another type's action, or from a page.
                [:hidden? {:optional true} boolean?]
                 ;; Replaces the "Generate <label>" button text, e.g. "Add character".
                [:generate-label {:optional true} string?]
                 ;; Collections this type reads or writes; defaults to one named
                 ;; after :id. Under :browser storage, the client sends exactly
                 ;; these with each request.
                [:store/collections {:optional true} [:sequential keyword?]]
                 ;; A DM-edited table in the first of those collections.
                [:store/manual {:optional true} ::manual-state]
                [:history {:optional true} ::history]
                 ;; Form fields whose values are sent as `inputs`.
                [:inputs {:optional true} [:sequential ::field]]]

   ;; A collection the DM fills in by hand, like a character table. Each key is
   ;; a row and `:fields` are its columns, coerced to their types, with a blank
   ;; falling back to `:default`. With `:list?`, a row holds a list of records.
   ::manual-state [:map
                   [:key-label {:optional true} string?]
                   [:list? {:optional true} boolean?]
                   [:fields [:sequential ::field]]]

   ;; --- view-model (the UI renderer's contract) ---
   ;; Beyond a `::field`'s types, an action field can pick a rendered `:section`
   ;; or `:item` of the result, arriving as `{:section i}` or `{:section i :item j}`.
   ::action-field [:merge
                   ::field
                   [:map [:type [:enum :enum :int :decimal :text :bool :section :item]]]]

   ;; `:action/event` is a UI event vector dispatched as-is, so only trusted
   ;; in-process plugins (e.g. `sns.builtin.relics`) build one directly. Plugins
   ;; across a boundary send `label`/`action`/`params` instead, and their adapter
   ;; builds the event, keeping it to their own route under their config id.
   ;; `:action/inputs` render beside the button and reach the plugin as `inputs`.
   ::action [:map
             [:action/label string?]
             [:action/event vector?]
             [:action/inputs {:optional true} [:sequential ::action-field]]]

   ;; A resolved template variable (see `sns.sdk.vars`). Values travel apart from
   ;; the template, so a DM can edit one without touching the prose, and a plugin
   ;; can read it back without parsing any. `:options` are offered as a combobox.
   ;; `:random`/`:args` record the source, so an action can reroll it. Without
   ;; `:label`, the UI derives one from the var's key.
   ::item-var [:map
               [:value any?]
               ;; Picks the browser's control and how it parses edits. Stamped at
               ;; resolution, since a value being edited may be blank. Absent means text.
               [:type {:optional true} [:enum :int :decimal :bool]]
               [:label {:optional true} string?]
               [:random {:optional true} keyword?]
               [:args {:optional true} [:map-of keyword? any?]]
               [:options {:optional true} [:sequential any?]]
               ;; An entry's own field: available to templates, but not editable.
               [:context? {:optional true} boolean?]
               ;; Ranking (see `sns.sdk.rank`). Ranks are 1-based, so no `:rank`
               ;; means the declared value. `:step` defaults to that value, and
               ;; a non-numeric var never ranks.
               [:step {:optional true} any?]
               [:max {:optional true} [:int {:min 1}]]
               [:rank {:optional true} [:int {:min 1}]]]

   ;; Keyed by the name templates use: `{{ damage }}` reads `:damage`.
   ::item-vars [:map-of keyword? ::item-var]

   ;; Title and body are templates rendered in the browser against `:item/vars`;
   ;; plain text renders as itself.
   ::item [:map
           [:item/title {:optional true} [:maybe string?]]
           [:item/body string?]
           [:item/metadata {:optional true} [:sequential string?]]
           [:item/vars {:optional true} ::item-vars]]

   ::section [:map
              [:section/heading {:optional true} [:maybe string?]]
              [:section/items [:sequential ::item]]
              ;; Shown and kept in history, but left out by reporters unless configured otherwise.
              [:section/secret? {:optional true} boolean?]]

   ::view-model [:map
                 [:loot/title string?]
                 [:loot/subtitle {:optional true} [:maybe string?]]
                 ;; Template context for the title and subtitle.
                 [:loot/vars {:optional true} ::item-vars]
                 [:loot/sections {:optional true} [:sequential ::section]]
                 [:loot/actions {:optional true} [:sequential ::action]]
                 ;; The result's anonymous handle, drawn by the engine from the
                 ;; config's word list so every surface shows the same words.
                 [:loot/words {:optional true} [:sequential string?]]
                 ;; Opaque plugin state, handed back untouched with the next
                 ;; action. Displayed values belong in `:item/vars`, so keep this
                 ;; to what can't be read back off the item.
                 [:loot/state {:optional true} any?]
                 ;; A failure rather than loot: shown as an overlay, and never
                 ;; saved to history automatically.
                 [:loot/error? {:optional true} boolean?]
                 ;; Declared rather than performed, so the engine applies them
                 ;; only once the view-model validates.
                 [:store/mutations {:optional true} ::mutations]]

   ;; `{<collection> {<key> <value>}}`; a nil value retracts that key.
   ::mutations [:map-of keyword? [:map-of any? any?]]

   ;; --- external plugin I/O (:cli, :ffi, :wasm) ---
   ;; External plugins exchange the view-model as JSON, where namespaced keys
   ;; round-trip (`"loot/title"` reads back as `:loot/title`).
   ::plugin-action [:map
                    [:label string?]
                    [:action string?]
                    [:params {:optional true} [:map-of keyword? any?]]
                    [:inputs {:optional true} [:sequential ::action-field]]]

   ;; The view-model as an external plugin writes it, with `::plugin-action`s in
   ;; place of built events. Validated before mapping, so errors name the
   ;; author's own keys.
   ::plugin-output [:merge
                    ::view-model
                    [:map [:loot/actions {:optional true} [:sequential ::plugin-action]]]]

   ;; A generate call (`inputs`) or an action call (`action`/`params`, plus the
   ;; on-screen `view-model` with the DM's edits and `:loot/state`). A union so
   ;; codegen emits a type for each. `state` holds the plugin's declared
   ;; collections; it writes back through `store/mutations`. Built by the
   ;; engine, so not validated at runtime.
   ::plugin-request [:or
                     [:map
                      [:inputs [:map-of keyword? any?]]
                      [:state {:optional true} [:map-of keyword? [:map-of any? any?]]]]
                     [:map
                      [:action string?]
                      [:params {:optional true} [:map-of keyword? any?]]
                      [:inputs {:optional true} [:map-of keyword? any?]]
                      [:view-model {:optional true} ::view-model]
                      [:state {:optional true} [:map-of keyword? [:map-of any? any?]]]]]

   ;; --- a mod that ranks up ---
   ;; The built-ins' convention, unused by the engine, which lets a plugin use
   ;; `sns.sdk.rank`: `:vars` declarations (see `sns.sdk.vars`) that each carry
   ;; their own progression, and `:max-ranks` capping ranks across all of them.
   ::mod [:map
          [:vars {:optional true} [:map-of keyword? any?]]
          [:template {:optional true} string?]
          [:max-ranks {:optional true} [:int {:min 1}]]]

   ;; --- the :data plugin DSL ---
   ;; `:title`/`:body` are a template string, or a keyword naming a field on the
   ;; drawn element. A field reference hands the browser that field's own
   ;; template (`:body :effect` → `"+4 {{ ability }}."`) with its tags intact.
   ;;
   ;; `string?` must lead the `:or`: the JSON transformer decodes into the first
   ;; matching branch, so a leading keyword branch would turn every template
   ;; into a keyword. In JSON, a string naming a field is read as a reference.
   ::data-item [:map
                [:title {:optional true} [:or string? keyword?]]
                [:body [:or string? keyword?]]
                [:metadata {:optional true} keyword?]]

   ::data-section [:map
                   [:heading {:optional true} string?]
                   [:each keyword?]
                   [:item ::data-item]]

   ::data-spec [:map
                [:history {:optional true} ::history]
                [:inputs {:optional true} [:sequential ::field]]
                [:items [:sequential [:map-of keyword? any?]]]
                [:take {:optional true} int?]
                [:weighted {:optional true} boolean?]
                [:title {:optional true} string?]
                [:subtitle {:optional true} string?]
                [:sections {:optional true} [:sequential ::data-section]]]

   ;; --- application config ---
   ::server [:map
             [:host {:optional true} string?]
             [:port {:optional true} int?]]

   ::storage [:map
              [:backend [:enum :file :memory :browser]]
              [:file {:optional true} [:map [:dir {:optional true} string?]]]]

   ;; Dispatch keywordises `:type` so JSON configs decode to the right branch.
   ;; Keys every tool shares sit at the top level, type-specific ones under the
   ;; type's key, and generator settings under `:generator`.
   ::tool [:multi {:dispatch (fn [p] (some-> (:type p) keyword))}
           ;; Shows other tools side by side; a tool no page lists gets its own.
           [:page (tool :page [:page [:map [:tools [:sequential {:min 1} keyword?]]]])]
           ;; :inline wins when both are given.
           [:data (generator-tool :data
                                  [:data [:and
                                          [:map
                                           [:source {:optional true} string?]
                                           [:inline {:optional true} ::data-spec]]
                                          [:fn {:error/message "a :data plugin needs a :source or an :inline spec"}
                                           (fn [d] (boolean (or (:source d) (:inline d))))]]])]
           [:cli (generator-tool :cli [:cli [:map [:command [:sequential string?]]]])]
           ;; A WASI command module run in-process on GraalWASM, using the :cli
           ;; stdio contract. `:dirs` maps guest paths to readable host
           ;; directories; none means no filesystem access.
           [:wasm (generator-tool :wasm
                                  [:wasm [:map
                                          [:module string?]
                                          [:args {:optional true} [:sequential string?]]
                                          [:dirs {:optional true} [:map-of [:or keyword? string?] string?]]]])]
           ;; A `char* (char* request_json)` symbol in a shared library. If
           ;; `:free-symbol` is given, it's called on the returned pointer;
           ;; otherwise the library owns that memory.
           [:ffi (generator-tool :ffi
                                 [:ffi [:map
                                        [:library string?]
                                        [:symbol string?]
                                        [:free-symbol {:optional true} string?]]])]
           ;; A Clojure `:entrypoint` factory var, or a `:class` with a no-arg
           ;; constructor. Unavailable in native images.
           [:jar (generator-tool :jar
                                 [:jar [:and
                                        [:map
                                         [:path string?]
                                         [:entrypoint {:optional true} symbol?]
                                         [:class {:optional true} string?]]
                                        [:fn {:error/message "a :jar plugin needs an :entrypoint or a :class"}
                                         (fn [j] (boolean (or (:entrypoint j) (:class j))))]]])]
           [:builtin (generator-tool :builtin
                                     [:builtin {:optional true} [:map [:entrypoint {:optional true} symbol?]]])]]

   ;; `:ranges` pins an entry to sides of the loot die, e.g. [[1 20] [99 99]];
   ;; overlapping ranges roll every entry they share.
   ::loot-entry [:and
                 [:map
                  [:id keyword?]
                  [:weight {:optional true} number?]
                  [:ranges {:optional true} [:sequential [:tuple pos-int? pos-int?]]]]
                 [:fn {:error/message "a loot-table entry takes :weight or :ranges, not both"}
                  (fn [e] (not (and (contains? e :weight) (contains? e :ranges))))]]

   ;; Named value lists a var can draw from with `{:random :<preset>}`.
   ::randoms [:map-of keyword? [:sequential any?]]

   ;; --- reporting (sending results elsewhere) ---
   ;; Dispatch keywordises `:backend` for JSON configs.
   ::reporting [:multi {:dispatch (fn [r] (some-> (:backend r) keyword))}
                [:discord [:map
                           [:backend [:= :discord]]
                           [:discord [:map
                                      [:webhook-url string?]
                                      [:username {:optional true} string?]
                                      [:avatar-url {:optional true} string?]
                                      [:include-secret? {:optional true} boolean?]]]]]]

   ::config [:map
             [:server {:optional true} ::server]
             [:storage {:optional true} ::storage]
             [:tools [:sequential ::tool]]
             [:randoms {:optional true} ::randoms]
             [:words {:optional true} [:sequential string?]]
             [:extra-words {:optional true} [:sequential string?]]
             [:reporting {:optional true} ::reporting]
             [:history {:optional true} ::history]
             [:loot-table {:optional true} [:sequential ::loot-entry]]
             [:loot-die-size {:optional true} pos-int?]]})

(def registry
  "Malli's defaults, its util schemas (for `:merge`), and ours."
  (merge (m/default-schemas) (mu/schemas) schemas))

(defn validate [schema value]
  (m/validate schema value {:registry registry}))

(defn decode
  "Decode `value` with malli's JSON transformer, so a JSON config reads into the
   same shape as an EDN one."
  [schema value]
  (m/decode schema value {:registry registry} (mt/transformer (mt/json-transformer) (mt/default-value-transformer))))

(defn explain
  "Why `value` doesn't conform to `schema`, or nil if it does."
  [schema value]
  (m/explain schema value {:registry registry}))

(defn assert!
  "Return `value`, or throw an ex-info describing why it doesn't conform."
  [schema value]
  (when-let [err (explain schema value)]
    (throw (ex-info (str "Schema validation failed: " schema)
                    {:schema schema :error (me/humanize err)})))
  value)
