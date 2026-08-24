(ns sns.builtin.data
  "Interpreter for `:data` plugins: a code-free loot type defined entirely as
   data (EDN or JSON). A spec declares a top-level `:items` list, optional
   `:take`/`:weighted` draw controls, and a view template; the interpreter
   samples it into a view-model.

   Nothing is rendered here. `:title`/`:subtitle` and an item's `:title`/`:body`
   travel to the browser as templates, alongside the vars they interpolate
   (`sns.sdk.vars`) — which is what lets a DM edit a drawn value without
   retyping the prose around it. An item's `:title`/`:body` may instead be a
   *field reference* (a keyword, as `:metadata` always was); that is how to
   reach entry data that is itself a template.

   This is the workhorse for DMs who don't write Clojure. See the README."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [jsonista.core :as j]
    [randy.core :as r]
    [sns.sdk.protocols :as p]
    [sns.sdk.schema :as schema]
    [sns.sdk.vars :as vars])
  (:import
    (java.io PushbackReader)))

(defn load-spec [source]
  (let [f (io/file source)]
    (when-not (.exists f)
      (throw (ex-info "Data plugin source not found" {:source source})))
    (if (str/ends-with? source ".json")
      (schema/decode ::schema/data-spec (j/read-value f j/keyword-keys-object-mapper))
      (with-open [r (PushbackReader. (io/reader f))]
        (edn/read r)))))

(defn- draw-entries
  "Sample from the spec's top-level `:items`. `take` (default 1) is how many to
   draw; `weighted` draws with replacement by each entry's `:weight`, otherwise
   draws without replacement."
  [rng items {:keys [take weighted]}]
  (let [pool items
        n    (or take 1)]
    (when (empty? pool)
      (throw (ex-info "Draw from empty :items list" {})))
    (cond
      weighted (mapv (fn [_] (r/weighted-sample rng (into {} (map (juxt identity :weight)) pool)))
                     (range n))
      (> n 1)  (vec (r/sample-without-replacement rng (min n (count pool)) pool))
      :else    [(r/sample rng pool)])))

(def ^:private declared-vars-key
  "Where an entry declares its vars. Namespaced so it cannot collide with the
   entry's own fields, and identical in JSON (\"item/vars\")."
  :item/vars)

(defn- structural?
  "A collection of maps is the spec's own scaffolding (an entry's `:mods`, say),
   never something a template interpolates."
  [v]
  (and (coll? v) (some map? v)))

(defn- context-fields
  "The entry/element fields a template may interpolate — its own data, minus the
   var declarations, the interpreter's controls, and nested structure. A plain
   collection is kept, so a template can index one (`{{ tags.[0] }}`)."
  [element]
  (into {}
        (remove (fn [[_ v]] (structural? v)))
        (dissoc element declared-vars-key :weight)))

(defn- context-vars
  "An element's own fields as vars, marked `:context?` — available to its
   templates, but not offered as editable values. Only what the entry
   *declared* is the DM's to change.

   Plain data, deliberately: these are not run through `vars/resolve-var`, so a
   field that happens to look like a behaviour stays the value it is, and no
   label is computed for something the editor never shows."
  [element]
  (update-vals (context-fields element) #(hash-map :value % :context? true)))

(defn- element-vars
  "The vars an item rendered from `element` is given: its own context fields,
   plus anything the element itself declares.

   A *single* drawn entry's declarations are deliberately absent — they are sent
   once as `:loot/vars`, which every template reads, so one draw is shared by
   every mod the entry produces rather than copied into each. Copies would
   desynchronise the moment the DM edited one. With `:each :items` the element
   *is* an entry, and each is its own item, so its declarations belong here."
  [rng entry-vars element]
  (merge (context-vars element)
         (vars/resolve-vars rng (apply dissoc (get element declared-vars-key) (keys entry-vars)))))

(defn- template-or-field
  "An item's `:title`/`:body`: a keyword reads that field off `element` (so
   entry data that is itself a template reaches the browser intact); a string is
   already the template."
  [spec element]
  (if (keyword? spec)
    (get element spec)
    spec))

(defn- build-item [rng entry-vars {:keys [title body metadata]} element]
  (let [item-vars (element-vars rng entry-vars element)]
    (cond-> {:item/body (str (template-or-field body element))}
            title (assoc :item/title (str (template-or-field title element)))
            (and metadata (seq (get element metadata))) (assoc :item/metadata (vec (get element metadata)))
            (seq item-vars) (assoc :item/vars item-vars))))

(defn- build-section [rng entry-vars {:keys [heading each item]} entries single]
  ;; `:each :items` iterates the drawn entries; any other keyword iterates that
  ;; field on the single drawn entry (e.g. `:each :mods`).
  (let [coll (if (= :items each) entries (get single each))]
    (cond-> {:section/items (mapv #(build-item rng entry-vars item %) coll)}
            heading (assoc :section/heading heading))))

(defn generate
  "Evaluate `spec` against the request `ctx`, returning a view-model."
  [spec {:keys [inputs rng]}]
  (let [rng       (or rng @r/default-rng)
        {:keys [items title subtitle sections]} spec
        entries   (draw-entries rng items spec)
        single    (when (= 1 (count entries)) (first entries))
        ;; An entry's declared vars are drawn once, then shared by every item
        ;; the entry produces — so two mods referring to `{{ ability }}` show
        ;; the same value, and editing it updates both.
        entry-vars (vars/resolve-vars rng (get single declared-vars-key))
        ;; The title/subtitle templates interpolate the drawn entry plus the
        ;; inputs the DM submitted.
        loot-vars  (merge (context-vars (merge inputs (or single {}))) entry-vars)]
    (cond-> {:loot/title (str (or title ""))}
            subtitle (assoc :loot/subtitle subtitle)
            (seq loot-vars) (assoc :loot/vars loot-vars)
            (seq sections) (assoc :loot/sections
                                  (mapv #(build-section rng entry-vars % entries single) sections)))))

(defn generator
  "Build a `LootGenerator` for a `:data` plugin from an inline `spec`."
  [id spec]
  (reify p/LootGenerator
    (loot-spec [_]
      (cond-> {:id id :label (:label spec)}
              (:utility? spec) (assoc :utility? true)
              (:inputs spec) (assoc :inputs (:inputs spec))))
    (generate [_ ctx]
      (generate spec ctx))))

(def ^:private reload-field
  {:id      :__reload?
   :label   "Reload data before generating?"
   :type    :bool
   :default false})

(defn file-generator
  "Build a `LootGenerator` for a `:data` plugin whose spec is loaded from
   `source`. Holds the spec in an atom and exposes a `:__reload?` input that,
   when set, re-reads `source` into that atom before generating."
  [id source]
  (let [spec-atom (atom (load-spec source))]
    (reify p/LootGenerator
      (loot-spec [_]
        (let [spec @spec-atom]
          (cond-> {:id id :label (:label spec) :inputs (into [reload-field] (:inputs spec))}
                  (:utility? spec) (assoc :utility? true))))
      (generate [_ ctx]
        (when (get-in ctx [:inputs :__reload?])
          (reset! spec-atom (load-spec source)))
        (generate @spec-atom (update ctx :inputs dissoc :__reload?))))))
