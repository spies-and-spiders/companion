(ns sns.server.main
  "Application entrypoint: load config, build the engine, serve the SPA + API."
  (:gen-class)
  (:require
    [ring.adapter.jetty :as jetty]
    [sns.server.config :as config]
    [sns.server.engine :as engine]
    [sns.server.http :as http]))

(defn start!
  "Start the HTTP server. Returns the Jetty server. `:join?` blocks the thread."
  ([] (start! {}))
  ([{:keys [port join?] :or {port 8080 join? false}}]
   (let [eng (engine/create (config/load-config))]
     (println (str "sns-companion listening on http://localhost:" port))
     (jetty/run-jetty (http/app eng) {:port port :join? join?}))))

(defn -main [& _args]
  (start! {:port  (parse-long (or (System/getenv "PORT") "8080"))
           :join? true}))
