(ns sns.server.reporter.discord
  "A `Reporter` that posts a loot view-model to a Discord webhook as Components
   V2 containers. Discord budgets every message as a whole, so a view-model too
   large for one message is spread over several."
  (:require
    [clojure.string :as str]
    [hato.client :as hc]
    [jsonista.core :as j]
    [sns.sdk.protocols :as p]))

(def ^:private default-username "💰 SNS Companion 💰")
(def ^:private gilt 0xC8A24C) ; container accent, matching the UI theme
(def ^:private components-v2 32768)

;; Discord's per-message ceilings: displayable text across every component, and
;; the component count including nested ones.
(def ^:private max-text 4000)
(def ^:private max-components 40)

(defn- text [content] {:type    10
                       :content content})
(def ^:private rule {:type    14
                     :divider true
                     :spacing 1})

;; ---------------------------------------------------------------- view-model

(defn- chips [metadata]
  (str/join " " (eduction (map #(str "`" % "`")) metadata)))

(defn- item-text [number {:item/keys [title body]}]
  (cond->> body
           number (str number ". ")
           title  (str "**" title "**\n")))

(defn- add-section
  "Renders one section's items, and its metadata keyed by whatever the reader
   can see: the item's title, the heading of a section holding it alone, or —
   failing both — a number put in front of the body."
  [acc {:section/keys [heading items]}]
  (let [single? (= 1 (count items))
        step    (fn [[texts n metas] {:item/keys [title metadata] :as item}]
                  (let [k      (or title (when single? heading))
                        number (when (and (seq metadata) (nil? k)) (inc n))]
                    [(conj texts (item-text number item))
                     (or number n)
                     (cond-> metas
                             (seq metadata) (conj (str "**" (or k number) "** " (chips metadata))))]))
        [texts n metas] (reduce step [[] (:n acc) []] items)]
    (-> (assoc acc :n n)
        (update :sections conj {:heading heading :items texts})
        (update :meta into metas))))

;; ------------------------------------------------------------------ batching

(defn- units
  "The indivisible pieces a message is packed from: whole sections, or — when
   there is only one section to give — its individual items."
  [sections]
  (if (= 1 (count sections))
    (let [{:keys [heading items]} (first sections)]
      (mapv #(hash-map :idx 0 :heading heading :items [%]) items))
    (into [] (map-indexed #(assoc %2 :idx %1)) sections)))

(defn- unit-size [{:keys [heading items]}]
  (+ (if heading (+ 3 (count heading)) 0)
     (count (str/join "\n\n" items))))

(defn- fits?
  "Whether `units` still clear both ceilings once the fixed per-message parts —
   the title, the spoilered words and the metadata — are paid for."
  [fixed units]
  (and (<= (+ fixed (transduce (map unit-size) + units)) max-text)
       (<= (+ 5 (* 3 (count units))) max-components)))

(defn- batch
  "Greedily groups units into message-sized batches. A unit larger than a whole
   message lands in a batch of its own and is left to Discord to reject."
  [fixed units]
  (reduce (fn [batches unit]
            (if-let [current (peek batches)]
              (let [grown (conj current unit)]
                (if (fits? fixed grown)
                  (conj (pop batches) grown)
                  (conj batches [unit])))
              [[unit]]))
          []
          units))

;; ----------------------------------------------------------------- rendering

(defn- blocks
  "Components for a batch, re-joining units that came from the same section so a
   split section is not given its heading twice in one message."
  [batch]
  (into []
        (comp (partition-by :idx)
              (mapcat (fn [group]
                        (let [heading (:heading (first group))
                              items (str/join "\n\n" (eduction (mapcat :items) group))]
                          (-> [rule]
                              (cond-> heading (conj (text (str "## " heading))))
                              (conj (text items)))))))
        batch))

(defn- container [components]
  {:type         17
   :accent_color gilt
   :components   (vec components)})

(defn- meta-container [meta]
  {:type       17
   :components [(text (str "-# Metadata\n" (str/join "\n" meta)))]})

(defn view-model->messages
  "Render a view-model as one or more Components V2 message bodies. Sections are
   kept whole; a lone section is split between its items instead. The spoilered
   content is the engine-drawn `:loot/words`, so the reader sees the same handle
   the UI showed. Metadata rides in a second, quieter container."
  [{:loot/keys [title subtitle words] :as vm}]
  (let [{:keys [sections meta]} (reduce add-section
                                        {:n 0 :sections [] :meta []}
                                        (:loot/sections vm))
        head     (text (cond-> (str "# " (or title "Loot"))
                               subtitle (str "\n-# " subtitle)))
        spoiler  (when (seq words) (text (str "||" (str/join \space words) "||")))
        metas    (when (seq meta) (meta-container meta))
        fixed    (+ (count (:content head))
                    (count (:content spoiler ""))
                    (count (get-in metas [:components 0 :content] "")))
        batches  (batch fixed (units sections))
        loot     (mapv #(vector (container (into [head] (blocks %))))
                       (if (seq batches) batches [[]]))
        bodies   (cond-> loot metas (update (dec (count loot)) conj metas))]
    (into []
          (map-indexed (fn [i components]
                         {:flags      components-v2
                          :components (cond->> components
                                               (and spoiler (zero? i)) (into [spoiler]))}))
          bodies)))

(defn payload
  "One webhook body, identifying the poster."
  [{:keys [avatar-url username]} message]
  (assoc message
         :avatar_url avatar-url
         :username (or username default-username)))

(defn create
  "Build a Discord `Reporter` posting to `webhook-url`."
  [{:keys [webhook-url] :as config}]
  (when (str/blank? webhook-url)
    (throw (ex-info "Discord reporting requires a :webhook-url" {})))
  (let [client (hc/build-http-client {:connect-timeout 10000})]
    (reify p/Reporter
      (report-label [_] "Send to Discord")
      (report! [_ view-model]
        (run! (fn [message]
                (hc/post webhook-url {:http-client  client
                                      :timeout      10000
                                      :content-type :json
                                      :query-params {:with_components true}
                                      :body         (-> (payload config message)
                                                        j/write-value-as-string)}))
              (view-model->messages view-model))
        nil))))
