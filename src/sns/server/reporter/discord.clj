(ns sns.server.reporter.discord
  "A `Reporter` that posts a loot view-model to a Discord webhook as a rich embed."
  (:require
    [clojure.string :as str]
    [hato.client :as hc]
    [jsonista.core :as j]
    [sns.sdk.protocols :as p]))

(def ^:private default-username "\uD83D\uDCB0 SNS Companion \uD83D\uDCB0")
(def ^:private gilt 0xC8A24C) ; embed accent, matching the UI theme

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

(defn payload
  "The webhook body. The spoilered content is the result's engine-drawn
   `:loot/words`, so the reader sees the same handle the UI showed."
  [{:keys [avatar-url discord-username]} view-model]
  (cond-> {:avatar_url avatar-url
           :username   (or discord-username default-username)
           :embeds     (view-model->embeds view-model)}
          (seq (:loot/words view-model))
          (assoc :content (str "||" (str/join \space (:loot/words view-model)) "||"))))

(defn create
  "Build a Discord `Reporter` posting to `webhook-url`."
  [{:keys [webhook-url] :as config}]
  (when (str/blank? webhook-url)
    (throw (ex-info "Discord reporting requires a :webhook-url" {})))
  (let [client (hc/build-http-client {:connect-timeout 10000})]
    (reify p/Reporter
      (report-label [_] "Send to Discord")
      (report! [_ view-model]
        (hc/post webhook-url {:http-client  client
                              :timeout      10000
                              :content-type :json
                              :body         (-> (payload config view-model)
                                                j/write-value-as-string)})
        nil))))
