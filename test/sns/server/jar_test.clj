(ns sns.server.jar-test
  "End-to-end test of external JAR plugin loading: builds a tiny jar containing a
   LootGenerator at runtime, then loads and invokes it through the registry."
  (:require
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.classpath :as classpath]
    [sns.server.registry :as registry]))

(def ^:private plugin-source
  "(ns testplugin.loot
     (:require [sns.sdk.protocols :as p]))
   (defn generator [_plugin]
     (reify p/LootGenerator
       (loot-spec [_] {:id :test-jar :label \"Test Jar\"})
       (generate [_ _ctx] {:loot/title \"From a JAR\"})))
   (defn other-generator [_plugin]
     (reify p/LootGenerator
       (loot-spec [_] {:id :test-jar-2 :label \"Test Jar 2\"})
       (generate [_ _ctx] {:loot/title \"Also from a JAR\"})))")

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

(def ^:private java-plugin-source
  "package testplugin;

   import java.util.List;
   import java.util.Map;
   import sns.sdk.LootAction;
   import sns.sdk.LootGenerator;
   import sns.sdk.Models;

   public class JavaLoot implements LootGenerator, LootAction {
       public Models.LootSpec lootSpec() {
           return new Models.LootSpec(\"java-jar\", \"Java Jar\");
       }

       private static Models.ViewModel blade(int keen) {
           return new Models.ViewModel(
               \"From Java\", null,
               List.of(new Models.Section(null,
                   List.of(new Models.Item(null, \"+{{ keen }} keener\", null,
                       Map.of(\"keen\", new Models.ItemVar(keen, \"int\")))))),
               List.of(new Models.Action(\"Sharpen\", \"sharpen\", Map.of())),
               null, null, Map.of(\"tier\", keen), null);
       }

       public Models.ViewModel generate(Map<String, Object> ctx) {
           return blade(0);
       }

       public Models.ViewModel handleAction(Map<String, Object> ctx, String action,
                                            Map<String, Object> params) {
           Models.ViewModel shown = (Models.ViewModel) ctx.get(\"view-model\");
           Number keen = (Number) shown.sections().get(0).items().get(0)
                                       .vars().get(\"keen\").value();
           return blade(keen.intValue() + 1);
       }
   }")

(defn- build-java-plugin-jar!
  "Compile a pure-Java `LootGenerator` (no Clojure entrypoint) and jar it."
  []
  (let [dir (io/file (System/getProperty "java.io.tmpdir")
                     (str "sns-jar-test-java-" (System/currentTimeMillis)))
        src (io/file dir "testplugin" "JavaLoot.java")
        jar (io/file dir "plugin.jar")]
    (.mkdirs (.getParentFile src))
    (spit src java-plugin-source)
    (doseq [cmd [["javac" "-cp" (System/getProperty "java.class.path") "-d" (str dir) (str src)]
                 ["jar" "cf" (str jar) "-C" (str dir) "testplugin"]]]
      (let [{:keys [exit err]} (apply shell/sh cmd)]
        (when-not (zero? exit)
          (throw (ex-info "failed to build java test jar" {:cmd cmd :err err})))))
    (str jar)))

(deftest loads-generator-from-external-jar
  (testing "a generator defined in an external jar is loaded and invoked"
    (let [jar (build-plugin-jar!)
          gen (registry/build-generator
                {:type :jar                                               :id :test-jar
                 :jar  {:path jar :entrypoint 'testplugin.loot/generator}})]
      (is (= {:id :test-jar :label "Test Jar"} (p/loot-spec gen)))
      (is (= "From a JAR" (:loot/title (p/generate gen {})))))))

(deftest reuses-loader-across-plugins-from-one-jar
  (testing "several plugins may name the same jar, which is loaded only once"
    (let [jar (build-plugin-jar!)
          reg (registry/build
                {:plugins [{:type :jar                                               :id :test-jar
                            :jar  {:path jar :entrypoint 'testplugin.loot/generator}}
                           {:type :jar                                                     :id :test-jar-2
                            :jar  {:path jar :entrypoint 'testplugin.loot/other-generator}}]})]
      (is (= "From a JAR" (:loot/title (p/generate (:test-jar reg) {}))))
      (is (= "Also from a JAR" (:loot/title (p/generate (:test-jar-2 reg) {}))))
      (is (identical? (classpath/add-jar! jar) (classpath/add-jar! jar))
          "the same shared classloader is returned rather than a fresh one per call"))))

(deftest loads-class-from-external-jar
  (testing "a :class plugin is constructed via its 0-arity constructor"
    (let [jar (build-java-plugin-jar!)
          gen (registry/build-generator
                {:type :jar :id :java-jar :jar {:path jar :class "testplugin.JavaLoot"}})]
      (is (= {:id :java-jar :label "Java Jar"} (p/loot-spec gen)))
      (is (= "From Java" (:loot/title (p/generate gen {})))))))

(deftest java-plugin-actions-read-the-displayed-view-model
  (testing "a Java :class plugin returns vars and state, and its action is handed
            the DM-edited view-model back as a Models.ViewModel"
    (let [jar (build-java-plugin-jar!)
          gen (registry/build-generator
                {:type :jar :id :java-jar :jar {:path jar :class "testplugin.JavaLoot"}})
          vm  (p/generate gen {})]
      (is (= {:keen {:value 0 :type :int}}
             (-> vm :loot/sections first :section/items first :item/vars)))
      (is (= 0 (get (:loot/state vm) "tier")))
      (testing "the DM's edit, not the generated value, is what the action builds on"
        (let [edited (assoc-in vm [:loot/sections 0 :section/items 0 :item/vars :keen :value] 7)
              after  (p/handle-action gen {:view-model edited} :sharpen {})]
          (is (= 8 (-> after :loot/sections first :section/items first :item/vars :keen :value)))
          (is (= 8 (get (:loot/state after) "tier"))))))))

(deftest missing-jar-throws
  (is (thrown? Exception
               (registry/build-generator
                 {:type :jar                                                      :id :nope
                  :jar  {:path "/no/such/plugin.jar" :entrypoint 'nope/generator}}))))
