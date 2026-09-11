(ns sns.sdk.vars
  "Item variables: the values a template interpolates, resolved here and
   rendered in the browser.

   A var is *declared* as data — in a mod's `:vars`, or a `:data` entry's
   `:item/vars` — and takes one of three forms:

   ```clojure
   {:ability  \"Wisdom\"                                  ; a raw literal
    :damage   {:random :damage-types}                     ; drawn from a preset
    :awkward  {:literal {:random :not-a-preset}}}         ; escaped: kept verbatim
   ```

   A map is read as a *behaviour* when it carries `:random` or `:literal` at
   the top level; `:literal` exists precisely so data that happens to look like
   a behaviour can say so. Every other value — number, string, boolean, vector,
   map — is itself.

   Resolving turns declarations into `sns.sdk.schema/item-var`s, keyed by id:

   ```clojure
   {:damage {:value \"fire\" :random :damage-types :options [\"fire\" \"cold\"]}
    :dice   {:value 2 :type :int}}
   ```

   `:options` is the preset's vocabulary, so the UI edits the value as a
   combobox over the same words it was drawn from; `:type` is what it must
   still be after that edit. Flattening the map back to `{:damage \"fire\"}` —
   the shape a template renders against — is the browser's job."
  (:require
    [clojure.string :as str]
    [sns.sdk.randoms :as randoms]))

(defn humanise-label
  "`:crit-damage` -> \"Crit damage\", for a display string the *server*
   composes — an action's label, say. A var's own label is not computed here:
   the browser derives it from the id, so nothing is sent that the client
   already knows."
  [id]
  (-> (name id)
      (str/replace #"[-_]" " ")
      str/capitalize))

(defn- resolved? [spec]
  (and (map? spec) (contains? spec :value)))

(defn- typed
  "Record the type the value resolved as, so an edited var comes back as the
   number or boolean the plugin sent."
  [v]
  (if-let [t (let [value (:value v)]
               (cond (boolean? value) :bool
                     (int? value)     :int
                     (number? value)  :decimal))]
    (assoc v :type t)
    v))

(def ^:private rank-keys
  "Keys that describe how a var ranks up, not how it is drawn. Held aside while
   a `{:random ...}` behaviour is resolved so they reach the var itself rather
   than the preset's draw arguments."
  [:step :max :rank])

(defn- fresh [rng spec]
  (cond
    (not (map? spec))         {:value spec}
    (contains? spec :literal) {:value (:literal spec)}
    (contains? spec :random)  (let [{preset-name :random :as args} (apply dissoc spec rank-keys)
                                    args (dissoc args :random)]
                                (merge (cond-> (assoc (randoms/draw rng preset-name args)
                                                      :random preset-name)
                                               (seq args) (assoc :args args))
                                       (select-keys spec rank-keys)))
    :else                     {:value spec}))

(defn resolve-var
  "Resolve one declared var into an `sns.sdk.schema/item-var`. Idempotent:
   resolving an already-resolved var returns it unchanged, so a round-tripped
   view-model keeps the values, types and ranks it arrived with.

   `id` is unused beyond documenting the call site — a var carries the label a
   plugin sets, and the UI derives one from the key otherwise."
  [rng _id spec]
  (if (resolved? spec)
    spec
    (typed (fresh rng spec))))

(defn resolve-vars
  "Resolve a map of declared vars, keyed by id. Nil in, nil out."
  [rng vars]
  (when (seq vars)
    (reduce-kv (fn [acc id spec] (assoc acc id (resolve-var rng id spec)))
               {}
               vars)))

(defn- resolve-vars-at
  "Resolve `m`'s vars under `k`, leaving `m` alone when it declares none — an
   absent key must stay absent rather than become nil."
  [rng m k]
  (cond-> m
          (seq (get m k)) (update k (partial resolve-vars rng))))

(defn resolve-view-model
  "Draw the `{:random ...}` declarations a view-model carries, at the loot level
   and in each item, so a plugin can name a preset instead of drawing the value
   itself. Idempotent, so finished `{:value ...}` vars are untouched."
  [rng vm]
  (cond-> (resolve-vars-at rng vm :loot/vars)
          (seq (:loot/sections vm))
          (update :loot/sections
                  (partial mapv #(update % :section/items
                                         (partial mapv (fn [item] (resolve-vars-at rng item :item/vars))))))))

(defn redraw
  "Re-draw `id`'s value, for an action that deliberately rerolls a var. A var
   that wasn't drawn from a preset has nothing to redraw and is left alone."
  [rng vars id]
  (if-let [preset-name (get-in vars [id :random])]
    (assoc-in vars [id :value] (:value (randoms/draw rng preset-name (get-in vars [id :args] {}))))
    vars))

(defn redraw-distinct
  "`redraw`, but keep drawing until the value differs from the current one —
   for a reroll that must visibly change something. Gives up after `tries`
   (default 100) so a single-value preset can't spin forever."
  ([rng vars id] (redraw-distinct rng vars id 100))
  ([rng vars id tries]
   (let [current (get-in vars [id :value])]
     (loop [n tries
            next-vars (redraw rng vars id)]
       (if (or (zero? n) (not= current (get-in next-vars [id :value])))
         next-vars
         (recur (dec n) (redraw rng vars id)))))))
