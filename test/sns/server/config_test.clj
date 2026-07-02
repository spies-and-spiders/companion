(ns sns.server.config-test
  (:require
    [aero.core :as aero]
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [malli.error :as me]
    [sns.server.config :as config]
    [sns.spi.schema :as schema]))

(defn- write-temp-json [content]
  (let [f (java.io.File/createTempFile "sns-config" ".json")]
    (.deleteOnExit f)
    (spit f content)
    f))

(deftest loads-json-config
  (testing "a JSON config decodes strings to keywords/symbols and validates"
    (let [f   (write-temp-json
                (str "{\"storage\":{\"backend\":\"file\"},"
                     "\"plugins\":[{\"type\":\"builtin\",\"id\":\"dust\","
                     "\"entrypoint\":\"sns.builtin.dust/generator\"},"
                     "{\"type\":\"data\",\"id\":\"uniques\",\"source\":\"data/uniques.edn\"}],"
                     "\"loot-table\":[{\"id\":\"dust\",\"weight\":40},{\"id\":\"uniques\"}]}"))
          cfg (config/load-config f)]
      (is (= :file (-> cfg :storage :backend)))
      (is (= [:builtin :data] (mapv :type (:plugins cfg))))
      (is (= 'sns.builtin.dust/generator (-> cfg :plugins first :entrypoint)))
      (is (= [:dust :uniques] (mapv :id (:plugins cfg))))
      (is (= [40 nil] (mapv :weight (:loot-table cfg)))))))

(deftest rejects-invalid-json-config
  (testing "an unknown plugin type fails validation"
    (let [f (write-temp-json "{\"plugins\":[{\"type\":\"bogus\",\"id\":\"x\"}]}")]
      (is (thrown? Exception (config/load-config f))))))

(def ^:private fixture-config "test/resources/config.edn")

(deftest loads-edn-config-from-filesystem
  (testing "an EDN config loads from an explicit filesystem path"
    (let [cfg (config/load-config fixture-config)]
      (is (seq (:plugins cfg)))
      (is (= :data (-> cfg :plugins (nth 2) :type)))))
  (testing "no-arg load discovers a config file in the working directory"
    ;; Exercise the default-source discovery without depending on a
    ;; (git-ignored) config.edn at the repo root: point the search at the
    ;; fixture via its absolute path so the test is hermetic in CI.
    (with-redefs [config/default-paths [(.getAbsolutePath (io/file fixture-config))]]
      (is (seq (:plugins (config/load-config)))))))

(deftest example-config-matches-schema
  (testing "examples/config.edn conforms to the ::config schema"
    ;; :reporting secrets resolve from #env at runtime; substitute placeholders
    ;; so the check is hermetic (doesn't depend on those vars being set).
    (let [cfg (-> (aero/read-config (io/file "examples/config.edn"))
                  (update :reporting merge {:webhook-url "https://example.test/webhook"
                                            :avatar-url  "https://example.test/avatar.png"}))
          err (schema/explain ::schema/config cfg)]
      (is (nil? err) (some-> err me/humanize pr-str)))))
