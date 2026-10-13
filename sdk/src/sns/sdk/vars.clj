(ns sns.sdk.vars
  "Template variables, resolved here and rendered in the browser. A declared
   var takes one of three forms:

   ```clojure
   {:ability  \"Wisdom\"                                  ; a raw literal
    :damage   {:random :damage-types}                     ; drawn from a preset
    :awkward  {:literal {:random :not-a-preset}}}         ; escaped: kept verbatim
   ```

   Resolving turns them into `sns.sdk.schema/item-var`s:

   ```clojure
   {:damage {:value \"fire\" :random :damage-types :options [\"fire\" \"cold\"]}
    :dice   {:value 2 :type :int}}
   ```"
  (:require
    [clojure.string :as str]
    [sns.sdk.randoms :as randoms]))

(defn humanise-label
  "`:crit-damage` -> \"Crit damage\""
  [id]
  (-> (name id)
      (str/replace #"[-_]" " ")
      str/capitalize))

(defn- resolved? [spec]
  (and (map? spec) (contains? spec :value)))

(defn- typed
  "Record the value's type, so an edited var comes back as the type the plugin
   sent."
  [v]
  (if-let [t (let [value (:value v)]
               (cond (boolean? value) :bool
                     (int? value)     :int
                     (number? value)  :decimal))]
    (assoc v :type t)
    v))

(def ^:private rank-keys
  "Kept out of a preset's draw arguments."
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
  "Resolve one declared var into an `sns.sdk.schema/item-var`. Idempotent, so a
   round-tripped view-model keeps its values."
  [rng _id spec]
  (if (resolved? spec)
    spec
    (typed (fresh rng spec))))

(defn resolve-vars
  "Resolve a map of declared vars, keyed by id."
  [rng vars]
  (when (seq vars)
    (reduce-kv (fn [acc id spec] (assoc acc id (resolve-var rng id spec)))
               {}
               vars)))

(defn- resolve-vars-at [rng m k]
  (cond-> m
          (seq (get m k)) (update k (partial resolve-vars rng))))

(defn resolve-view-model
  "Resolve the declared vars across a view-model, so a plugin can name a preset
   rather than draw the value itself."
  [rng vm]
  (cond-> (resolve-vars-at rng vm :loot/vars)
          (seq (:loot/sections vm))
          (update :loot/sections
                  (partial mapv #(update % :section/items
                                         (partial mapv (fn [item] (resolve-vars-at rng item :item/vars))))))))

(defn redraw
  "Re-draw `id`'s value from its preset, if it has one."
  [rng vars id]
  (if-let [preset-name (get-in vars [id :random])]
    (assoc-in vars [id :value] (:value (randoms/draw rng preset-name (get-in vars [id :args] {}))))
    vars))

(defn redraw-distinct
  "`redraw` until the value changes, giving up after `tries` (default 100)."
  ([rng vars id] (redraw-distinct rng vars id 100))
  ([rng vars id tries]
   (let [current (get-in vars [id :value])]
     (loop [n tries
            next-vars (redraw rng vars id)]
       (if (or (zero? n) (not= current (get-in next-vars [id :value])))
         next-vars
         (recur (dec n) (redraw rng vars id)))))))
