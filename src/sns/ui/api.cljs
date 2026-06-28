(ns sns.ui.api
  "Transit-over-HTTP client for the backend API."
  (:require
    [clojure.string :as str]
    [cognitect.transit :as t]))

(def ^:private reader (t/reader :json))
(def ^:private writer (t/writer :json))

(defn request
  "Issue a transit request to `url`. On success calls `(on-ok decoded-body)`;
   on any error calls `(on-err {:error msg ...})`."
  [{:keys [method url body]} on-ok on-err]
  (let [init (cond-> {:method  (-> (or method :get) name str/upper-case)
                      :headers {"Accept" "application/transit+json"}}
                     body (-> (assoc :body (t/write writer body))
                              (assoc-in [:headers "Content-Type"] "application/transit+json")))]
    (-> (js/fetch url (clj->js init))
        (.then (fn [res] (.then (.text res) (fn [text] #js [res text]))))
        (.then (fn [pair]
                 (let [res  (aget pair 0)
                       text (aget pair 1)
                       data (when (seq text) (t/read reader text))]
                   (if (.-ok res)
                     (on-ok data)
                     (on-err (if (map? data) data {:error (str "HTTP " (.-status res))}))))))
        (.catch (fn [err] (on-err {:error (str err)}))))))
