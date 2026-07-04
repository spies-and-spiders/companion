(ns sns.server.reporter.discord
  "A `Reporter` that posts a loot view-model to a Discord webhook as a rich
   embed. Uses the JDK HTTP client so no extra dependency is needed."
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [jsonista.core :as j]
    [randy.core :as r]
    [sns.spi.protocols :as p])
  (:import
    (java.io PushbackReader)
    (java.net URI)
    (java.net.http HttpClient HttpRequest HttpRequest$BodyPublishers HttpResponse$BodyHandlers)
    (java.time Duration)))

(def ^:private default-username "\uD83D\uDCB0 SNS Companion \uD83D\uDCB0")
(def ^:private words (-> (io/resource "words.edn")
                         io/reader
                         PushbackReader.
                         edn/read))
(def ^:private gilt 0xC8A24C)              ; embed accent, matching the UI theme

(defn- loot-message-unique-name []
  (->> (r/sample-without-replacement 2 words)
       (str/join \space)))

(defn- item-lines [{:item/keys [title body metadata]}]
  (cond-> []
          title          (conj (str "**" title "**"))
          body           (conj body)
          (seq metadata) (conj (str/join " " (mapv #(str "`" % "`") metadata)))))

(defn- section-lines [{:section/keys [heading items]}]
  (cond-> []
          heading (conj (str "__" heading "__"))
          true    (into (mapcat item-lines items))))

(defn view-model->embed
  "Render a view-model as a Discord embed map."
  [{:loot/keys [title subtitle sections]}]
  (let [desc (->> sections
                  (mapcat section-lines)
                  (str/join "\n")
                  (str (when subtitle (str "*" subtitle "*\n\n"))))]
    (cond-> {:title (or title "Loot") :color gilt}
            (seq desc) (assoc :description desc))))

(defn- payload [{:keys [avatar-url discord-username]} view-model]
  (j/write-value-as-string {:content    (str "||" (loot-message-unique-name) "||")
                            :avatar_url avatar-url
                            :username   (or discord-username default-username)
                            :embeds     [(view-model->embed view-model)]}))

(defn create
  "Build a Discord `Reporter` posting to `webhook-url`."
  [{:keys [webhook-url] :as config}]
  (when (str/blank? webhook-url)
    (throw (ex-info "Discord reporting requires a :webhook-url" {})))
  (let [client (-> (HttpClient/newBuilder)
                   (.connectTimeout (Duration/ofSeconds 10))
                   (.build))]
    (reify p/Reporter
      (report-label [_] "Send to Discord")
      (report! [_ view-model]
        (let [req  (-> (HttpRequest/newBuilder (URI/create webhook-url))
                       (.header "Content-Type" "application/json")
                       (.timeout (Duration/ofSeconds 10))
                       (.POST (HttpRequest$BodyPublishers/ofString (payload config view-model)))
                       (.build))
              resp (.send client req (HttpResponse$BodyHandlers/ofString))
              code (.statusCode resp)]
          (if (<= 200 code 299)
            {:ok true :status code}
            (throw (ex-info "Discord webhook rejected the report"
                            {:status code :body (.body resp)}))))))))
