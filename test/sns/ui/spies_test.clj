(ns sns.ui.spies-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [sns.ui.spies :as spies]))

(deftest target
  (testing "spell and maneuver links name their page and entry"
    (is (= ["spells" "earth tremor"] (spies/target "https://spies.tools/spells.html#earth%20tremor_sns")))
    (is (= ["maneuvers" "blackguard's blight"] (spies/target "https://spies.tools/maneuvers.html#blackguard's%20blight_sns")))
    (is (= ["spells" "foo (bar)"] (spies/target "https://spies.tools/spells.html#foo%20%28bar%29_sns"))))
  (testing "anything else is not previewed"
    (is (nil? (spies/target "https://spies.tools/bestiary.html#imp_sns")))
    (is (nil? (spies/target "https://example.com/spells.html#x_sns")))
    (is (nil? (spies/target "https://spies.tools/spells.html#bad%zz_sns")))))

(deftest index
  (is (= {"earth tremor" {:name "Earth Tremor"}}
         (spies/index "spells" {:spell [{:name "Earth Tremor"}]}))))

(deftest card
  (testing "a spell's level and school, and its text with tags shown as text"
    (is (= [:div.spies__card
            [:div.spies__name "Earth Tremor"]
            [:div.spies__meta "Level 2 Evocation"]
            [[:p "Creatures are knocked prone on a DC 15, taking 1d6."]
             [:div.spies__section
              [:b.spies__heading "Cast at Higher Levels"]
              [[:ul [[:li [[:b "2 Rounds. "] ["Grapple them."]]]]]]]]]
           (spies/card "spells"
                       {:name               "Earth Tremor"
                        :level              2
                        :school             "V"
                        :entries            ["Creatures are knocked {@condition prone} on a {@dc 15}, taking {@dice 1d6}."]
                        :entriesHigherLevel [{:type    "entries"
                                              :name    "Cast at Higher Levels"
                                              :entries [{:type  "list"
                                                         :items [{:type    "item"
                                                                  :name    "2 Rounds"
                                                                  :entries ["{@condition grappled||Grapple} them."]}]}]}]}))))
  (testing "cantrips and maneuvers"
    (is (= "Evocation Cantrip" (get-in (spies/card "spells" {:level 0 :school "V"}) [2 1])))
    (is (= "Basic maneuver · 0 points" (get-in (spies/card "maneuvers" {:degree 0}) [2 1])))
    (is (= "1st degree maneuver · 1 point" (get-in (spies/card "maneuvers" {:degree 1 :points 1}) [2 1])))
    (is (= "2nd degree Adamant Mountain maneuver · 3 points"
           (get-in (spies/card "maneuvers" {:degree 2 :traditions ["Adamant Mountain"] :points 3}) [2 1])))))
