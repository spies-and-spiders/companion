(ns sns.server.schema-gen
  "Generate the project's JSON Schemas from the malli registry:
     - `schemas.json` — the config (`::config`) plus the plugin I/O contracts
       (`::plugin-request`/`::plugin-output`) merged in as definitions, so a
       hand-written `config.json` and plugin I/O can be validated/autocompleted in
       an editor;
     - `plugin-request.schema.json` / `plugin-output.schema.json` — each rooted at
       a single plugin I/O type, so codegen tools (quicktype, typify,
       go-jsonschema) emit clean structs for authors writing `:cli`/`:ffi` plugins
       in another language.
   Run via `clojure -M -m sns.server.schema-gen` (or `make schemas`)."
  (:require
    [jsonista.core :as j]
    [malli.core :as m]
    [malli.json-schema :as mjs]
    [sns.sdk.schema :as schema]))

(def ^:private draft "http://json-schema.org/draft-07/schema#")

(defn- transform [schema-key]
  (mjs/transform (m/schema schema-key {:registry schema/registry})))

(defn json-schema
  "The config spec rendered as a JSON Schema, rooted at `::config`, with the
   plugin I/O contracts (not part of the config) merged in as extra definitions
   for editors/tools to `$ref` against."
  []
  (let [config  (transform ::schema/config)
        output  (transform ::schema/plugin-output)
        request (transform ::schema/plugin-request)]
    (-> config
        (update :definitions merge (:definitions output) (:definitions request))
        (assoc :$schema draft :title "sns-companion schemas"))))

(defn rooted-schema
  "A standalone JSON Schema rooted at `schema-key` (its `$ref` plus the
   `definitions` it needs), for single-type codegen."
  [schema-key title]
  (assoc (transform schema-key) :$schema draft :title title))

(def ^:private outputs
  [["schemas.json"               #(json-schema)]
   ["plugin-request.schema.json" #(rooted-schema ::schema/plugin-request "sns-companion plugin request")]
   ["plugin-output.schema.json"  #(rooted-schema ::schema/plugin-output "sns-companion plugin output")]])

(defn write!
  "Write all schema files, returning their paths."
  []
  (mapv (fn [[path build]]
          (spit path (j/write-value-as-string (build) (j/object-mapper {:pretty true})))
          (println "Wrote" path)
          path)
        outputs))

(defn -main [& _]
  (write!))
