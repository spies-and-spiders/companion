(ns sns.sdk.schema
  "Malli schemas shared across the backend, plugins, and (the view-model/spec
   subset) the ClojureScript frontend. All schemas live in one registry so they
   can reference each other, including the recursive upgrade-graph."
  (:require
    [malli.core :as m]
    [malli.error :as me]
    [malli.transform :as mt]
    [malli.util :as mu]))

(def ^:private op-entries
  "The mutation entries an upgrade option may carry. Shared by `::op` (a bare
   op) and `::option` (an op plus identity/recursion) to avoid `:merge`, which
   expands eagerly and cannot express the recursive graph. These are the ops
   `sns.sdk.progression` defines; a plugin's own op is an extra key the open
   `:map` admits."
  [[:inc {:optional true} [:map-of keyword? number?]]
   [:dec {:optional true} [:map-of keyword? number?]]
   [:append {:optional true} [:map-of keyword? string?]]
   [:conj {:optional true} [:map-of keyword? any?]]
   [:assoc-template {:optional true} string?]
   [:enable {:optional true} [:sequential keyword?]]
   [:disable {:optional true} [:sequential keyword?]]])

(def schemas
  "The project's named schemas, keyed by qualified keyword."
  {;; --- loot specifications (drive the generic input forms) ---
   ::field [:map
            [:id keyword?]
            [:label string?]
            [:type [:enum :enum :int :text :bool]]
            [:default {:optional true} any?]
            [:options {:optional true} [:sequential any?]]]

   ::loot-spec [:map
                [:id keyword?]
                [:label string?]
                [:stateful? {:optional true} boolean?]
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
                [:inputs {:optional true} [:sequential ::field]]]

   ;; --- view-model (the only contract the UI renderer understands) ---
   ::action [:map
             [:action/label string?]
             [:action/event vector?]]

   ::item [:map
           [:item/title {:optional true} [:maybe string?]]
           [:item/body string?]
           [:item/metadata {:optional true} [:sequential string?]]
           [:item/change {:optional true} any?]]

   ::section [:map
              [:section/heading {:optional true} [:maybe string?]]
              [:section/items [:sequential ::item]]]

   ::view-model [:map
                 [:loot/title string?]
                 [:loot/subtitle {:optional true} [:maybe string?]]
                 [:loot/sections {:optional true} [:sequential ::section]]
                 [:loot/actions {:optional true} [:sequential ::action]]]

   ;; --- upgrade-graph DSL (mod state + progression) ---
   ;; `::option` and `::upgrades` are mutually recursive, so the recursive edges
   ;; use lazy `[:ref ...]` rather than bare keyword children.
   ::op (into [:map] op-entries)

   ::option (into [:map
                   [:id keyword?]
                   [:repeatable {:optional true :default true} [:or boolean? [:int {:min 1}]]]
                   [:upgrades {:optional true} [:ref ::upgrades]]]
                  op-entries)

   ::upgrades [:map
               [:select [:enum :choice :random :all]]
               [:options [:sequential [:ref ::option]]]]

   ::mod [:map
          [:state {:optional true} [:map-of keyword? any?]]
          [:template string?]
          [:upgrades {:optional true} [:ref ::upgrades]]]

   ;; --- a persisted progression step ---
   ::path-step [:map
                [:id keyword?]]
   ::path      [:sequential ::path-step]

   ;; --- the :data plugin DSL ---
   ::data-item [:map
                [:title {:optional true} string?]
                [:body string?]
                [:metadata {:optional true} keyword?]]

   ::data-section [:map
                   [:heading {:optional true} string?]
                   [:each keyword?]
                   [:item ::data-item]]

   ::data-spec [:map
                [:label string?]
                [:utility? {:optional true} boolean?]
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
              [:backend keyword?]
              [:url {:optional true} string?]
              [:dir {:optional true} string?]]

   ;; Dispatch coerces `:type` to a keyword so a JSON config (where it is the
   ;; string "data" etc.) routes to the right branch during decoding.
   ;; `:hidden?` works on every plugin type — it is applied by the engine rather
   ;; than the generator, so even a compiled :jar plugin can be hidden.
   ::plugin [:multi {:dispatch (fn [p] (some-> (:type p) keyword))}
                 ;; A :data plugin's spec comes either from a :source file or
                 ;; written :inline (which wins when both are given).
             [:data [:and
                     [:map
                      [:type [:= :data]]
                      [:id keyword?]
                      [:hidden? {:optional true} boolean?]
                      [:source {:optional true} string?]
                      [:inline {:optional true} ::data-spec]]
                     [:fn {:error/message "a :data plugin needs a :source or an :inline spec"}
                      (fn [p] (boolean (or (:source p) (:inline p))))]]]
             [:cli [:map
                    [:type [:= :cli]]
                    [:id keyword?]
                    [:hidden? {:optional true} boolean?]
                    [:command [:sequential string?]]
                    [:utility? {:optional true} boolean?]]]
                 ;; A :jar plugin names its generator either as a Clojure
                 ;; :entrypoint factory var or as a :class with a 0-arity
                 ;; constructor (for pure-JVM-language plugins).
             [:jar [:and
                    [:map
                     [:type [:= :jar]]
                     [:id keyword?]
                     [:hidden? {:optional true} boolean?]
                     [:jar string?]
                     [:entrypoint {:optional true} symbol?]
                     [:class {:optional true} string?]]
                    [:fn {:error/message "a :jar plugin needs an :entrypoint or a :class"}
                     (fn [p] (boolean (or (:entrypoint p) (:class p))))]]]
             [:builtin [:map
                        [:type [:= :builtin]]
                        [:id keyword?]
                        [:hidden? {:optional true} boolean?]
                        [:entrypoint {:optional true} symbol?]]]]

   ::loot-entry [:map [:id keyword?] [:weight {:optional true} number?]]

   ;; --- random presets (content for `sns.sdk.randoms`) ---
   ;; Named value lists any plugin's templates can draw from with
   ;; `{{ ""|random:<preset> }}`. The library ships the mechanism only; these
   ;; are the DM's vocabulary.
   ::randoms [:map-of keyword? [:sequential any?]]

   ;; --- reporting (send a generated item to an external destination) ---
   ;; A `:multi` like `::plugin` so new backends slot in; dispatch coerces the
   ;; backend to a keyword for JSON configs.
   ::reporting [:multi {:dispatch (fn [r] (some-> (:backend r) keyword))}
                [:discord [:map
                           [:backend [:= :discord]]
                           [:webhook-url string?]
                           [:discord-username {:optional true} string?]
                           [:avatar-url {:optional true} string?]]]]

   ::config [:map
             [:server {:optional true} ::server]
             [:storage {:optional true} ::storage]
             [:plugins [:sequential ::plugin]]
             [:randoms {:optional true} ::randoms]
             [:reporting {:optional true} ::reporting]
             [:loot-table {:optional true} [:sequential ::loot-entry]]]})

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
