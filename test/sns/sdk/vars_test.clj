(ns sns.sdk.vars-test
  (:require
    [clojure.test :refer [deftest is testing use-fixtures]]
    [randy.rng :as rng]
    [sns.sdk.randoms :as randoms]
    [sns.sdk.vars :as vars]))

(def ^:private feats ["Alert" "Athlete" "Brawler"])

(use-fixtures :each (fn [f]
                      (defmethod randoms/preset :feats [_ _] feats)
                      (defmethod randoms/preset :damage-types [_ _] ["fire" "cold"])
                      (try
                        (f)
                        (finally
                          (remove-method randoms/preset :feats)
                          (remove-method randoms/preset :damage-types)))))

(defn- fixed-rng [i]
  (reify rng/RandomNumberGenerator
    (next-int [_ _] i)
    (next-int [_ _ _] i)))

(deftest a-literal-value-is-itself
  (testing "every non-behaviour value passes straight through as the var's value"
    (is (= 4 (-> (vars/resolve-var (fixed-rng 0) :damage 4) :value)))
    (is (= "Wisdom" (-> (vars/resolve-var (fixed-rng 0) :ability "Wisdom") :value)))
    (is (= false (-> (vars/resolve-var (fixed-rng 0) :flag false) :value)))
    (is (= ["a" "b"] (-> (vars/resolve-var (fixed-rng 0) :tags ["a" "b"]) :value))))
  (testing "including a map, as long as it declares no behaviour"
    (is (= {:hp 4} (-> (vars/resolve-var (fixed-rng 0) :block {:hp 4}) :value)))))

(deftest a-random-behaviour-draws-and-offers-its-vocabulary
  (let [v (vars/resolve-var (fixed-rng 2) :x {:random :feats})]
    (is (= "Brawler" (:value v)))
    (is (= feats (:options v)) "the vocabulary the UI edits against")
    (is (= :feats (:random v)) "recorded, so an action can reroll it"))
  (testing "the preset's other keys are passed to it as named args"
    (let [v (vars/resolve-var (fixed-rng 1) :x {:random :literal :options ["a" "b"]})]
      (is (= "b" (:value v)))
      (is (= ["a" "b"] (:options v)))
      (is (= {:options ["a" "b"]} (:args v))))))

(deftest literal-escapes-a-behaviour-shaped-value
  (testing "data that happens to look like a behaviour says so, and is kept whole"
    (is (= {:random :not-a-preset}
           (-> (vars/resolve-var (fixed-rng 0) :x {:literal {:random :not-a-preset}}) :value))))
  (testing "escaping a plain value works too"
    (is (= "x" (-> (vars/resolve-var (fixed-rng 0) :v {:literal "x"}) :value)))))

(deftest a-var-carries-no-label-of-its-own
  (testing "the id is already on the wire as the key, so a label would be a
            second copy of it — the UI derives one from the id instead"
    (is (nil? (:label (vars/resolve-var (fixed-rng 0) :ability "Wisdom"))))
    (is (nil? (:label (vars/resolve-var (fixed-rng 0) :damage-type {:random :feats})))))
  (testing "humanise-label remains, for a display string the server composes"
    (is (= "Improved skill" (vars/humanise-label :improved_skill)))
    (is (= "Crit damage" (vars/humanise-label :crit-damage)))))

(deftest resolving-is-idempotent
  (testing "an already-resolved var passes through, so a round-tripped view-model
            is not redrawn behind the DM's back"
    (let [resolved (vars/resolve-var (fixed-rng 2) :x {:random :feats})]
      (is (= resolved (vars/resolve-var (fixed-rng 0) :x resolved))))))

(deftest a-var-records-the-type-it-resolved-as
  (testing "so an edited value comes back as what the plugin sent, rather than
            the string a browser input hands over"
    (is (= :int (:type (vars/resolve-var (fixed-rng 0) :ab 1))))
    (is (= :decimal (:type (vars/resolve-var (fixed-rng 0) :mult 1.5))))
    (is (= :bool (:type (vars/resolve-var (fixed-rng 0) :fire false)))))
  (testing "text is what an untagged var is, so it carries no tag"
    (is (nil? (:type (vars/resolve-var (fixed-rng 0) :ability "Wisdom"))))
    (is (nil? (:type (vars/resolve-var (fixed-rng 0) :x {:random :feats})))))
  (testing "a round-tripped var keeps its type rather than restamping from a
            half-typed value"
    (let [blanked {:value nil :type :int}]
      (is (= blanked (vars/resolve-var (fixed-rng 0) :ab blanked))))))

(deftest resolve-vars-keys-by-id
  (is (= {:a {:value 1 :type :int} :b {:value 2 :type :int}}
         (vars/resolve-vars (fixed-rng 0) {:a 1 :b 2})))
  (testing "nothing declared, nothing resolved"
    (is (nil? (vars/resolve-vars (fixed-rng 0) nil)))
    (is (nil? (vars/resolve-vars (fixed-rng 0) {})))))

(deftest redraw-rerolls-only-what-was-drawn
  (let [vars (vars/resolve-vars (fixed-rng 0) {:x {:random :damage-types} :n 5})]
    (is (= "fire" (-> vars :x :value)))
    (testing "a drawn var rerolls from the preset it came from"
      (is (= "cold" (-> (vars/redraw (fixed-rng 1) vars :x) :x :value))))
    (testing "a literal has nothing to redraw"
      (is (= vars (vars/redraw (fixed-rng 1) vars :n))))
    (testing "redraw-distinct keeps going until the value actually changes"
      (is (= "cold" (-> (vars/redraw-distinct @randy.core/default-rng vars :x) :x :value))))
    (testing "and gives up rather than spinning on a single-value preset"
      (let [one (vars/resolve-vars (fixed-rng 0) {:x {:random :literal :options ["only"]}})]
        (is (= "only" (-> (vars/redraw-distinct @randy.core/default-rng one :x 5) :x :value)))))))
