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
    [sns.server.social :as social]
    [sns.server.store.edn :as edn-store]
    [sns.server.store.request :as request]
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
  (fn [_req]
    (ok (engine/loot-specs eng))))

(defn- with-mutations
  "Return `view-model`, carrying any writes the plugin made under
   `:store/mutations` for the client to apply. Only `:browser` storage records
   them; every other backend has already persisted its own."
  [eng view-model]
  (cond-> view-model
          (request/recorded-mutations (:store eng))
          (assoc :store/mutations (request/recorded-mutations (:store eng)))))

(defn- generate-handler [eng]
  (fn [{{:keys [id inputs state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/generate eng id (or inputs {})))))))

(defn- roll-handler [eng]
  (fn [{{:keys [inputs n state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/roll eng (or inputs {}) n))))))

(defn- action-handler [eng]
  (fn [{{:keys [id action params view-model state]} :body-params}]
    (let [eng (engine/with-state eng state)]
      (ok (with-mutations eng (engine/handle-action eng id action params view-model))))))

(defn- capabilities-handler [eng]
  (fn [_req]
    (ok (engine/capabilities eng))))

(defn- report-handler [eng]
  (fn [{{:keys [view-model]} :body-params}]
    (engine/report! eng view-model)
    (ok)))

;; --- the always-on Group Deception & Persuasion tracker (not a plugin) -------
;; State-in/mutations-out like every other endpoint, so the tracker persists the
;; same way plugins do — including under :browser, where its `:social`
;; collection sits in IndexedDB alongside theirs and is covered by export.

(defn- social-handler
  "Wrap a tracker operation so it reads the client's state (`:browser`) or the
   server's (everything else), and carries any writes back."
  [eng f]
  (fn [{params :body-params}]
    (let [eng (engine/with-state eng (:state params))]
      (ok (with-mutations eng (f eng params))))))

(defn- social-routes [eng]
  [["/api/social"
    {:post (social-handler eng (fn [{:keys [store]} _]
                                 (social/snapshot store)))}]
   ["/api/social/character"
    {:post (social-handler eng (fn [{:keys [store]} {:keys [character]}]
                                 (social/upsert! store character)))}]
   ["/api/social/toggle"
    {:post (social-handler eng (fn [{:keys [store]} {char-name :name}]
                                 (social/toggle! store char-name)))}]
   ["/api/social/remove"
    {:post (social-handler eng (fn [{:keys [store]} {char-name :name}]
                                 (social/remove! store char-name)))}]
   ["/api/social/roll"
    {:post (social-handler eng (fn [{:keys [store rng]} {:keys [skill]}]
                                 (social/roll store rng skill)))}]])

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
  (fn [_req]
    (when-not (:store eng)
      (throw (ex-info "Browser storage exports from the client, not the server" {})))
    {:status  200
     :headers {"Content-Type"        "application/zip"
               "Content-Disposition" "attachment; filename=\"sns-state.zip\""}
     :body    (ByteArrayInputStream. (zip-bytes (edn-store/all-state (:store eng))))}))

(defn app [eng]
  (http/ring-handler
    (http/router
      (into [["/api/loot-types" {:get (loot-types-handler eng)}]
             ["/api/capabilities" {:get (capabilities-handler eng)}]
             ["/api/generate" {:post (generate-handler eng)}]
             ["/api/roll" {:post (roll-handler eng)}]
             ["/api/action" {:post (action-handler eng)}]
             ["/api/report" {:post (report-handler eng)}]
             ["/api/export" {:get (export-handler eng)}]]
            (social-routes eng))
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
