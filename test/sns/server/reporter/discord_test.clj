(ns sns.server.reporter.discord-test
  (:require
    [clojure.string :as str]
    [clojure.test :refer [deftest is testing]]
    [sns.server.reporter.discord :as discord]))

(def ^:private vm
  {:loot/title    "Pacifist's Vow"
   :loot/subtitle "Unique · armour"
   :loot/sections [{:section/heading "Mods"
                    :section/items   [{:item/title    "Glint"
                                       :item/body     "+1 AB"
                                       :item/metadata ["accuracy"]}
                                      {:item/body "Non-lethal weapons"}]}]})

(deftest view-model->embeds-renders-markdown
  (testing "the loot embed holds title, subtitle, headings and items"
    (let [[{:keys [title description color]}] (discord/view-model->embeds vm)]
      (is (= "Pacifist's Vow" title))
      (is (number? color))
      (is (str/includes? description "*Unique · armour*"))
      (is (str/includes? description "__Mods__"))
      (is (str/includes? description "**Glint**"))
      (is (str/includes? description "+1 AB"))
      (is (str/includes? description "Non-lethal weapons"))
      (is (not (str/includes? description "`accuracy`"))
          "metadata stays out of the copy-pasteable description")))
  (testing "metadata moves to a second embed, keyed by the item's title"
    (let [[_ {:keys [description]} :as embeds] (discord/view-model->embeds vm)]
      (is (= 2 (count embeds)))
      (is (str/starts-with? description "-# Metadata\n"))
      (is (str/includes? description "**Glint** `accuracy`")))))

(deftest metadata-keys
  (testing "an untitled item alone in its section is keyed by the heading"
    (let [[main meta] (discord/view-model->embeds
                        {:loot/title    "Soul"
                         :loot/sections [{:section/heading "Passive"
                                          :section/items   [{:item/body     "+2 damage"
                                                             :item/metadata ["offence"]}]}]})]
      (is (str/includes? (:description meta) "**Passive** `offence`"))
      (is (str/includes? (:description main) "\n+2 damage")
          "no number is added when a heading can serve as the key")))

  (testing "with neither title nor sole-item heading, items are numbered"
    (let [[main meta] (discord/view-model->embeds
                        {:loot/title    "Relic"
                         :loot/sections [{:section/items [{:item/body     "first affix"
                                                           :item/metadata ["[1; +6/1]"]}
                                                          {:item/body     "second affix"
                                                           :item/metadata ["[4; +1/4]"]}]}]})]
      (is (str/includes? (:description main) "1. first affix"))
      (is (str/includes? (:description main) "2. second affix"))
      (is (str/includes? (:description meta) "**1** `[1; +6/1]`"))
      (is (str/includes? (:description meta) "**2** `[4; +1/4]`")))))

(deftest minimal-view-model
  (testing "a title-only view-model still produces a single valid embed"
    (let [embeds (discord/view-model->embeds {:loot/title "Dust"})]
      (is (= 1 (count embeds)))
      (is (= "Dust" (:title (first embeds))))
      (is (not (contains? (first embeds) :description))))))

(deftest blank-webhook-rejected
  (testing "creating a reporter without a webhook URL throws"
    (is (thrown? Exception (discord/create "")))
    (is (thrown? Exception (discord/create nil)))))
