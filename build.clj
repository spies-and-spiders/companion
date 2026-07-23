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

;; The SDK library jar shipped for plugin authors
(def ^:private sdk-lib 'tools.spies/sns-companion-sdk)
(def ^:private sdk-class-dir "target/sdk-classes")
(def ^:private sdk-javadoc-dir "target/sdk-javadoc")
;; Consistently named (unversioned) like the uberjar, so local commands and CI
;; steps never have to know the version; the release workflow stamps the version
;; into the filenames Maven Central requires.
(def ^:private sdk-jar-file (format "target/%s.jar" (name sdk-lib)))
(def ^:private sdk-pom-file (format "target/%s.pom" (name sdk-lib)))
(def ^:private sdk-sources-jar-file (format "target/%s-sources.jar" (name sdk-lib)))
(def ^:private sdk-javadoc-jar-file (format "target/%s-javadoc.jar" (name sdk-lib)))

(def ^:private github-url "https://github.com/spies-and-spiders/companion")

(def ^:private sdk-pom-data
  [[:description "The sns-companion plugin SDK: the Java interfaces, Clojure protocols and data schemas loot plugins implement against, plus the shared logic (config schemas, template randoms, upgrade-graph ops) they build on."]
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

(defn- sdk-javadoc []
  (b/delete {:path sdk-javadoc-dir})
  (let [java-files (into []
                         (comp (filter #(str/ends-with? (.getName ^java.io.File %) ".java"))
                               (map str))
                         (file-seq (io/file "sdk/src")))
        {:keys [exit]} (b/process {:command-args (into ["javadoc" "-d" sdk-javadoc-dir
                                                        "-quiet" "-Xdoclint:none"
                                                        "--release" "21"]
                                                       (sort java-files))})]
    (when-not (zero? exit)
      (throw (ex-info "javadoc failed for SDK sources" {:exit exit}))))
  (b/jar {:class-dir sdk-javadoc-dir
          :jar-file  sdk-javadoc-jar-file}))

(defn sdk-jar
  "Build the standalone SDK library jar plugin authors depend on: the `sdk/src`
   Clojure sources (`sns.sdk.protocols`/`sns.sdk.schema`/`sns.sdk.randoms`/
   `sns.sdk.progression`) plus the compiled Java interfaces and records. Shipped
   as a release artifact and deployed to Maven
   Central, so this also produces the pom, -sources jar, and -javadoc jar
   Central requires."
  [_]
  (b/delete {:path sdk-class-dir})
  (b/copy-dir {:src-dirs   ["sdk/src"]
               :target-dir sdk-class-dir})
  (b/copy-file {:src    "sdk/LICENSE"
                :target (str sdk-class-dir "/META-INF/LICENSE")})
  (b/javac {:src-dirs   ["sdk/src"]
            :class-dir  sdk-class-dir
            :basis      (b/create-basis {:project "sdk/deps.edn"})
            :javac-opts ["--release" "21"]})
  (b/write-pom {:class-dir sdk-class-dir
                :lib       sdk-lib
                :version   version
                :basis     (b/create-basis {:project "sdk/deps.edn"})
                :src-dirs  ["sdk/src"]
                :scm       {:url                 github-url
                            :connection          (str "scm:git:" github-url ".git")
                            :developerConnection (str "scm:git:ssh://git@github.com/spies-and-spiders/companion.git")
                            :tag                 version}
                :pom-data  sdk-pom-data})
  (b/jar {:class-dir sdk-class-dir
          :jar-file  sdk-jar-file})
  (b/copy-file {:src    (format "%s/META-INF/maven/%s/%s/pom.xml"
                                sdk-class-dir (namespace sdk-lib) (name sdk-lib))
                :target sdk-pom-file})
  (b/jar {:class-dir "sdk/src"
          :jar-file  sdk-sources-jar-file})
  (sdk-javadoc)
  (println "Built" sdk-jar-file "+ pom, sources, and javadoc jars"))

(defn uber [{:keys [aliases]}]
  (clean nil)
  (let [namespaces (sns-namespaces ["src" "sdk/src"])]
    (b/copy-dir {:src-dirs   ["src" "resources" "sdk/src"]
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
