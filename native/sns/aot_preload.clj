(ns sns.aot-preload
  "Eagerly loads all SNS namespaces so that plugins, which load .clj files at runtime, can still work."
  (:gen-class)
  (:require
    [sns.preload.discover :refer [require-all-sns!]]
    [sns.server.core :as core]))

(require-all-sns!)

(defn -main [& args]
  (apply core/-main args))
