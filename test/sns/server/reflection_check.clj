(ns sns.server.reflection-check
  "Standalone reflection guard, run in its own JVM (`clojure -M:reflect` /
   `make reflection`). Discovers every `sns.*` namespace under `src` and
   `sdk/src`, then compiles each with reflection warnings enabled and exits
   non-zero if any `sns.*` reflection is reported. Third-party deps (e.g.
   randy) are out of scope. Not a deftest: reloading namespaces that define
   protocols would change their identity and break the rest of the suite."
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.tools.namespace.find :as ns-find]))

(defn- backend-nses []
  (->> ["src" "sdk/src"]
       (mapcat #(ns-find/find-namespaces-in-dir (io/file %)))
       (filter #(str/starts-with? (name %) "sns."))
       sort))

(defn -main [& _]
  (let [sw (java.io.StringWriter.)]
    (binding [*err* sw, *warn-on-reflection* true]
      (doseq [n (backend-nses)] (require n)))
    (let [hits (->> (str/split-lines (str sw))
                    (filter #(re-find #"Reflection warning, sns/" %)))]
      (if (seq hits)
        (do (println "Backend reflection detected:")
            (run! println hits)
            (System/exit 1))
        (do (println "No backend reflection. ✓")
            (System/exit 0))))))
