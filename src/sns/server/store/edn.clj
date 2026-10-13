(ns sns.server.store.edn
  "The `:file` and `:memory` stores.

   `:file` keeps one EDN file per collection, which the DM may edit by hand while
   the app runs. Parsed files are cached until their mtime or size changes, and
   writes re-read first so they never undo an external edit."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.pprint :as pp]
    [sns.sdk.protocols :as p])
  (:import
    (java.io File PushbackReader)
    (java.nio.file Files StandardCopyOption)))

(defn- collection-file ^File [^File dir coll]
  (io/file dir (str (name coll) ".edn")))

(defn- stamp
  "Size as well as mtime, since coarse mtime resolution could miss an edit
   landing in the same tick as our own write."
  [^File file]
  (when (.exists file)
    [(.lastModified file) (.length file)]))

(defn- parse [^File file]
  (if (.exists file)
    (with-open [r (PushbackReader. (io/reader file))]
      (edn/read r))
    {}))

(defn- read-stable
  "Read a file with its stamp, retrying if it changed mid-read."
  [^File file]
  (loop []
    (let [before (stamp file)
          value  (parse file)]
      (if (= before (stamp file))
        [before value]
        (recur)))))

(defn edn-str
  "Serialise a collection, for both the file backend and export."
  [value]
  (binding [*print-namespace-maps* false]
    (with-out-str (pp/pprint value))))

(defn- write! [^File file value]
  (let [tmp (File. (str file ".tmp"))]
    (spit tmp (edn-str value))
    ;; Rename, so an interrupted write can't leave a half-written file.
    (Files/move (.toPath tmp) (.toPath file)
                (into-array StandardCopyOption [StandardCopyOption/REPLACE_EXISTING]))))

(defn- apply-mutation [value mutations]
  (reduce-kv (fn [acc k v] (if (nil? v) (dissoc acc k) (assoc acc k v)))
             (or value {})
             mutations))

(defprotocol Writable
  "Server-side writes, kept out of the SDK's `Store`."
  (mutate! [this mutations]
    "Apply `{<collection> {<key> <value>}}`, a nil value retracting its key."))

(defprotocol Exportable
  "Every collection a store holds, for export. Kept out of the SDK's `Store`."
  (all-state [this]))

(defrecord FileStore [^File dir cache]
  p/Store
  (setup! [_]
    (.mkdirs dir))
  (read-collection [_ coll]
    (let [file   (collection-file dir coll)
          cached (get @cache coll)]
      (if (and cached (= (:stamp cached) (stamp file)))
        (:value cached)
        (let [[st v] (read-stable file)]
          (swap! cache assoc coll {:stamp st :value v})
          v))))
  Writable
  (mutate! [_ mutations]
    (locking cache
      (doseq [[coll changes] mutations]
        (let [file (collection-file dir coll)
              ;; Not from the cache, so external edits survive.
              next (apply-mutation (second (read-stable file)) changes)]
          (write! file next)
          (swap! cache assoc coll {:stamp (stamp file) :value next})))
      nil))
  Exportable
  (all-state [this]
    (into {}
          (keep (fn [^File f]
                  (let [n (.getName f)]
                    (when (.endsWith n ".edn")
                      (let [coll (keyword (subs n 0 (- (count n) 4)))]
                        [coll (p/read-collection this coll)])))))
          (or (.listFiles dir) []))))

(defrecord MemoryStore [state]
  p/Store
  (setup! [_])
  (read-collection [_ coll]
    (get @state coll {}))
  Writable
  (mutate! [_ mutations]
    (swap! state (fn [s]
                   (reduce-kv (fn [acc coll changes]
                                (update acc coll apply-mutation changes))
                              s
                              mutations)))
    nil)
  Exportable
  (all-state [_] @state))

(defn create
  "A `:file` store under `:dir` (default `./state`), or else an in-memory one."
  [{:keys [backend] {:keys [dir]} :file}]
  (if (= :file backend)
    (->FileStore (io/file (or dir "./state")) (atom {}))
    (->MemoryStore (atom {}))))
