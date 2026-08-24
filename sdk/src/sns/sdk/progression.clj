(ns sns.sdk.progression
  "The open op vocabulary of the upgrade-graph DSL (`sns.sdk.schema/option`).

   An upgrade option carries *mutation ops* that transform a mod's variables —
   the same `sns.sdk.schema/item-vars` its template interpolates, so levelling
   up and drawing a random touch one map rather than two. Ops address a var by
   id and change its `:value`; nothing here renders text.

   The built-in ops are `defmethod`s of `apply-op`, so a plugin adds to the
   vocabulary the same way the app defines it:

   ```clojure
   (defmethod sp/apply-op :multiply [acc _ m]
     (sp/update-values acc m *))
   ```

   Op names are unqualified, because they are written by hand in the upgrade
   graphs a DM authors as plain data. A plugin that wants to be certain it cannot
   clash with another's op may use a qualified keyword instead — at the cost of
   spelling it out in full wherever the graph is written. `:default` is reserved
   by the multimethod for the unknown-op error, so it is not usable as an op.

   Only the vocabulary is shared: the `Progression` that folds these ops over a
   path is handed to plugins on the request context, so a custom op works in
   ordinary upgrade graphs without a bespoke `Progression` implementation.

   `mod-item`/`options-at` are the two steps every plugin takes between a mod
   and the view-model, kept here so each one need not restate them."
  (:require
    [sns.sdk.protocols :as p]))

(defmulti apply-op
  "Apply mutation `op` with value `v` to the progression accumulator
   `{:vars … :template …}`, returning the updated accumulator."
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

(defn update-values
  "Combine each `{id v}` in `m` into the matching var's `:value` with `f`. The
   building block for the arithmetic ops, and for a plugin's own — a var the
   mod never declared is created, so an upgrade may introduce one."
  [acc m f]
  (update acc :vars
          (fn [vars]
            (reduce-kv (fn [vars id v]
                         (update-in vars [id :value] f v))
                       vars
                       m))))

(defn set-values
  "Set each `{id v}` in `m` as the matching var's `:value`."
  [acc m]
  (update-values acc m (fn [_old v] v)))

(defmethod apply-op :assoc-template [acc _ template]
  (assoc acc :template template))

(defmethod apply-op :inc [acc _ m]
  (update-values acc m (fnil + 0)))

(defmethod apply-op :dec [acc _ m]
  (update-values acc m (fnil - 0)))

(defmethod apply-op :append [acc _ m]
  (update-values acc m (fnil str "")))

(defmethod apply-op :conj [acc _ m]
  (update-values acc m (fnil conj [])))

(defmethod apply-op :enable [acc _ ks]
  (set-values acc (zipmap ks (repeat true))))

(defmethod apply-op :disable [acc _ ks]
  (set-values acc (zipmap ks (repeat false))))

;; --- mod -> view-model ---------------------------------------------------
;; A mod's `:path` defaults to `[]`, so a freshly drawn mod needs no special
;; case at the call site.

(defn options-at
  "The upgrade options available to `mod` as its next step, or nil at a
   terminal node."
  [progression mod]
  (:options (p/level-options progression mod (:path mod []))))

(defn mod-item
  "`mod` as an `sns.sdk.schema/item`: its active template as `:item/body`, and
   the vars progression derived for it as `:item/vars`. The browser renders one
   against the other, so nothing here produces text."
  [progression mod]
  (let [{:keys [template vars]} (p/current-state progression mod (:path mod []))]
    (cond-> {:item/body template}
            (seq vars) (assoc :item/vars vars))))
