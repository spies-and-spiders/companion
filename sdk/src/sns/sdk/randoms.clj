(ns sns.sdk.randoms
  "Randomness available to templates: `{{ x|random:<preset>:<args…> }}` samples a
   named preset while an effect is being rendered, and binds the draw to a
   variable (`x` by convention) rather than inlining it. The filter only draws
   when the piped variable is nil — a template can `{% with %}`-bind it once and
   reuse the same draw at every later use site, and a caller that persists the
   drawn value back onto its state (e.g. so a DM can edit it) gets that same
   value echoed back on every subsequent render instead of a fresh roll.

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

(def ^:dynamic *rng* r/default-rng)

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
    #(r/sample-without-replacement (force *rng*) amount values)))

(defn- sample-values
  "Resolve `k`'s definition and realise it into one sampled value."
  [k args]
  (let [values (preset k args)]
    (if (fn? values)
      (values)
      (r/sample (force *rng*) values))))

(defn sample-preset
  "Sample one value from the preset named `preset-name`, using `rng`. The
   programmatic entry point — templates use the `random` filter instead."
  [rng preset-name & args]
  (with-rng rng
    (sample-values (keyword preset-name) args)))

(defn preset-values
  "The full vocabulary behind `preset-name`, or nil for a self-sampling preset
   (e.g. `:without-replacement`) that has none to offer. For a plugin (typically
   `:builtin`/`:jar`, which builds its own view-model) pairing a draw with the
   `:options` of an `sns.sdk.schema/item-var`, so the DM can edit the value
   in the UI as a combobox over the same vocabulary the draw came from —
   `{:id :x :value (sample-preset rng :feats) :options (preset-values :feats)}`."
  [preset-name & args]
  (let [values (preset (keyword preset-name) args)]
    (when-not (fn? values) values)))

(defn- random-filter
  "`{{ x|random:preset-name:arg1:arg2 }}` — the piped value is the variable's
   current value (`x` by convention, but any name), and the first filter argument
   names the preset; the rest are passed to it. A nil piped value (the variable
   was never bound, e.g. this is its first draw) samples a fresh value; a non-nil
   one — already bound by an enclosing `{% with %}`, or supplied via the render
   context by a caller reusing a previously-persisted draw — is returned
   unchanged, so the same value is echoed rather than re-rolled. The drawn value
   is returned as-is (Selmer stringifies it on output), so a `{% with %}` binding
   can still index into a preset that draws a collection."
  [v preset-name & args]
  (if (some? v)
    v
    (sample-values (keyword preset-name) args)))

(selmer/add-filter! :random random-filter)
