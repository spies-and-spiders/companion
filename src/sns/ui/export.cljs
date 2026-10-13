(ns sns.ui.export
  "Downloads the DM's state as a ZIP of `<collection>.edn` files, built by the
   server or, under `:browser` storage, from IndexedDB. Either unzips into a
   usable `:file` state directory."
  (:require
    ["fflate" :as fflate]
    [cljs.pprint :as pprint]
    [sns.ui.idb :as idb]))

(def ^:private file-name "sns-state.zip")

(defn- click-download! [href download]
  (let [a (.createElement js/document "a")]
    (set! (.-href a) href)
    (set! (.-download a) download)
    (.click a)))

(defn- ->bytes [s]
  (.encode (js/TextEncoder.) s))

(defn- zip-entries
  "`{\"relics.edn\" <bytes>}`, printed as the server prints them."
  [state]
  (reduce-kv (fn [acc coll value]
               (doto acc
                 (unchecked-set (str (name coll) ".edn")
                                (->bytes (with-out-str (pprint/pprint value))))))
             #js {}
             state))

(defn download!
  "Download the current state, calling `on-err` with `{:error msg}` on failure."
  [browser-storage? on-err]
  (if-not browser-storage?
    (click-download! "/api/export" file-name)
    (-> (idb/export-state)
        (.then (fn [state]
                 (let [zipped (fflate/zipSync (zip-entries state))
                       blob   (js/Blob. #js [zipped] #js {:type "application/zip"})
                       url    (.createObjectURL js/URL blob)]
                   (click-download! url file-name)
                   ;; The click has already handed the blob over.
                   (.revokeObjectURL js/URL url))))
        (.catch (fn [e] (on-err {:error (str e)}))))))
