(ns sns.server.config-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.server.config :as config]))

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

(deftest loads-edn-config-from-filesystem
  (testing "the shipped config.edn loads from the working-directory path"
    (let [cfg (config/load-config "config.edn")]
      (is (seq (:plugins cfg)))
      (is (= :data (-> cfg :plugins (nth 2) :type)))))
  (testing "no-arg load discovers config.edn in the working directory"
    (is (seq (:plugins (config/load-config))))))
