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
(def ^:private version "0.1.0")
(def ^:private class-dir "target/classes")
(def ^:private entrypoint 'sns.server.core)
;; Native-image entrypoint that preloads every sns.* namespace at build time so
;; config-driven plugins land in the closed-world image. Lives under native/ (only
;; on the classpath via the :graalvm alias). See native/sns/aot_preload.clj.
(def ^:private native-entrypoint 'sns.aot-preload)
(def ^:private uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

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
