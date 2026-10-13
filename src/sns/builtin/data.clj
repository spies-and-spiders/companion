(ns sns.builtin.data
  "Interpreter for `:data` plugins: loot types defined entirely in EDN or JSON.
   A spec's `:items` are sampled (per `:take`/`:weighted`) into a view-model
   whose titles and bodies are left as templates for the browser to render."
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
  "Draw `take` (default 1) entries: with replacement by `:weight` when
   `weighted`, otherwise without replacement."
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
  "Namespaced so it can't collide with an entry's own fields."
  :item/vars)

(defn- structural?
  "A collection of maps, like an entry's `:mods`, is scaffolding rather than
   something to interpolate."
  [v]
  (and (coll? v) (some map? v)))

(defn- context-fields
  "The element's fields a template may interpolate. Plain collections are kept,
   so a template can index one (`{{ tags.[0] }}`)."
  [element]
  (into {}
        (remove (fn [[_ v]] (structural? v)))
        (dissoc element declared-vars-key :weight)))

(defn- context-vars
  "An element's own fields as `:context?` vars. Not resolved, so a field that
   looks like a behaviour stays as it is."
  [element]
  (update-vals (context-fields element) #(hash-map :value % :context? true)))

(defn- element-vars
  "An element's context fields plus its own declared vars. A single drawn
   entry's vars are left to `:loot/vars`, so one draw is shared by all its
   items and a DM's edit updates them all."
  [rng entry-vars element]
  (merge (context-vars element)
         (vars/resolve-vars rng (apply dissoc (get element declared-vars-key) (keys entry-vars)))))

(defn- template-or-field
  "Resolve a field reference against `element`, or return the template as is.
   JSON has no keywords, so a string naming one of the element's fields also
   counts as a reference."
  [spec element]
  (cond
    (keyword? spec)              (get element spec)
    (contains? element spec)     (get element spec)
    (and (string? spec)
         (contains? element (keyword spec))) (get element (keyword spec))
    :else                        spec))

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
        entry-vars (vars/resolve-vars rng (get single declared-vars-key))
        loot-vars  (merge (context-vars (merge inputs (or single {}))) entry-vars)]
    (cond-> {:loot/title (str (or title ""))}
            subtitle (assoc :loot/subtitle subtitle)
            (seq loot-vars) (assoc :loot/vars loot-vars)
            (seq sections) (assoc :loot/sections
                                  (mapv #(build-section rng entry-vars % entries single) sections)))))

(defn generator
  "Build a `Generator` for a `:data` plugin from an inline `spec`."
  [_id spec]
  (reify p/Generator
    (loot-spec [_]
      (select-keys spec [:history :inputs]))
    (generate [_ ctx]
      (generate spec ctx))))

(def ^:private reload-field
  {:id      :__reload?
   :label   "Reload data before generating?"
   :type    :bool
   :default false})

(defn file-generator
  "Build a `Generator` for a `:data` plugin loaded from `source`, with a
   `:__reload?` input to re-read it before generating."
  [_id source]
  (let [spec-atom (atom (load-spec source))]
    (reify p/Generator
      (loot-spec [_]
        (let [spec @spec-atom]
          (-> (select-keys spec [:history])
              (assoc :inputs (into [reload-field] (:inputs spec))))))
      (generate [_ ctx]
        (when (get-in ctx [:inputs :__reload?])
          (reset! spec-atom (load-spec source)))
        (generate @spec-atom (update ctx :inputs dissoc :__reload?))))))
