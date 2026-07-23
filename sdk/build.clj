(ns build
  "Compiles the SDK's Java interfaces and records (the .java files under sdk/src)
   into target/classes so the protocols namespace can extend onto them. Wired as
   this lib's `:deps/prep-lib` step, so consumers run it via `clojure -X:deps
   prep`."
  (:require
    [clojure.tools.build.api :as b]))

(def ^:private class-dir "target/classes")

(defn compile-java [_]
  (b/delete {:path class-dir})
  (b/javac {:src-dirs   ["src"]
            :class-dir  class-dir
            :basis      (b/create-basis {:project "deps.edn"})
            :javac-opts ["--release" "21"]})
  (println "Compiled SDK Java interfaces to sdk/" class-dir))
