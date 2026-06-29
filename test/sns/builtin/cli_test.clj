(ns sns.builtin.cli-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.builtin.cli :as cli]
    [sns.spi.protocols :as p]
    [sns.spi.schema :as schema]))

(deftest converts-stdout-json-to-view-model
  (testing "friendly JSON on stdout becomes a namespaced, valid view-model"
    (let [cmd ["bash" "-c"
               (str "cat >/dev/null; "
                    "printf '%s' '{\"title\":\"Fogfall\",\"subtitle\":\"weather\","
                    "\"sections\":[{\"heading\":\"Sky\",\"items\":["
                    "{\"body\":\"Thick fog rolls in.\",\"tags\":[\"obscured\"]}]}]}'")]
          gen (cli/generator :weather cmd "Weather")
          vm  (p/generate gen {:inputs {} :session nil})]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Fogfall" (:loot/title vm)))
      (is (= "Sky" (-> vm :loot/sections first :section/heading)))
      (is (= ["obscured"] (-> vm :loot/sections first :section/items first :item/tags))))))

(deftest receives-context-on-stdin
  (testing "the request context is delivered as JSON on stdin"
    (let [cmd ["python3" "-c"
               "import sys,json; d=json.load(sys.stdin); print(json.dumps({'title': d['inputs']['who']}))"]
          gen (cli/generator :echo cmd "Echo")
          vm  (p/generate gen {:inputs {:who "Thoros"} :session nil})]
      (is (= "Thoros" (:loot/title vm))))))

(deftest nonzero-exit-throws
  (let [gen (cli/generator :boom ["bash" "-c" "exit 3"] "Boom")]
    (is (thrown? Exception (p/generate gen {:inputs {} :session nil})))))

(deftest example-script-runs
  (testing "the shipped example weather.py produces a valid view-model"
    (let [gen (cli/generator :weather ["python3" "examples/cli-plugin/weather.py"] "Weather")
          vm  (p/generate gen {:inputs {} :session nil})]
      (is (schema/validate ::schema/view-model vm))
      (is (seq (:loot/title vm))))))
