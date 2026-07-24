(ns sns.sdk.randoms
  "Randomness available to templates: `{{ \"\"|random:<preset>:<args…> }}` samples
   a named preset while an effect is being rendered.

   Only the *mechanism* lives here — preset values are content, added with
   `defmethod preset`, whether by a plugin or by the app on the DM's behalf (it
   installs a method per `:randoms` entry in config.edn). Two generic presets are
   built in: `:literal` (values written inline in the template) and
   `:without-replacement` (draw N distinct values from another preset).

   Requiring this namespace registers the Selmer `random` filter, so a plugin
   rendering its own templates gets it by depending on the SDK alone."
  (:require
    [randy.core :as r]
    [selmer.parser :as selmer]))

(def ^:dynamic *rng* @r/default-rng)

(defmacro with-rng
  "Evaluate `body` with `*rng*` bound to `rng`."
  [rng & body]
  `(binding [*rng* ~rng]
     ~@body))

(defmulti preset
  "The values behind named `preset`, given the remaining template `args` (always
   strings when they come from a template). Returns either a collection to
   sample one value from, or a 0-arity fn producing the sampled value itself —
   the latter for presets that decide their own draw (see
   `:without-replacement`)."
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

(defmethod preset :literal [_ values]
  (vec values))

(defn- ->long [n]
  (if (string? n) (parse-long n) (long n)))

;; Returns the drawn values as a vector rather than one string, so a template
;; can bind and index them: `{% with x=x|random:without-replacement:2:skills %}
;; {{x.0}} and {{x.1}}{% endwith %}`. Rendered on its own it prints as a vector.
(defmethod preset :without-replacement [_ [amount preset-name & args]]
  (let [values (preset (keyword preset-name) args)
        amount (->long amount)]
    (when (fn? values)
      (throw (ex-info "Cannot draw without replacement from a self-sampling preset"
                      {:preset (keyword preset-name)})))
    #(r/sample-without-replacement *rng* amount values)))

(defn- sample-values
  "Resolve `k`'s definition and realise it into one sampled value."
  [k args]
  (let [values (preset k args)]
    (if (fn? values)
      (values)
      (r/sample *rng* values))))

(defn sample-preset
  "Sample one value from the preset named `preset-name`, using `rng`. The
   programmatic entry point — templates use the `random` filter instead."
  [rng preset-name & args]
  (with-rng rng
    (sample-values (keyword preset-name) args)))

(defn- random-filter
  "`{{ \"\"|random:preset-name:arg1:arg2 }}` — the piped value is ignored; the first filter
   argument names the preset and the rest are passed to it. The drawn value is
   returned as-is (Selmer stringifies it on output), so a `{% with %}` binding
   can still index into a preset that draws a collection."
  [_ preset-name & args]
  (sample-values (keyword preset-name) args))

(selmer/add-filter! :random random-filter)
