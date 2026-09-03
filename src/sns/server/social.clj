(ns sns.server.social
  "Store-backed wrapper around the shared tracker logic in `sns.social`. One
   entry per character in the `:social` collection, keyed by name — which is
   already the shape the shared logic works on, so this only reads, writes and
   delegates."
  (:require
    [randy.rng :as rng]
    [sns.sdk.protocols :as p]
    [sns.social :as social]))

(def ^:private coll :social)

(defn- characters [store]
  (p/read-collection store coll))

(defn snapshot [store]
  (social/snapshot (characters store)))

(defn upsert! [store character]
  (let [[char-name attrs] (or (social/normalise-character character)
                              (throw (ex-info "Character name is required" {})))]
    (p/mutate! store {coll {char-name attrs}})
    (snapshot store)))

(defn toggle! [store char-name]
  (when-let [character (get (characters store) char-name)]
    (p/mutate! store {coll {char-name (update character :present? not)}}))
  (snapshot store))

(defn remove! [store char-name]
  (p/mutate! store {coll {char-name nil}})
  (snapshot store))

(defn roll
  "Roll 1d20 + the group bonus for `skill` (:deception or :persuasion). Returns
   the snapshot with the result under :roll."
  [store rng skill]
  (let [skill (keyword skill)]
    (when-not (contains? #{:deception :persuasion} skill)
      (throw (ex-info "Unknown skill" {:skill skill})))
    (let [chars (characters store)]
      (assoc (social/snapshot chars)
             :roll (social/roll-result chars skill (rng/next-int rng 1 21))))))
