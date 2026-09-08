(ns sns.builtin.cli-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.builtin.cli :as cli]
    [sns.sdk.protocols :as p]
    [sns.sdk.schema :as schema]
    [sns.server.store.edn :as edn-store]))

(deftest converts-stdout-json-to-view-model
  (testing "view-model JSON on stdout reads back as a valid view-model"
    (let [cmd ["bash" "-c"
               (str "cat >/dev/null; "
                    "printf '%s' '{\"loot/title\":\"Fogfall\",\"loot/subtitle\":\"weather\","
                    "\"loot/sections\":[{\"section/heading\":\"Sky\",\"section/items\":["
                    "{\"item/body\":\"Thick fog rolls in.\",\"item/metadata\":[\"obscured\"]}]}]}'")]
          gen (cli/generator {:id :weather :cli {:command cmd} :label "Weather"})
          vm  (p/generate gen {:inputs {}})]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Fogfall" (:loot/title vm)))
      (is (= "Sky" (-> vm :loot/sections first :section/heading)))
      (is (= ["obscured"] (-> vm :loot/sections first :section/items first :item/metadata))))))

(deftest receives-context-on-stdin
  (testing "the request context is delivered as JSON on stdin"
    (let [cmd ["python3" "-c"
               "import sys,json; d=json.load(sys.stdin); print(json.dumps({'loot/title': d['inputs']['who']}))"]
          gen (cli/generator {:id :echo :cli {:command cmd} :label "Echo"})
          vm  (p/generate gen {:inputs {:who "Thoros"}})]
      (is (= "Thoros" (:loot/title vm))))))

(deftest utility-flag-surfaces-in-loot-spec
  (is (true? (:utility? (p/loot-spec (cli/generator {:id :init :cli {:command ["true"]} :label "Initiative" :utility? true})))))
  (is (nil? (:utility? (p/loot-spec (cli/generator {:id :weather :cli {:command ["true"]} :label "Weather"}))))))

(deftest declared-inputs-surface-in-loot-spec
  (testing "config-declared :inputs drive the UI form for an external plugin"
    (let [fields [{:id :who :label "Who" :type :text}]
          spec   (p/loot-spec (cli/generator {:id :echo :cli {:command ["true"]} :inputs fields}))]
      (is (= fields (:inputs spec)))
      (is (schema/validate ::schema/loot-spec spec))))
  (is (nil? (:inputs (p/loot-spec (cli/generator {:id :echo :cli {:command ["true"]}}))))))

(deftest nonzero-exit-throws
  (let [gen (cli/generator {:id :boom :cli {:command ["bash" "-c" "exit 3"]} :label "Boom"})]
    (is (thrown? Exception (p/generate gen {:inputs {}})))))

(deftest invalid-stdout-rejected-against-plugin-output
  (testing "stdout that breaks the ::plugin-output contract throws before mapping,
            reporting the author's own JSON keys"
    (let [cmd ["bash" "-c"
               (str "cat >/dev/null; "
                    "printf '%s' '{\"loot/title\":\"Loot\",\"loot/sections\":[{\"section/items\":["
                    "{\"item/title\":\"no body\"}]}]}'")]
          gen (cli/generator {:id :bad :cli {:command cmd} :label "Bad"})
          err (try (p/generate gen {:inputs {}})
                   (catch clojure.lang.ExceptionInfo e (ex-data e)))]
      (is (= ::schema/plugin-output (:schema err)))
      (is (= {:loot/sections [{:section/items [{:item/body ["missing required key"]}]}]}
             (:error err))))))

(deftest item-vars-survive-the-round-trip
  (testing "an external plugin can carry :item/vars, as a :jar plugin can — the
            keyword-valued :type arrives as a JSON string and decodes"
    (let [cmd ["bash" "-c"
               (str "cat >/dev/null; "
                    "printf '%s' '{\"loot/title\":\"Ring\",\"loot/sections\":[{\"section/items\":["
                    "{\"item/body\":\"+{{ dmg }} fire\",\"item/vars\":{\"dmg\":"
                    "{\"value\":3,\"type\":\"int\"}}}]}]}'")]
          gen (cli/generator {:id :ring :cli {:command cmd} :label "Ring"})
          vm  (p/generate gen {:inputs {}})]
      (is (schema/validate ::schema/view-model vm))
      (is (= {:dmg {:value 3 :type :int}}
             (-> vm :loot/sections first :section/items first :item/vars))))))

(deftest action-request-carries-the-current-view-model
  (testing "an action is sent the result the UI had on screen, DM edits included,
            so a plugin rebuilds from what is displayed rather than from :params"
    (let [cmd ["python3" "-c"
               (str "import sys,json; d=json.load(sys.stdin); "
                    "print(json.dumps({'loot/title': str("
                    "d['view-model']['loot/sections'][0]['section/items'][0]['item/vars']['dmg']['value'])}))")]
          gen (cli/generator {:id :ring :cli {:command cmd} :label "Ring"})
          vm  {:loot/title    "Ring"
               :loot/sections [{:section/items [{:item/body "+{{ dmg }}"
                                                 :item/vars {:dmg {:value 9 :type :int}}}]}]}]
      (is (= "9" (:loot/title (p/handle-action gen {:view-model vm} :level-up {})))))))

