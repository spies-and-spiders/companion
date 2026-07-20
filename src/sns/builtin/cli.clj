(ns sns.builtin.cli
  "Adapter for `:cli` plugins: shell out to an external command, writing the
   request context as JSON to stdin and reading a *friendly* JSON view-model from
   stdout. Lets DMs write loot generators in any language. The un-namespaced JSON
   ({\"title\",\"subtitle\",\"sections\",\"actions\"}) is mapped to the namespaced
   view-model here, so external authors never deal with `:loot/...` keys.

   Actions round-trip: a stdout `action` becomes a `:loot/action` event that the
   UI dispatches back to the engine, which re-invokes the same command with an
   `action`/`params` context on stdin (rather than `inputs`), so a `:cli` plugin
   can drive stateful follow-ups (e.g. levelling up) entirely in its own language."
  (:require
    [clojure.java.shell :as shell]
    [jsonista.core :as j]
    [sns.spi.protocols :as p]))

(def ^:private mapper j/keyword-keys-object-mapper)

(defn- ->item [{:keys [title body metadata]}]
  (cond-> {:item/body body}
          title (assoc :item/title title)
          (seq metadata) (assoc :item/metadata (vec metadata))))

(defn- ->section [{:keys [heading items]}]
  (cond-> {:section/items (mapv ->item items)}
          heading (assoc :section/heading heading)))

(defn- ->action
  "Map a friendly `{:label :action :params}` to a namespaced view-model action
   whose event routes back to this same CLI plugin (`id`) via `handle-action`."
  [id {:keys [label action params]}]
  {:action/label label
   :action/event [:loot/action {:id id :action (keyword action) :params (or params {})}]})

(defn ->view-model
  "Convert a friendly (un-namespaced) JSON map into a view-model. Actions are
   wired back to CLI plugin `id`."
  [id {:keys [title subtitle sections actions]}]
  (cond-> {:loot/title title}
          subtitle (assoc :loot/subtitle subtitle)
          (seq sections) (assoc :loot/sections (mapv ->section sections))
          (seq actions) (assoc :loot/actions (mapv #(->action id %) actions))))

(defn- run
  "Run `command` with `ctx` written as JSON on stdin, returning the friendly
   stdout JSON mapped to a view-model. A non-zero exit is treated as an error."
  [id command ctx]
  (let [payload (j/write-value-as-string ctx)
        {:keys [exit out err]} (apply shell/sh (concat command [:in payload]))]
    (when-not (zero? exit)
      (throw (ex-info "CLI plugin failed" {:id id :exit exit :err err})))
    (->view-model id (j/read-value out mapper))))

(defn generator
  "Build a `LootGenerator`/`LootAction` that runs `command` (a vector of program
   + args). Generation writes `{:inputs :session}` to stdin; an action writes
   `{:action :params :session}` — the presence of `action` tells the script which
   it is. `utility?` marks a session tool rather than loot (grouped separately in
   the UI, barred from the :loot-table)."
  ([id command label] (generator id command label false))
  ([id command label utility?]
   (reify
     p/LootGenerator
     (loot-spec [_]
       (cond-> {:id id :label (or label (name id))}
               utility? (assoc :utility? true)))
     (generate [_ {:keys [inputs session]}]
       (run id command {:inputs inputs :session session}))
     p/LootAction
     (handle-action [_ {:keys [session]} action params]
       (run id command {:action action :params params :session session})))))
