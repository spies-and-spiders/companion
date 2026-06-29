(ns sns.server.jar-test
  "End-to-end test of external JAR plugin loading: builds a tiny jar containing a
   LootGenerator at runtime, then loads and invokes it through the registry."
  (:require
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.test :refer [deftest is testing]]
    [sns.server.registry :as registry]
    [sns.spi.protocols :as p]))

(def ^:private plugin-source
  "(ns testplugin.loot
     (:require [sns.spi.protocols :as p]))
   (defn generator [_plugin]
     (reify p/LootGenerator
       (loot-spec [_] {:id :test-jar :label \"Test Jar\"})
       (generate [_ _ctx] {:loot/title \"From a JAR\"})))")

(defn- build-plugin-jar! []
  (let [dir (io/file (System/getProperty "java.io.tmpdir")
                     (str "sns-jar-test-" (System/currentTimeMillis)))
        ns-dir (io/file dir "testplugin")
        jar (io/file dir "plugin.jar")]
    (.mkdirs ns-dir)
    (spit (io/file ns-dir "loot.clj") plugin-source)
    (let [{:keys [exit err]} (shell/sh "jar" "cf" (str jar) "-C" (str dir) "testplugin")]
      (when-not (zero? exit)
        (throw (ex-info "failed to build test jar" {:err err}))))
    (str jar)))

(deftest loads-generator-from-external-jar
  (testing "a generator defined in an external jar is loaded and invoked"
    (let [jar (build-plugin-jar!)
          gen (registry/build-generator
                {:type       :jar                       :id :test-jar :jar jar
                 :entrypoint 'testplugin.loot/generator})]
      (is (= {:id :test-jar :label "Test Jar"} (p/loot-spec gen)))
      (is (= "From a JAR" (:loot/title (p/generate gen {})))))))

(deftest missing-jar-throws
  (is (thrown? Exception
               (registry/build-generator
                 {:type       :jar            :id :nope :jar "/no/such/plugin.jar"
                  :entrypoint 'nope/generator}))))
