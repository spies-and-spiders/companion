(ns sns.server.engine-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.config :as config]
    [sns.server.engine :as engine]
    [sns.server.store.edn :as edn-store]))

(def ^:private test-config
  {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}]
   :loot-table [{:id :divine-dust :weight 100}]})

(deftest builds-registry-and-generates
  (let [eng (engine/create test-config)]
    (testing "loot-specs lists the registered type, with its collections defaulted"
      (is (= [{:id :divine-dust :label "Divine Dust" :store/collections [:divine-dust]}]
             (engine/loot-specs eng))))
    (testing "generate returns a validated view-model"
      (is (= {:loot/title    "Divine Dust"
              :loot/subtitle "A pinch of divine residue"}
             (dissoc (engine/generate eng :divine-dust) :loot/words))))
    (testing "roll picks from the loot-table, returning the chosen id and view-model"
      (is (= {:id         :divine-dust
              :view-model {:loot/title    "Divine Dust"
                           :loot/subtitle "A pinch of divine residue"}}
             (update (engine/roll eng) :view-model dissoc :loot/words))))
    (testing "an unknown loot type is rejected"
      (is (thrown? Exception (engine/generate eng :nonexistent))))))

(deftest loot-table-without-weights-is-uniform
  (testing "a loot-table entry may omit :weight (defaults to 1, sampled uniformly)"
    (let [eng (engine/create
                {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}]
                 :loot-table [{:id :divine-dust}]})]
      (is (= :divine-dust (:id (engine/roll eng)))))))

(deftest roll-by-number-resolves-via-allocation
  (let [eng (engine/create
              {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator}
                            {:type :builtin :id :other :entrypoint 'sns.builtin.dust/generator}]
               :loot-table [{:id :divine-dust :weight 40} {:id :other :weight 60}]})]
    (testing "a number lands in the weighted slice it belongs to (1-40 vs 41-100)"
      (is (= :divine-dust (:id (engine/roll eng {} 1))))
      (is (= :divine-dust (:id (engine/roll eng {} 40))))
      (is (= :other (:id (engine/roll eng {} 41))))
      (is (= :other (:id (engine/roll eng {} 100)))))
    (testing "out-of-range and non-integer rolls are rejected"
      (is (thrown? Exception (engine/roll eng {} 0)))
      (is (thrown? Exception (engine/roll eng {} 101)))
      (is (thrown? Exception (engine/roll eng {} 4.5))))))

