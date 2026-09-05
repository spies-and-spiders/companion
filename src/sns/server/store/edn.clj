(ns sns.server.store.edn
  "The `:file` and `:memory` stores.

   `:file` keeps one EDN file per collection under `:dir` and treats those files
   as the source of truth: an edit made by hand while the app is running is
   picked up by the next read. Each file's parsed value is cached and re-read
   only when its modification time or size changes, so the common case costs a
   `stat` rather than a parse. Writes re-read first, so a write can never
   resurrect state from before an external edit.

   `:memory` is the same shape without files, and offers no way to edit state by
   hand."
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
  "What we compare to decide whether a file changed under us. Size joins the
   modification time because filesystems vary in mtime resolution, and an edit
   landing in the same tick as our own write would otherwise be missed."
  [^File file]
  (when (.exists file)
    [(.lastModified file) (.length file)]))

(defn- parse [^File file]
  (if (.exists file)
    (with-open [r (PushbackReader. (io/reader file))]
      (edn/read r))
    {}))

(defn- read-stable
  "Read a file along with the stamp it actually had when read. Stamping only
   before the read would let an edit landing mid-read be cached under the old
   stamp, and so go unnoticed until the file changed again."
  [^File file]
  (loop []
    (let [before (stamp file)
          value  (parse file)]
      (if (= before (stamp file))
        [before value]
        (recur)))))

(defn edn-str
  "How a collection is serialised. Shared by the file backend and by export, so
   the two cannot drift into writing different files."
  [value]
  (binding [*print-namespace-maps* false]
    (with-out-str (pp/pprint value))))

(defn- write! [^File file value]
  (let [tmp (File. (str file ".tmp"))]
    (spit tmp (edn-str value))
    ;; Rename rather than writing in place, so an interrupted write cannot leave
    ;; a half-written file behind.
    (Files/move (.toPath tmp) (.toPath file)
                (into-array StandardCopyOption [StandardCopyOption/REPLACE_EXISTING]))))

(defn- apply-mutation
  "Merge one collection's mutations in, dropping the keys whose value is nil."
  [value mutations]
  (reduce-kv (fn [acc k v] (if (nil? v) (dissoc acc k) (assoc acc k v)))
             (or value {})
             mutations))

(defprotocol Writable
  "Server-side only: applying writes. Plugins declare theirs on the view-model
   and the engine applies them here once it has validated, so nothing can change
   state without the view-model describing it having survived validation."
  (mutate! [this mutations]
    "Apply `mutations`, shaped `{<collection> {<key> <value>}}`. A nil value
     retracts that key; keys left out are untouched. Returns nil."))

(defprotocol Exportable
  "Server-side only: every collection a store holds, for the export endpoint.
   Not part of the SDK `Store` protocol — plugins have no business enumerating
   another plugin's state."
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
              ;; From the file, not the cache: an edit made since the last read
              ;; has to survive this write.
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
  "A `Store`. `:file` keeps one EDN file per collection under `:dir` (default
   `./state`); anything else stays in memory. Construction is side-effect-free —
   `setup!` creates the directory."
  [{:keys [backend dir]}]
  (if (= :file backend)
    (->FileStore (io/file (or dir "./state")) (atom {}))
    (->MemoryStore (atom {}))))
