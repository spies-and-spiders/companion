(ns sns.builtin.json
  "Interpreter for `:json` plugins: a code-free loot type defined entirely as
   data (EDN or JSON). A spec declares named `:tables`, a `:draw` directive, and a
   view template; the interpreter samples and renders it to a view-model.

   This is the workhorse for DMs who don't write Clojure. See spi/README.md."
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [jsonista.core :as j]
    [randy.core :as r]
    [sns.spi.protocols :as p]))

(defn load-spec
  "Load a loot spec from a classpath resource. `.json` is parsed as JSON with
   keywordised keys; anything else is read as EDN."
  [resource]
  (let [url (or (io/resource resource)
                (throw (ex-info "JSON plugin data not found" {:resource resource})))]
    (if (str/ends-with? resource ".json")
      (j/read-value (slurp url) j/keyword-keys-object-mapper)
      (read-string (slurp url)))))

(defn- enabled [entries]
  (filterv #(:enabled? % true) entries))

(defn- draw-entries
  "Resolve a `:draw` directive against `tables`, returning a vector of entries."
  [tables {:keys [from take weighted]}]
  (let [pool (enabled (get tables from))
        n    (or take 1)]
    (when (empty? pool)
      (throw (ex-info "Draw from empty/unknown table" {:from from})))
    (cond
      weighted (mapv (fn [_] (r/weighted-sample (into {} (map (juxt identity :weight)) pool)))
                     (range n))
      (> n 1)  (vec (r/sample-without-replacement (min n (count pool)) pool))
      :else    [(r/sample pool)])))

(defn- build-item [render {:keys [title body tags]} entry]
  (cond-> {:item/body (render body entry)}
          title (assoc :item/title (render title entry))
          (and tags (seq (get entry tags))) (assoc :item/tags (vec (get entry tags)))))

(defn- build-section [render {:keys [heading each item]} entries single]
  (let [coll (if (= :draw each) entries (get single each))]
    (cond-> {:section/items (mapv #(build-item render item %) coll)}
            heading (assoc :section/heading heading))))

(defn interpret
  "Evaluate `spec` against the request `ctx`, returning a view-model."
  [spec {:keys [render inputs]}]
  (let [{:keys [tables draw title subtitle sections]} spec
        entries   (draw-entries tables draw)
        single    (when (= 1 (count entries)) (first entries))
        ;; templates render against the single drawn entry, with inputs available
        base      (merge (or single {}) inputs)
        render-in (fn [tmpl ctx] (render tmpl (merge inputs ctx)))]
    (cond-> {:loot/title (render-in (or title "") base)}
            subtitle (assoc :loot/subtitle (render-in subtitle base))
            (seq sections) (assoc :loot/sections
                                  (mapv #(build-section render-in % entries single) sections)))))

(defn generator
  "Build a `LootGenerator` for a `:json` plugin from its loaded `spec`."
  [id spec]
  (reify p/LootGenerator
    (loot-spec [_]
      (cond-> {:id id :label (:label spec)}
              (:inputs spec) (assoc :inputs (:inputs spec))))
    (generate [_ ctx]
      (interpret spec ctx))))
