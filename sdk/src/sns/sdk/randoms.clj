(ns sns.sdk.randoms
  "Named random vocabularies that vars draw from. Presets are added with
   `defmethod preset`, by plugins or from config's `:randoms`, and take the var
   spec minus `:random` as named arguments:

   ```clojure
   {:random :without-replacement :amount 2 :preset :skills}
   {:random :defences :type \"non-armour\"}
   ```"
  (:require
    [randy.core :as r]))

(def ^:dynamic *rng* r/default-rng)

(defmacro with-rng
  "Evaluate `body` with `*rng*` bound to `rng`."
  [rng & body]
  `(binding [*rng* ~rng]
     ~@body))

(defmulti preset
  "The values behind `preset`: a collection to sample from, or a 0-arity fn for
   presets that draw for themselves (see `:without-replacement`)."
  (fn [preset _args] preset))

(defn known-presets []
  (->> (keys (methods preset))
       (remove #{:default})
       sort
       vec))

(defmethod preset :default [k _]
  (throw (ex-info "Unknown random preset" {:preset k :known (known-presets)})))

;; `{:random :literal :options ["harm" "damage"]}`
(defmethod preset :literal [_ {:keys [options]}]
  (vec options))

(defn- ->long [n]
  (if (string? n) (parse-long n) (long n)))

;; Draws a collection, which templates index as `{{ x.[0] }}`.
(defmethod preset :without-replacement [_ {:keys [amount] inner :preset :as args}]
  (let [inner  (keyword inner)
        values (preset inner (dissoc args :amount :preset))
        amount (->long amount)]
    (when (fn? values)
      (throw (ex-info "Cannot draw without replacement from a self-sampling preset"
                      {:preset inner})))
    #(r/sample-without-replacement (force *rng*) amount values)))

(defn- sample-values [k args]
  (let [values (preset k args)]
    (if (fn? values)
      (values)
      (r/sample (force *rng*) values))))

(defn sample-preset
  "Sample one value from `preset-name` with `args`."
  ([rng preset-name] (sample-preset rng preset-name {}))
  ([rng preset-name args]
   (with-rng rng
     (sample-values (keyword preset-name) args))))

(defn preset-values
  "The full vocabulary behind `preset-name`, or nil for a self-sampling preset."
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
