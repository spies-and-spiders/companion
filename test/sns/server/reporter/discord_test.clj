(ns sns.server.reporter.discord-test
  (:require
    [clojure.string :as str]
    [clojure.test :refer [deftest is testing]]
    [sns.server.reporter.discord :as discord]))

(def ^:private vm
  {:loot/title    "Pacifist's Vow"
   :loot/subtitle "Unique · armour"
   :loot/sections [{:section/heading "Mods"
                    :section/items   [{:item/title "Glint"
                                       :item/body  "+1 AB"
                                       :item/tags  ["accuracy"]}
                                      {:item/body "Non-lethal weapons"}]}]})

(deftest view-model->embed-renders-markdown
  (testing "title, subtitle, headings, items and tags map into one embed"
    (let [{:keys [title description color]} (discord/view-model->embed vm)]
      (is (= "Pacifist's Vow" title))
      (is (number? color))
      (is (str/includes? description "*Unique · armour*"))
      (is (str/includes? description "__Mods__"))
      (is (str/includes? description "**Glint**"))
      (is (str/includes? description "+1 AB"))
      (is (str/includes? description "`accuracy`"))
      (is (str/includes? description "Non-lethal weapons")))))

(deftest minimal-view-model
  (testing "a title-only view-model still produces a valid embed"
    (let [embed (discord/view-model->embed {:loot/title "Dust"})]
      (is (= "Dust" (:title embed)))
      (is (not (contains? embed :description))))))

(deftest blank-webhook-rejected
  (testing "creating a reporter without a webhook URL throws"
    (is (thrown? Exception (discord/create "")))
    (is (thrown? Exception (discord/create nil)))))
