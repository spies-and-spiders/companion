(ns build
  "Uberjar build. Run the CLJS release first (`npm run release`) so the compiled
   SPA is under resources/public/js, then `clojure -T:build uber`. `npm run dist`
   chains both."
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.tools.build.api :as b]
    [clojure.tools.build.tasks.uber :as uber]
    [clojure.tools.namespace.find :as find]))

(def ^:private lib 'sns/companion)
(def ^:private version (str/trim (slurp "VERSION")))
(def ^:private class-dir "target/classes")
(def ^:private entrypoint 'sns.server.core)
;; Native-image entrypoint that preloads every sns.* namespace at build time so
;; config-driven plugins land in the closed-world image. Lives under native/ (only
;; on the classpath via the :graalvm alias). See native/sns/aot_preload.clj.
(def ^:private native-entrypoint 'sns.aot-preload)
(def ^:private uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

;; The SPI library jar shipped for plugin authors: Clojure sources (protocols +
;; schema) plus the compiled Java interfaces they implement.
(def ^:private spi-class-dir "target/spi-classes")
(def ^:private spi-jar-file (format "target/sns-spi-%s.jar" version))

(defn- basis [aliases]
  (b/create-basis {:project "deps.edn" :aliases aliases}))

(defn- sns-namespaces [src-dirs]
  (into [] (comp (mapcat #(find/find-namespaces-in-dir (io/file %) find/clj))
                 (filter #(str/starts-with? (name %) "sns"))
                 (distinct))
        src-dirs))

;See https://github.com/mpenet/hirundo#building-uberjars-with-hirundo
(defn- append-json
  "Conflict handler that merges two JSON-array resources by concatenating them."
  [{:keys [path in existing]}]
  {:write {path {:append false
                 :string (json/write-str
                           (concat (json/read-str (slurp existing))
                                   (json/read-str (#'uber/stream->string in))))}}})

(def ^:private helidon-conflict-handlers
  {"META-INF/helidon/service.loader"              :append-dedupe
   "META-INF/helidon/feature-metadata.properties" :append-dedupe
   "META-INF/helidon/config-metadata.json"        append-json
   "META-INF/helidon/service-registry.json"       append-json})

(defn clean [_]
  (b/delete {:path "target"}))

(defn spi-jar
  "Build the standalone SPI library jar plugin authors depend on: the `spi/src`
   Clojure sources (`sns.spi.protocols`/`sns.spi.schema`) plus the compiled Java
   interfaces and records. Shipped as a release artifact."
  [_]
  (b/delete {:path spi-class-dir})
  (b/copy-dir {:src-dirs   ["spi/src"]
               :target-dir spi-class-dir})
  (b/javac {:src-dirs   ["spi/src"]
            :class-dir  spi-class-dir
            :basis      (b/create-basis {:project "spi/deps.edn"})
            :javac-opts ["--release" "21"]})
  (b/jar {:class-dir spi-class-dir
          :jar-file  spi-jar-file})
  (println "Built" spi-jar-file))

(defn uber [{:keys [aliases]}]
  (clean nil)
  (let [native?    (contains? (set aliases) :graalvm)
        namespaces (sns-namespaces ["src" "spi/src"])]
    (b/copy-dir {:src-dirs   (cond-> ["src" "resources" "spi/src"]
                                     native? (conj "native"))
                 :target-dir class-dir})
    (b/compile-clj {:basis      (basis aliases)
                    :ns-compile (cond-> namespaces
                                        native? (conj native-entrypoint))
                    :class-dir  class-dir})
    (b/uber {:class-dir         class-dir
             :uber-file         uber-file
             :basis             (basis aliases)
             :main              (if native? native-entrypoint entrypoint)
             :conflict-handlers helidon-conflict-handlers})
    (println "Built" uber-file)))
