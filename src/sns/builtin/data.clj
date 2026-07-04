(ns sns.builtin.data
  "Interpreter for `:data` plugins: a code-free loot type defined entirely as
   data (EDN or JSON). A spec declares a top-level `:items` list, optional
   `:take`/`:weighted` draw controls, and a view template; the interpreter
   samples and renders it to a view-model.

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
                (throw (ex-info "Data plugin source not found" {:resource resource})))]
    (if (str/ends-with? resource ".json")
      (j/read-value (slurp url) j/keyword-keys-object-mapper)
      (read-string (slurp url)))))

(defn- enabled [entries]
  (filterv #(:enabled? % true) entries))

(defn- draw-entries
  "Sample from the spec's top-level `:items`. `take` (default 1) is how many to
   draw; `weighted` draws with replacement by each entry's `:weight`, otherwise
   draws without replacement."
  [items {:keys [take weighted]}]
  (let [pool (enabled items)
        n    (or take 1)]
    (when (empty? pool)
      (throw (ex-info "Draw from empty :items list" {})))
    (cond
      weighted (mapv (fn [_] (r/weighted-sample (into {} (map (juxt identity :weight)) pool)))
                     (range n))
      (> n 1)  (vec (r/sample-without-replacement (min n (count pool)) pool))
      :else    [(r/sample pool)])))

(defn- build-item [render {:keys [title body metadata]} entry]
  (cond-> {:item/body (render body entry)}
          title (assoc :item/title (render title entry))
          (and metadata (seq (get entry metadata))) (assoc :item/metadata (vec (get entry metadata)))))

(defn- build-section [render {:keys [heading each item]} entries single]
  ;; `:each :items` iterates the drawn entries; any other keyword iterates that
  ;; field on the single drawn entry (e.g. `:each :mods`).
  (let [coll (if (= :items each) entries (get single each))]
    (cond-> {:section/items (mapv #(build-item render item %) coll)}
            heading (assoc :section/heading heading))))

(defn interpret
  "Evaluate `spec` against the request `ctx`, returning a view-model."
  [spec {:keys [render inputs]}]
  (let [{:keys [items title subtitle sections]} spec
        entries   (draw-entries items spec)
        single    (when (= 1 (count entries)) (first entries))
        ;; templates render against the single drawn entry, with inputs available
        base      (merge (or single {}) inputs)
        render-in (fn [tmpl ctx] (render tmpl (merge inputs ctx)))]
    (cond-> {:loot/title (render-in (or title "") base)}
            subtitle (assoc :loot/subtitle (render-in subtitle base))
            (seq sections) (assoc :loot/sections
                                  (mapv #(build-section render-in % entries single) sections)))))

(defn generator
  "Build a `LootGenerator` for a `:data` plugin from its loaded `spec`."
  [id spec]
  (reify p/LootGenerator
    (loot-spec [_]
      (cond-> {:id id :label (:label spec)}
              (:utility? spec) (assoc :utility? true)
              (:inputs spec) (assoc :inputs (:inputs spec))))
    (generate [_ ctx]
      (interpret spec ctx))))
