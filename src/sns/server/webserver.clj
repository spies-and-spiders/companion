(ns sns.server.webserver
  "Helidon webserver entrypoint, standing in for `s-exp.hirundo`.

   `s-exp.hirundo` unconditionally requires `s-exp.hirundo.grpc.routing`, which
   imports `com.google.protobuf` and `io.grpc.stub` classes at load time. We
   exclude Helidon's gRPC module (see deps.edn), so we assemble the server from
   the same option multimethods while requiring only the HTTP routing namespace.

   Supports the `s-exp.hirundo/start!` options minus `:grpc-services` and
   `:websocket-endpoints`; unknown keys are ignored by the multimethod default."
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
  "Starts a new server, returning the running `WebServer`."
  ^WebServer
  [options]
  (-> (server-builder options)
      .build
      .start))
