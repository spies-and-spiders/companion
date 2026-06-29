(ns sns.server.main
  "Application entrypoint: load config, build the engine, serve the SPA + API."
  (:gen-class)
  (:require
    [s-exp.hirundo :as hirundo]
    [sns.server.config :as config]
    [sns.server.engine :as engine]
    [sns.server.http :as http]))

(defn start!
  "Start the Helidon (Hirundo) HTTP server and return it (stop via
   `s-exp.hirundo/stop!`). Returns immediately — it does not block the caller."
  ([] (start! {}))
  ([{:keys [port host] :or {port 8080 host "0.0.0.0"}}]
   (let [eng    (engine/create (config/load-config))
         server (hirundo/start! {:http-handler (http/app eng)
                                 :port         port
                                 :host         host})]
     (println (str "sns-companion listening on http://localhost:" port))
     server)))

(defn -main [& _args]
  (start! {:port (parse-long (or (System/getenv "PORT") "8080"))
           :host (or (System/getenv "HOST") "0.0.0.0")})
  @(promise))                          ; block forever so the server keeps running
