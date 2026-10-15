(ns sns.ui.template
  "Renders the Handlebars templates the backend sends, so edited vars re-render
   without a round trip. Index collections as `{{ x.[0] }}`, since `{{ x.0 }}`
   doesn't parse."
  (:require
    ["@budibase/handlebars-helpers/lib/array" :as array-helpers]
    ["@budibase/handlebars-helpers/lib/collection" :as collection-helpers]
    ["@budibase/handlebars-helpers/lib/comparison" :as comparison-helpers]
    ["@budibase/handlebars-helpers/lib/inflection" :as inflection-helpers]
    ["@budibase/handlebars-helpers/lib/math" :as math-helpers]
    ["@budibase/handlebars-helpers/lib/misc" :as misc-helpers]
    ["@budibase/handlebars-helpers/lib/number" :as number-helpers]
    ["@budibase/handlebars-helpers/lib/object" :as object-helpers]
    ["@budibase/handlebars-helpers/lib/regex" :as regex-helpers]
    ["@budibase/handlebars-helpers/lib/string" :as string-helpers]
    ["handlebars" :as handlebars]
    [clojure.string :as str]
    [sns.sdk.rank :as rank]
    [sns.ui.dice :as dice]))

;; The @budibase fork, since upstream's lazy requires can't be bundled by
;; shadow-cljs. Groups needing node, or useless in loot, are left out.
(doseq [group [array-helpers collection-helpers comparison-helpers inflection-helpers
               math-helpers misc-helpers number-helpers object-helpers regex-helpers
               string-helpers]]
  (handlebars/registerHelper group))

(defn- spies-link
  "A helper rendering a markdown link to a spies.tools `page` entry:
   `{{sns-spell \"Earth Tremor\"}}` or `{{#sns-spell}}Earth Tremor{{/sns-spell}}`.
   A second argument, or a block body alongside the name, is the link text."
  [page]
  (fn [& args]
    (this-as ctx
             (let [^js options (last args)
                   [entry display] (butlast args)
                   body (when (.-fn options) (.fn options ctx))
                   text (or display body entry)
                   slug (-> (str/lower-case (or entry body))
                            js/encodeURIComponent
                            (str/replace "(" "%28")
                            (str/replace ")" "%29"))]
               (str "[" text "](https://spies.tools/" page ".html#" slug "_sns)")))))

(handlebars/registerHelper "die-size" (fn [x _options] (dice/die-size x)))

(handlebars/registerHelper "sns-spell" (spies-link "spells"))
(handlebars/registerHelper "sns-maneuver" (spies-link "maneuvers"))
(handlebars/registerHelper "sns-condition" (spies-link "conditions"))

;; Compiling is ~70x the cost of rendering, and `render` runs for every field on
;; every re-render. Bounded, since editing caches every keystroke.
;; ponytail: dropped wholesale when full, an LRU if the refill ever shows.
(def ^:private cache-limit 1000)

(defonce ^:private compiled (atom {}))

(defn- compile-template [template]
  (or (get @compiled template)
      (let [f (handlebars/compile template #js {:noEscape true})]
        (swap! compiled #(assoc (if (< (count %) cache-limit) % {}) template f))
        f)))

(defn- context
  "Vars flattened to `{name value}` at their ranked values, for Handlebars."
  [vars]
  (clj->js (reduce-kv (fn [acc id v] (assoc acc id (rank/stepped v))) {} vars)))

(defn render
  "Render `template` against `vars`. An invalid template, like one being typed,
   is returned as is."
  [template vars]
  (if-not (string? template)
    template
    (try
      ((compile-template template) (context vars))
      (catch :default _ template))))

(defn- blank-item? [{:item/keys [body]}]
  (str/blank? body))

(defn render-view-model
  "`vm` with every template rendered and blank items dropped, as the card shows
   it."
  [{:loot/keys [vars] :as vm}]
  (letfn [(item [i]
            (let [vars (merge vars (:item/vars i))]
              (cond-> (-> i
                          (update :item/body render vars)
                          (update :item/title render vars))
                      (:item/metadata i) (update :item/metadata (partial mapv #(render % vars))))))]
    (-> vm
        (update :loot/title render vars)
        (update :loot/subtitle render vars)
        (update :loot/sections
                (fn [sections]
                  (into []
                        (keep (fn [section]
                                (let [items (into [] (comp (map item) (remove blank-item?))
                                                  (:section/items section))]
                                  (when (seq items)
                                    (cond-> (assoc section :section/items items)
                                            (:section/heading section) (update :section/heading render vars))))))
                        sections))))))
