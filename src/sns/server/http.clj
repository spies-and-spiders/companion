(ns sns.server.http
  (:require
    [muuntaja.core :as muuntaja]
    [reitit.http :as http]
    [reitit.http.interceptors.exception :as exception]
    [reitit.http.interceptors.muuntaja :as format]
    [reitit.interceptor.sieppari :as sieppari]
    [reitit.ring :as ring]
    [ring.util.response :as response]
    [sns.server.engine :as engine]
    [taoensso.telemere :as t])
  (:import
    (clojure.lang ExceptionInfo)
    (java.net URL)
    (java.util Date)))

(defmethod response/resource-data :resource
  [^URL url]
  (let [conn (.openConnection url)
        len  (.getContentLength conn)
        lm   (.getLastModified conn)]
    {:content        (.getInputStream conn)
     :content-length (when (pos? len) len)
     :last-modified  (when (pos? lm) (Date. lm))}))

(def ^:private m
  (muuntaja/create
    (-> muuntaja/default-options
        (assoc :default-format "application/edn")
        (update :formats select-keys ["application/edn"]))))

(defn- ok [body] {:status 200 :body body})

(def ^:private exception-interceptor
  (exception/exception-interceptor
    (merge
      exception/default-handlers
      {ExceptionInfo
       (fn [e _req] {:status 400 :body {:error (ex-message e) :data (ex-data e)}})

       ::exception/default
       (fn [e _req] {:status 500 :body {:error (ex-message e)}})

       ::exception/wrap
       (fn [handler e req]
         (if (instance? ExceptionInfo e)
           (t/log! {:level :warn :id ::bad-request :data (ex-data e)} (ex-message e))
           (t/log! {:level :error :id ::server-error :error e} "Unhandled request error"))
         (handler e req))})))

(defn- loot-types-handler [eng]
  (fn [_req]
    (ok (engine/loot-specs eng))))

(defn- generate-handler [eng]
  (fn [{{:keys [id inputs]} :body-params}]
    (ok (engine/generate eng id (or inputs {})))))

(defn- roll-handler [eng]
  (fn [{{:keys [inputs n]} :body-params}]
    (ok (engine/roll eng (or inputs {}) n))))

(defn- action-handler [eng]
  (fn [{{:keys [id action params]} :body-params}]
    (ok (engine/handle-action eng id action params))))

(defn- capabilities-handler [eng]
  (fn [_req]
    (ok (engine/capabilities eng))))

(defn- report-handler [eng]
  (fn [{{:keys [view-model]} :body-params}]
    (ok (engine/report eng view-model))))

(defn app [eng]
  (http/ring-handler
    (http/router
      [["/api/loot-types" {:get (loot-types-handler eng)}]
       ["/api/capabilities" {:get (capabilities-handler eng)}]
       ["/api/generate" {:post (generate-handler eng)}]
       ["/api/roll" {:post (roll-handler eng)}]
       ["/api/action" {:post (action-handler eng)}]
       ["/api/report" {:post (report-handler eng)}]]
      {:data {:muuntaja     m
              :interceptors [(format/format-negotiate-interceptor m)
                             (format/format-response-interceptor m)
                             exception-interceptor
                             (format/format-request-interceptor m)]}})
    (ring/routes
      (ring/create-resource-handler {:path "/" :root "public"})
      ;; SPA fallback: unmatched GETs serve the app shell.
      (fn [_req]
        (or (some-> (response/resource-response "index.html" {:root "public"})
                    (response/content-type "text/html"))
            (response/not-found "Not found"))))
    {:executor sieppari/executor}))
