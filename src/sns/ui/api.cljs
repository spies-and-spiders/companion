(ns sns.ui.api
  "EDN-over-HTTP client for the backend API."
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]))

(def ^:private content-type "application/edn")

(defn ->error
  "Any rejection as the `{:error msg}` map the UI shows."
  [e]
  (if (map? e) e {:error (str e)}))

(defn request
  "Issue an EDN request to `url`, resolving to the decoded body or rejecting
   with `{:error msg ...}`."
  [{:keys [method url body]}]
  (let [init (cond-> {:method  (-> (or method :get) name str/upper-case)
                      :headers {"Accept" content-type}}
                     body (-> (assoc :body (pr-str body))
                              (assoc-in [:headers "Content-Type"] content-type)))]
    (-> (js/fetch url (clj->js init))
        (.then (fn [res]
                 (.then (.text res)
                        (fn [text]
                          (let [data (when (seq text) (edn/read-string text))]
                            (if (.-ok res)
                              data
                              (throw (cond-> {:error (or (when (map? data) (:error data))
                                                         (str "HTTP " (.-status res)))}
                                             (map? data) (merge (dissoc data :error))))))))))
        (.catch (fn [e] (throw (->error e)))))))
