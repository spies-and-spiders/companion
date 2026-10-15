(ns sns.builtin.data
  "Interpreter for `:data` plugins: loot types defined entirely in EDN or JSON.
   A spec's `:let` draws from its `:tables`, and its `:view-model` is a literal
   view-model whose `:data/*` directives expand against those draws. Titles and
   bodies are left as templates for the browser to render."
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
    (schema/assert! ::schema/data-spec
                    (if (str/ends-with? source ".json")
                      (schema/decode ::schema/data-spec (j/read-value f j/keyword-keys-object-mapper))
                      (with-open [r (PushbackReader. (io/reader f))]
                        (edn/read r))))))

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
        (dissoc element :data/vars)))

(defn- context-var [v]
  {:value v :context? true})

(defn- context-vars [element]
  (update-vals (context-fields element) context-var))

(defn- roll-vars
  "Resolve an element's `:data/vars` in place, so every use of the element
   shares one draw. Resolving is idempotent, so a rolled element stays as is."
  [rng element]
  (cond-> element
          (and (map? element) (seq (:data/vars element)))
          (update :data/vars (partial vars/resolve-vars rng))))

(defn- evaluate-let [rng tables {:keys [draw take choose n]}]
  (let [table (or draw take choose)
        pool  (get tables table)]
    (when (empty? pool)
      (throw (ex-info "Draw from an empty or missing table" {:table table})))
    (cond
      draw  (mapv (partial roll-vars rng) (r/sample-without-replacement rng (min n (count pool)) pool))
      take  (mapv (fn [_] (roll-vars rng (r/sample rng pool))) (range n))
      :else (roll-vars rng (r/sample rng pool)))))

(defn- chosen-vars
  "Each chosen entry as a context var, plus its rolled `:data/vars` flat, so
   every template it reaches shares them."
  [lets choices]
  (reduce (fn [acc id]
            (let [entry (get lets id)
                  rolled (:data/vars entry)]
              (when-let [clash (seq (filter (partial contains? acc) (keys rolled)))]
                (throw (ex-info "Chosen entries declare the same var" {:let id :vars (vec clash)})))
              (merge acc {id (context-var (context-fields entry))} rolled)))
          {}
          choices))

(defn- path [p]
  (mapv #(if (string? %) (keyword %) %) (if (sequential? p) p [p])))

(defn- lookup
  "Resolve `p` against the innermost scope holding its first key, or nil."
  [{:keys [scopes]} p]
  (let [[k :as ks] (path p)]
    (some-> (first (filter #(and (map? %) (contains? % k)) scopes))
            (get-in ks))))

(defn- push [{:keys [rng] :as env} element]
  (let [element (roll-vars rng element)]
    (-> env
        (update :scopes conj element)
        (update :vars merge (when (map? element)
                              (merge (context-vars element) (:data/vars element)))))))

(defn- without-vars [x]
  (cond (map? x)        (dissoc x :data/vars)
        (sequential? x) (mapv without-vars x)
        :else           x))

(defn- directive? [k v]
  (and (map? v) (contains? v k)))

(declare expand)

(defn- expand-each [env {:data/keys [each as]}]
  (mapv #(expand (push env %) as) (lookup env each)))

(defn- expand-map [env m]
  (let [m (into {} (keep (fn [[k v]] (some->> (expand env v) (vector k)))) m)]
    (if (contains? m :item/body)
      (let [item-vars (merge (:vars env) (:item/vars m))]
        (cond-> m (seq item-vars) (assoc :item/vars item-vars)))
      m)))

(defn- expand [env v]
  (cond
    (directive? :data/field v) (lookup env (:data/field v))
    (directive? :data/var v)   (context-var (without-vars (lookup env (:data/var v))))
    (directive? :data/each v)  (expand-each env v)
    (map? v)                   (expand-map env v)
    (sequential? v)            (into [] (mapcat #(if (directive? :data/each %) (expand env %) [(expand env %)])) v)
    :else                      v))

(defn generate
  "Evaluate `spec` against the request `ctx`, returning a view-model."
  [{:keys [tables view-model] :as spec} {:keys [inputs rng]}]
  (let [rng       (or rng @r/default-rng)
        lets      (update-vals (:let spec) (partial evaluate-let rng tables))
        loot-vars (merge (update-vals inputs context-var)
                         (chosen-vars lets (keep (fn [[id l]] (when (:choose l) id)) (:let spec))))
        vm        (expand {:rng rng :scopes (list lets inputs) :vars {}} view-model)
        all-vars  (merge loot-vars (:loot/vars vm))]
    (cond-> (dissoc vm :loot/vars)
            (seq all-vars) (assoc :loot/vars all-vars))))

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
