(ns sns.ui.template
  "Template rendering — the only place it happens. The backend sends templates
   unrendered alongside the `sns.sdk.schema/item-vars` they interpolate, so a
   var edited in the browser re-renders immediately, with no round trip.

   Handlebars is the language: `{{ name }}`, `{{ x.[0] }}` to index a drawn
   collection (a numeric segment needs the brackets — `{{ x.0 }}` is a parse
   error), `{{#if flag}}…{{/if}}` for a conditional."
  (:require
    ["handlebars" :as handlebars]
    ["@budibase/handlebars-helpers/lib/array" :as array-helpers]
    ["@budibase/handlebars-helpers/lib/collection" :as collection-helpers]
    ["@budibase/handlebars-helpers/lib/comparison" :as comparison-helpers]
    ["@budibase/handlebars-helpers/lib/inflection" :as inflection-helpers]
    ["@budibase/handlebars-helpers/lib/math" :as math-helpers]
    ["@budibase/handlebars-helpers/lib/misc" :as misc-helpers]
    ["@budibase/handlebars-helpers/lib/number" :as number-helpers]
    ["@budibase/handlebars-helpers/lib/object" :as object-helpers]
    ["@budibase/handlebars-helpers/lib/regex" :as regex-helpers]
    ["@budibase/handlebars-helpers/lib/string" :as string-helpers]))

;; handlebars-helpers (the @budibase fork — upstream 0.10.0 lazy-requires its
;; deps in a way shadow-cljs cannot bundle), minus the groups that need node
;; (fs, path, url) or are dead weight in a loot template (code, i18n, html,
;; match, lorem, uuid).
(doseq [group [array-helpers collection-helpers comparison-helpers inflection-helpers
               math-helpers misc-helpers number-helpers object-helpers regex-helpers
               string-helpers]]
  (handlebars/registerHelper group))

;; Handlebars caches compilation on the function it returns, so compiling per
;; call throws that away — measured ~70x the cost of rendering an already
;; compiled template. `render` runs on every replicant re-render, for every
;; field of every item, so memoise on the template string. Unbounded is fine:
;; the editor renders `result-editor`, not `result`, so half-typed templates
;; never reach here — only finished ones do.
(def ^:private compile-template
  (memoize #(handlebars/compile % #js {:noEscape true})))

(defn- context
  "Resolved vars flattened to the `{name value}` a template is rendered
   against, JS-ified for Handlebars."
  [vars]
  (clj->js (reduce-kv (fn [acc id {:keys [value]}] (assoc acc id value)) {} vars)))

(defn render
  "Render `template` against `vars`. A template with no tags is itself, so a
   plugin that sends finished text needs no vars at all; a nil template (an
   absent title) stays nil.

   A template that fails to compile is shown as-is rather than blanking the
   item — a DM editing one mid-keystroke routinely passes through invalid
   states (`{{ ab`), and losing the text they are typing would be worse than
   showing it."
  [template vars]
  (if-not (string? template)
    template
    (try
      ((compile-template template) (context vars))
      (catch :default _ template))))

(defn render-view-model
  "A view-model with every template resolved against its vars — for a consumer
   that wants finished text rather than a template (currently: reporting,
   since the backend has no renderer to fall back on).

   Knowing *which* fields are templates lives here rather than at the call
   site, so `sns.ui.render` and the report path cannot drift apart."
  [{:loot/keys [vars] :as vm}]
  (letfn [(item [i]
            ;; Loot vars are ambient, an item's own shadow them - as in
            ;; `sns.ui.render/entry`.
            (let [vars (merge vars (:item/vars i))]
              (-> i
                  (update :item/body render vars)
                  (update :item/title render vars))))]
    (-> vm
        (update :loot/title render vars)
        (update :loot/subtitle render vars)
        (update :loot/sections
                (partial mapv #(update % :section/items (partial mapv item)))))))
