(ns sns.server.classpath
  "Runtime classpath additions for external JAR plugins."
  (:require
    [clojure.java.io :as io])
  (:import
    (clojure.lang DynamicClassLoader RT)))

(defonce ^:private plugin-loader
  ;; One DynamicClassLoader shared by every jar plugin. Sharing it means jars can
  ;; see each other's classes, and a jar hosting several plugins is added once
  ;; rather than defining all its classes again per plugin.
  (delay (DynamicClassLoader. (RT/baseLoader))))

(defonce ^:private added-jars
  ;; Canonical paths already added to `plugin-loader`.
  (atom #{}))

(defn add-jar!
  "Add `path` (a jar file) to the runtime classpath so its namespaces can be
   required, and install the shared plugin classloader as the thread context
   classloader so subsequent `requiring-resolve` calls can see the jar's
   namespaces. Adding a jar already loaded is a no-op, so several plugins may
   name the same jar.

   This resolves plugins at registry-build time (startup). Inside a packaged
   uberjar this still works for resolution, but if a host environment forbids
   replacing the context classloader, put the plugin jar on the JVM classpath
   (`-cp`) instead."
  [path]
  (let [file (io/file path)]
    (when-not (.exists file)
      (throw (ex-info "Plugin jar not found" {:path path})))
    (let [loader    @plugin-loader
          canonical (.getCanonicalPath file)
          [already] (swap-vals! added-jars conj canonical)]
      (when-not (contains? already canonical)
        (DynamicClassLoader/.addURL loader (.. file toURI toURL)))
      (.setContextClassLoader (Thread/currentThread) loader)
      loader)))

(defn construct
  "Instantiate `class-name` from `loader` via its 0-arity constructor. Used for
   `:jar` plugins that name a class (e.g. a Java `LootGenerator` implementation)
   instead of a Clojure `:entrypoint` var."
  [^ClassLoader loader ^String class-name]
  (let [cls (Class/forName class-name true loader)]
    (.newInstance (.getDeclaredConstructor cls (make-array Class 0))
                  (make-array Object 0))))
