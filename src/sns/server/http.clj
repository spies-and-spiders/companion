(ns sns.server.http
  (:require
    [muuntaja.core :as muuntaja]
    [reitit.http :as http]
    [reitit.http.interceptors.exception :as exception]
    [reitit.http.interceptors.muuntaja :as format]
    [reitit.interceptor.sieppari :as sieppari]
    [reitit.ring :as ring]
    [ring.util.http-response :refer [ok]]
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
       (fn [e _req] {:status 500 :body {:error (ex-message e)}})

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
  "The writes declared on `response` are the client's to apply under `:browser`,
   where the state lives in its IndexedDB. Every other backend has persisted them
   already, so they are stripped rather than sent."
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
  (fn handle-action [{{:keys [id action params view-model state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/handle-action eng id action params view-model))))))

(defn- capabilities-handler [eng]
  (fn capabilities [_req]
    (ok (engine/capabilities eng))))

(defn- report-handler [eng]
  (fn report [{{:keys [view-model]} :body-params}]
    (engine/report! eng view-model)
    (ok)))

(defn- state-handler
  "Read (and first apply `:mutations` to) a loot type's manually-managed
   collection — the DM-owned table its `:store/manual` spec declares. Shaped
   like every other stateful call: state in, mutations out, so the same handler
   serves a server-side store and one living in the DM's browser."
  [eng]
  (fn manual-state [{{:keys [id mutations state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/manual-state eng id mutations))))))

(defn- history-handler
  "The result history, read and written like any other collection so it follows
   the configured storage backend."
  [eng]
  (fn history [{{:keys [mutations state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/history eng mutations))))))

(defn- zip-bytes
  "A ZIP holding one `<collection>.edn` per collection, written by the same
   serialiser the `:file` backend uses so the archive unzips straight into a
   usable state directory."
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
    (ring/routes
      (ring/create-resource-handler {:path "/" :root "public"})
      ;; SPA fallback: unmatched GETs serve the app shell.
      (fn [_req]
        (or (some-> (response/resource-response "index.html" {:root "public"})
                    (response/content-type "text/html"))
            (response/not-found "Not found"))))
    {:executor sieppari/executor}))
