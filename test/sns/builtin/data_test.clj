(ns sns.builtin.data-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [randy.rng]
    [sns.builtin.data :as data]
    [sns.sdk.protocols]
    [sns.sdk.schema :as schema])
  (:import
    (java.util.random RandomGeneratorFactory)))

(def ^:private ctx {:inputs {}})

(defn- items [vm]
  (-> vm :loot/sections first :section/items))

(def ^:private stub-rng
  "Always picks index 0."
  (reify randy.rng/RandomNumberGenerator
    (next-int [_ _] 0)
    (next-int [_ _ _] 0)))

(deftest choose-with-submods
  (testing "a chosen entry is bound as itself, and its mods are iterated by path"
    (let [spec {:tables     {:uniques [{:name      "Only One"
                                        :base      "armour"
                                        :data/vars {:ability "Wisdom"}
                                        :mods      [{:effect "+4 {{ ability }}." :metadata ["x"]}
                                                    {:effect "Effect B"}]}]}
                :let        {:unique {:choose :uniques}}
                :view-model {:loot/title    "{{unique.name}}"
                             :loot/subtitle "Unique · {{unique.base}}"
                             :loot/sections [{:section/heading "Mods"
                                              :section/items   {:data/each [:unique :mods]
                                                                :data/as   {:item/body     {:data/field :effect}
                                                                            :item/metadata {:data/field :metadata}}}}]}}
          vm   (data/generate spec ctx)]
      (is (schema/validate ::schema/view-model vm))
      (testing "titles travel as templates, with the entry in :loot/vars minus its mods"
        (is (= "{{unique.name}}" (:loot/title vm)))
        (is (= {:value {:name "Only One" :base "armour"} :context? true}
               (-> vm :loot/vars :unique))))
      (testing "the entry's declared vars are shared flat at the loot level"
        (is (= "Wisdom" (-> vm :loot/vars :ability :value)))
        (is (not (-> vm :loot/vars :ability :context?))))
      (is (= ["+4 {{ ability }}." "Effect B"] (map :item/body (items vm))))
      (testing "metadata only present when the entry declares them"
        (is (= ["x"] (-> vm items first :item/metadata)))
        (is (not (contains? (-> vm items second) :item/metadata)))))))

