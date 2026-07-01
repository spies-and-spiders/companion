(ns sns.server.http
  "Ring/reitit HTTP layer: a small EDN-over-HTTP API plus static serving of the
   compiled SPA. EDN (not transit) keeps the wire format readable and lets both
   ends share `pr-str`/`read-string` without a transit dependency; the engine is
   the single source of generation."
  (:require
    [clojure.edn :as edn]
    [reitit.ring :as ring]
    [ring.util.response :as response]
    [sns.server.engine :as engine])
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

(def ^:private content-type "application/edn")

(defn- edn-response
  ([data] (edn-response 200 data))
  ([status data]
   {:status  status
    :headers {"Content-Type" content-type}
    :body    (pr-str data)}))

(defn- read-body
  "Parse an EDN request body (a Ring InputStream), nil for an empty body."
  [body]
  (when body
    (let [s (slurp body)]
      (when (seq s) (edn/read-string s)))))

(defn- wrap-errors
  "Translate uncaught exceptions into an EDN error response so the client can
   surface a message rather than an opaque 500 body."
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch ExceptionInfo e
        (edn-response 400 {:error (ex-message e) :data (ex-data e)}))
      (catch Exception e
        (edn-response 500 {:error (ex-message e)})))))

(defn- loot-types-handler [eng]
  (fn [_req]
    (edn-response (engine/loot-specs eng))))

(defn- generate-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [id inputs]} (read-body body)]
      (edn-response (engine/generate eng id (or inputs {}))))))

(defn- roll-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [inputs]} (read-body body)]
      (edn-response (engine/roll eng (or inputs {}))))))

(defn- action-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [id action params]} (read-body body)]
      (edn-response (engine/handle-action eng id action params)))))

(defn- capabilities-handler [eng]
  (fn [_req]
    (edn-response (engine/capabilities eng))))

(defn- report-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [view-model]} (read-body body)]
      (edn-response (engine/report eng view-model)))))

(defn app
  "Build the ring handler for loot `eng`."
  [eng]
  (ring/ring-handler
    (ring/router
      [["/api/loot-types" {:get (loot-types-handler eng)}]
       ["/api/capabilities" {:get (capabilities-handler eng)}]
       ["/api/generate" {:post (generate-handler eng)}]
       ["/api/roll" {:post (roll-handler eng)}]
       ["/api/action" {:post (action-handler eng)}]
       ["/api/report" {:post (report-handler eng)}]]
      {:data {:middleware [wrap-errors]}})
    (ring/routes
      (ring/create-resource-handler {:path "/" :root "public"})
      ;; SPA fallback: unmatched GETs serve the app shell.
      (fn [_req]
        (or (some-> (response/resource-response "index.html" {:root "public"})
                    (response/content-type "text/html"))
            (response/not-found "Not found"))))))
