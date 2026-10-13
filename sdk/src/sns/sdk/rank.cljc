(ns sns.sdk.rank
  "Var ranks, shared with the browser, which applies them as it renders.

   Ranks are 1-based: a var's declared `:value` is rank 1.

   ```clojure
   {:ab {:value 1 :step 1 :max 5 :rank 3}}   ; renders as 3
   ```

   `:step` defaults to the var's own value and `:max` is uncapped, so a bare
   `{:ab 1}` can rank up. Text gated on rank keys off the value itself:
   `{{#gte ab 3}}…{{/gte}}`.")

(defn- rank-of [v]
  (or (:rank v) 1))

(defn upgradeable?
  "Whether `v` can rank up at all: a declared (not `:context?`) number whose
   `:max` allows more than one rank."
  [{:keys [value context?] mx :max}]
  (and (number? value)
       (not context?)
       (or (nil? mx) (> mx 1))))

(defn- step-of [{:keys [value step]}]
  (if (some? step) step value))

(defn stepped
  "`v`'s value at its current rank. Non-numeric values are returned as is."
  [{:keys [value] :as v}]
  (let [ranks (dec (rank-of v))
        step  (step-of v)]
    (if (and (pos? ranks) (number? value) (number? step))
      (+ value (* step ranks))
      value)))

(defn- spent [vars]
  (transduce (map (comp dec rank-of val)) + 0 vars))

(defn mod-rank
  "The rank of the mod `vars` belong to: 1 plus the ranks its vars have spent."
  [vars]
  (inc (spent vars)))

(defn available
  "The var ids that can still rank up, or nil. `cap`, if given, limits the
   ranks spent across all `vars`."
  [vars cap]
  (when (or (nil? cap) (< (spent vars) cap))
    (not-empty (into [] (keep (fn [[id v]]
                                (when (and (upgradeable? v)
                                           (< (rank-of v) (or (:max v) ##Inf)))
                                  id)))
                     vars))))

(defn rank-up
  "Rank `id` up once. Doesn't check caps; pick `id` from `available`."
  [vars id]
  (update-in vars [id :rank] (fnil inc 1)))
