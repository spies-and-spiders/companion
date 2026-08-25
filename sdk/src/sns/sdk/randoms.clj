(ns sns.sdk.randoms
  "Named random vocabularies a loot type can draw a value from.

   Only the *mechanism* lives here — preset values are content, added with
   `defmethod preset`, whether by a plugin or by the app on the DM's behalf (it
   installs a method per `:randoms` entry in config.edn). Two generic presets
   are built in: `:literal` (values written inline, under `:options`) and
   `:without-replacement` (draw N distinct values from another preset).

   Presets take *named* arguments — the map the var spec was written as, minus
   its `:random` key — so a preset reads what it needs by name:

   ```clojure
   {:random :without-replacement :amount 2 :preset :skills}
   {:random :defences :type \"non-armour\"}
   ```

   Drawing happens here, on the server, because it needs the request's seeded
   rng and the presets registered against it. Rendering does not happen here at
   all: a drawn value travels to the UI as an `sns.sdk.schema/item-var` and is
   interpolated into the template there. See `sns.sdk.vars`."
  (:require
    [randy.core :as r]))

(def ^:dynamic *rng* r/default-rng)

(defmacro with-rng
  "Evaluate `body` with `*rng*` bound to `rng`."
  [rng & body]
  `(binding [*rng* ~rng]
     ~@body))

(defmulti preset
  "The values behind named `preset`, given the var spec's remaining keys as
   `args`. Returns either a collection to sample one value from, or a 0-arity
   fn producing the sampled value itself — the latter for presets that decide
   their own draw (see `:without-replacement`)."
  (fn [preset _args] preset))

(defn known-presets
  "Every preset name currently resolvable, for error messages and tooling."
  []
  (->> (keys (methods preset))
       (remove #{:default})
       sort
       vec))

(defmethod preset :default [k _]
  (throw (ex-info "Unknown random preset" {:preset k :known (known-presets)})))

;; Values written inline where the var is declared, rather than registered as a
;; named vocabulary: `{:random :literal :options ["harm" "damage"]}`.
(defmethod preset :literal [_ {:keys [options]}]
  (vec options))

(defn- ->long [n]
  (if (string? n) (parse-long n) (long n)))

;; Draws a *collection*, so a template indexes it. Handlebars needs brackets
;; around a numeric segment: `{{ x.[0] }}` and `{{ x.[1] }}`.
(defmethod preset :without-replacement [_ {:keys [amount] inner :preset :as args}]
  (let [inner  (keyword inner)
        values (preset inner (dissoc args :amount :preset))
        amount (->long amount)]
    (when (fn? values)
      (throw (ex-info "Cannot draw without replacement from a self-sampling preset"
                      {:preset inner})))
    #(r/sample-without-replacement (force *rng*) amount values)))

(defn- sample-values
  "Resolve `k`'s definition and realise it into one sampled value."
  [k args]
  (let [values (preset k args)]
    (if (fn? values)
      (values)
      (r/sample (force *rng*) values))))

(defn sample-preset
  "Sample one value from the preset named `preset-name` with named `args`,
   using `rng`."
  ([rng preset-name] (sample-preset rng preset-name {}))
  ([rng preset-name args]
   (with-rng rng
     (sample-values (keyword preset-name) args))))

(defn preset-values
  "The full vocabulary behind `preset-name`, or nil for a self-sampling preset
   (e.g. `:without-replacement`) that has none to offer. Becomes an
   `sns.sdk.schema/item-var`'s `:options`, so the UI can offer the same
   vocabulary the draw came from as a combobox."
  ([preset-name] (preset-values preset-name {}))
  ([preset-name args]
   (let [values (preset (keyword preset-name) args)]
     (when-not (fn? values) values))))

(defn draw [rng preset-name args]
  (let [values (preset (keyword preset-name) args)]
    (with-rng rng
      (if (fn? values)
        {:value (values)}
        {:value   (r/sample (force *rng*) values)
         :options values}))))
