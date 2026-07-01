(ns sns.server.core
  "Application entrypoint: load config, build the engine, serve the SPA + API."
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [s-exp.hirundo :as hirundo]
    [sns.server.config :as config]
    [sns.server.engine :as engine]
    [sns.server.http :as http])
  (:import
    (java.io IOException)
    (java.net InetAddress ServerSocket)
    (java.util.concurrent.locks LockSupport)))

(defn- check-port-free! [host port]
  (try
    (.close (ServerSocket. port 0 (InetAddress/getByName host)))
    (catch IOException _
      (throw (ex-info (str host ":" port " is already in use.")
                      {:host host :port port})))))

(defn start! [config]
  (let [{:keys [host port] :or {host "127.0.0.1" port 8080}} (:server config)]
    (check-port-free! host port)
    (let [server (hirundo/start! {:http-handler (http/app (engine/create config))
                                  :port         port
                                  :host         host})]
      (println (str "sns-companion listening on http://" host ":" port))
      server)))

(defn- config-arg
  "The value of a `--config <path>` (or `--config=<path>`) CLI option, or nil."
  [args]
  (loop [[a & more] args]
    (when a
      (cond
        (= "--config" a)                 (first more)
        (str/starts-with? a "--config=") (subs a (count "--config="))
        :else                            (recur more)))))

(defn -main [& args]
  (let [path   (config-arg args)
        config (if path
                 (config/load-config (io/file path))
                 (config/load-config))]
    (start! config))
  (LockSupport/park))
