(ns sns.server.http
  "Ring/reitit HTTP layer: a small Transit-over-HTTP API plus static serving of
   the compiled SPA. The engine is the single source of generation."
  (:require
    [reitit.ring :as ring]
    [ring.util.response :as response]
    [sns.server.engine :as engine]
    [sns.server.transit :as transit]))

(defn- transit-response
  ([data] (transit-response 200 data))
  ([status data]
   {:status  status
    :headers {"Content-Type" transit/content-type}
    :body    (transit/write-str data)}))

(defn- wrap-errors
  "Translate uncaught exceptions into a transit error response so the client can
   surface a message rather than an opaque 500 body."
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch clojure.lang.ExceptionInfo e
        (transit-response 400 {:error (ex-message e) :data (ex-data e)}))
      (catch Exception e
        (transit-response 500 {:error (ex-message e)})))))

(defn- loot-types-handler [eng]
  (fn [_req]
    (transit-response (engine/loot-specs eng))))

(defn- generate-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [id inputs]} (transit/read-stream body)]
      (transit-response (engine/generate eng id (or inputs {}))))))

(defn- roll-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [inputs]} (transit/read-stream body)]
      (transit-response (engine/roll eng (or inputs {}))))))

(defn- action-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [id action params]} (transit/read-stream body)]
      (transit-response (engine/handle-action eng id action params)))))

(defn- capabilities-handler [eng]
  (fn [_req]
    (transit-response (engine/capabilities eng))))

(defn- report-handler [eng]
  (fn [{:keys [body]}]
    (let [{:keys [view-model]} (transit/read-stream body)]
      (transit-response (engine/report eng view-model)))))

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
