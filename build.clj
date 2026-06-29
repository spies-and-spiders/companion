(ns build
  "Uberjar build. Run the CLJS release first (`npm run release`) so the compiled
   SPA is under resources/public/js, then `clojure -T:build uber`. `npm run dist`
   chains both."
  (:require
    [clojure.tools.build.api :as b]))

(def ^:private lib 'sns/companion)
(def ^:private version "0.1.0")
(def ^:private class-dir "target/classes")
(def ^:private uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

(defn- basis []
  (b/create-basis {:project "deps.edn"}))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber
  "Build the standalone uberjar with sns.server.main as the entrypoint."
  [_]
  (clean nil)
  (b/copy-dir {:src-dirs   ["src" "resources" "spi/src"]
               :target-dir class-dir})
  (b/compile-clj {:basis      (basis)
                  :ns-compile '[sns.server.main]
                  :class-dir  class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis     (basis)
           :main      'sns.server.main})
  (println "Built" uber-file))
