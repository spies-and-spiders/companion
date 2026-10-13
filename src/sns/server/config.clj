(ns sns.server.config
  (:require
    [aero.core :as aero]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [jsonista.core :as j]
    [sns.sdk.schema :as schema])
  (:import
    (java.io File)))

(defn- json-source? [source]
  (let [name (cond
               (string? source)                 source
               (instance? File source)  (.getName ^File source)
               :else                             (str source))]
    (str/ends-with? name ".json")))

(def ^:private default-paths ["config.edn" "config.json"])

(defn default-source
  []
  (or (some (fn [p] (let [f (io/file p)] (when (.exists f) f))) default-paths)
      (throw (ex-info (str "No config file found. Looked for "
                           (str/join ", " default-paths)
                           " in the working directory; pass --config <path> to choose one.")
                      {:searched default-paths}))))

(defn- read-source
  "Read EDN through aero, or JSON decoded against the config schema."
  [source]
  (let [readable (if (string? source) (io/file source) source)]
    (if (json-source? source)
      (->> (j/read-value readable j/keyword-keys-object-mapper)
           (schema/decode ::schema/config))
      (aero/read-config readable))))

(defn load-config
  "Read and validate the config from `source` (a path, File or URL), or from
   the working directory's `config.edn` or `config.json`."
  ([] (load-config (default-source)))
  ([source]
   (->> (read-source source)
        (schema/assert! ::schema/config))))
