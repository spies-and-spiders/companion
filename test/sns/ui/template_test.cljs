(ns sns.ui.template-test
  (:require
    [cljs.test :refer [deftest is testing]]
    [sns.ui.template :as template]))

(deftest render
  (testing "a template without tags is itself, and nil stays nil"
    (is (= "Plain text." (template/render "Plain text." {})))
    (is (nil? (template/render nil {}))))
  (testing "vars interpolate at their rank"
    (is (= "+3 damage" (template/render "+{{ dmg }} damage" {:dmg {:value 1 :step 2 :rank 2}}))))
  (testing "a template that fails to compile is shown as-is"
    (is (= "{{ ab" (template/render "{{ ab" {})))))

(deftest spies-links
  (testing "inline and block forms"
    (is (= "[Earth Tremor](https://spies.tools/spells.html#earth%20tremor_sns)"
           (template/render "{{sns-spell \"Earth Tremor\"}}" {})))
    (is (= "[Blackguard's Blight](https://spies.tools/maneuvers.html#blackguard's%20blight_sns)"
           (template/render "{{#sns-maneuver}}Blackguard's Blight{{/sns-maneuver}}" {})))
    (is (= "[Blinded](https://spies.tools/conditions.html#blinded_sns)"
           (template/render "{{sns-condition \"Blinded\"}}" {}))))
  (testing "a second argument overrides the text shown"
    (is (= "[blinding](https://spies.tools/conditions.html#blinded_sns)"
           (template/render "{{sns-condition \"blinded\" \"blinding\"}}" {})))
    (is (= "[Foo](https://spies.tools/spells.html#earth%20tremor_sns)"
           (template/render "{{sns-spell \"Earth Tremor\" t}}" {:t {:value "Foo"}})))
    (is (= "[blinding](https://spies.tools/conditions.html#blinded_sns)"
           (template/render "{{#sns-condition \"blinded\"}}blinding{{/sns-condition}}" {}))))
  (testing "a var as the name, with brackets encoded"
    (is (= "Cast [Foo (Bar)](https://spies.tools/spells.html#foo%20%28bar%29_sns)."
           (template/render "Cast {{sns-spell s}}." {:s {:value "Foo (Bar)"}})))))

(deftest die-size
  (testing "a rung, from a literal or a ranked var"
    (is (= "3d4" (template/render "{{die-size 6}}" {})))
    (is (= "3d6 fire" (template/render "{{die-size dmg}} fire" {:dmg {:value 7 :step 1 :rank 3}}))))
  (testing "a var that is not a rung renders as nothing"
    (is (= "" (template/render "{{die-size dmg}}" {:dmg {:value ""}})))))

(deftest render-view-model
  (let [vm (template/render-view-model
             {:loot/title    "{{ name }}"
              :loot/vars     {:name {:value "Hoard"} :n {:value 2}}
              :loot/sections [{:section/items [{:item/body "{{ n }} gold"}
                                               {:item/body "{{#if missing}}gone{{/if}}"}]}
                              {:section/items [{:item/body " "}]}]})]
    (testing "loot vars reach every template"
      (is (= "Hoard" (:loot/title vm)))
      (is (= "2 gold" (get-in vm [:loot/sections 0 :section/items 0 :item/body]))))
    (testing "blank items are dropped, and sections left empty with them"
      (is (= 1 (count (:loot/sections vm))))
      (is (= 1 (count (get-in vm [:loot/sections 0 :section/items])))))))
