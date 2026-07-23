(ns sns.server.config
  (:require
    [aero.core :as aero]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [jsonista.core :as j]
    [sns.sdk.schema :as schema])
  (:import
    (java.io File)))

(defn- json-source?
  "True if `source` names a `.json` file."
  [source]
  (let [name (cond
               (string? source)                 source
               (instance? File source)  (.getName ^File source)
               :else                             (str source))]
    (str/ends-with? name ".json")))

(def ^:private default-paths
  "Config filenames looked for in the working directory when none is given."
  ["config.edn" "config.json"])

(defn default-source
  "Find a config file in the working directory: `config.edn`, then `config.json`."
  []
  (or (some (fn [p] (let [f (io/file p)] (when (.exists f) f))) default-paths)
      (throw (ex-info (str "No config file found. Looked for "
                           (str/join ", " default-paths)
                           " in the working directory; pass --config <path> to choose one.")
                      {:searched default-paths}))))

(defn- read-source
  "Read raw config from `source` (a filesystem path string, File, or URL). EDN
   goes through aero (so env-var tags etc. work); JSON is parsed with keywordised
   keys and decoded against the schema, coercing string values (`:type`,
   `:backend`, `:id`, `:entrypoint`, …) to the keywords/symbols the app expects."
  [source]
  (let [readable (if (string? source) (io/file source) source)]
    (if (json-source? source)
      (->> (j/read-value readable j/keyword-keys-object-mapper)
           (schema/decode ::schema/config))
      (aero/read-config readable))))

(defn load-config
  "Read and validate the application config. With no argument, looks for
   `config.edn` (then `config.json`) in the working directory. `source` may be a
   filesystem path string, File, or URL; a `.json` extension selects the JSON
   reader. Secrets belong in env vars referenced via aero tags (EDN only), not in
   the file."
  ([] (load-config (default-source)))
  ([source]
   (->> (read-source source)
        (schema/assert! ::schema/config))))
