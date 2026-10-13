(ns sns.server.registry
  "Builds a `Generator` for each config tool, by `:type`."
  (:require
    [flatland.ordered.map :refer [ordered-map]]
    [sns.builtin.cli :as cli]
    [sns.builtin.data :as data]
    [sns.builtin.dust :as dust]
    [sns.builtin.ffi :as ffi]
    [sns.builtin.relics :as relics]
    [sns.builtin.social :as social]
    [sns.builtin.wasm :as wasm]
    [sns.server.classpath :as classpath]))

(def builtins
  "Factories for the built-in generators, keyed by the `:id` that selects them
   when a `:builtin` tool has no `:entrypoint`."
  {:divine-dust dust/generator
   :relics      relics/generator
   :social      social/generator})

(defn- resolve-fn [sym]
  (or (requiring-resolve sym)
      (throw (ex-info "Could not resolve plugin entrypoint" {:entrypoint sym}))))

(defmulti build-generator :type)

(defmethod build-generator :builtin [{:keys [id builtin] :as plugin}]
  (if-let [entrypoint (:entrypoint builtin)]
    ((resolve-fn entrypoint) plugin)
    (if-let [factory (get builtins id)]
      (factory plugin)
      (throw (ex-info "Unknown builtin plugin" {:id id :known (vec (keys builtins))})))))

(defmethod build-generator :data [{:keys [id] {:keys [source inline]} :data}]
  (if inline
    (data/generator id inline)
    (data/file-generator id source)))

(defmethod build-generator :cli [plugin]
  (cli/generator plugin))

(defmethod build-generator :ffi [plugin]
  (ffi/generator plugin))

(defmethod build-generator :wasm [plugin]
  (wasm/generator plugin))

(defmethod build-generator :jar [{{:keys [path entrypoint] class-name :class} :jar :as plugin}]
  (let [loader (classpath/add-jar! path)]
    (if class-name
      (classpath/construct loader class-name)
      ((resolve-fn entrypoint) plugin))))

(defmethod build-generator :default [{:keys [type] :as plugin}]
  (throw (ex-info "Unsupported plugin type" {:type type :plugin plugin})))

(defn build
  "An ordered map of loot-type id to `Generator` for every non-page tool."
  [{:keys [tools]}]
  (doseq [[id n] (frequencies (map :id tools)) :when (> n 1)]
    (throw (ex-info "Duplicate tool id" {:id id})))
  (into (ordered-map)
        (comp (remove (comp #{:page} :type))
              (map (juxt :id build-generator)))
        tools))
