(ns sns.preload.discover
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.tools.namespace.find :as find]))

(defn- sns-namespaces []
  (->> ["src" "spi/src"]
       (mapcat #(find/find-namespaces-in-dir (io/file %) find/clj))
       (filter #(str/starts-with? (name %) "sns"))
       distinct
       sort))

(defmacro require-all-sns!
  "Expand to `(do (require 'ns) ...)` for every sns.* namespace on the source path."
  []
  `(do ~@(map (fn [n] `(require '~n)) (sns-namespaces))))
