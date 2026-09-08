(ns sns.server.store.edn-test
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.test :refer [deftest is testing]]
    [sns.sdk.protocols :as p]
    [sns.server.store.edn :as edn-store]))

(defn- temp-dir []
  (str (io/file (System/getProperty "java.io.tmpdir")
                (str "sns-store-" (System/nanoTime)))))

(defn- cleanup! [dir]
  (run! io/delete-file (reverse (file-seq (io/file dir)))))

(defn- round-trips [store]
  (p/setup! store)
  (testing "an absent collection reads as an empty map, needing no declaration"
    (is (= {} (p/read-collection store :relics))))
  (testing "written entries read straight back"
    (edn-store/mutate! store {:relics {"r1" {:name "Sunblade" :level 1}}})
    (is (= {"r1" {:name "Sunblade" :level 1}} (p/read-collection store :relics))))
  (testing "a second write merges rather than replacing the collection"
    (edn-store/mutate! store {:relics {"r2" {:name "Moonblade" :level 5}}})
    (is (= #{"r1" "r2"} (set (keys (p/read-collection store :relics))))))
  (testing "a nil value retracts just that key"
    (edn-store/mutate! store {:relics {"r1" nil}})
    (is (= #{"r2"} (set (keys (p/read-collection store :relics))))))
  (testing "collections are independent"
    (edn-store/mutate! store {:social {"Vex" {:present? true}}})
    (is (= #{"r2"} (set (keys (p/read-collection store :relics)))))
    (is (= {"Vex" {:present? true}} (p/read-collection store :social))))
  (testing "one call can mutate several collections"
    (edn-store/mutate! store {:relics {"r3" {:name "Starblade"}}
                              :social {"Vex" nil}})
    (is (= #{"r2" "r3"} (set (keys (p/read-collection store :relics)))))
    (is (= {} (p/read-collection store :social))))
  (testing "keyword values survive (regression: a relic's upgrade choices)"
    (edn-store/mutate! store {:relics {"k" {:path [{:id :precise} {:id :elemental}]}}})
    (is (= [:precise :elemental]
           (mapv :id (get-in (p/read-collection store :relics) ["k" :path])))))
  (testing "path order is preserved as written"
    (edn-store/mutate! store {:relics {"k" {:path [{:id :elemental} {:id :precise}]}}})
    (is (= [:elemental :precise]
           (mapv :id (get-in (p/read-collection store :relics) ["k" :path]))))))

(deftest memory-backend
  (round-trips (edn-store/create {:backend :memory})))

(deftest file-backend
  (let [dir (temp-dir)]
    (try
      (round-trips (edn-store/create {:backend :file :file {:dir dir}}))
      (testing "one file per collection, named for it"
        (is (= #{"relics.edn" "social.edn"}
               (set (map #(.getName %) (.listFiles (io/file dir)))))))
      (testing "state survives a fresh store on the same dir"
        (let [reopened (doto (edn-store/create {:backend :file :file {:dir dir}}) p/setup!)]
          (is (= "Moonblade" (get-in (p/read-collection reopened :relics) ["r2" :name])))))
      (finally (cleanup! dir)))))

(deftest file-is-the-source-of-truth
  ;; The point of the file backend: a DM edits state in a text editor while the
  ;; app is running, and the next read sees it.
  (let [dir   (temp-dir)
        store (doto (edn-store/create {:backend :file :file {:dir dir}}) p/setup!)
        file  (io/file dir "relics.edn")]
    (try
      (edn-store/mutate! store {:relics {"r1" {:name "Sunblade" :level 3}}})
      (testing "the file is readable EDN naming the entry by its own key"
        (let [text (slurp file)]
          (is (str/includes? text "\"r1\""))
          (is (str/includes? text ":name \"Sunblade\""))))
      (testing "a hand edit propagates without restarting"
        (spit file (str/replace (slurp file) "Sunblade" "Moonblade"))
        (is (= "Moonblade" (get-in (p/read-collection store :relics) ["r1" :name]))))
      (testing "a write after a hand edit keeps the edit"
        (spit file (str/replace (slurp file) ":level 3" ":level 9"))
        (edn-store/mutate! store {:relics {"r2" {:name "Starblade"}}})
        (let [relics (p/read-collection store :relics)]
          (is (= 9 (get-in relics ["r1" :level])) "the external edit survived our write")
          (is (= "Starblade" (get-in relics ["r2" :name])))))
      (finally (cleanup! dir)))))
