(ns sns.builtin.dust
  "A trivial stateless built-in loot type, used to exercise the engine."
  (:require
    [sns.sdk.protocols :as p]))

(defn generator
  "Factory: always yields Divine Dust, ignoring inputs and state."
  [_plugin]
  (reify p/Generator
    (loot-spec [_] {})
    (generate [_ _ctx]
      {:loot/title    "Divine Dust"
       :loot/subtitle "A pinch of divine residue"})))
