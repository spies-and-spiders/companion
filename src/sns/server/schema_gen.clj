(ns sns.server.schema-gen
  "Generate `schemas.json`: a JSON Schema bundling the application config
   (`::config`) and the :cli plugin's stdout contract (`::cli-output`), so a
   hand-written `config.json` and CLI plugin output can be validated and
   autocompleted in an editor. Run via `clojure -M -m sns.server.schema-gen
   [out-path]` (or `make schemas`)."
  (:require
    [jsonista.core :as j]
    [malli.core :as m]
    [malli.json-schema :as mjs]
    [sns.sdk.schema :as schema]))

(defn json-schema
  "The config spec rendered as a JSON Schema (a plain Clojure map), rooted at
   `::config`. The :cli plugin's stdout contract (`::cli-output`) is not part of
   the config, so it is merged in as an extra definition for editors/tools to
   `$ref` against."
  []
  (let [config (mjs/transform (m/schema ::schema/config {:registry schema/registry}))
        cli    (mjs/transform (m/schema ::schema/cli-output {:registry schema/registry}))]
    (-> config
        (update :definitions merge (:definitions cli))
        (assoc :$schema "http://json-schema.org/draft-07/schema#"
               :title "sns-companion schemas"))))

(defn write!
  "Write the JSON Schema to `path` (default \"schemas.json\")."
  ([] (write! "schemas.json"))
  ([path]
   (spit path (j/write-value-as-string (json-schema)
                                       (j/object-mapper {:pretty true})))
   (println "Wrote" path)
   path))

(defn -main [& [path]]
  (write! (or path "schemas.json")))
