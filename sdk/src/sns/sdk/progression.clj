(ns sns.sdk.progression
  "The open op vocabulary of the upgrade-graph DSL (`sns.sdk.schema/option`).

   An upgrade option carries *mutation ops* that transform a mod's structured
   state; the effect text is re-derived from the accumulated state, never edited
   in place. The built-in ops are `defmethod`s of `apply-op`, so a plugin adds to
   the vocabulary the same way the app defines it:

   ```clojure
   (defmethod sp/apply-op :multiply [acc _ m]
     (update acc :state #(merge-with * % m)))
   ```

   Op names are unqualified, because they are written by hand in the upgrade
   graphs a DM authors as plain data. A plugin that wants to be certain it cannot
   clash with another's op may use a qualified keyword instead — at the cost of
   spelling it out in full wherever the graph is written. `:default` is reserved
   by the multimethod for the unknown-op error, so it is not usable as an op.

   Only the vocabulary is shared: the `Progression` that folds these ops over a
   path is handed to plugins on the request context, so a custom op works in
   ordinary upgrade graphs without a bespoke `Progression` implementation."
  (:require
    [randy.rng :as rng]))

(defmulti apply-op
  "Apply mutation `op` with value `v` to the progression accumulator
   `{:state … :template …}`, returning the updated accumulator."
  (fn [_acc op _v] op))

(defn known-ops
  "Every op currently resolvable, for error messages and tooling."
  []
  (->> (keys (methods apply-op))
       (remove #{:default})
       sort
       vec))

(defmethod apply-op :default [_ op v]
  (throw (ex-info "Unknown upgrade op" {:op op :value v :known (known-ops)})))

(defmethod apply-op :assoc-template [acc _ template]
  (assoc acc :template template))

(defmethod apply-op :set [acc _ m]
  (update acc :state merge m))

;; Values rolled when the option was taken (persisted on the path step), applied
;; as an op so they slot into the same ordering as everything else.
(defmethod apply-op :rolled [acc _ m]
  (update acc :state merge m))

(defmethod apply-op :inc [acc _ m]
  (update acc :state #(merge-with + % m)))

(defmethod apply-op :dec [acc _ m]
  (update acc :state #(merge-with - % m)))

(defmethod apply-op :append [acc _ m]
  (update acc :state #(merge-with str % m)))

(defmethod apply-op :conj [acc _ m]
  (update acc :state (fn [state]
                       (reduce-kv (fn [state k v]
                                    (update state k (fnil conj []) v))
                                  state m))))

(defmethod apply-op :enable [acc _ ks]
  (update acc :state #(reduce (fn [state k] (assoc state k true)) % ks)))

(defmethod apply-op :disable [acc _ ks]
  (update acc :state #(reduce (fn [state k] (assoc state k false)) % ks)))

(defn roll-option
  "Build the persisted path-step for taking `option`, rolling any `:roll` specs
   with `rng` (inclusive bounds). The rolled values are recorded under `:rolled`
   so the resulting effect re-derives deterministically."
  [rng {:keys [id roll]}]
  (cond-> {:id id}
          roll (assoc :rolled (update-vals roll (fn [[lo hi]]
                                                  (rng/next-int rng lo (inc hi)))))))
