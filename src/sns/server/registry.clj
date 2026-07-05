(ns sns.server.registry
  "Resolves config plugin entries into `LootGenerator` instances. Each plugin
   `:type` has a `build-generator` method."
  (:require
    [flatland.ordered.map :refer [ordered-map]]
    [sns.builtin.cli :as cli]
    [sns.builtin.data :as data]
    [sns.builtin.dust :as dust]
    [sns.builtin.relics :as relics]
    [sns.server.classpath :as classpath]))

(def builtins
  "Factories for the generators shipped with the app, keyed by the :id a
   `:builtin` plugin registers under. Eagerly required, so config needs no
   `:entrypoint` (and no Clojure symbols) to use them."
  {:divine-dust dust/generator
   :relics      relics/generator})

(defn- resolve-fn
  "Resolve a fully-qualified symbol to its var, requiring its namespace."
  [sym]
  (or (requiring-resolve sym)
      (throw (ex-info "Could not resolve plugin entrypoint" {:entrypoint sym}))))

(defmulti build-generator
  "Construct a `LootGenerator` from a single plugin config entry."
  :type)

(defmethod build-generator :builtin [{:keys [id entrypoint] :as plugin}]
  ;; With an :entrypoint, resolve it as a factory fn: (entrypoint plugin) ->
  ;; LootGenerator. Without one, the :id names a registered builtin.
  (if entrypoint
    ((resolve-fn entrypoint) plugin)
    (if-let [factory (get builtins id)]
      (factory plugin)
      (throw (ex-info "Unknown builtin plugin" {:id id :known (vec (keys builtins))})))))

(defmethod build-generator :data [{:keys [id source]}]
  (data/generator id (data/load-spec source)))

(defmethod build-generator :cli [{:keys [id command label utility?]}]
  (cli/generator id command label utility?))

(defmethod build-generator :jar [{:keys [jar entrypoint] :as plugin}]
  ;; Add the external jar to the classpath, then resolve its generator: a
  ;; `:class` is constructed via its 0-arity constructor, otherwise the
  ;; `:entrypoint` factory var is resolved exactly like a builtin.
  (let [loader (classpath/add-jar! jar)]
    (if-let [class-name (:class plugin)]
      (classpath/construct loader class-name)
      ((resolve-fn entrypoint) plugin))))

(defmethod build-generator :default [{:keys [type] :as plugin}]
  (throw (ex-info "Unsupported plugin type" {:type type :plugin plugin})))

(defn build
  "Resolve all plugins into a map of loot-type id -> LootGenerator, ordered as
   they appear in config (the UI picker preserves this order)."
  [{:keys [plugins]}]
  (reduce (fn [reg {:keys [id] :as plugin}]
            (when (contains? reg id)
              (throw (ex-info "Duplicate loot-type id" {:id id})))
            (assoc reg id (build-generator plugin)))
          (ordered-map)
          plugins))
