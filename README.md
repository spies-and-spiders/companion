## Customising the S&S Companion

There are four types of loot plugins, all used to define custom loot:

| Type           | Description                                                                                                                                                                                        |
|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **`:builtin`** | A pre-defined loot plugin provided by us.                                                                                                                                                          |
| **`:data`**    | A .json or .edn file that defines the loot that is to be randomly sampled. This cannot support stateful loot.                                                                                      |
| **`:cli`**     | Shell out to a program in any language, communicating via JSON. CLI plugins that wish to use state must implement their own persistence strategy and state management.                             |
| **`:jar`**     | An external JAR implementing the protocols or interfaces provided. This provides the best integration into the Companion, but requires the use of a JVM language (e.g. Java/Kotlin/Clojure/Scala). |

All four are configured the same way in `config.edn`:
```clojure
{:storage    {:backend :mysql :url "jdbc:mariadb://localhost:3306/sns"}
 :plugins    [{:type :data    :id :uniques :source "data/uniques.edn"}
              {:type :cli     :id :weather :label "Weather"
                              :command ["python3" "examples/cli-plugin/weather.py"]}
              {:type :jar     :id :custom  :jar "plugins/custom.jar"
                              :entrypoint my.plugin/generator}
              {:type :builtin :id :relics  :entrypoint sns.builtin.relics/generator}]
 :loot-table [{:id :uniques :weight 30} {:id :relics :weight 10}]}
```

`:loot-table` is the weighted d100 "roll". Entries may omit `:weight` (defaults to 1, i.e. uniform).

You may provide **`config.json`** instead of `config.edn`; simply replace all keywords (e.g. `:weight`) and symbols (e.g. `my.plugin/generator`) with regular JSON strings. 

---

## The view-model (what every loot type returns)

The UI renders this shape generically — a new loot type needs **no** UI code, but it must return this shape of data.

```clojure
{:loot/title    "Pacifist's Vow"                 ; required
 :loot/subtitle "Unique · armour"                ; optional
 :loot/sections [{:section/heading "Mods"        ; heading optional
                  :section/items [{:item/title nil      ; optional
                                   :item/body  "+1 AB…" ; required
                                   :item/tags  ["accuracy"]}]}]  ; optional
 :loot/actions  [{:action/label "Level up"
                  :action/event [:loot/action {:id :relics :action :level-up
                                               :params {:relic-id "…"}}]}]}
```

or

```json
{
  "title": "Pacifist's Vow",
  "subtitle": "Unique · armour",
  "sections": [
    {
      "heading": "Mods",
      "items": [
        {
          "body": "+1 AB…",
          "tags": [
            "accuracy"
          ]
        }
      ]
    }
  ],
  "actions": [
    {
      "label": "Level up",
      "action": "level-up",
      "params": {
        "relic-id": "…"
      }
    }
  ]
}
```


`:action/event` is dispatched verbatim by the frontend; for stateful follow-ups it
should be `[:loot/action {:id <loot-type> :action <kw> :params <map>}]`, which the
backend routes to your `LootAction/handle-action`.

---
### TODO: Update the below slop


## Protocols (`sns.spi.protocols`)

### `LootGenerator` (required)
```clojure
(loot-spec [this])   ; => {:id :relics :label "Relic" :stateful? true :inputs [...]}
(generate  [this ctx]) ; => a view-model
```

