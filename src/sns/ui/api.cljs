(ns sns.ui.api
  "EDN-over-HTTP client for the backend API."
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]))

(def ^:private content-type "application/edn")

(defn request
  "Issue an EDN request to `url`. On success calls `(on-ok decoded-body)`;
   on any error calls `(on-err {:error msg ...})`."
  [{:keys [method url body]} on-ok on-err]
  (let [init (cond-> {:method  (-> (or method :get) name str/upper-case)
                      :headers {"Accept" content-type}}
                     body (-> (assoc :body (pr-str body))
                              (assoc-in [:headers "Content-Type"] content-type)))]
    (-> (js/fetch url (clj->js init))
        (.then (fn [res] (.then (.text res) (fn [text] #js [res text]))))
        (.then (fn [pair]
                 (let [res  (aget pair 0)
                       text (aget pair 1)
                       data (when (seq text) (edn/read-string text))]
                   (if (.-ok res)
                     (on-ok data)
                     (on-err (if (map? data) data {:error (str "HTTP " (.-status res))}))))))
        (.catch (fn [err] (on-err {:error (str err)}))))))
