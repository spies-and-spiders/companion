(ns sns.sdk.randoms-test
  (:require
    [clojure.string :as str]
    [clojure.test :refer [deftest is testing use-fixtures]]
    [randy.core :as r]
    [randy.rng :as rng]
    [sns.sdk.randoms :as randoms]
    [sns.server.render :as render])
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
  (is (thrown? Exception (randoms/sample-preset (fixed-rng 0) :not-a-preset))))

(deftest literal-preset-samples-inline-values
  (testing "values written in the template need no registration"
    (is (= "b" (randoms/sample-preset (fixed-rng 1) :literal "a" "b" "c")))))

(deftest without-replacement-draws-distinct-values
  (let [drawn (randoms/sample-preset @r/default-rng :without-replacement "2" "feats")]
    (is (= 2 (count drawn)))
    (is (= 2 (count (set drawn))))
    (is (every? (set feats) drawn)))
  (testing "any RandomGenerator works, not just java.util.Random"
    ;; regression: randy's shuffle strategy proxies onto java.util.Random and
    ;; throws for the engine's rng, so the draw must avoid that path
    (let [drawn (randoms/sample-preset (.create (RandomGeneratorFactory/of "L64X128MixRandom"))
                                       :without-replacement "2" "feats")]
      (is (= 2 (count (set drawn))))
      (is (every? (set feats) drawn))))
  (testing "the drawn values stay a collection, so a template can index them"
    (let [parts (-> (randoms/with-rng (java.util.Random. 7)
                      (render/render
                        "{% with x=v|random:without-replacement:2:feats %}{{x.0}} & {{x.1}}{% endwith %}"
                        {}))
                    (str/split #" & "))]
      (is (= 2 (count (set parts))))
      (is (every? (set feats) parts)))))

(deftest defmethod-extends-the-vocabulary
  (testing "a plugin adds a preset in code the same way the built-ins are defined"
    (defmethod randoms/preset ::colours [_ _] ["red" "blue"])
    (is (= "blue" (randoms/sample-preset (fixed-rng 1) ::colours)))
    (remove-method randoms/preset ::colours)))

(deftest random-filter-renders-in-templates
  (testing "the filter is registered by requiring the SDK, and draws from *rng*"
    (randoms/with-rng (fixed-rng 2)
      (is (= "Gain the Brawler feat."
             (render/render "Gain the {{ \"\"|random:feats }} feat." {})))))
  (testing "the piped value may just be a missing variable"
    (randoms/with-rng (fixed-rng 0)
      (is (= "Alert" (render/render "{{x|random:feats}}" {})))))
  (testing "filter args are passed through to the preset"
    (randoms/with-rng (fixed-rng 2)
      (is (= "c" (render/render "{{x|random:literal:a:b:c}}" {}))))))

(deftest rendering-uses-the-bound-rng
  (testing "the same bound rng renders the same text"
    (let [render-once #(randoms/with-rng (java.util.Random. 7)
                         (render/render "{{x|random:feats}}" {}))]
      (is (= (render-once) (render-once))))))
