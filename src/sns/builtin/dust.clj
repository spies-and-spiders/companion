(ns sns.builtin.dust
  "A trivial stateless built-in loot type, used to exercise the engine."
  (:require
    [sns.sdk.protocols :as p]))

(defn generator
  "Factory: always yields Divine Dust, ignoring inputs and state."
  [{:keys [id]}]
  (reify p/LootGenerator
    (loot-spec [_]
      {:id (or id :divine-dust) :label "Divine Dust"})
    (generate [_ _ctx]
      {:loot/title    "Divine Dust"
       :loot/subtitle "A pinch of divine residue"})))
