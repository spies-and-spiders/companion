(ns sns.sdk.rank
  "Ranks. A var carries its own progression, so there is no separate upgrade
   graph and no path to replay — the rank is one more key on the
   `sns.sdk.schema/item-var` the template already interpolates.

   Ranks are 1-based: a var's declared `:value` *is* rank 1, and one upgrade
   takes it to `:rank 2`.

   ```clojure
   {:ab {:value 1 :step 1 :max 5 :rank 3}}   ; renders as 3
   ```

   `:step` defaults to the var's own value and `:max` is uncapped, so a bare
   `{:ab 1}` ranks up by itself; a var holding anything but a number has nowhere
   to step to and never ranks. Text that should appear only once a var is high
   enough keys off the number — `{{#gte ab 3}}…{{/gte}}` — rather than off a
   second flag var that could disagree with it.

   Nothing is stamped on a var to set this up: the defaults are read here, by
   the same code on both sides of the wire, so a plugin's vars travel exactly as
   it wrote them.

   Shared with the browser, which folds the rank in as it renders; that is why
   the vocabulary is closed rather than extensible.")

(defn- rank-of
  "A var's rank. Absent means 1, and so does blank — a DM mid-edit has cleared
   the field, not asked for rank zero."
  [v]
  (or (:rank v) 1))

(defn upgradeable?
  "Whether `v` can be ranked up at all: a number the plugin declared, not pinned
   to its first rank. Nothing is stamped on a var to say so — a non-numeric one
   simply has nowhere to step to. A `:context?` var is an entry's own field
   rather than something the plugin declared, so it never ranks either."
  [{:keys [value context?] mx :max}]
  (and (number? value)
       (not context?)
       (or (nil? mx) (> mx 1))))

(defn- step-of
  "How far one rank moves `v`. A var that names no `:step` steps by its own
   value, so a bare `{:ab 1}` ranks 1, 2, 3."
  [{:keys [value step]}]
  (if (some? step) step value))

(defn stepped
  "`v`'s value at its current rank: the declared value moved by a step for each
   rank above the first. A var still at rank 1 — or one holding anything but a
   number — is its own value, which is also what a blank mid-edit value stays."
  [{:keys [value] :as v}]
  (let [ranks (dec (rank-of v))
        step  (step-of v)]
    (if (and (pos? ranks) (number? value) (number? step))
      (+ value (* step ranks))
      value)))

(defn- spent
  "Ranks taken across `vars`, counting from the first."
  [vars]
  (transduce (map (comp dec rank-of val)) + 0 vars))

(defn mod-rank
  "The rank of the mod `vars` belong to: one more than its vars have spent
   between them, so a mod nobody has upgraded is rank 1."
  [vars]
  (inc (spent vars)))

(defn available
  "The var ids that can still be ranked up, or nil when none can. `cap`, when
   given, is the total ranks this mod may spend across all of its vars."
  [vars cap]
  (when (or (nil? cap) (< (spent vars) cap))
    (seq (keep (fn [[id v]]
                 (when (and (upgradeable? v)
                            (< (rank-of v) (or (:max v) ##Inf)))
                   id))
               vars))))

(defn rank-up
  "Take one rank on `id`. Callers pick from `available`, which is where the caps
   are enforced."
  [vars id]
  (update-in vars [id :rank] (fnil inc 1)))
