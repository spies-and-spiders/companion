[![Check](https://github.com/spies-and-spiders/companion/actions/workflows/check.yaml/badge.svg)](https://github.com/spies-and-spiders/companion/actions/workflows/check.yaml)
[![sns-spi on Maven Central](https://img.shields.io/maven-central/v/tools.spies/sns-spi?label=tools.spies%2Fsns-spi)](https://central.sonatype.com/artifact/tools.spies/sns-spi)

## Customising the S&S Companion

There are four types of loot plugins, all used to define custom loot:

| Type           | Description                                                                                                                                                                                        |
|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **`:builtin`** | A pre-defined plugin provided by us, selected by its `:id` alone (`:divine-dust`, `:relics`).                                                                                                      |
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
              {:type :builtin :id :relics}]
 :loot-table [{:id :uniques :weight 30} {:id :relics :weight 10}]}
```

`:loot-table` is the weighted d100 "roll". Entries may omit `:weight` (defaults to 1, i.e. uniform).

A plugin may declare itself a **utility** — a session tool rather than loot. Utilities
are grouped separately in the UI and rejected from the `:loot-table` at startup.
Builtin/`:jar` plugins set `:utility? true` in their loot-spec (Java: the `LootSpec`
record's `utility` component); `:data` specs set it in the spec file; `:cli` plugins
set it on the config entry.

A plugin may also be marked **hidden** with `:hidden? true` on its config entry — this
works for every plugin type, since the engine applies it rather than the generator (so
a compiled `:jar` plugin can be hidden without touching its code). A hidden type is
kept out of the UI picker, so the only way to reach it is by rolling the `:loot-table`
(or from another loot type's action) — useful for loot that should only ever turn up by
chance. Whichever hidden type is on screen appears on the picker while it is there, so
the rail always reflects what you're looking at. A hidden type that is in no
`:loot-table` is unreachable; nothing stops you configuring that.

Separate from plugins, the **Group Deception & Persuasion tracker** is part of the app
itself: always available under Utilities, with its own page (add characters and their
two bonuses, tick/untick who's present, roll 1d20 + the group bonus). Its state
persists via the configured storage under the `__social` collection — the `__` prefix
marks internal collections, which can never clash with plugin loot-type ids.

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
                                   :item/metadata  ["accuracy"]}]}]  ; optional
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
          "metadata": [
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

**SPI** = *Service Provider Interface*: the contract you implement to plug into the
Companion, as opposed to an API you call. Everything below lives in the `spi/`
module, published as `tools.spies/sns-spi`.

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

A code-free loot type in one EDN (or `.json`) file, loaded from `:source` (a
filesystem path) — or written straight into the config under `:inline`, which
takes precedence over `:source` when both are given:

```clojure
{:label    "Unique"
 :inputs   []                              ; optional loot-spec inputs
 :items    [{:name "Pacifist's Vow" :base "armour"
             :mods [{:effect "+1 AB…" :metadata ["accuracy"]}]}]
 :take     1                               ; how many to draw (default 1)
 :weighted false                           ; draw with replacement by :weight (default false)
 :title    "{{name}}"                      ; Selmer, against the drawn entry
 :subtitle "Unique · {{base}}"
 :sections [{:heading "Mods" :each :mods   ; iterate a field on the entry…
             :item {:body "{{effect}}" :metadata :metadata}}]}
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
 "sections": [{"heading": "Sky", "items": [{"body": "…", "metadata": ["obscured"]}]}]}
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

A plugin with no Clojure in it (e.g. pure Java/Kotlin) can skip the factory var:
implement the `sns.spi.LootGenerator` interface on a class with a 0-arity
constructor and point `:jar`/`:class` at it instead:

```clojure
{:type :jar :id :custom :jar "plugins/custom.jar" :class "my.plugin.CustomLoot"}
```

