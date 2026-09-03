(ns sns.ui.export
  "Downloading the DM's state as a ZIP.

   Built wherever the state actually lives: the server streams it for `:file` and
   `:memory`, while `:browser` zips from IndexedDB here and never makes a
   request. Both write the same layout — one pretty-printed `<collection>.edn`
   per collection — so either archive unzips into a directory a `:file`
   deployment can be pointed at."
  (:require
    ["fflate" :as fflate]
    [cljs.pprint :as pprint]
    [sns.ui.idb :as idb]))

(def ^:private file-name "sns-state.zip")

(defn- click-download!
  "Trigger a download from `href`. The anchor is never added to the document —
   clicking a detached one is enough, and leaves no node to clean up."
  [href download]
  (let [a (.createElement js/document "a")]
    (set! (.-href a) href)
    (set! (.-download a) download)
    (.click a)))

(defn- ->bytes [s]
  (.encode (js/TextEncoder.) s))

(defn- zip-entries
  "`{\"relics.edn\" <bytes>}`, printed the way the server prints them so the two
   archives hold the same files."
  [state]
  (reduce-kv (fn [acc coll value]
               (doto acc
                 (unchecked-set (str (name coll) ".edn")
                                (->bytes (with-out-str (pprint/pprint value))))))
             #js {}
             state))

(defn download!
  "Download the current state. `browser-storage?` decides which side builds it.
   Calls `on-err` with `{:error msg}` if the browser refuses."
  [browser-storage? on-err]
  (if-not browser-storage?
    (click-download! "/api/export" file-name)
    (-> (idb/export-state)
        (.then (fn [state]
                 (let [zipped (fflate/zipSync (zip-entries state))
                       blob   (js/Blob. #js [zipped] #js {:type "application/zip"})
                       url    (.createObjectURL js/URL blob)]
                   (click-download! url file-name)
                   ;; The click has already handed the blob to the browser, so
                   ;; the URL can go straight away.
                   (.revokeObjectURL js/URL url))))
        (.catch (fn [e] (on-err {:error (str e)}))))))
