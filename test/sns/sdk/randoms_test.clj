(ns sns.sdk.randoms-test
  (:require
    [clojure.test :refer [deftest is testing use-fixtures]]
    [randy.core :as r]
    [randy.rng :as rng]
    [sns.sdk.randoms :as randoms])
  (:import
    (java.util.random RandomGeneratorFactory)))

(def ^:private feats ["Alert" "Athlete" "Brawler"])

(use-fixtures :each (fn [f]
                      (defmethod randoms/preset :feats [_ _] feats)
                      (try
                        (f)
                        (finally
                          (remove-method randoms/preset :feats)))))

(defn- fixed-rng
  "A stub rng that always picks index `i`."
  [i]
  (reify rng/RandomNumberGenerator
    (next-int [_ _] i)
    (next-int [_ _ _] i)))

(deftest registered-preset-is-sampled
  (testing "config-declared values are drawn from"
    (is (contains? (set feats) (randoms/sample-preset (fixed-rng 1) :feats)))
    (is (= "Athlete" (randoms/sample-preset (fixed-rng 1) :feats)))))

(deftest unknown-preset-is-rejected
  (is (thrown? Exception (randoms/sample-preset (fixed-rng 0) :not-a-preset)))
  (testing "the error names what is available"
    (is (contains? (set (-> (try (randoms/sample-preset (fixed-rng 0) :nope)
                                 (catch Exception e (ex-data e)))
                            :known))
                   :feats))))

(deftest literal-preset-samples-values-written-inline
  (testing "values given where the var is declared need no registration"
    (is (= "b" (randoms/sample-preset (fixed-rng 1) :literal {:options ["a" "b" "c"]})))))

(deftest without-replacement-draws-distinct-values
  (let [drawn (randoms/sample-preset @r/default-rng :without-replacement {:amount 2 :preset :feats})]
    (is (= 2 (count drawn)))
    (is (= 2 (count (set drawn))))
    (is (every? (set feats) drawn)))
  (testing "any RandomGenerator works, not just java.util.Random"
    ;; regression: randy's shuffle strategy proxies onto java.util.Random and
    ;; throws for the engine's rng, so the draw must avoid that path
    (let [drawn (randoms/sample-preset (.create (RandomGeneratorFactory/of "L64X128MixRandom"))
                                       :without-replacement {:amount 2 :preset :feats})]
      (is (= 2 (count (set drawn))))
      (is (every? (set feats) drawn))))
  (testing "`amount` may arrive as a string (a JSON spec, a submitted input)"
    (is (= 2 (count (randoms/sample-preset @r/default-rng :without-replacement
                                           {:amount "2" :preset :feats})))))
  (testing "drawing without replacement from a self-sampling preset is rejected"
    (is (thrown? Exception (randoms/sample-preset @r/default-rng :without-replacement
                                                  {:amount 2 :preset :without-replacement})))))

(deftest preset-args-are-named
  (testing "a preset reads the var spec's other keys by name"
    (defmethod randoms/preset ::defences [_ {:keys [type]}]
      (cond-> ["Fortitude" "Reflexes" "Will"]
              (not= "non-armour" type) (conj "Armour")))
    (is (= 4 (count (randoms/preset-values ::defences))))
    (is (= 3 (count (randoms/preset-values ::defences {:type "non-armour"}))))
    (remove-method randoms/preset ::defences)))

(deftest preset-values-exposes-the-vocabulary
  (testing "a plain preset's full vocabulary, for editing the draw as a combobox"
    (is (= feats (randoms/preset-values :feats))))
  (testing "a literal preset's vocabulary is the options written alongside it"
    (is (= ["a" "b" "c"] (randoms/preset-values :literal {:options ["a" "b" "c"]}))))
  (testing "a self-sampling preset has no fixed vocabulary to offer"
    (is (nil? (randoms/preset-values :without-replacement {:amount 2 :preset :feats})))))

(deftest defmethod-extends-the-vocabulary
  (testing "a plugin adds a preset in code the same way the built-ins are defined"
    (defmethod randoms/preset ::colours [_ _] ["red" "blue"])
    (is (= "blue" (randoms/sample-preset (fixed-rng 1) ::colours)))
    (remove-method randoms/preset ::colours)))

(deftest draw-resolves-the-preset-exactly-once
  (testing "`preset` is an open extension point, so a plugin's defmethod must not
            run twice to produce one var's value and its options"
    (let [calls (atom 0)]
      (defmethod randoms/preset ::counted [_ _] (swap! calls inc) ["a" "b"])
      (try
        (let [{:keys [value options]} (randoms/draw (fixed-rng 1) ::counted {})]
          (is (= "b" value))
          (is (= ["a" "b"] options))
          (is (= 1 @calls)))
        (finally (remove-method randoms/preset ::counted)))))
  (testing "a self-sampling preset draws its own value and offers no options"
    (let [{:keys [value options]} (randoms/draw @r/default-rng :without-replacement
                                                {:amount 2 :preset :feats})]
      (is (= 2 (count (set value))))
      (is (nil? options)))))

(deftest drawing-uses-the-supplied-rng
  (testing "the same seeded rng draws the same value"
    (let [draw-once #(randoms/sample-preset (java.util.Random. 7) :feats)]
      (is (= (draw-once) (draw-once))))))
