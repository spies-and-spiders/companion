(ns sns.server.classpath
  "Runtime classpath additions for external JAR plugins."
  (:require
    [clojure.java.io :as io])
  (:import
    (clojure.lang DynamicClassLoader RT)))

(defonce ^:private plugin-loader
  ;; Shared, so jars see each other's classes and a jar is only loaded once.
  (delay (DynamicClassLoader. (RT/baseLoader))))

(defonce ^:private added-jars
  (atom #{}))

(defn add-jar!
  "Add the jar at `path` to the plugin classloader, if it isn't already, and
   make that the thread's context classloader so `requiring-resolve` sees it."
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
  "Instantiate `class-name` from `loader` with its no-arg constructor."
  [^ClassLoader loader ^String class-name]
  (let [cls (Class/forName class-name true loader)]
    (.newInstance (.getDeclaredConstructor cls (make-array Class 0))
                  (make-array Object 0))))
