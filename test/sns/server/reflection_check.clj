(ns sns.server.reflection-check
  "Standalone reflection guard, run in its own JVM (`clojure -M:reflect` /
   `make reflection`). Compiles every backend namespace with reflection warnings
   enabled and exits non-zero if any `sns.*` reflection is reported. Third-party
   deps (e.g. randy) are out of scope. Not a deftest: reloading namespaces that
   define protocols would change their identity and break the rest of the suite."
  (:require
    [clojure.string :as str]))

(def ^:private backend-nses
  ;; dependency order; each compiles on first require under the binding below
  '[sns.spi.protocols sns.spi.schema
    sns.server.classpath sns.server.render sns.server.progression
    sns.server.store.memory sns.server.store.file sns.server.store.mysql sns.server.store
    sns.server.registry
    sns.builtin.cli sns.builtin.data sns.builtin.dust sns.builtin.relics
    sns.server.reporter.discord sns.server.reporter
    sns.server.engine sns.server.http sns.server.config
    sns.server.schema-gen sns.server.core])

(defn -main [& _]
  (let [sw (java.io.StringWriter.)]
    (binding [*err* sw, *warn-on-reflection* true]
      (doseq [n backend-nses] (require n)))
    (let [hits (->> (str/split-lines (str sw))
                    (filter #(re-find #"Reflection warning, sns/" %)))]
      (if (seq hits)
        (do (println "Backend reflection detected:")
            (run! println hits)
            (System/exit 1))
        (do (println "No backend reflection. ✓")
            (System/exit 0))))))
