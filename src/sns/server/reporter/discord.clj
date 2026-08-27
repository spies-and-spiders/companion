(ns sns.server.reporter.discord
  "A `Reporter` that posts a loot view-model to a Discord webhook as a rich embed."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [hato.client :as hc]
    [jsonista.core :as j]
    [randy.core :as r]
    [sns.sdk.protocols :as p])
  (:import
    (java.io PushbackReader)))

(def ^:private default-username "\uD83D\uDCB0 SNS Companion \uD83D\uDCB0")
(def ^:private default-words (-> (io/resource "words.edn")
                                 io/reader
                                 PushbackReader.
                                 edn/read))
(def ^:private gilt 0xC8A24C) ; embed accent, matching the UI theme

(defn- loot-message-unique-name [words]
  (->> (r/sample-without-replacement 2 words)
       (str/join \space)))

(defn- chips [metadata]
  (str/join " " (map #(str "`" % "`") metadata)))

(defn- add-item
  "Adds one item's lines, and its metadata keyed by whatever the reader can see:
   the item's title, the heading of a section holding it alone, or — failing
   both — a number put in front of the body."
  [heading single? {:keys [n] :as acc} {:item/keys [title body metadata]}]
  (let [k      (or title (when single? heading))
        number (when (and (seq metadata) (nil? k)) (inc n))]
    (cond-> acc
            number         (assoc :n number)
            title          (update :lines conj (str "**" title "**"))
            true           (update :lines conj (cond->> body number (str number ". ")))
            (seq metadata) (update :meta conj (str "**" (or k number) "** " (chips metadata))))))

(defn- add-section [acc {:section/keys [heading items]}]
  (reduce #(add-item heading (= 1 (count items)) %1 %2)
          (cond-> acc heading (update :lines conj (str "__" heading "__")))
          items))

(defn view-model->embeds
  "Render a view-model as Discord embeds: the loot itself, then — when anything
   carries metadata — a second, quieter embed holding it. Keeping metadata out
   of the first embed leaves its description copy-pasteable as-is."
  [{:loot/keys [title subtitle sections]}]
  (let [{:keys [lines meta]} (reduce add-section {:lines [] :meta [] :n 0} sections)
        desc (->> (str/join "\n" lines)
                  (str (when subtitle (str "*" subtitle "*\n\n"))))]
    (cond-> [(cond-> {:title (or title "Loot") :color gilt}
                     (seq desc) (assoc :description desc))]
            (seq meta) (conj {:description (str "-# Metadata\n" (str/join "\n" meta))}))))

(defn- payload [{:keys [avatar-url discord-username words]} view-model]
  {:content    (str "||" (loot-message-unique-name words) "||")
   :avatar_url avatar-url
   :username   (or discord-username default-username)
   :embeds     (view-model->embeds view-model)})

(defn- build-words [words extra-words]
  (cond
    (seq words) words
    (seq extra-words) (vec (into (set default-words) extra-words))
    :else default-words))

(defn create
  "Build a Discord `Reporter` posting to `webhook-url`."
  [{:keys [webhook-url extra-words] :as config}]
  (when (str/blank? webhook-url)
    (throw (ex-info "Discord reporting requires a :webhook-url" {})))
  (let [client (hc/build-http-client {:connect-timeout 10000})
        config (-> (update config :words build-words extra-words)
                   (dissoc :extra-words))]
    (reify p/Reporter
      (report-label [_] "Send to Discord")
      (report! [_ view-model]
        (hc/post webhook-url {:http-client  client
                              :timeout      10000
                              :content-type :json
                              :body         (-> (payload config view-model)
                                                j/write-value-as-string)})
        nil))))
