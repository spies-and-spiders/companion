(ns sns.server.schema-gen
  "Generates JSON Schemas from the malli registry: `schemas.json` for editing
   configs, and one schema per plugin I/O type for codegen tools. Run with
   `make schemas`."
  (:require
    [jsonista.core :as j]
    [malli.core :as m]
    [malli.json-schema :as mjs]
    [sns.sdk.schema :as schema]))

(def ^:private draft "http://json-schema.org/draft-07/schema#")

(defn- transform [schema-key]
  (mjs/transform (m/schema schema-key {:registry schema/registry})))

(defn json-schema
  "The config's JSON Schema, with the plugin I/O types added as definitions."
  []
  (let [config  (transform ::schema/config)
        output  (transform ::schema/plugin-output)
        request (transform ::schema/plugin-request)]
    (-> config
        (update :definitions merge (:definitions output) (:definitions request))
        (assoc :$schema draft :title "sns-companion schemas"))))

(defn rooted-schema
  "A standalone JSON Schema rooted at `schema-key`."
  [schema-key title]
  (assoc (transform schema-key) :$schema draft :title title))

(def ^:private outputs
  [["schemas.json"               #(json-schema)]
   ["plugin-request.schema.json" #(rooted-schema ::schema/plugin-request "sns-companion plugin request")]
   ["plugin-output.schema.json"  #(rooted-schema ::schema/plugin-output "sns-companion plugin output")]])

(defn write! []
  (mapv (fn [[path build]]
          (spit path (j/write-value-as-string (build) (j/object-mapper {:pretty true})))
          (println "Wrote" path)
          path)
        outputs))

(defn -main [& _]
  (write!))
