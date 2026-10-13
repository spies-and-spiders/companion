(ns sns.server.http
  (:require
    [clojure.string :as str]
    [muuntaja.core :as muuntaja]
    [reitit.http :as http]
    [reitit.http.interceptors.exception :as exception]
    [reitit.http.interceptors.muuntaja :as format]
    [reitit.interceptor.sieppari :as sieppari]
    [reitit.ring :as ring]
    [ring.middleware.not-modified :as not-modified]
    [ring.util.http-response :refer [ok]]
    [ring.util.mime-type :as mime]
    [ring.util.response :as response]
    [sns.server.engine :as engine]
    [sns.server.store :as store]
    [sns.server.store.edn :as edn-store]
    [taoensso.telemere :as t])
  (:import
    (clojure.lang ExceptionInfo)
    (java.io ByteArrayInputStream ByteArrayOutputStream)
    (java.net URL)
    (java.nio.charset StandardCharsets)
    (java.util Date)
    (java.util.zip ZipEntry ZipOutputStream)))

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

(def ^:private exception-interceptor
  (exception/exception-interceptor
    (merge
      exception/default-handlers
      {ExceptionInfo
       (fn [e _req] {:status 400 :body {:error (ex-message e) :data (ex-data e)}})

       ::exception/default
       (fn [e _req] {:status 500 :body {:error (or (ex-message e) (.getName (class e)))}})

       ::exception/wrap
       (fn [handler e req]
         (if (instance? ExceptionInfo e)
           (t/log! {:level :warn :id ::bad-request :data (ex-data e)} (ex-message e))
           (t/log! {:level :error :id ::server-error :error e} "Unhandled request error"))
         (handler e req))})))

(defn- loot-types-handler [eng]
  (fn loot-types [_req]
    (ok (engine/loot-specs eng))))

(defn- with-mutations
  "Keep `response`'s writes only under `:browser` storage, where the client
   applies them to its IndexedDB."
  [eng response]
  (cond-> response
          (not (store/browser? (:config eng))) (dissoc :store/mutations)))

(defn- generate-handler [eng]
  (fn generate [{{:keys [id inputs state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/generate eng id (or inputs {})))))))

(defn- roll-handler [eng]
  (fn roll [{{:keys [inputs n state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/roll eng (or inputs {}) n))))))

(defn- action-handler [eng]
  (fn handle-action [{{:keys [id action params view-model inputs state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/handle-action eng id action params view-model (or inputs {})))))))

(defn- capabilities-handler [eng]
  (fn capabilities [_req]
    (ok (engine/capabilities eng))))

(defn- report-handler [eng]
  (fn report [{{:keys [view-model]} :body-params}]
    (engine/report! eng view-model)
    (ok)))

(defn- state-handler
  "Apply `:mutations` to a loot type's `:store/manual` collection, then read it."
  [eng]
  (fn manual-state [{{:keys [id mutations state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/manual-state eng id mutations))))))

(defn- history-handler [eng]
  (fn history [{{:keys [mutations state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/history eng mutations))))))

(defn- zip-bytes
  "A ZIP of `<collection>.edn` files that unzips into a usable `:file` state
   directory."
  ^bytes [state]
  (let [out (ByteArrayOutputStream.)]
    (with-open [zip (ZipOutputStream. out)]
      (doseq [[coll value] (sort-by key state)]
        (.putNextEntry zip (ZipEntry. (str (name coll) ".edn")))
        (.write zip ^bytes (.getBytes ^String (edn-store/edn-str value) StandardCharsets/UTF_8))
        (.closeEntry zip)))
    (.toByteArray out)))

(defn- export-handler [eng]
  (fn export-state [_req]
    (when-not (:store eng)
      (throw (ex-info "Browser storage exports from the client, not the server" {})))
    {:status  200
     :headers {"Content-Type"        "application/zip"
               "Content-Disposition" "attachment; filename=\"sns-state.zip\""}
     :body    (ByteArrayInputStream. (zip-bytes (edn-store/all-state (:store eng))))}))

(defn- accepts-gzip? [request]
  (some-> (get-in request [:headers "accept-encoding"]) (str/includes? "gzip")))

(defn- gzipped
  "The resource's precompressed `.gz`, if the client accepts gzip."
  [resources {:keys [uri] :as request}]
  (when (accepts-gzip? request)
    (some-> (resources (assoc request :uri (str uri ".gz")))
            (response/content-type (mime/ext-mime-type uri))
            (response/header "Content-Encoding" "gzip"))))

(defn- static-handler
  "The SPA's files, revalidated on every use. Unknown paths are client routes,
   so they get index.html."
  []
  (let [resources (ring/create-resource-handler {:path "/" :root "public"})]
    (fn [request]
      (some-> (or (gzipped resources request)
                  (resources request)
                  (resources (assoc request :uri "/")))
              (update :headers merge {"Cache-Control" "no-cache"
                                      "Vary"          "Accept-Encoding"})))))

(def ^:private not-modified-interceptor
  "Return 304 when the client's copy is current."
  {:name  ::not-modified
   :leave (fn [{:keys [request] :as ctx}]
            (update ctx :response not-modified/not-modified-response request))})

(defn app [eng]
  (http/ring-handler
    (http/router
      [["/api/loot-types" {:get (loot-types-handler eng)}]
       ["/api/capabilities" {:get (capabilities-handler eng)}]
       ["/api/generate" {:post (generate-handler eng)}]
       ["/api/roll" {:post (roll-handler eng)}]
       ["/api/action" {:post (action-handler eng)}]
       ["/api/report" {:post (report-handler eng)}]
       ["/api/state" {:post (state-handler eng)}]
       ["/api/history" {:post (history-handler eng)}]
       ["/api/export" {:get (export-handler eng)}]]
      {:data {:muuntaja     m
              :interceptors [(format/format-negotiate-interceptor m)
                             (format/format-response-interceptor m)
                             exception-interceptor
                             (format/format-request-interceptor m)]}})
    (static-handler)
    {:executor     sieppari/executor
     :interceptors [not-modified-interceptor]}))
