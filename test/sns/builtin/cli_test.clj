(ns sns.builtin.cli-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.builtin.cli :as cli]
    [sns.sdk.protocols :as p]
    [sns.sdk.schema :as schema]))

(deftest converts-stdout-json-to-view-model
  (testing "friendly JSON on stdout becomes a namespaced, valid view-model"
    (let [cmd ["bash" "-c"
               (str "cat >/dev/null; "
                    "printf '%s' '{\"title\":\"Fogfall\",\"subtitle\":\"weather\","
                    "\"sections\":[{\"heading\":\"Sky\",\"items\":["
                    "{\"body\":\"Thick fog rolls in.\",\"metadata\":[\"obscured\"]}]}]}'")]
          gen (cli/generator :weather cmd "Weather")
          vm  (p/generate gen {:inputs {} :session nil})]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Fogfall" (:loot/title vm)))
      (is (= "Sky" (-> vm :loot/sections first :section/heading)))
      (is (= ["obscured"] (-> vm :loot/sections first :section/items first :item/metadata))))))

(deftest receives-context-on-stdin
  (testing "the request context is delivered as JSON on stdin"
    (let [cmd ["python3" "-c"
               "import sys,json; d=json.load(sys.stdin); print(json.dumps({'title': d['inputs']['who']}))"]
          gen (cli/generator :echo cmd "Echo")
          vm  (p/generate gen {:inputs {:who "Thoros"} :session nil})]
      (is (= "Thoros" (:loot/title vm))))))

(deftest utility-flag-surfaces-in-loot-spec
  (is (true? (:utility? (p/loot-spec (cli/generator :init ["true"] "Initiative" true)))))
  (is (nil? (:utility? (p/loot-spec (cli/generator :weather ["true"] "Weather"))))))

(deftest nonzero-exit-throws
  (let [gen (cli/generator :boom ["bash" "-c" "exit 3"] "Boom")]
    (is (thrown? Exception (p/generate gen {:inputs {} :session nil})))))

(deftest invalid-stdout-rejected-against-cli-output
  (testing "stdout that breaks the ::cli-output contract throws before mapping,
            reporting the author's own JSON keys"
    (let [cmd ["bash" "-c"
               (str "cat >/dev/null; "
                    "printf '%s' '{\"title\":\"Loot\",\"sections\":[{\"items\":["
                    "{\"title\":\"no body\"}]}]}'")]
          gen (cli/generator :bad cmd "Bad")
          err (try (p/generate gen {:inputs {} :session nil})
                   (catch clojure.lang.ExceptionInfo e (ex-data e)))]
      (is (= ::schema/cli-output (:schema err)))
      (is (= {:sections [{:items [{:body ["missing required key"]}]}]}
             (:error err))))))

(deftest example-script-runs
  (testing "the shipped example weather.py produces a valid view-model"
    (let [gen (cli/generator :weather ["python3" "examples/cli-plugin/weather.py"] "Weather")
          vm  (p/generate gen {:inputs {} :session nil})]
      (is (schema/validate ::schema/view-model vm))
      (is (seq (:loot/title vm))))))

(deftest friendly-actions-route-back-to-plugin
  (testing "a stdout action becomes a :loot/action event targeting this plugin"
    (let [cmd ["python3" "-c"
               (str "import sys,json; json.load(sys.stdin); "
                    "print(json.dumps({'title':'Blade','actions':["
                    "{'label':'Sharpen','action':'sharpen','params':{'by':1}}]}))")]
          gen (cli/generator :forge cmd "Forge")
          vm  (p/generate gen {:inputs {} :session nil})
          [action] (:loot/actions vm)]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Sharpen" (:action/label action)))
      (is (= [:loot/action {:id :forge :action :sharpen :params {:by 1}}]
             (:action/event action))))))

(deftest handle-action-reinvokes-command-with-action-context
  (testing "handle-action pipes {action,params,session} on stdin and reads a new view-model"
    (let [cmd ["python3" "-c"
               (str "import sys,json; d=json.load(sys.stdin); "
                    "print(json.dumps({'title': d['action'] + ':' + str(d['params']['n'])}))")]
          gen (cli/generator :counter cmd "Counter")
          vm  (p/handle-action gen {:session nil} :bump {:n 5})]
      (is (schema/validate ::schema/view-model vm))
      (is (= "bump:5" (:loot/title vm))))))