(deftest example-script-runs
  (testing "the shipped example weather.py produces a valid view-model"
    (let [gen (cli/generator {:id :weather :cli {:command ["python3" "examples/cli-plugin/weather.py"]} :label "Weather"})
          vm  (p/generate gen {:inputs {}})]
      (is (schema/validate ::schema/view-model vm))
      (is (seq (:loot/title vm))))))

(deftest actions-route-back-to-plugin
  (testing "a stdout action becomes a :loot/action event targeting this plugin"
    (let [cmd ["python3" "-c"
               (str "import sys,json; json.load(sys.stdin); "
                    "print(json.dumps({'loot/title':'Blade','loot/actions':["
                    "{'label':'Sharpen','action':'sharpen','params':{'by':1}}]}))")]
          gen (cli/generator {:id :forge :cli {:command cmd} :label "Forge"})
          vm  (p/generate gen {:inputs {}})
          [action] (:loot/actions vm)]
      (is (schema/validate ::schema/view-model vm))
      (is (= "Sharpen" (:action/label action)))
      (is (= [:loot/action {:id :forge :action :sharpen :params {:by 1}}]
             (:action/event action))))))

(deftest handle-action-reinvokes-command-with-action-context
  (testing "handle-action pipes {action,params} on stdin and reads a new view-model"
    (let [cmd ["python3" "-c"
               (str "import sys,json; d=json.load(sys.stdin); "
                    "print(json.dumps({'loot/title': d['action'] + ':' + str(d['params']['n'])}))")]
          gen (cli/generator {:id :counter :cli {:command cmd} :label "Counter"})
          vm  (p/handle-action gen {} :bump {:n 5})]
      (is (schema/validate ::schema/view-model vm))
      (is (= "bump:5" (:loot/title vm))))))

;; --- declared state (`:store/collections` / `:store/manual`) ------------------

(def ^:private manual
  {:key-label "Character"
   :list?     true
   :fields    [{:id :crystal :label "Crystal" :type :text}
               {:id :chance :label "Flare chance (%)" :type :int :default 10}]})

(defn- store-of [state]
  (edn-store/->MemoryStore (atom state)))

(deftest declared-storage-surfaces-in-loot-spec
  (testing "config-declared storage reaches the spec, so the UI renders its editor"
    (let [spec (p/loot-spec (cli/generator {:id :crystals :cli {:command ["true"]} :store/manual manual}))]
      (is (= manual (:store/manual spec)))
      (is (schema/validate ::schema/loot-spec spec))))
  (testing "a plugin declaring nothing gets no storage keys"
    (let [spec (p/loot-spec (cli/generator {:id :echo :cli {:command ["true"]}}))]
      (is (not (contains? spec :store/manual)))
      (is (not (contains? spec :store/collections))))))

(deftest declared-collections-are-read-and-sent-as-state
  (let [cmd ["python3" "-c"
             (str "import sys,json; d=json.load(sys.stdin); "
                  "print(json.dumps({'loot/title': json.dumps(d.get('state'), sort_keys=True)}))")]
        state {:crystals {"Quincy" [{:crystal "Blaze Wretch" :chance 27}]}
               :other    {"x" {:n 1}}}]
    (testing ":store/manual implies the collection named after the plugin's id"
      (let [gen (cli/generator {:id :crystals :cli {:command cmd} :store/manual manual})
            vm  (p/generate gen {:inputs {} :store (store-of state)})]
        (is (= "{\"crystals\": {\"Quincy\": [{\"chance\": 27, \"crystal\": \"Blaze Wretch\"}]}}"
               (:loot/title vm)))))
    (testing ":store/collections ships exactly what it names"
      (let [gen (cli/generator {:id :crystals :cli {:command cmd} :store/collections [:other]})
            vm  (p/generate gen {:inputs {} :store (store-of state)})]
        (is (= "{\"other\": {\"x\": {\"n\": 1}}}" (:loot/title vm)))))
    (testing "a plugin declaring nothing is sent no state, and the store is untouched"
      (let [gen (cli/generator {:id :echo :cli {:command cmd}})
            vm  (p/generate gen {:inputs {} :store (reify p/Store
                                                     (setup! [_])
                                                     (read-collection [_ _]
                                                       (throw (ex-info "should not read" {}))))})]
        (is (= "null" (:loot/title vm)))))
    (testing "an action call carries the same state"
      (let [gen (cli/generator {:id :crystals :cli {:command cmd} :store/manual manual})
            vm  (p/handle-action gen {:store (store-of state)} :roll {})]
        (is (= "{\"crystals\": {\"Quincy\": [{\"chance\": 27, \"crystal\": \"Blaze Wretch\"}]}}"
               (:loot/title vm)))))))

(deftest returned-mutations-become-declared-writes
  (testing "a script's `store/mutations` reach the view-model with entry keys still strings"
    (let [cmd ["python3" "-c"
               (str "import sys,json; json.load(sys.stdin); "
                    "print(json.dumps({'loot/title':'Logged','store/mutations':"
                    "{'crystals':{'Quincy':{'flares':3},'Viktor':None}}}))")]
          gen (cli/generator {:id :crystals :cli {:command cmd} :store/manual manual})
          vm  (p/generate gen {:inputs {} :store (store-of {})})]
      (is (schema/validate ::schema/view-model vm))
      (is (= {:crystals {"Quincy" {:flares 3} "Viktor" nil}} (:store/mutations vm))
          "collection names keywordise; the keys within a collection do not"))))
