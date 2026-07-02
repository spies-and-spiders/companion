(ns sns.server.classpath
  "Runtime classpath additions for external JAR plugins."
  (:require
    [clojure.java.io :as io])
  (:import
    (clojure.lang DynamicClassLoader RT)))

(defn add-jar!
  "Add `path` (a jar file) to the runtime classpath so its namespaces can be
   required. Creates a DynamicClassLoader parented to the current base loader,
   adds the jar, and installs it as the thread context classloader so subsequent
   `requiring-resolve` calls can see the jar's namespaces.

   This resolves plugins at registry-build time (startup). Inside a packaged
   uberjar this still works for resolution, but if a host environment forbids
   replacing the context classloader, put the plugin jar on the JVM classpath
   (`-cp`) instead."
  [path]
  (let [file (io/file path)]
    (when-not (.exists file)
      (throw (ex-info "Plugin jar not found" {:path path})))
    (let [url    (.. file toURI toURL)
          loader (doto (DynamicClassLoader. (RT/baseLoader))
                   (.addURL url))]
      (.setContextClassLoader (Thread/currentThread) loader)
      loader)))
