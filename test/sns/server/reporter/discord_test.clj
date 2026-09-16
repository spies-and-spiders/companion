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

(defn- containers [message]
  (filterv (comp #{17} :type) (:components message)))

(defn- content
  "All displayable text in a message, in order."
  [message]
  (str/join "\n" (eduction (mapcat #(or (:components %) [%]))
                           (keep :content)
                           (:components message))))

(defn- text-size [message]
  (count (str/replace (content message) "\n" "")))

(defn- component-count [message]
  (+ (count (:components message))
     (transduce (map #(count (:components %))) + (:components message))))

(deftest renders-a-components-v2-message
  (testing "the loot container holds title, subtitle, headings and items"
    (let [[message :as messages] (discord/view-model->messages vm)
          [loot] (containers message)]
      (is (= 1 (count messages)))
      (is (= 32768 (:flags message)))
      (is (= 0xC8A24C (:accent_color loot)))
      (is (str/includes? (content message) "# Pacifist's Vow"))
      (is (str/includes? (content message) "-# Unique · armour"))
      (is (str/includes? (content message) "## Mods"))
      (is (str/includes? (content message) "**Glint**"))
      (is (str/includes? (content message) "+1 AB"))
      (is (str/includes? (content message) "Non-lethal weapons"))
      (is (some (comp #{14} :type) (:components loot))
          "a separator rules off the section")))
  (testing "metadata rides in a second, unaccented container"
    (let [[message]      (discord/view-model->messages vm)
          [loot metas]   (containers message)]
      (is (some? metas))
      (is (nil? (:accent_color metas)))
      (is (not (str/includes? (str/join (eduction (keep :content) (:components loot))) "`accuracy`"))
          "metadata stays out of the copy-pasteable loot container")
      (is (str/includes? (:content (first (:components metas))) "-# Metadata\n"))
      (is (str/includes? (:content (first (:components metas))) "**Glint** `accuracy`")))))

(deftest metadata-keys
  (testing "an untitled item alone in its section is keyed by the heading"
    (let [[message] (discord/view-model->messages
                      {:loot/title    "Soul"
                       :loot/sections [{:section/heading "Passive"
                                        :section/items   [{:item/body     "+2 damage"
                                                           :item/metadata ["offence"]}]}]})]
      (is (str/includes? (content message) "**Passive** `offence`"))
      (is (str/includes? (content message) "\n+2 damage")
          "no number is added when a heading can serve as the key")))
  (testing "with neither title nor sole-item heading, items are numbered"
    (let [[message] (discord/view-model->messages
                      {:loot/title    "Relic"
                       :loot/sections [{:section/items [{:item/body     "first affix"
                                                         :item/metadata ["[1; +6/1]"]}
                                                        {:item/body     "second affix"
                                                         :item/metadata ["[4; +1/4]"]}]}]})]
      (is (str/includes? (content message) "1. first affix"))
      (is (str/includes? (content message) "2. second affix"))
      (is (str/includes? (content message) "**1** `[1; +6/1]`"))
      (is (str/includes? (content message) "**2** `[4; +1/4]`"))))
  (testing "an item without metadata keeps its place in a numbered list"
    (let [[message] (discord/view-model->messages
                      {:loot/title    "Relic"
                       :loot/sections [{:section/items [{:item/body "A" :item/metadata ["a"]}
                                                        {:item/body "B"}
                                                        {:item/body "C" :item/metadata ["c"]}]}]})]
      (is (str/includes? (content message) "1. A\n\n2. B\n\n3. C"))
      (is (str/includes? (content message) "**3** `c`")))))

(deftest minimal-view-model
  (testing "a title-only view-model still produces a single valid message"
    (let [[message :as messages] (discord/view-model->messages {:loot/title "Dust"})]
      (is (= 1 (count messages)))
      (is (= 1 (count (containers message))))
      (is (= "# Dust" (content message))))))

(deftest words-are-spoilered-once
  (testing "the words the engine drew lead the first message"
    (let [[message] (discord/view-model->messages (assoc vm :loot/words ["quiet" "ember"]))]
      (is (= "||quiet ember||" (:content (first (:components message)))))))
  (testing "a view-model without words posts containers only"
    (let [[message] (discord/view-model->messages vm)]
      (is (every? (comp #{17} :type) (:components message))))))

;; ------------------------------------------------------------------ splitting

(defn- long-section [heading n]
  {:section/heading heading
   :section/items   (mapv (fn [i] {:item/body (str i " " (str/join (repeat 400 \x)))}) (range n))})

(deftest oversized-view-models-are-split
  (testing "every message stays inside Discord's ceilings"
    (let [messages (discord/view-model->messages
                     {:loot/title    "Boon options"
                      :loot/words    ["quiet" "ember"]
                      :loot/sections (mapv #(long-section (str "Option " %) 3) (range 6))})]
      (is (< 1 (count messages)))
      (is (every? #(<= (text-size %) 4000) messages))
      (is (every? #(<= (component-count %) 40) messages))))
  (testing "sections are never split across messages"
    (let [messages (discord/view-model->messages
                     {:loot/title    "Boon options"
                      :loot/sections (mapv #(long-section (str "Option " %) 3) (range 6))})
          homes    (mapv (fn [heading]
                           (count (filterv #(str/includes? (content %) heading) messages)))
                         (mapv #(str "## Option " %) (range 6)))]
      (is (every? #{1} homes) "each heading appears in exactly one message")))
  (testing "a lone section is split between its items instead"
    (let [messages (discord/view-model->messages
                     {:loot/title    "Relic"
                      :loot/sections [(long-section "Affixes" 15)]})
          homes    (reduce (fn [acc i]
                             (assoc acc i (count (filterv #(str/includes? (content %) (str "\n" i " xxx"))
                                                          messages))))
                           {}
                           (range 15))]
      (is (< 1 (count messages)))
      (is (every? #(<= (text-size %) 4000) messages))
      (is (= (zipmap (range 15) (repeat 1)) homes)
          "every item survives the split, in exactly one message")
      (is (every? #(str/includes? (content %) "## Affixes") messages)
          "each message repeats the heading it is continuing")))
  (testing "metadata joins the last message"
    (let [messages (discord/view-model->messages
                     {:loot/title    "Boon options"
                      :loot/sections (conj (mapv #(long-section (str "Option " %) 3) (range 6))
                                           {:section/heading "Cost"
                                            :section/items   [{:item/body     "3 dust"
                                                               :item/metadata ["dust"]}]})})]
      (is (str/includes? (content (last messages)) "**Cost** `dust`"))
      (is (not-any? #(str/includes? (content %) "-# Metadata") (butlast messages))))))

(deftest blank-webhook-rejected
  (testing "creating a reporter without a webhook URL throws"
    (is (thrown? Exception (discord/create "")))
    (is (thrown? Exception (discord/create nil)))))

(deftest secret-sections
  (let [vm {:loot/title    "Loot"
            :loot/sections [{:section/heading "Public" :section/items [{:item/body "a"}]}
                            {:section/heading "Hidden" :section/secret? true :section/items [{:item/body "b"}]}]}]
    (testing "are left out by default"
      (let [text (str/join (map content (discord/view-model->messages vm)))]
        (is (str/includes? text "## Public"))
        (is (not (str/includes? text "## Hidden")))))
    (testing "are sent when include-secret?"
      (is (str/includes? (str/join (map content (discord/view-model->messages vm true))) "## Hidden")))))

(deftest empty-sections
  (testing "a section with no items, or none that render anything, is left out"
    (let [text (->> (discord/view-model->messages
                      {:loot/title    "Loot"
                       :loot/sections [{:section/heading "Kept" :section/items [{:item/body "a"}]}
                                       {:section/heading "Blanked"
                                        :section/items   [{:item/body "" :item/metadata ["gone"]}
                                                          {:item/body "  "}]}
                                       {:section/heading "Emptied" :section/items []}]})
                    (map content)
                    str/join)]
      (is (str/includes? text "## Kept"))
      (is (not (str/includes? text "## Blanked")))
      (is (not (str/includes? text "## Emptied")))
      (is (not (str/includes? text "gone"))
          "a dropped item takes its metadata with it")))
  (testing "a blank item neither shows nor takes a number from the ones that do"
    (let [[message] (discord/view-model->messages
                      {:loot/title    "Relic"
                       :loot/sections [{:section/items [{:item/body "A" :item/metadata ["a"]}
                                                        {:item/body ""}
                                                        {:item/body "C" :item/metadata ["c"]}]}]})]
      (is (str/includes? (content message) "1. A\n\n2. C"))
      (is (str/includes? (content message) "**2** `c`"))))
  (testing "a view-model whose sections all drop still posts one valid message"
    (let [messages (discord/view-model->messages
                     {:loot/title    "Loot"
                      :loot/sections [{:section/heading "Emptied" :section/items []}]})]
      (is (= 1 (count messages)))
      (is (= ["# Loot"] (mapv content messages))))))
