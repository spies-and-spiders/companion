(ns sns.sdk.schema
  "Malli schemas shared across the backend, plugins, and (the view-model/spec
   subset) the ClojureScript frontend. All schemas live in one registry so they
   can reference each other, including the recursive upgrade-graph."
  (:require
    [malli.core :as m]
    [malli.error :as me]
    [malli.transform :as mt]
    [malli.util :as mu]))

(def schemas
  "The project's named schemas, keyed by qualified keyword."
  {;; --- loot specifications (drive the generic input forms) ---
   ::field [:map
            [:id keyword?]
            [:label string?]
            [:type [:enum :enum :int :decimal :text :bool]]
            [:default {:optional true} any?]
            [:list? {:optional true} boolean?]
            [:options {:optional true} [:sequential any?]]]

   ;; When a generated result is added to the browser-side history: on every
   ;; generation, only when reported, only on the manual button, or not at all.
   ::history [:enum :always :on-report :button :never]

   ::loot-spec [:map
                [:id keyword?]
                [:label string?]
                 ;; Utilities are session tools (e.g. the group social roller)
                 ;; rather than loot: grouped separately in the UI and barred
                 ;; from the :loot-table.
                [:utility? {:optional true} boolean?]
                 ;; Hidden types are kept out of the UI picker, so they are only
                 ;; reached by rolling the :loot-table (or by another type's
                 ;; action). Set on the plugin's config entry; the engine folds
                 ;; it into the spec the UI receives.
                [:hidden? {:optional true} boolean?]
                 ;; Overrides the UI's "Generate <label>" button text — useful
                 ;; when generating means something else (e.g. "Add character").
                [:generate-label {:optional true} string?]
                 ;; The store collections this type reads or writes. Defaults to
                 ;; a single collection named after :id. Under :browser storage
                 ;; the client ships exactly these with each request.
                [:store/collections {:optional true} [:sequential keyword?]]
                 ;; Declares a DM-owned table in the first of those collections:
                 ;; the UI renders a generic editor for it and the plugin reads
                 ;; it back through the store. See `::manual-state`.
                [:store/manual {:optional true} ::manual-state]
                 ;; Overrides the config's global `:history` for this type.
                [:history {:optional true} ::history]
                [:inputs {:optional true} [:sequential ::field]]]

   ;; --- manually-managed state (the `:store/manual` editor) ---
   ;; A collection the DM fills in by hand rather than one the app generates: a
   ;; character table, a soul ledger. Each key is a row, and `:fields` are the
   ;; columns it holds — coerced to their declared types on the way in, with a
   ;; blank falling back to `:default`. With `:list?` the row holds a *sequence*
   ;; of those field maps instead of one, for a key that owns several records.
   ::manual-state [:map
                   [:key-label {:optional true} string?]
                   [:list? {:optional true} boolean?]
                   [:fields [:sequential ::field]]]

   ;; --- view-model (the only contract the UI renderer understands) ---
   ;; `:action/event` is a UI action vector, dispatched as-is by the browser, so
   ;; only in-process plugins write one directly (see `sns.builtin.relics`) — they
   ;; are already trusted and know the `:id` they were registered under. Anything
   ;; crossing a boundary supplies `label`/`action`/`params` instead (the SDK's
   ;; `Models.Action`, or `::plugin-action` as JSON) and its adapter builds the
   ;; event, which both keeps a plugin to its own `:loot/action` route and fills
   ;; in the config-assigned id it has no way to know.
   ::action [:map
             [:action/label string?]
             [:action/event vector?]]

   ;; One resolved variable a template interpolates (see `sns.sdk.vars`).
   ;; Templates are rendered in the browser, so this is how a value reaches it:
   ;; separately from the text, which is what lets a DM edit *the value* without
   ;; touching the prose (and the plugin read a value back, never parsing prose).
   ;; `:options`, when given, is the preset's vocabulary — the UI offers it as a
   ;; combobox, matching `::field`'s `:enum` type. `:random`/`:args` record what
   ;; it was drawn from, so an action can reroll it (`sns.sdk.vars/redraw`).
   ;; `:label` is opt-in: with none, the UI derives one from the id it is keyed
   ;; under, so a plugin sets this only to override that.
   ::item-var [:map
               [:value any?]
               ;; The type the value resolved as: the browser picks its control
               ;; from this and parses the string an input hands back into it.
               ;; Stamped once at resolution, since mid-edit the value may be
               ;; blank and say nothing. Absent means text.
               [:type {:optional true} [:enum :int :decimal :bool]]
               [:label {:optional true} string?]
               [:random {:optional true} keyword?]
               [:args {:optional true} [:map-of keyword? any?]]
               [:options {:optional true} [:sequential any?]]
               ;; Available to templates but not offered for editing: an entry's
               ;; own fields, as opposed to what it declared as a var.
               [:context? {:optional true} boolean?]
               ;; How the var ranks up (see `sns.sdk.rank`). Ranks are 1-based,
               ;; so an absent `:rank` is the declared value itself. `:step`
               ;; defaults to that value; a var holding anything but a number
               ;; has nowhere to step to and never ranks.
               [:step {:optional true} any?]
               [:max {:optional true} [:int {:min 1}]]
               [:rank {:optional true} [:int {:min 1}]]]

   ;; Vars keyed by the name the template refers to them by: `{{ damage }}`
   ;; reads `:damage`. A map (not a list) because that is both the render
   ;; context's shape and the shape a plugin addresses a var by.
   ::item-vars [:map-of keyword? ::item-var]

   ;; `:item/title` and `:item/body` are *templates*, rendered in the browser
   ;; against `:item/vars`. A plugin with nothing to interpolate just sends
   ;; finished text — a template with no tags renders as itself.
   ::item [:map
           [:item/title {:optional true} [:maybe string?]]
           [:item/body string?]
           [:item/metadata {:optional true} [:sequential string?]]
           [:item/vars {:optional true} ::item-vars]]

   ::section [:map
              [:section/heading {:optional true} [:maybe string?]]
              [:section/items [:sequential ::item]]]

   ::view-model [:map
                 [:loot/title string?]
                 [:loot/subtitle {:optional true} [:maybe string?]]
                 ;; Context for the title/subtitle templates, as `:item/vars` is
                 ;; for an item's.
                 [:loot/vars {:optional true} ::item-vars]
                 [:loot/sections {:optional true} [:sequential ::section]]
                 [:loot/actions {:optional true} [:sequential ::action]]
                 ;; The anonymous handle this result travels under, drawn by the
                 ;; engine from the config's word list rather than by a plugin,
                 ;; so every surface that shows a result shows the same pair.
                 [:loot/words {:optional true} [:sequential string?]]
                 ;; Opaque, plugin-owned state the engine and UI carry untouched
                 ;; and hand back with the next action — bookkeeping (a stored
                 ;; id, the data entry a mod came from) that has no place in the
                 ;; rendered item. The *displayed* values live in `:item/vars`
                 ;; and are the source of truth for everything else, so keep
                 ;; this to what genuinely cannot be read back off the item.
                 [:loot/state {:optional true} any?]
                 ;; Writes the plugin wants made. Declared rather than performed,
                 ;; so the engine can apply them only once this view-model has
                 ;; validated — a call that fails changes nothing.
                 [:store/mutations {:optional true} ::mutations]]

   ;; `{<collection> {<key> <value>}}`; a nil value retracts that key, and keys
   ;; left out are untouched.
   ::mutations [:map-of keyword? [:map-of any? any?]]

   ;; --- external plugin I/O contract (:cli, :ffi, :wasm) ---
   ;; External plugins exchange the same view-model a `:jar` plugin does, as
   ;; JSON: namespaced keys survive intact (`"loot/title"` reads back as
   ;; `:loot/title`), so there is one output contract, not two. Only the request
   ;; is modelled here — the output *is* `::view-model`.

   ;; What the engine sends the plugin, as one of two shapes (never both): a
   ;; generate call carries `inputs`, an action call carries `action`/`params`
   ;; plus the `view-model` the UI had on screen (DM edits included, and the
   ;; carrier of `:loot/state`). Modelled as a union so codegen emits two request
   ;; types and the generate-vs-action split is structural. Not runtime-validated
   ;; — the engine produces it.
   ;; `state` carries the collections the plugin declared with
   ;; `:store/collections`/`:store/manual`, read for it by the engine — an
   ;; external plugin never touches the store itself, and writes back by
   ;; returning `store/mutations` on its view-model.
   ;; The view-model as an external plugin *writes* it — identical but for
   ;; `:loot/actions`, which carry the three fields of `::plugin-action` rather
   ;; than a built `:action/event` (see `::action`). Validated before mapping, so
   ;; a contract breach reports in the author's own keys; the engine asserts the
   ;; mapped result against `::view-model` regardless.
   ::plugin-action [:map
                    [:label string?]
                    [:action string?]
                    [:params {:optional true} [:map-of keyword? any?]]]

   ::plugin-output [:merge
                    ::view-model
                    [:map [:loot/actions {:optional true} [:sequential ::plugin-action]]]]

   ::plugin-request [:or
                     [:map
                      [:inputs [:map-of keyword? any?]]
                      [:state {:optional true} [:map-of keyword? [:map-of any? any?]]]]
                     [:map
                      [:action string?]
                      [:params {:optional true} [:map-of keyword? any?]]
                      [:view-model {:optional true} ::view-model]
                      [:state {:optional true} [:map-of keyword? [:map-of any? any?]]]]]

   ;; --- a mod that ranks up ---
   ;; The convention the built-ins use: `:vars` the template interpolates, each
   ;; carrying its own progression, and an optional `:max-ranks` capping what
   ;; the mod may spend across all of them. Nothing in the engine reads this — a
   ;; plugin owns its own mod shape — but a plugin that follows it gets
   ;; `sns.sdk.rank` for free.
   ;;
   ;; `:vars` are declared (see `sns.sdk.vars`) — a raw literal, or a
   ;; `{:random …}`/`{:literal …}` behaviour — and resolve to the `::item-vars`
   ;; the item carries.
   ::mod [:map
          [:vars {:optional true} [:map-of keyword? any?]]
          [:template {:optional true} string?]
          [:max-ranks {:optional true} [:int {:min 1}]]]

   ;; --- the :data plugin DSL ---
   ;; `:title`/`:body` are either a *field reference* (a keyword, naming a field
   ;; on the drawn element — as `:metadata` always was) or a *template* string,
   ;; rendered in the browser against the item's vars. A field reference is the
   ;; way to reach data that is itself a template: `:body :effect` hands the
   ;; browser the entry's own `"+4 {{ ability }}."` to render, rather than
   ;; rendering `"{{ effect }}"` here and losing the inner tag.
   ;;
   ;; `string?` leads the `:or`, and must: the JSON transformer decodes a string
   ;; into whichever branch matches first, so a keyword branch in front turns
   ;; every JSON template into a keyword — `"{{result}}"` becomes `:{{result}}`,
   ;; a reference to a field no entry has. JSON keeps its strings, and a bare
   ;; one naming a field on the entry is read as a reference there.
   ::data-item [:map
                [:title {:optional true} [:or string? keyword?]]
                [:body [:or string? keyword?]]
                [:metadata {:optional true} keyword?]]

   ::data-section [:map
                   [:heading {:optional true} string?]
                   [:each keyword?]
                   [:item ::data-item]]

   ::data-spec [:map
                [:label string?]
                [:utility? {:optional true} boolean?]
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
              ;; Backend-specific settings nest under the backend's own key.
              [:file {:optional true} [:map [:dir {:optional true} string?]]]]

   ;; Dispatch coerces `:type` to a keyword so a JSON config (where it is the
   ;; string "data" etc.) routes to the right branch during decoding.
   ;; `:hidden?` works on every plugin type — it is applied by the engine rather
   ;; than the generator, so even a compiled :jar plugin can be hidden.
   ::plugin [:multi {:dispatch (fn [p] (some-> (:type p) keyword))}
                 ;; Keys every plugin shares stay at the top level; the ones
                 ;; only its :type understands nest under that type's key.
                 ;; A :data plugin's spec comes either from a :source file or
                 ;; written :inline (which wins when both are given).
             [:data [:map
                     [:type [:= :data]]
                     [:id keyword?]
                     [:hidden? {:optional true} boolean?]
                     [:data [:and
                             [:map
                              [:source {:optional true} string?]
                              [:inline {:optional true} ::data-spec]]
                             [:fn {:error/message "a :data plugin needs a :source or an :inline spec"}
                              (fn [d] (boolean (or (:source d) (:inline d))))]]]]]
             [:cli [:map
                    [:type [:= :cli]]
                    [:id keyword?]
                    [:hidden? {:optional true} boolean?]
                    [:cli [:map [:command [:sequential string?]]]]
                    [:utility? {:optional true} boolean?]
                    [:history {:optional true} ::history]
                    [:label {:optional true} string?]
                    [:store/collections {:optional true} [:sequential keyword?]]
                    [:store/manual {:optional true} ::manual-state]
                        ;; An external plugin has no loot-spec of its own, so it
                        ;; declares its input fields here; the engine folds them
                        ;; into the spec and sends the collected values as
                        ;; `inputs`.
                    [:inputs {:optional true} [:sequential ::field]]]]
             ;; A :wasm plugin is a WASI command module run on GraalWASM,
             ;; sharing the stdio JSON contract with :cli. `:args` are its
             ;; program arguments and `:dirs` maps guest paths it may read to
             ;; host directories (none means no filesystem). Works in native
             ;; images (unlike :jar), in-process (unlike :cli).
             [:wasm [:map
                     [:type [:= :wasm]]
                     [:id keyword?]
                     [:hidden? {:optional true} boolean?]
                     [:wasm [:map
                             [:module string?]
                             [:args {:optional true} [:sequential string?]]
                             [:dirs {:optional true} [:map-of [:or keyword? string?] string?]]]]
                     [:utility? {:optional true} boolean?]
                     [:history {:optional true} ::history]
                     [:label {:optional true} string?]
                     [:store/collections {:optional true} [:sequential keyword?]]
                     [:store/manual {:optional true} ::manual-state]
                     [:inputs {:optional true} [:sequential ::field]]]]
                 ;; An :ffi plugin calls a C-ABI symbol in a shared library
                 ;; (.so/.dylib/.dll): `(char* request_json) -> char* output_json`.
                 ;; If `free-symbol` is given it is called on the returned pointer
                 ;; after the output is read; otherwise the library owns that
                 ;; memory. Works in native images (unlike :jar).
             [:ffi [:map
                    [:type [:= :ffi]]
                    [:id keyword?]
                    [:hidden? {:optional true} boolean?]
                    [:ffi [:map
                           [:library string?]
                           [:symbol string?]
                           [:free-symbol {:optional true} string?]]]
                    [:utility? {:optional true} boolean?]
                    [:history {:optional true} ::history]
                    [:label {:optional true} string?]
                    [:store/collections {:optional true} [:sequential keyword?]]
                    [:store/manual {:optional true} ::manual-state]
                    [:inputs {:optional true} [:sequential ::field]]]]
                 ;; A :jar plugin names its generator either as a Clojure
                 ;; :entrypoint factory var or as a :class with a 0-arity
                 ;; constructor (for pure-JVM-language plugins).
             [:jar [:map
                    [:type [:= :jar]]
                    [:id keyword?]
                    [:hidden? {:optional true} boolean?]
                    [:jar [:and
                           [:map
                            [:path string?]
                            [:entrypoint {:optional true} symbol?]
                            [:class {:optional true} string?]]
                           [:fn {:error/message "a :jar plugin needs an :entrypoint or a :class"}
                            (fn [j] (boolean (or (:entrypoint j) (:class j))))]]]]]
             [:builtin [:map
                        [:type [:= :builtin]]
                        [:id keyword?]
                        [:hidden? {:optional true} boolean?]
                        [:builtin {:optional true} [:map [:entrypoint {:optional true} symbol?]]]]]]

   ::loot-entry [:map [:id keyword?] [:weight {:optional true} number?]]

   ;; --- random presets (content for `sns.sdk.randoms`) ---
   ;; Named value lists any plugin's vars can draw from with
   ;; `{:random :<preset>}`. The library ships the mechanism only; these are
   ;; the DM's vocabulary.
   ::randoms [:map-of keyword? [:sequential any?]]

   ;; --- reporting (send a generated item to an external destination) ---
   ;; A `:multi` like `::plugin` so new backends slot in; dispatch coerces the
   ;; backend to a keyword for JSON configs.
   ::reporting [:multi {:dispatch (fn [r] (some-> (:backend r) keyword))}
                [:discord [:map
                           [:backend [:= :discord]]
                           [:discord [:map
                                      [:webhook-url string?]
                                      [:username {:optional true} string?]
                                      [:avatar-url {:optional true} string?]]]]]]

   ::config [:map
             [:server {:optional true} ::server]
             [:storage {:optional true} ::storage]
             [:plugins [:sequential ::plugin]]
             [:randoms {:optional true} ::randoms]
             [:words {:optional true} [:sequential string?]]
             [:extra-words {:optional true} [:sequential string?]]
             [:reporting {:optional true} ::reporting]
             [:history {:optional true} ::history]
             [:loot-table {:optional true} [:sequential ::loot-entry]]
             [:loot-die-size {:optional true} pos-int?]]})

(def registry
  "Combined registry: malli defaults + util schemas (for `:merge`) + ours."
  (merge (m/default-schemas) (mu/schemas) schemas))

(defn validate
  "True if `value` conforms to `schema` (a keyword from `schemas` or inline)."
  [schema value]
  (m/validate schema value {:registry registry}))

(defn decode
  "Decode `value` against `schema` with malli's JSON transformer, coercing
   strings to keywords/symbols/etc. Used to read a JSON config into the same
   shape an EDN config produces."
  [schema value]
  (m/decode schema value {:registry registry} (mt/transformer (mt/json-transformer) (mt/default-value-transformer))))

(defn explain
  "Explain why `value` does not conform to `schema`, or nil if it does."
  [schema value]
  (m/explain schema value {:registry registry}))

(defn assert!
  "Throw an ex-info describing the failure if `value` does not conform."
  [schema value]
  (when-let [err (explain schema value)]
    (throw (ex-info (str "Schema validation failed: " schema)
                    {:schema schema :error (me/humanize err)})))
  value)
