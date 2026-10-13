(ns sns.server.webserver
  "Stands in for `s-exp.hirundo`, which unconditionally loads gRPC classes that
   deps.edn excludes. Takes `s-exp.hirundo/start!`'s options, minus
   `:grpc-services` and `:websocket-endpoints`."
  (:require
    [s-exp.hirundo.http.routing]
    [s-exp.hirundo.options :as options])
  (:import
    (io.helidon.webserver WebServer WebServerConfig WebServerConfig$Builder)))

(defn- server-builder
  ^WebServerConfig$Builder
  [options]
  (reduce (fn [builder [k v]]
            (options/set-server-option! builder k v options))
          (WebServerConfig/builder)
          options))

(defn start!
  ^WebServer
  [options]
  (-> (server-builder options)
      .build
      .start))
