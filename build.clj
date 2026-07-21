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
(def ^:private uber-file (format "target/%s.jar" (name lib)))

;; The SPI library jar shipped for plugin authors
(def ^:private spi-lib 'tools.spies/sns-spi)
(def ^:private spi-class-dir "target/spi-classes")
(def ^:private spi-javadoc-dir "target/spi-javadoc")
(def ^:private spi-jar-file (format "target/sns-spi-%s.jar" version))
(def ^:private spi-pom-file (format "target/sns-spi-%s.pom" version))
(def ^:private spi-sources-jar-file (format "target/sns-spi-%s-sources.jar" version))
(def ^:private spi-javadoc-jar-file (format "target/sns-spi-%s-javadoc.jar" version))

(def ^:private github-url "https://github.com/spies-and-spiders/companion")

(def ^:private spi-pom-data
  [[:description "Service-provider interfaces for sns-companion loot plugins: the Java interfaces, Clojure protocols, and data schemas plugin authors implement against."]
   [:url github-url]
   [:licenses
    [:license
     [:name "Eclipse Public License 2.0"]
     [:url "https://www.eclipse.org/legal/epl-2.0/"]]]
   [:developers
    [:developer
     [:name "Dekel Pilli"]]]])

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

(defn- spi-javadoc []
  (b/delete {:path spi-javadoc-dir})
  (let [java-files (into []
                         (comp (filter #(str/ends-with? (.getName ^java.io.File %) ".java"))
                               (map str))
                         (file-seq (io/file "spi/src")))
        {:keys [exit]} (b/process {:command-args (into ["javadoc" "-d" spi-javadoc-dir
                                                        "-quiet" "-Xdoclint:none"
                                                        "--release" "21"]
                                                       (sort java-files))})]
    (when-not (zero? exit)
      (throw (ex-info "javadoc failed for SPI sources" {:exit exit}))))
  (b/jar {:class-dir spi-javadoc-dir
          :jar-file  spi-javadoc-jar-file}))

(defn spi-jar
  "Build the standalone SPI library jar plugin authors depend on: the `spi/src`
   Clojure sources (`sns.spi.protocols`/`sns.spi.schema`) plus the compiled Java
   interfaces and records. Shipped as a release artifact and deployed to Maven
   Central, so this also produces the pom, -sources jar, and -javadoc jar
   Central requires."
  [_]
  (b/delete {:path spi-class-dir})
  (b/copy-dir {:src-dirs   ["spi/src"]
               :target-dir spi-class-dir})
  (b/copy-file {:src    "spi/LICENSE"
                :target (str spi-class-dir "/META-INF/LICENSE")})
  (b/javac {:src-dirs   ["spi/src"]
            :class-dir  spi-class-dir
            :basis      (b/create-basis {:project "spi/deps.edn"})
            :javac-opts ["--release" "21"]})
  (b/write-pom {:class-dir spi-class-dir
                :lib       spi-lib
                :version   version
                :basis     (b/create-basis {:project "spi/deps.edn"})
                :src-dirs  ["spi/src"]
                :scm       {:url                 github-url
                            :connection          (str "scm:git:" github-url ".git")
                            :developerConnection (str "scm:git:ssh://git@github.com/spies-and-spiders/companion.git")
                            :tag                 version}
                :pom-data  spi-pom-data})
  (b/jar {:class-dir spi-class-dir
          :jar-file  spi-jar-file})
  (b/copy-file {:src    (format "%s/META-INF/maven/%s/%s/pom.xml"
                                spi-class-dir (namespace spi-lib) (name spi-lib))
                :target spi-pom-file})
  (b/jar {:class-dir "spi/src"
          :jar-file  spi-sources-jar-file})
  (spi-javadoc)
  (println "Built" spi-jar-file "+ pom, sources, and javadoc jars"))

(defn uber [{:keys [aliases]}]
  (clean nil)
  (let [namespaces (sns-namespaces ["src" "spi/src"])]
    (b/copy-dir {:src-dirs   ["src" "resources" "spi/src"]
                 :target-dir class-dir})
    (b/compile-clj {:basis      (basis aliases)
                    :ns-compile namespaces
                    :class-dir  class-dir})
    (b/uber {:class-dir         class-dir
             :uber-file         uber-file
             :basis             (basis aliases)
             :main              entrypoint
             :conflict-handlers helidon-conflict-handlers})
    (println "Built" uber-file)))
