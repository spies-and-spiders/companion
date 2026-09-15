(ns sns.server.jar-test
  "End-to-end test of external JAR plugin loading: builds a tiny jar containing a
   Generator at runtime, then loads and invokes it through the registry."
  (:require
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.classpath :as classpath]
    [sns.server.engine :as engine]
    [sns.server.registry :as registry]))

(def ^:private plugin-source
  "(ns testplugin.loot
     (:require [sns.sdk.protocols :as p]))
   (defn generator [_plugin]
     (reify p/Generator
       (loot-spec [_] {:history :never})
       (generate [_ _ctx] {:loot/title \"From a JAR\"})))
   (defn other-generator [_plugin]
     (reify p/Generator
       (loot-spec [_] {})
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
   import sns.sdk.Action;
   import sns.sdk.Generator;
   import sns.sdk.Models;

   public class JavaLoot implements Generator, Action {
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
  "Compile a pure-Java `Generator` (no Clojure entrypoint) and jar it."
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
      (is (= {:history :never} (p/loot-spec gen)))
      (is (= "From a JAR" (:loot/title (p/generate gen {})))))))

(deftest reuses-loader-across-plugins-from-one-jar
  (testing "several plugins may name the same jar, which is loaded only once"
    (let [jar (build-plugin-jar!)
          reg (registry/build
                {:tools [{:type :jar                                               :id :test-jar
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
      (is (= {} (p/loot-spec gen)) "a Java plugin may leave its loot-spec to the default")
      (is (= "From Java" (:loot/title (p/generate gen {})))))))

(deftest jar-plugins-take-their-description-from-config
  (let [jar (build-java-plugin-jar!)
        eng (engine/create {:tools [{:type      :jar                                                              :id :blade :label "Blade" :section "Arms"
                                     :generator {:history :always :inputs [{:id :edge :label "Edge" :type :int}]}
                                     :jar       {:path jar :class "testplugin.JavaLoot"}}]})]
    (is (= {:id     :blade                                 :label             "Blade"  :section "Arms" :history :always
            :inputs [{:id :edge :label "Edge" :type :int}] :store/collections [:blade]}
           (first (engine/loot-specs eng))))
    (testing "its actions route to the configured id"
      (is (= :blade (-> (engine/generate eng :blade) :loot/actions first :action/event second :id))))))

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
