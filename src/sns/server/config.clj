(ns sns.server.config
  (:require
    [aero.core :as aero]
    [clojure.java.io :as io]
    [sns.spi.schema :as schema]))

(defn load-config
  "Read and validate the application config. `source` is either a classpath
   resource name (default \"config.edn\") or anything aero can read (File/URL).
   Secrets belong in env vars referenced via aero tags, not in the file."
  ([] (load-config "config.edn"))
  ([source]
   (let [readable (if (string? source)
                    (or (io/resource source)
                        (throw (ex-info "Config resource not found" {:source source})))
                    source)]
     (->> (aero/read-config readable)
          (schema/assert! ::schema/config)))))
