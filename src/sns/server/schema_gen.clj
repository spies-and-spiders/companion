(ns sns.server.schema-gen
  "Generate a JSON Schema for the application config from the malli `::config`
   spec, so a hand-written `config.json` can be validated and autocompleted in an
   editor. Run via `clojure -M -m sns.server.schema-gen [out-path]` (or
   `make config-schema`)."
  (:require
    [jsonista.core :as j]
    [malli.core :as m]
    [malli.json-schema :as mjs]
    [sns.sdk.schema :as schema]))

(defn config-json-schema
  "The config spec rendered as a JSON Schema (a plain Clojure map)."
  []
  (-> (m/schema ::schema/config {:registry schema/registry})
      (mjs/transform)
      (assoc :$schema "http://json-schema.org/draft-07/schema#"
             :title "sns-companion config")))

(defn write!
  "Write the config JSON Schema to `path` (default \"config.schema.json\")."
  ([] (write! "config.schema.json"))
  ([path]
   (spit path (j/write-value-as-string (config-json-schema)
                                       (j/object-mapper {:pretty true})))
   (println "Wrote" path)
   path))

(defn -main [& [path]]
  (write! (or path "config.schema.json")))
