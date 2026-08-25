(ns sns.sdk.progression
  "The open op vocabulary of the upgrade-graph DSL (`sns.sdk.schema/option`).

   An upgrade option carries *mutation ops* that transform a mod's variables —
   the same `sns.sdk.schema/item-vars` its template interpolates, so levelling
   up and drawing a random touch one map rather than two. Ops take vars and
   return vars; text that varies with an upgrade is a `{{#if flag}}` in the
   template, switched by `:enable`/`:disable`.

   The built-in ops are `defmethod`s of `apply-op`, so a plugin adds to the
   vocabulary the same way the app defines it:

   ```clojure
   (defmethod sp/apply-op :multiply [vars _ m]
     (sp/update-values vars m *))
   ```

   Op names are unqualified, because they are written by hand in the upgrade
   graphs a DM authors as plain data. A plugin that wants to be certain it cannot
   clash with another's op may use a qualified keyword instead — at the cost of
   spelling it out in full wherever the graph is written. `:default` is reserved
   by the multimethod for the unknown-op error, so it is not usable as an op.

   Only the vocabulary is shared: the `Progression` that folds these ops over a
   path is handed to plugins on the request context, so a custom op works in
   ordinary upgrade graphs without a bespoke `Progression` implementation.

   Everything here takes vars and returns vars; a plugin owns its own mod shape,
   and the step from a mod to a view-model item. There are two ways to keep one
   levelling, and mixing them double-counts every upgrade:

   - *the path is the state* — keep the **declared** vars and the path, and
     re-derive with `sns.sdk.protocols/current-state` on each render. The mod is
     reproducible from what was persisted, and a DM's edit to a value lasts only
     until the next derivation.
   - *the vars are the state* — keep the **resolved** vars and move them one
     upgrade at a time with `apply-ops` as each is chosen. This is what a plugin
     that reads its item back off the view-model wants, since the displayed
     value is then what the next upgrade builds on."
  (:require
    [sns.sdk.vars :as vars]))

(defmulti apply-op
  "Apply mutation `op` with value `v` to `vars` (`sns.sdk.schema/item-vars`),
   returning the updated vars."
  (fn [_vars op _v] op))

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
  "Combine each `{id v}` in `m` into the matching var's `:value` with `f`. A var
   the mod never declared is created, so an upgrade may introduce one."
  [vars m f]
  (reduce-kv (fn [vars id v] (update-in vars [id :value] f v))
             vars
             m))

(defn set-values
  "Set each `{id v}` in `m` as the matching var's `:value`."
  [vars m]
  (update-values vars m (fn [_old v] v)))

(defmethod apply-op :inc [vars _ m]
  (update-values vars m (fnil + 0)))

(defmethod apply-op :dec [vars _ m]
  (update-values vars m (fnil - 0)))

(defmethod apply-op :conj [vars _ m]
  (update-values vars m (fnil conj [])))

(defmethod apply-op :enable [vars _ ks]
  (set-values vars (zipmap ks (repeat true))))

(defmethod apply-op :disable [vars _ ks]
  (set-values vars (zipmap ks (repeat false))))

;; --- applying one option's ops -------------------------------------------

(def ^:private structural-keys
  "Keys on an option that describe the graph, skipped when its ops are applied.
   Everything else on an option is an op, which is what makes a typo'd op an
   error."
  #{:id :repeatable :upgrades})

(def ^:private op-order
  "The built-in ops in application order. Ops outside this list (a plugin's own)
   are applied afterwards in name order, so an option's ops resolve identically
   every time regardless of map ordering."
  [:inc :dec :conj :enable :disable])

(defn- ordered [ops]
  (let [order (zipmap op-order (range))]
    (sort-by (fn [op] [(get order op (count op-order)) (name op)])
             (keys ops))))

(defn apply-ops
  "Apply every mutation op on `option` to `vars` — one upgrade's worth of
   change, and the whole of what taking an option does to a mod's values.

   `rng` resolves vars still in their declared form (see `sns.sdk.vars`), so an
   option taken before anything is drawn lands on the drawn value. Resolving is
   idempotent, which is what lets a path stepped one option at a time agree with
   the same path replayed from the start."
  [rng vars option]
  (let [ops (apply dissoc option structural-keys)]
    (reduce (fn [vars op] (apply-op vars op (get ops op)))
            (vars/resolve-vars rng vars)
            (ordered ops))))
