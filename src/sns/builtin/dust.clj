(ns sns.builtin.dust
  "A trivial stateless loot type, for exercising the engine."
  (:require
    [sns.sdk.protocols :as p]))

(defn generator
  [_plugin]
  (reify p/Generator
    (loot-spec [_] {})
    (generate [_ _ctx]
      {:loot/title    "Divine Dust"
       :loot/subtitle "A pinch of divine residue"})))
