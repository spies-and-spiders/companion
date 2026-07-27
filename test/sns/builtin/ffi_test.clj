(ns sns.builtin.ffi-test
  (:require
    [clojure.java.shell :as shell]
    [clojure.string :as str]
    [clojure.test :refer [deftest is testing]]
    [sns.builtin.ffi :as ffi]
    [sns.sdk.protocols :as p]
    [sns.sdk.schema :as schema]))

(def ^:private os (str/lower-case (System/getProperty "os.name")))

(defn- lib-extension []
  (cond (str/includes? os "mac") "dylib"
        (str/includes? os "win") "dll"
        :else "so"))

(defn- compiler []
  (let [{:keys [exit out]} (shell/sh "sh" "-c"
                                     "command -v cc || command -v clang || command -v gcc")]
    (when (zero? exit) (str/trim out))))

(defn- compile-example
  "Compile examples/ffi-plugin/loot.c to a temp shared library, returning its
   path, or nil when no C compiler is available."
  []
  (when-let [cc (compiler)]
    (let [out (java.io.File/createTempFile "libloot" (str "." (lib-extension)))
          {:keys [exit err]} (shell/sh cc "-shared" "-fPIC" "-o" (.getPath out)
                                       "examples/ffi-plugin/loot.c")]
      (when-not (zero? exit)
        (throw (ex-info "Failed to compile FFI example" {:err err})))
      (.getPath out))))

(deftest ffi-plugin-generates-and-actions
  (cond
    (str/includes? os "win")
    (println "Skipping FFI test on Windows")

    (nil? (compiler))
    (println "Skipping FFI test: no C compiler found")

    :else
    (let [lib (compile-example)
          gen (ffi/generator :ffi-loot lib "generate" "loot_free" false)]
      (testing "generate maps the friendly C output to a valid view-model"
        (let [vm (p/generate gen {:inputs {} :session nil})]
          (is (schema/validate ::schema/view-model vm))
          (is (= "Rusty Dagger" (:loot/title vm)))
          (is (= ["common"] (-> vm :loot/sections first :section/items first :item/metadata)))
          (let [[action] (:loot/actions vm)]
            (is (= "Sharpen" (:action/label action)))
            (is (= [:loot/action {:id :ffi-loot :action :sharpen :params {:by 1}}]
                   (:action/event action))))))
      (testing "an action re-invokes the same symbol with an action request"
        (let [vm (p/handle-action gen {:session nil} :sharpen {:by 1})]
          (is (schema/validate ::schema/view-model vm))
          (is (= "Sharpened Blade" (:loot/title vm))))))))

(deftest same-library-loaded-once
  (cond
    (str/includes? os "win") (println "Skipping FFI test on Windows")
    (nil? (compiler))        (println "Skipping FFI test: no C compiler found")
    :else
    (let [lib (compile-example)]
      (testing "repeated lookups of one library path share a single handle"
        (is (identical? (#'ffi/library-lookup lib) (#'ffi/library-lookup lib))))
      (testing "two plugins on the same library each generate correctly"
        (let [a (ffi/generator :ffi-a lib "generate" "loot_free" false)
              b (ffi/generator :ffi-b lib "generate" "loot_free" false)]
          (is (= "Rusty Dagger" (:loot/title (p/generate a {:inputs {} :session nil}))))
          (is (= "Rusty Dagger" (:loot/title (p/generate b {:inputs {} :session nil})))))))))
