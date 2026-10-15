(ns sns.server.config-test
  (:require
    [aero.core :as aero]
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [malli.error :as me]
    [sns.sdk.schema :as schema]
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
                     "\"tools\":[{\"type\":\"builtin\",\"id\":\"dust\","
                     "\"builtin\":{\"entrypoint\":\"sns.builtin.dust/generator\"}},"
                     "{\"type\":\"data\",\"id\":\"uniques\",\"data\":{\"source\":\"data/uniques.edn\"}}],"
                     "\"loot-table\":[{\"id\":\"dust\",\"weight\":40},{\"id\":\"uniques\"}]}"))
          cfg (config/load-config f)]
      (is (= :file (-> cfg :storage :backend)))
      (is (= [:builtin :data] (mapv :type (:tools cfg))))
      (is (= 'sns.builtin.dust/generator (-> cfg :tools first :builtin :entrypoint)))
      (is (= [:dust :uniques] (mapv :id (:tools cfg))))
      (is (= [40 nil] (mapv :weight (:loot-table cfg)))))))

(deftest json-loot-table-ranges-decode
  (let [f (write-temp-json
            (str "{\"tools\":[{\"type\":\"builtin\",\"id\":\"dust\"}],"
                 "\"loot-table\":[{\"id\":\"dust\",\"ranges\":[[1,98],[99,100]]}]}"))]
    (is (= [{:id :dust :ranges [[1 98] [99 100]]}] (:loot-table (config/load-config f))))))

(deftest json-data-plugin-keeps-its-templates
  (testing "a JSON :data view-model's templates stay strings, and its lets decode
            to keywords"
    (let [f    (write-temp-json
                 (str "{\"tools\":[{\"type\":\"data\",\"id\":\"cards\",\"data\":{\"inline\":"
                      "{\"tables\":{\"cards\":[{\"result\":\"Draw 2.\"}]},"
                      "\"let\":{\"card\":{\"choose\":\"cards\"}},"
                      "\"view-model\":{\"loot/title\":\"Tarot\",\"loot/sections\":[{\"section/items\":"
                      "[{\"item/title\":\"A card\",\"item/body\":\"{{card.result}}\"}]}]}}}}]}"))
          spec (-> (config/load-config f) :tools first :data :inline)]
      (is (= :cards (-> spec :let :card :choose)))
      (is (= "{{card.result}}" (-> spec :view-model :loot/sections first :section/items first :item/body))))))

(deftest json-history-decodes-to-keywords
  (testing "the global setting and a plugin's override both arrive as keywords"
    (let [f   (write-temp-json
                (str "{\"history\":\"on-report\","
                     "\"tools\":[{\"type\":\"cli\",\"id\":\"weather\","
                     "\"cli\":{\"command\":[\"echo\"]},\"generator\":{\"history\":\"always\"}}]}"))
          cfg (config/load-config f)]
      (is (= :on-report (:history cfg)))
      (is (= :always (-> cfg :tools first :generator :history)))))
  (testing "an unknown mode fails validation"
    (let [f (write-temp-json "{\"history\":\"sometimes\",\"tools\":[]}")]
      (is (thrown? Exception (config/load-config f))))))

(deftest json-pages-decode-to-keywords
  (let [f (write-temp-json
            (str "{\"tools\":[{\"type\":\"cli\",\"id\":\"weather\",\"cli\":{\"command\":[\"echo\"]}},"
                 "{\"type\":\"page\",\"id\":\"combat\",\"page\":{\"tools\":[\"weather\"]}}]}"))]
    (is (= {:type :page :id :combat :page {:tools [:weather]}} (second (:tools (config/load-config f))))))
  (testing "an empty page fails validation"
    (let [f (write-temp-json "{\"tools\":[{\"type\":\"page\",\"id\":\"combat\",\"page\":{\"tools\":[]}}]}")]
      (is (thrown? Exception (config/load-config f))))))

(deftest rejects-invalid-json-config
  (testing "an unknown plugin type fails validation"
    (let [f (write-temp-json "{\"tools\":[{\"type\":\"bogus\",\"id\":\"x\"}]}")]
      (is (thrown? Exception (config/load-config f))))))

(def ^:private fixture-config "test/resources/config.edn")

(deftest loads-edn-config-from-filesystem
  (testing "an EDN config loads from an explicit filesystem path"
    (let [cfg (config/load-config fixture-config)]
      (is (seq (:tools cfg)))
      (is (= :data (-> cfg :tools (nth 2) :type)))))
  (testing "no-arg load discovers a config file in the working directory"
    ;; The repo-root config.edn is git-ignored, so search for the fixture instead.
    (with-redefs [config/default-paths [(.getAbsolutePath (io/file fixture-config))]]
      (is (seq (:tools (config/load-config)))))))

(deftest example-config-matches-schema
  (testing "examples/config.edn conforms to the ::config schema"
    ;; Stand in for the #env :reporting secrets.
    (let [cfg (-> (aero/read-config (io/file "examples/config.edn"))
                  (update-in [:reporting :discord] merge {:webhook-url "https://example.test/webhook"
                                                          :avatar-url  "https://example.test/avatar.png"}))
          err (schema/explain ::schema/config cfg)]
      (is (nil? err) (some-> err me/humanize pr-str)))))
