(ns sns.server.registry
  "Resolves config plugin entries into `LootGenerator` instances. Each plugin
   `:type` has a `build-generator` method."
  (:require
    [sns.builtin.cli :as cli]
    [sns.builtin.data :as data]
    [sns.server.classpath :as classpath]))

(defn- resolve-fn
  "Resolve a fully-qualified symbol to its var, requiring its namespace."
  [sym]
  (or (requiring-resolve sym)
      (throw (ex-info "Could not resolve plugin entrypoint" {:entrypoint sym}))))

(defmulti build-generator
  "Construct a `LootGenerator` from a single plugin config entry."
  :type)

(defmethod build-generator :builtin [{:keys [entrypoint] :as plugin}]
  ;; The entrypoint is a factory fn: (entrypoint plugin) -> LootGenerator.
  ((resolve-fn entrypoint) plugin))

(defmethod build-generator :data [{:keys [id source]}]
  (data/generator id (data/load-spec source)))

(defmethod build-generator :cli [{:keys [id command label]}]
  (cli/generator id command label))

(defmethod build-generator :jar [{:keys [jar entrypoint] :as plugin}]
  ;; Add the external jar to the classpath, then resolve its entrypoint factory
  ;; exactly like a builtin.
  (classpath/add-jar! jar)
  ((resolve-fn entrypoint) plugin))

(defmethod build-generator :default [{:keys [type] :as plugin}]
  (throw (ex-info "Unsupported plugin type" {:type type :plugin plugin})))

(defn build
  "Resolve all plugins into a map of loot-type id -> LootGenerator."
  [{:keys [plugins]}]
  (reduce (fn [reg {:keys [id] :as plugin}]
            (when (contains? reg id)
              (throw (ex-info "Duplicate loot-type id" {:id id})))
            (assoc reg id (build-generator plugin)))
          {}
          plugins))
