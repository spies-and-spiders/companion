(ns sns.ui.spies
  "Hover previews for spies.tools spell and maneuver links: which entry a link
   points at, and a compact card for it, rendered from the site's own data."
  (:require
    [clojure.string :as str]))

(def ^:private data-keys {"spells" :spell "maneuvers" :maneuver})

(defn- decode [s]
  (try
    #?(:clj  (java.net.URLDecoder/decode (str/replace s "+" "%2B") "UTF-8")
       :cljs (js/decodeURIComponent s))
    (catch #?(:clj Exception :cljs :default) _ nil)))

(defn target
  "`[page name]` for a spies.tools spell or maneuver link, the name lowercased
   as the link's hash carries it, or nil for any other link."
  [href]
  (when-let [[_ page hash] (re-matches #"https://spies\.tools/(spells|maneuvers)\.html#(.+)_sns" href)]
    (some->> (decode hash) (vector page))))

(defn data-url [page]
  (str "https://spies.tools/data/" page "/" page "-sns.json"))

(defn index
  "A page's data file, keyed by lowercased name as `target` returns it."
  [page data]
  (into {} (map (juxt (comp str/lower-case :name) identity)) (get data (data-keys page))))

(defn- untag
  "`{@tag text|source|display}` as the text the site shows for it."
  [s]
  (str/replace s #"\{@(\w+) ([^}]*)\}"
               (fn [[_ tag body]]
                 (let [[text _ display] (str/split body #"\|")]
                   (if (= "dc" tag)
                     (str "DC " text)
                     (or (not-empty display) text))))))

(defn- entry [e]
  (if (string? e)
    [:p (untag e)]
    (case (:type e)
      "entries" [:div.spies__section
                 (when (:name e) [:b.spies__heading (:name e)])
                 (map entry (:entries e))]
      "list"    [:ul (for [i (:items e)]
                       [:li (if (string? i) (untag i) (entry i))])]
      "item"    (list (when (:name e) [:b (str (:name e) ". ")])
                      (for [x (or (:entries e) [(:entry e)])]
                        (if (string? x) (untag x) (entry x))))
      "table"   [:table
                 (when (:caption e) [:caption (:caption e)])
                 (when (:colLabels e) [:thead [:tr (for [l (:colLabels e)] [:th (untag l)])]])
                 [:tbody (for [row (:rows e)]
                           [:tr (for [c row] [:td (if (string? c) (untag c) (entry c))])])]]
      nil)))

(def ^:private schools
  {"A" "Abjuration" "C" "Conjuration" "D" "Divination" "E" "Enchantment"
   "I" "Illusion"   "N" "Necromancy"  "P" "Psionic"    "T" "Transmutation" "V" "Evocation"})

(defn- ordinal [n]
  (str n (let [k (mod n 100)]
           (cond (<= 11 k 13)     "th"
                 (= 1 (mod n 10)) "st"
                 (= 2 (mod n 10)) "nd"
                 (= 3 (mod n 10)) "rd"
                 :else            "th"))))

(defn- subtitle [page {:keys [level school degree traditions points]}]
  (if (= "spells" page)
    (let [school (schools school school)]
      (if (zero? level) (str school " Cantrip") (str "Level " level " " school)))
    (str (str/join " " (remove nil? [(if (zero? degree) "Basic" (str (ordinal degree) " degree"))
                                     (some->> (seq traditions) (str/join ", "))
                                     "maneuver"]))
         " · " (or points 0) (if (= 1 points) " point" " points"))))

(defn card [page e]
  [:div.spies__card
   [:div.spies__name (:name e)]
   [:div.spies__meta (subtitle page e)]
   (map entry (concat (:entries e) (:entriesHigherLevel e)))])