(deftest roll-by-number-normalises-arbitrary-weight-totals
  (testing "weights that don't sum to 100 are stretched onto the 1-100 scale"
    (let [eng (engine/create
                {:plugins    [{:type :builtin :id :a :entrypoint 'sns.builtin.dust/generator}
                              {:type :builtin :id :b :entrypoint 'sns.builtin.dust/generator}
                              {:type :builtin :id :c :entrypoint 'sns.builtin.dust/generator}]
                 :loot-table [{:id :a} {:id :b} {:id :c}]})]
      (is (= :a (:id (engine/roll eng {} 1))))
      (is (= :a (:id (engine/roll eng {} 34))))
      (is (= :b (:id (engine/roll eng {} 35))))
      (is (= :c (:id (engine/roll eng {} 100)))))))

(def ^:private utility-plugin
  ;; The command never runs during engine creation — only its loot-spec is read.
  {:type :cli :id :tools :command ["true"] :utility? true})

(deftest validates-loot-table-at-startup
  (testing "a utility in the loot-table is rejected"
    (is (thrown? Exception
                 (engine/create
                   {:plugins    [utility-plugin]
                    :loot-table [{:id :tools}]}))))
  (testing "a utility registers fine when kept off the table"
    (let [eng (engine/create {:plugins [utility-plugin]})]
      (is (true? (:utility? (first (engine/loot-specs eng)))))))
  (testing "an unknown loot-table id is rejected"
    (is (thrown? Exception
                 (engine/create
                   {:plugins    [{:type :builtin :id :divine-dust}]
                    :loot-table [{:id :nonexistent}]})))))

(deftest folds-config-hidden-into-loot-specs
  (let [eng (engine/create
              {:plugins    [{:type :builtin :id :divine-dust :entrypoint 'sns.builtin.dust/generator :hidden? true}
                            {:type :builtin :id :other :entrypoint 'sns.builtin.dust/generator}]
               :loot-table [{:id :divine-dust}]})]
    (testing "the flag is applied by the engine, so it works for a generator that knows nothing of it"
      (is (= [true nil] (mapv :hidden? (engine/loot-specs eng)))))
    (testing "a hidden type is still listed (the UI needs its spec) and still rollable"
      (is (= [:divine-dust :other] (mapv :id (engine/loot-specs eng))))
      (is (= :divine-dust (:id (engine/roll eng)))))
    (testing "and is still generable by id, e.g. for a follow-up action"
      (is (= "Divine Dust" (:loot/title (engine/generate eng :divine-dust)))))))

(deftest rejects-duplicate-ids
  (is (thrown? Exception
               (engine/create
                 {:plugins [{:type :builtin :id :dup :entrypoint 'sns.builtin.dust/generator}
                            {:type :builtin :id :dup :entrypoint 'sns.builtin.dust/generator}]}))))

(deftest loads-and-validates-resource-config
  (testing "a real filesystem config loads, validates, and drives the engine"
    ;; Load a committed, hermetic fixture rather than the git-ignored repo-root
    ;; config.edn (absent in CI). Override the store to keep the test in-memory.
    (let [eng (engine/create (config/load-config "test/resources/config.edn")
                             {:store (edn-store/create {:backend :memory})})]
      (is (= "Divine Dust" (:loot/title (engine/generate eng :divine-dust))))
      (is (re-find #"Relic" (:loot/subtitle (engine/generate eng :relics)))))))

(defn- title-var
  "The value the title template would interpolate — templates are rendered in
   the browser, so the backend asserts on the var, not on text."
  [vm k]
  (get-in vm [:loot/vars k :value]))

(deftest data-inline-spec
  (let [spec {:label "Omen" :items [{:text "a crow lands"}] :title "{{text}}"}]
    (testing "an :inline spec is used in place of a file"
      (let [eng (engine/create {:plugins [{:type :data :id :omens :inline spec}]})]
        (is (= "a crow lands" (title-var (engine/generate eng :omens) :text)))))
    (testing ":inline takes precedence over :source, which is not read"
      (let [eng (engine/create {:plugins [{:type   :data
                                           :id     :omens
                                           :inline spec
                                           :source "test/resources/does-not-exist.edn"}]})]
        (is (= "a crow lands" (title-var (engine/generate eng :omens) :text)))))))

(deftest config-randoms-are-available-to-vars
  (testing "a config-declared preset is drawn from by a plugin's declared var"
    (let [eng (engine/create
                {:randoms {:omens ["a crow lands" "the lanterns gutter"]}
                 :plugins [{:type   :data
                            :id     :portents
                            :inline {:label "Portent"
                                     :items [{:kind      :portent
                                              :item/vars {:x {:random :omens}}}]
                                     :title "You see {{x}}."}}]})
          vm  (engine/generate eng :portents)]
      (is (= "You see {{x}}." (:loot/title vm)))
      (is (contains? #{"a crow lands" "the lanterns gutter"} (title-var vm :x)))
      (testing "and the UI is offered the same vocabulary to edit it against"
        (is (= ["a crow lands" "the lanterns gutter"] (get-in vm [:loot/vars :x :options])))))))

(deftest input-defaults-fill-blank-values
  (let [eng (engine/create
              {:plugins [{:type :data :id :potion :source "test/resources/enum-default.edn"}]})]
    (testing "a blank enum input falls back to its declared :default"
      (is (= "common" (title-var (engine/generate eng :potion {}) :rarity)))
      (is (= "common" (title-var (engine/generate eng :potion {:rarity ""}) :rarity))))
    (testing "a provided value overrides the default"
      (is (= "rare" (title-var (engine/generate eng :potion {:rarity "rare"}) :rarity))))))

(deftest decimal-inputs-reach-the-generator-as-exact-numbers
  ;; The plugin echoes the raw request back as its title, so these assert on the
  ;; literal JSON sent over the wire — parsing it first would round the precision
  ;; away in the test harness rather than in the engine.
  (let [eng   (engine/create
                {:plugins [{:type    :cli
                            :id      :echo
                            :label   "Echo"
                            :inputs  [{:id :multiplier :label "Multiplier" :type :decimal :default "1.3"}]
                            :command ["python3" "-c"
                                      (str "import sys,json; "
                                           "print(json.dumps({'title': sys.stdin.read()}))")]}]})
        sent  #(:loot/title (engine/generate eng :echo %))]
    (testing "a :decimal field is sent as a JSON number, not the form's string"
      (is (= "{\"inputs\":{\"multiplier\":1.3}}" (sent {:multiplier "1.3"}))))
    (testing "it keeps a BigDecimal's precision, which a double would round away"
      (is (= "{\"inputs\":{\"multiplier\":1.0000000000000000001}}"
             (sent {:multiplier "1.0000000000000000001"}))))
    (testing "a blank field falls back to its :default, coerced the same way"
      (is (= "{\"inputs\":{\"multiplier\":1.3}}" (sent {:multiplier ""}))))
    (testing "an unparseable value is passed through for the generator to reject"
      (is (= "{\"inputs\":{\"multiplier\":\"abc\"}}" (sent {:multiplier "abc"}))))))

(deftest int-inputs-reach-the-generator-as-numbers
  (let [eng (engine/create
              {:plugins [{:type    :cli
                          :id      :echo
                          :label   "Echo"
                          :inputs  [{:id :bonus :label "Bonus" :type :int}]
                          :command ["python3" "-c"
                                    (str "import sys,json; d=json.load(sys.stdin); "
                                         "print(json.dumps({'title': type(d['inputs']['bonus']).__name__}))")]}]})]
    (testing "a :int field entered in the browser form arrives parsed"
      (is (= "int" (:loot/title (engine/generate eng :echo {:bonus "3"})))))
    (testing "an unparseable value is passed through for the generator to reject"
      (is (= "str" (:loot/title (engine/generate eng :echo {:bonus "abc"})))))))

(deftest list-inputs-reach-the-generator-as-a-vector
  (let [echo-bonuses ["python3" "-c"
                      (str "import sys,json; d=json.load(sys.stdin); "
                           "print(json.dumps({'title': json.dumps(d['inputs']['bonuses'])}))")]
        eng          (engine/create
                       {:plugins [{:type    :cli
                                   :id      :echo
                                   :label   "Echo"
                                   :inputs  [{:id :bonuses :label "Bonuses" :type :int :list? true}]
                                   :command echo-bonuses}]})]
    (testing "several entered values are coerced element-wise and sent as a JSON array"
      (is (= "[1, 2, 3]" (:loot/title (engine/generate eng :echo {:bonuses ["1" "2" "3"]})))))
    (testing "a single value still arrives as a one-element vector, not a bare scalar"
      (is (= "[5]" (:loot/title (engine/generate eng :echo {:bonuses ["5"]})))))
    (testing "nothing submitted defaults to an empty vector"
      (is (= "[]" (:loot/title (engine/generate eng :echo {}))))))
  (testing "a declared :default is used, coerced, when nothing is submitted"
    (let [eng (engine/create
                {:plugins [{:type    :cli
                            :id      :echo
                            :label   "Echo"
                            :inputs  [{:id :bonuses :label "Bonuses" :type :int :list? true :default ["1" "2"]}]
                            :command ["python3" "-c"
                                      (str "import sys,json; d=json.load(sys.stdin); "
                                           "print(json.dumps({'title': json.dumps(d['inputs']['bonuses'])}))")]}]})]
      (is (= "[1, 2]" (:loot/title (engine/generate eng :echo {})))))))

;; --- issue #8: the current view-model reaches the action -----------------------

(defrecord ^:private EchoStateGenerator []
  p/LootGenerator
  (loot-spec [_] {:id :echo-state :label "Echo State"})
  (generate [_ _ctx] {:loot/title "Echo State" :loot/state {:count 0}})
  p/LootAction
  ;; Reads its own state back off the view-model it was handed, rather than
  ;; from :params — the campaign5 pattern.
  (handle-action [_ {:keys [view-model]} _action _params]
    {:loot/title (str "count=" (inc (:count (:loot/state view-model) 0)))
     :loot/state (update (:loot/state view-model {:count 0}) :count inc)}))

(defn echo-state-generator [_plugin-config] (->EchoStateGenerator))

(deftest action-receives-the-current-view-model
  (let [eng (engine/create {:plugins [{:type :builtin :id :echo-state :entrypoint 'sns.server.engine-test/echo-state-generator}]})]
    (testing "a generator reads the displayed (possibly DM-edited) view-model, so
              state round-trips without being copied into every action's params"
      (let [vm (engine/generate eng :echo-state)]
        (is (= {:count 0} (:loot/state vm)))
        (let [vm' (engine/handle-action eng :echo-state :bump {} vm)]
          (is (= "count=1" (:loot/title vm')))
          (testing "a DM edit to the round-tripped state is what the next action sees"
            (is (= "count=6" (:loot/title (engine/handle-action eng :echo-state :bump {}
                                                                (assoc vm' :loot/state {:count 5})))))))))
    (testing "an action invoked without a view-model still works (nil :loot/state)"
      (is (= "count=1" (:loot/title (engine/handle-action eng :echo-state :bump {} nil)))))))

(defn- declaring
  "A generator returning `view-model` verbatim, for exercising the engine's
   handling of the writes a plugin declares on it."
  [view-model]
  (reify p/LootGenerator
    (loot-spec [_] {:id :writer :label "Writer"})
    (generate [_ _] view-model)))

(deftest declared-writes-are-applied-after-validation
  (let [store (doto (edn-store/create {:backend :memory}) p/setup!)
        eng   (-> (engine/create {:plugins []} {:store store})
                  (assoc-in [:registry :writer]
                            (declaring {:loot/title      "Written"
                                        :store/mutations {:things {"a" {:n 1}}}})))]
    (testing "a plugin's declared writes reach the store"
      (is (= "Written" (:loot/title (engine/generate eng :writer))))
      (is (= {"a" {:n 1}} (p/read-collection store :things))))
    (testing "an invalid view-model leaves the store exactly as it was"
      (let [eng (assoc-in eng [:registry :writer]
                          ;; no :loot/title, so validation rejects it
                          (declaring {:loot/subtitle   "Broken"
                                      :store/mutations {:things {"a" nil "b" {:n 2}}}}))]
        (is (thrown? Exception (engine/generate eng :writer)))
        (is (= {"a" {:n 1}} (p/read-collection store :things))
            "the retraction and the insert both went nowhere")))))

(deftest browser-state-is-read-and-written-per-request
  (let [eng (-> (engine/create {:storage {:backend :browser} :plugins []})
                (assoc-in [:registry :writer]
                          (declaring {:loot/title      "Written"
                                      :store/mutations {:things {"b" {:n 2}}}})))]
    (testing "the client's state is what the plugin reads, and is not kept"
      (let [seeded (engine/with-state eng {:things {"a" {:n 1}}})]
        (is (= {"a" {:n 1}} (p/read-collection (:store seeded) :things)))
        (is (= {} (p/read-collection (:store (engine/with-state eng nil)) :things)))))
    (testing "declared writes come back on the view-model for the client to apply"
      (let [vm (engine/generate (engine/with-state eng {:things {"a" {:n 1}}}) :writer)]
        (is (= {:things {"b" {:n 2}}} (:store/mutations vm)))))))

(deftest words-are-drawn-by-the-engine
  (let [eng (engine/create test-config)]
    (testing "every result carries two words from the built-in vocabulary"
      (let [words (:loot/words (engine/generate eng :divine-dust))]
        (is (= 2 (count words)))
        (is (every? string? words))
        (is (apply not= words) "the pair is drawn without replacement")))
    (testing "a config-declared vocabulary replaces the built-in one"
      (let [eng (engine/create (assoc test-config :words ["alpha" "beta"]))]
        (is (= #{"alpha" "beta"} (set (:loot/words (engine/generate eng :divine-dust)))))))
    (testing ":extra-words widens the built-in vocabulary rather than replacing it"
      (let [eng (engine/create (assoc test-config :extra-words ["zzz-extra"]))]
        (is (< 2 (count (:words eng))))
        (is (some #{"zzz-extra"} (:words eng)))))))
