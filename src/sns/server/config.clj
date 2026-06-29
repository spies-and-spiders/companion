(ns sns.server.config
  (:require
    [aero.core :as aero]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [jsonista.core :as j]
    [sns.spi.schema :as schema]))

(defn- json-source?
  "True if `source` names a `.json` file."
  [source]
  (let [name (cond
               (string? source)                 source
               (instance? java.io.File source)  (.getName ^java.io.File source)
               :else                             (str source))]
    (str/ends-with? name ".json")))

(defn- resolve-source
  "Turn a classpath resource name into a URL; pass File/URL through unchanged."
  [source]
  (if (string? source)
    (or (io/resource source)
        (throw (ex-info "Config resource not found" {:source source})))
    source))

(defn- default-source
  "Prefer config.edn on the classpath, falling back to config.json."
  []
  (or (when (io/resource "config.edn") "config.edn")
      (when (io/resource "config.json") "config.json")
      (throw (ex-info "No config.edn or config.json found on the classpath" {}))))

(defn- read-source
  "Read raw config from `source`. EDN goes through aero (so env-var tags etc.
   work); JSON is parsed with keywordised keys and decoded against the schema,
   coercing string values (`:type`, `:backend`, `:id`, `:entrypoint`, …) to the
   keywords/symbols the rest of the app expects."
  [source]
  (let [readable (resolve-source source)]
    (if (json-source? source)
      (->> (j/read-value (slurp readable) j/keyword-keys-object-mapper)
           (schema/decode ::schema/config))
      (aero/read-config readable))))

(defn load-config
  "Read and validate the application config. With no argument, prefers
   `config.edn` on the classpath and falls back to `config.json`. `source` may
   also be an explicit classpath resource name or anything aero/JSON can read
   (File/URL); a `.json` extension selects the JSON reader. Secrets belong in env
   vars referenced via aero tags (EDN only), not in the file."
  ([] (load-config (default-source)))
  ([source]
   (->> (read-source source)
        (schema/assert! ::schema/config))))
