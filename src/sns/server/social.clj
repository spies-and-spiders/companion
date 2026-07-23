(ns sns.server.social
  "Store-backed wrapper around the shared tracker logic in `sns.social`.
   Persists under the `__`-prefixed :__social collection so it can never clash
   with user-defined loot types. (When storage is :none the browser runs the
   tracker locally instead — see the capabilities endpoint.)"
  (:require
    [randy.rng :as rng]
    [sns.sdk.protocols :as p]
    [sns.social :as social]))

(def ^:private collection :__social)
(def ^:private doc-id "characters")

(defn snapshot [store]
  (social/snapshot (p/fetch store collection doc-id)))

(defn upsert! [store character]
  (let [entry (or (social/normalise-character character)
                  (throw (ex-info "Character name is required" {})))]
    (p/update! store collection doc-id #(conj (or % {}) entry))
    (snapshot store)))

(defn toggle! [store char-name]
  (p/update! store collection doc-id #(social/toggle % char-name))
  (snapshot store))

(defn remove! [store char-name]
  (p/update! store collection doc-id #(social/remove-character % char-name))
  (snapshot store))

(defn roll
  "Roll 1d20 + the group bonus for `skill` (:deception or :persuasion). Returns
   the snapshot with the result under :roll."
  [store rng skill]
  (let [skill (keyword skill)]
    (when-not (contains? #{:deception :persuasion} skill)
      (throw (ex-info "Unknown skill" {:skill skill})))
    (let [characters (p/fetch store collection doc-id)]
      (assoc (social/snapshot characters)
             :roll (social/roll-result characters skill (rng/next-int rng 1 21))))))
