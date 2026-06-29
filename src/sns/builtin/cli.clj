(ns sns.builtin.cli
  "Adapter for `:cli` plugins: shell out to an external command, writing the
   request context as JSON to stdin and reading a *friendly* JSON view-model from
   stdout. Lets DMs write loot generators in any language. The un-namespaced JSON
   ({\"title\",\"subtitle\",\"sections\",\"actions\"}) is mapped to the namespaced
   view-model here, so external authors never deal with `:loot/...` keys."
  (:require
    [clojure.java.shell :as shell]
    [jsonista.core :as j]
    [sns.spi.protocols :as p]))

(def ^:private mapper j/keyword-keys-object-mapper)

(defn- ->item [{:keys [title body tags]}]
  (cond-> {:item/body body}
          title (assoc :item/title title)
          (seq tags) (assoc :item/tags (vec tags))))

(defn- ->section [{:keys [heading items]}]
  (cond-> {:section/items (mapv ->item items)}
          heading (assoc :section/heading heading)))

(defn- ->action [{:keys [label event]}]
  {:action/label label :action/event (vec event)})

(defn ->view-model
  "Convert a friendly (un-namespaced) JSON map into a view-model."
  [{:keys [title subtitle sections actions]}]
  (cond-> {:loot/title title}
          subtitle (assoc :loot/subtitle subtitle)
          (seq sections) (assoc :loot/sections (mapv ->section sections))
          (seq actions) (assoc :loot/actions (mapv ->action actions))))

(defn generator
  "Build a `LootGenerator` that runs `command` (a vector of program + args)."
  [id command label]
  (reify p/LootGenerator
    (loot-spec [_]
      {:id id :label (or label (name id))})
    (generate [_ {:keys [inputs session]}]
      (let [payload (j/write-value-as-string {:inputs inputs :session session})
            {:keys [exit out err]} (apply shell/sh (concat command [:in payload]))]
        (when-not (zero? exit)
          (throw (ex-info "CLI plugin failed" {:id id :exit exit :err err})))
        (-> (j/read-value out mapper)
            ->view-model)))))