(deftest draw-renders-each-entry
  (let [spec  {:tables     {:rings [{:name "A"} {:name "B"} {:name "C"}]}
               :let        {:rings {:draw :rings :n 2}}
               :view-model {:loot/title    "Two rings"
                            :loot/sections [{:section/items {:data/each :rings
                                                             :data/as   {:item/title "Ring: {{name}}"
                                                                         :item/body  {:data/field :name}}}}]}}
        vm    (data/generate spec ctx)]
    (is (schema/validate ::schema/view-model vm))
    (is (= 2 (count (items vm))))
    (is (= 2 (count (set (map :item/body (items vm))))) "without replacement")
    (testing "each item carries its element's fields as context vars"
      (is (every? #(= (:item/body %) (-> % :item/vars :name :value)) (items vm))))))

(deftest draw-works-with-the-engine-rng
  ;; Regression: drawing many without replacement used randy's shuffle, which
  ;; throws for any rng but a java.util.Random, like the engine's.
  (let [spec {:tables     {:rings [{:name "A"} {:name "B"} {:name "C"}]}
              :let        {:rings {:draw :rings :n 2}}
              :view-model {:loot/title    "Two rings"
                           :loot/sections [{:section/items {:data/each :rings
                                                            :data/as   {:item/body {:data/field :name}}}}]}}
        rng  (.create (RandomGeneratorFactory/of "L64X128MixRandom"))
        vm   (data/generate spec (assoc ctx :rng rng))]
    (is (= 2 (count (set (map :item/body (items vm))))))))

(deftest take-samples-with-replacement
  (let [spec {:tables     {:rings [{:name "A"} {:name "B"}]}
              :let        {:rings {:take :rings :n 3}}
              :view-model {:loot/title    "Rings"
                           :loot/sections [{:section/items {:data/each :rings
                                                            :data/as   {:item/body {:data/field :name}}}}]}}]
    (is (= ["A" "A" "A"] (map :item/body (items (data/generate spec (assoc ctx :rng stub-rng))))))))

(deftest drawn-vars-are-rolled-once-per-element
  (testing "iterating a let twice shows the same rolled values"
    (let [spec {:tables     {:rings [{:name "A" :data/vars {:feat {:random :literal :options (range 1000)}}}]}
                :let        {:rings {:draw :rings :n 1}}
                :view-model {:loot/title    "Rings"
                             :loot/sections [{:section/items {:data/each :rings :data/as {:item/body "{{feat}}"}}}
                                             {:section/items {:data/each :rings :data/as {:item/body "{{feat}}"}}}]}}
          vm   (data/generate spec ctx)
          feat #(-> vm :loot/sections (nth %) :section/items first :item/vars :feat :value)]
      (is (= (feat 0) (feat 1))))))

(deftest static-view-model-needs-no-tables
  (let [vm (data/generate {:view-model {:loot/title    "Tarot cards"
                                        :loot/sections [{:section/items [{:item/body "Draw 1 tarot card"}]}]}}
                          ctx)]
    (is (schema/validate ::schema/view-model vm))
    (is (= "Draw 1 tarot card" (-> vm items first :item/body)))))

(deftest each-splices-into-a-literal-list
  (let [spec {:tables     {:rings [{:name "A"} {:name "B"}]}
              :let        {:rings {:draw :rings :n 2}}
              :view-model {:loot/title    "Rings"
                           :loot/sections [{:section/items [{:item/body "First"}
                                                            {:data/each :rings
                                                             :data/as   {:item/body {:data/field :name}}}]}]}}]
    (is (= 3 (count (items (data/generate spec ctx)))))))

(deftest items-see-enclosing-elements
  (testing "a section built per element passes that element's fields to its items"
    (let [spec {:tables     {:rings [{:name "A" :mods [{:effect "e"}]}]}
                :let        {:rings {:draw :rings :n 1}}
                :view-model {:loot/title    "Rings"
                             :loot/sections {:data/each :rings
                                             :data/as   {:section/heading {:data/field :name}
                                                         :section/items   {:data/each :mods
                                                                           :data/as   {:item/body {:data/field :effect}}}}}}}
          item (-> (data/generate spec ctx) items first)]
      (is (= "e" (:item/body item)))
      (is (= "A" (-> item :item/vars :name :value)))
      (is (= "e" (-> item :item/vars :effect :value))))))

(deftest data-var-exposes-a-draw
  (let [spec {:tables     {:rings [{:name "A" :data/vars {:x 1}} {:name "B"}]}
              :let        {:rings {:draw :rings :n 2}}
              :view-model {:loot/title "{{pluck drawn \"name\"}}"
                           :loot/vars  {:drawn {:data/var :rings}}}}
        v    (-> (data/generate spec ctx) :loot/vars :drawn)]
    (is (:context? v))
    (is (= #{{:name "A"} {:name "B"}} (set (:value v))) "without :data/vars")))

(deftest clashing-chosen-vars-fail
  (let [spec {:tables     {:a [{:data/vars {:x 1}}] :b [{:data/vars {:x 2}}]}
              :let        {:a {:choose :a} :b {:choose :b}}
              :view-model {:loot/title "t"}}]
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"same var" (data/generate spec ctx)))))

(defn- are-invalid [specs]
  (doseq [s specs]
    (is (not (schema/validate ::schema/data-spec s)) (pr-str s))))

(deftest spec-validation
  (testing "lets must name one known table, and :draw/:take need an :n"
    (are-invalid [{:tables {:t [{}]} :let {:x {:draw :t}} :view-model {}}
                  {:tables {:t [{}]} :let {:x {:choose :t :draw :t :n 1}} :view-model {}}
                  {:tables {:t [{}]} :let {:x {:choose :missing}} :view-model {}}
                  {:tables {:t [{}]}}])))

(deftest shipped-data-loads
  (doseq [f ["data/uniques.edn" "data/rings.edn"]]
    (let [vm (sns.sdk.protocols/generate (data/generator :x (data/load-spec f)) ctx)]
      (is (schema/validate ::schema/view-model vm) f))))

(deftest json-spec-decodes
  (let [spec (data/load-spec "test/resources/json-spec.json")]
    (is (= :uniques (-> spec :let :unique :choose)))
    (is (= [:who :text] (-> spec :inputs first ((juxt :id :type)))))
    (testing "and produces the same template + vars an EDN spec would"
      (let [vm   (data/generate spec (assoc ctx :inputs {:who "Thoros"}))
            item (first (items vm))]
        (is (schema/validate ::schema/view-model vm))
        (is (= "{{unique.name}} for {{who}}" (:loot/title vm)))
        (is (= "Thoros" (-> vm :loot/vars :who :value)))
        (is (= "Effect A" (:item/body item)))
        (is (= ["accuracy"] (:item/metadata item)))))))

(deftest history-override-surfaces-in-loot-spec
  (let [gen (data/generator :tools {:history :never :view-model {:loot/title "t"}})]
    (is (= :never (:history (sns.sdk.protocols/loot-spec gen))))))

(deftest honours-injected-rng
  (let [spec {:tables     {:t [{:name "A"} {:name "B"}]}
              :let        {:pick {:choose :t}}
              :view-model {:loot/title "{{pick.name}}"}}
        vm   (data/generate spec (assoc ctx :rng stub-rng))]
    (is (= "A" (-> vm :loot/vars :pick :value :name)))))

(deftest inputs-available-to-templates
  (let [vm (data/generate {:view-model {:loot/title "Hail, {{who}}"}} (assoc ctx :inputs {:who "Thoros"}))]
    (is (= "Thoros" (-> vm :loot/vars :who :value)))))

(defn- spec-file [title]
  (doto (java.io.File/createTempFile "data-test" ".edn") .deleteOnExit (spit (pr-str {:view-model {:loot/title title}}))))

(deftest file-generator-exposes-reload-input
  (let [gen (data/file-generator :greeting (.getPath (spec-file "Hail")))]
    (is (= [:__reload?] (map :id (:inputs (sns.sdk.protocols/loot-spec gen)))))))

(deftest file-generator-reloads-on-request
  (let [f   (spec-file "Before")
        gen (data/file-generator :greeting (.getPath f))]
    (is (= "Before" (:loot/title (sns.sdk.protocols/generate gen ctx))))
    (spit f (pr-str {:view-model {:loot/title "After"}}))
    (is (= "Before" (:loot/title (sns.sdk.protocols/generate gen ctx)))
        "without the reload flag, the stale in-memory spec is used")
    (is (= "After" (:loot/title (sns.sdk.protocols/generate gen (assoc ctx :inputs {:__reload? true}))))
        "with the reload flag, the file is re-read")))