`ctx` is `{:rng :store :render :progression :config :session :inputs}`:
- `:rng` — a randy RNG (or use randy's default-rng functions).
- `:store` — the `Store` (see below) for stateful loot.
- `:render` — `(fn [template state] -> string)`, Selmer with cosmetic filters
  (`times`, `dice`, `ordinal`, `percentage`).
- `:progression` — the default `Progression` (upgrade-graph interpreter).
- `:inputs` — values collected from the loot-spec's declared `:inputs`.

### `LootAction` (optional — stateful follow-ups)
```clojure
(action-spec   [this])
(handle-action [this ctx action params]) ; => an updated view-model
```

### `Progression` (optional — custom upgrade systems)
```clojure
(current-state [this mod path]) ; derive a mod at a progression path
(level-options [this mod path]) ; next options
```
The default implementation interprets the upgrade-graph DSL; implement this only
if you need bespoke logic.

### `Reporter` (optional — send loot to an external destination)
```clojure
(report-label [this])        ; => button label, e.g. "Send to Discord"
(report! [this view-model])  ; => {:ok true}; throws on failure
```
Config-driven via `:reporting` (mirrors `:storage`), and **optional** — when none
is configured the UI hides its per-item report button. One built-in backend:

```clojure
:reporting {:backend :discord :webhook-url #env DISCORD_WEBHOOK_URL}
```

`POST /api/report` `{:view-model …}` forwards the (validated) view-model to the
reporter; `GET /api/capabilities` tells the UI whether to show the button.

### `Store` (optional — custom persistence)
```clojure
(fetch [this coll id]) (query [this coll q]) (put! [this coll id doc]) (update! [this coll id f])
```
Three built-in backends, chosen by config `:storage {:backend ...}`:
`:mysql` (any MySQL-compatible server via JDBC `:url` — see
[docs/storage.md](docs/storage.md)), `:file` (one transit-encoded file per
loot-type under `:dir`, default `./state`), and `:memory` (default; for tests/dev).
Docs are transit-serialised so Clojure values (e.g. keyword-valued upgrade mods)
round-trip losslessly.

---

## `loot-spec` inputs (drive the generic form)

```clojure
{:id :relics :label "Relic"
 :inputs [{:id :character :label "Character" :type :enum :options ["Thoros" "Simo"]}
          {:id :lucky? :label "Lucky" :type :bool}]}
```
Field `:type` is one of `:enum` `:int` `:text` `:bool`. The collected values arrive
as `(:inputs ctx)`.

---

## The upgrade-graph DSL (mod state & progression)

A mod carries structured `:state`, a `:template` referencing it, and an
`:upgrades` graph. **Upgrades mutate state, never the rendered string** — the text
is derived from accumulated state, so choosing the same option N times is
well-defined.

```clojure
{:state    {:ab 1}
 :template "+{{ab}} AB with effects that cannot deal damage."
 :upgrades {:select  :choice            ; :choice | :random | :all
            :options [{:id :precise :repeatable true :inc {:ab 1}}
                      {:id :elemental :roll {:dmg [1 6]}
                       :assoc-template "+{{ab}} AB; deal {{dmg}} fire damage on hit."}]}}
```

Op vocabulary on an option: `:set` `:roll` (rolled at selection, persisted)
`:inc` `:dec` `:append` `:conj` `:assoc-template` `:enable` `:disable`. A persisted
progression is a `path` of `{:id … :rolled {…}}` steps; the effect re-derives
deterministically from it.

---

## The data DSL (`:data`)

A code-free loot type in one EDN (or `.json`) file, loaded from `:source`:

```clojure
{:label    "Unique"
 :inputs   []                              ; optional loot-spec inputs
 :items    [{:name "Pacifist's Vow" :base "armour"
             :mods [{:effect "+1 AB…" :tags ["accuracy"]}]}]
 :take     1                               ; how many to draw (default 1)
 :weighted false                           ; draw with replacement by :weight (default false)
 :title    "{{name}}"                      ; Selmer, against the drawn entry
 :subtitle "Unique · {{base}}"
 :sections [{:heading "Mods" :each :mods   ; iterate a field on the entry…
             :item {:body "{{effect}}" :tags :tags}}]}
```

`:take`>1 draws without replacement (unless `:weighted`, which draws with
replacement by each item's `:weight`). Use `:each :items` to iterate the drawn
entries themselves (e.g. drawing 2 rings), or `:each :<field>` to iterate a field
on the single drawn entry. Items support `:enabled? false` to disable. Input values
are available to all templates. (The data DSL renders mods at base state;
upgrade-graph progression for data loot is not yet wired — use `:builtin`/`:jar`
for stateful loot.)

---

## The CLI contract (`:cli`)

The engine runs your `:command`, writes the context as JSON to **stdin**:
```json
{"inputs": {...}, "session": {...}}
```
and reads a **friendly, un-namespaced** view-model from **stdout**:
```json
{"title": "Fogfall", "subtitle": "Weather",
 "sections": [{"heading": "Sky", "items": [{"body": "…", "tags": ["obscured"]}]}]}
```
A non-zero exit is treated as an error. See `examples/cli-plugin/weather.py`.

### Actions (stateful follow-ups)

A `:cli` plugin can also drive follow-up actions (e.g. "level up") in its own
language. Emit `actions` in the view-model:
```json
{"title": "Aegis", "actions": [{"label": "Sharpen", "action": "sharpen",
                                 "params": {"by": 1}}]}
```
The engine turns each into a button; clicking it re-invokes your **same command**
with an action context on **stdin** (note `action`/`params` instead of `inputs`):
```json
{"action": "sharpen", "params": {"by": 1}, "session": {...}}
```
Your program returns a fresh view-model on stdout (which may itself carry the next
round of `actions`). Branch on whether `action` is present in the stdin JSON to
tell a generate from an action. Persist any state yourself (via a file, the
`session`, or an external store) — the engine does not persist CLI plugin state.

---

## Writing a `:jar` plugin

Depend on this module, implement `LootGenerator`, and expose a factory:

```clojure
(ns my.plugin
  (:require [sns.spi.protocols :as p]))

(defn generator [_plugin-config]
  (reify p/LootGenerator
    (loot-spec [_] {:id :custom :label "Custom"})
    (generate  [_ ctx] {:loot/title "Hello from a jar"})))
```

Build a jar, point `:jar`/`:entrypoint` at it, and it loads at startup.

