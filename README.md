[![Check](https://github.com/spies-and-spiders/companion/actions/workflows/check.yaml/badge.svg)](https://github.com/spies-and-spiders/companion/actions/workflows/check.yaml)
[![sns-companion-sdk on Maven Central](https://img.shields.io/maven-central/v/tools.spies/sns-companion-sdk?label=tools.spies%2Fsns-companion-sdk)](https://central.sonatype.com/artifact/tools.spies/sns-companion-sdk)

## Customising the S&S Companion

There are five types of loot plugins, all used to define custom loot:

| Type           | Description                                                                                                                                                                                        |
|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **`:builtin`** | A pre-defined plugin provided by us, selected by its `:id` alone (`:divine-dust`, `:relics`).                                                                                                      |
| **`:data`**    | A .json or .edn file that defines the loot that is to be randomly sampled. This cannot support stateful loot.                                                                                      |
| **`:cli`**     | Shell out to a program in any language, communicating via JSON. CLI plugins that wish to use state must implement their own persistence strategy and state management.                             |
| **`:ffi`**     | Call a C-ABI symbol in a shared library (.so/.dylib/.dll), exchanging the same JSON as `:cli`. In-process (so it can hold state) and works in the native image, unlike `:jar` — at the cost of process isolation and a per-platform build. |
| **`:jar`**     | An external JAR implementing the protocols or interfaces provided. This provides the best integration into the Companion, but requires the use of a JVM language (e.g. Java/Kotlin/Clojure/Scala) **and a JVM build (not the native image)**. |

All five are configured the same way in `config.edn`:
```clojure
{:storage    {:backend :mysql :url "jdbc:mariadb://localhost:3306/sns"}
 :plugins    [{:type :data    :id :uniques :source "data/uniques.edn"}
              {:type :cli     :id :weather :label "Weather"
                              :command ["python3" "examples/cli-plugin/weather.py"]}
              {:type :ffi     :id :ffi-loot :library "plugins/libloot.dylib"
                              :symbol "generate" :free-symbol "loot_free"}
              {:type :jar     :id :custom  :jar "plugins/custom.jar"
                              :entrypoint my.plugin/generator}
              {:type :builtin :id :relics}]
 :loot-table [{:id :uniques :weight 30} {:id :relics :weight 10}]}
```

`:loot-table` is the weighted d100 "roll". Entries may omit `:weight` (defaults to 1, i.e. uniform).

A plugin may declare itself a **utility** — a session tool rather than loot. Utilities
are grouped separately in the UI and rejected from the `:loot-table` at startup.
Builtin/`:jar` plugins set `:utility? true` in their loot-spec (Java: the `LootSpec`
record's `utility` component); `:data` specs set it in the spec file; `:cli`/`:ffi`
plugins set it on the config entry.

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
{:loot/title    "{{ name }}"                     ; required — a template
 :loot/subtitle "Unique · {{ base }}"            ; optional
 :loot/vars     {:name {:value "Pacifist's Vow"} ; context for the two above
                 :base {:value "armour"}}
 :loot/sections [{:section/heading "Mods"        ; heading optional
                  :section/items [{:item/title nil               ; optional
                                   :item/body  "+{{ ab }} {{ x }} damage" ; required
                                   :item/metadata ["accuracy"]   ; optional
                                   :item/vars  {:ab {:value 1}
                                                :x  {:value   "fire"
                                                     :label   "Damage types"
                                                     :random  :damage-types
                                                     :options ["fire" "cold"]}}}]}]
 :loot/actions  [{:action/label "Level up"
                  :action/event [:loot/action {:id :relics :action :level-up}]}]
 :loot/state    {…}}                             ; optional, opaque
```

**Titles and bodies are templates, not finished text.** Nothing is rendered on
the server — a template travels to the browser beside the variables it
interpolates, and [Handlebars](https://handlebarsjs.com) renders it there:
`{{ name }}`, `{{ x.[0] }}` to index a drawn collection, `{{#if flag}}…{{/if}}`.
A plugin with nothing to interpolate just sends finished text; a template with
no tags renders as itself.

That split is the point. A DM edits the **value** in its own control without
retyping the prose, and edits the **prose** without disturbing the value —
each re-renders live, with no round trip. Your plugin reads a value back, and
never parses a rendered string.

`:item/vars` are keyed by the name the template refers to them by, so
`{{ damage }}` reads `:damage`. `:options`, when present, is the preset's
vocabulary, and the UI offers it as a combobox rather than free text.
`:label` is optional: with none, the UI derives one from the id
(`:damage-type` -> "Damage type"), so name a var for what it holds and the
editor reads well with no extra work. Set it only to override that.

A var marked `:context?` is available to templates but not offered for editing — that is how a `:data` entry's own fields reach
its templates. `:loot/vars` does the same job for the title and subtitle.

`:loot/state` is opaque, plugin-owned state the engine and UI carry untouched
and hand back with the next action (as `ctx`'s `:view-model`). Keep it to
progression bookkeeping that has no place in the rendered item — an upgrade
`:path`, a stored id. Everything the DM can *see* should be read back off the
view-model itself.

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


## The plugin SDK (`sns.sdk.*`)

Everything below lives in the `sdk/` module, published as
**`tools.spies/sns-companion-sdk`** — the one dependency a plugin needs, and the only
part of this repo with a stable contract.

| Namespace | What it gives you |
|---|---|
| `sns.sdk.protocols` | the protocols you implement (`LootGenerator`, `LootAction`, `Progression`, `Reporter`, `Store`), and the Java interfaces they are bridged onto |
| `sns.sdk.schema` | the malli schemas for every shape crossing the boundary (loot-spec, view-model, upgrade graph, config) |
| `sns.sdk.randoms` | the `random` template filter and its preset vocabulary |
| `sns.sdk.progression` | the upgrade-graph op vocabulary |

The first two are contracts you implement; the last two are shared logic you can use
and extend without depending on the app.

### `LootGenerator` (required)
```clojure
(loot-spec [this])   ; => {:id :relics :label "Relic" :inputs [...]}
(generate  [this ctx]) ; => a view-model
```

`ctx` is `{:rng :store :progression :config :inputs}`:
- `:rng` — a randy RNG (or use randy's default-rng functions).
- `:store` — the `Store` (see below) for stateful loot.
- `:progression` — the default `Progression` (upgrade-graph interpreter). It
  resolves a mod's declared vars and folds its upgrade path over them; it does
  no rendering.
- `:inputs` — values collected from the loot-spec's declared `:inputs`.

There is no renderer on the context, because there is no rendering on the
server: return templates plus their vars, and the browser renders them.

### `LootAction` (optional — stateful follow-ups)
```clojure
(handle-action [this ctx action params]) ; => an updated view-model
```

`ctx` additionally carries **`:view-model`** — the view-model the UI had on
screen, DM edits included. Reconstruct your item from it rather than trusting a
copy frozen into `params`, so an action operates on what is actually displayed:

```clojure
(handle-action [_ ctx action params]
  (-> (view-model->relic (:view-model ctx))   ; your own inverse of the below
      (level-up)
      (relic->view-model ctx)))
```

Write that pair as inverses over everything the UI can change. Whatever the
view-model cannot express — an upgrade `:path`, a stored id — travels in
`:loot/state`; keep it small, since anything read back from there is a value
the DM's edits cannot reach.

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
Field `:type` is one of `:enum` `:int` `:decimal` `:text` `:bool`. The collected
values arrive as `(:inputs ctx)` — an `:int` as a long and a `:decimal` as a
BigDecimal (so a value like `1.3` is exact rather than a binary float), the rest
as typed. A numeric value that will not parse is passed through as the entered
string, for the generator to reject in its own terms.

An `:enum` renders as a **typeahead combobox** (a text input over a `<datalist>`),
not a plain dropdown, so a long `:options` list is filtered by typing rather than
scrolled. The field accepts anything: a value that isn't one of the `:options` is
flagged in the UI but still submitted, so a generator that wants to be strict
should say so itself. Clearing the field falls back to the field's `:default`.

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
            :options [{:id :precise :inc {:ab 1}}
                      {:id :elemental :repeatable false
                       :assoc-template "+{{ab}} AB; deal 6 fire damage on hit."}]}}
```

An option can be taken again and again; add `:repeatable false` for a one-shot,
or `:repeatable N` to cap it at N picks. A consumed option is dropped from the
node (its siblings stay reachable, and the node only goes terminal when every
option in it is consumed).

Op vocabulary on an option: `:inc` `:dec` `:append` `:conj` `:assoc-template`
`:enable` `:disable`. A persisted progression is a `path` of `{:id …}` steps; the
effect re-derives deterministically from it.

The vocabulary is **open**: each op is a method of `sns.sdk.progression/apply-op`,
so a plugin can add one without replacing the graph interpreter.

```clojure
(ns my.plugin
  (:require [sns.sdk.progression :as sp]))

(defmethod sp/apply-op :multiply [acc _ m]
  (update acc :state #(merge-with * % m)))

;; …now usable in any upgrade graph: {:id :doubled :multiply {:ab 2}}
```

Only the vocabulary is shared — the interpreter that folds ops over a path is the
`Progression` handed to you on the context. It applies ops in a fixed order (a
template swap first, then the accumulating ops), with plugin ops last in name
order, so a mod derives identically every time. Anything on an option that isn't
a structural key (`:id` `:repeatable` `:upgrades`) is treated as an op, so a
typo'd op name is an error rather than a silent no-op.

---

## Randoms (`{:random :preset}`)

A loot type can draw a random value per item — an effect like "you gain the
Alert feat" where the feat is rolled each time. Declare the vocabulary in
config:

```clojure
:randoms {:feats  ["Alert" "Athlete" "Brawler"]
          :skills ["Athletics" "Deception" "Insight" "Stealth"]}
```

Then **declare a variable** wherever your loot type keeps its data — a mod's
`:vars`, or a `:data` entry's `:item/vars` — and refer to it from the template
by name:

```clojure
{:vars     {:damage 1
            :x      {:random :damage-types}}
 :template "+{{ damage }} {{ x }} damage with attacks"}
```

A declared var is one of three things:

```clojure
{:ability "Wisdom"                            ; a raw literal — number, string,
                                              ; boolean, vector, or map
 :damage  {:random :damage-types}             ; drawn from a preset
 :awkward {:literal {:random :not-a-preset}}} ; escaped: kept verbatim
```

A map is read as a *behaviour* when it carries `:random` or `:literal` at the
top level. `:literal` exists so data that happens to look like a behaviour can
say so; everything else is itself.

Drawing happens on the server, with the request's seeded rng, so an item is
reproducible from its seed. The drawn value then travels to the browser as an
`:item/vars` entry — with the preset's vocabulary as its `:options`, so the DM
can edit it as a combobox over the same words it was drawn from.

**Presets take named arguments** — the var spec's other keys:

```clojure
{:x {:random :literal :options ["harm" "damage"]}}          ; values written inline
{:x {:random :defences :type "non-armour"}}                 ; a preset's own argument
{:x {:random :without-replacement :amount 2 :preset :skills}} ; draws a collection…
;; …which the template then indexes: "Proficiency in {{ x.[0] }} and {{ x.[1] }}."
```

Two presets are always available: **`:literal`** (values written inline, under
`:options`) and **`:without-replacement`** (N distinct values from another
preset). Everything else is your campaign's content — the app ships no
vocabulary of its own.

A `:jar` plugin can add presets in code by depending on the SDK alone:

```clojure
(ns my.plugin
  (:require [sns.sdk.randoms :as randoms]
            [sns.sdk.vars :as vars]))

(defmethod randoms/preset :monster-types [_ _]
  ["aberration" "beast" "celestial" "construct"])

;; a preset may read its own named arguments
(defmethod randoms/preset :defences [_ {:keys [type]}]
  (cond-> ["Fortitude" "Reflexes" "Will"]
          (not= "non-armour" type) (conj "Armour")))

;; resolve declarations into `:item/vars`; drawing once, up front
(vars/resolve-vars rng {:x {:random :monster-types}})

;; and reroll one later, for an action that deliberately changes it
(vars/redraw-distinct rng item-vars :x)
```

### Progression and vars are the same map

An upgrade's ops (`:inc`, `:dec`, `:append`, …) address vars by the same ids the
template interpolates, so levelling a mod up and drawing its randoms touch one
map rather than two:

```clojure
{:vars     {:ab 1 :x {:random :damage-types}}
 :template "+{{ ab }} AB, {{ x }} damage"
 :upgrades {:select :choice :options [{:id :precise :inc {:ab 1}}]}}
```

Taking `:precise` twice gives `:ab` 3; `:x` is untouched. Vars are re-derived by
replaying the path from the mod's declared starting values, so the same path
always yields the same result.

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
 :title    "{{name}}"                      ; a template, against the drawn entry
 :subtitle "Unique · {{base}}"
 :sections [{:heading "Mods" :each :mods   ; iterate a field on the entry…
             :item {:body :effect          ; …a field *reference* (see below)
                    :metadata :metadata}}]}
```

`:take`>1 draws without replacement (unless `:weighted`, which draws with
replacement by each item's `:weight`). Use `:each :items` to iterate the drawn
entries themselves (e.g. drawing 2 rings), or `:each :<field>` to iterate a field
on the single drawn entry. Items support `:enabled? false` to disable. Input values
are available to all templates.

An item's **`:title`/`:body` may be a template string or a field reference** (a
keyword, as `:metadata` always was). A field reference is how to reach entry data
that is *itself* a template: `:body :effect` hands the browser the entry's own
`"+4 {{ ability }}."` to render, whereas `:body "{{ effect }}"` would resolve one
level and leave the inner tag stranded.

An entry may declare its own variables under **`:item/vars`** (`"item/vars"` in
JSON), which is how a `:data` plugin gets a randomised value the DM can edit:

```clojure
{:name      "Researcher's Power"
 :item/vars {:ability {:random :literal :options ["Intelligence" "Wisdom"]}}
 :mods      [{:effect "+4 {{ ability }}."}
             {:effect "You cannot use {{ ability }} to cast spells."}]}
```

The draw happens once per entry, so every item that entry produces shares it —
edit it in the UI and both mods above update together. The entry's other fields
are available to its templates too (as `:context?` vars, so they render but are
not offered for editing).

(The data DSL renders mods at base state; upgrade-graph progression for data
loot is not yet wired — use `:builtin`/`:jar` for stateful loot.)

---

## The external plugin contract (`:cli` and `:ffi`)

`:cli` and `:ffi` plugins speak the **same friendly, un-namespaced JSON** — only
the transport differs. The engine sends a **request** and reads back an **output**:

```json
// request → plugin        // output ← plugin
{"inputs": {...}}           {"title": "Fogfall", "subtitle": "Weather",
                             "sections": [{"heading": "Sky", "items": [
                               {"body": "…", "metadata": ["obscured"]}]}]}
```

- **`:cli`** runs your `:command`, writing the request to **stdin** and reading the
  output from **stdout**. A non-zero exit is an error, and whatever the command
  wrote to **stderr** becomes the error the DM sees. See
  `examples/cli-plugin/weather.py`.
- **`:ffi`** calls `:symbol` in `:library` — a C-ABI function
  `char* generate(const char* request_json)` returning a malloc'd output string.
  If `:free-symbol` is set (e.g. `loot_free`) it is called on the returned pointer
  once the bytes are read; otherwise the library owns that memory. The same C ABI
  is reachable from any language that can export it — see `examples/ffi-plugin/`
  for equivalent plugins in C (`loot.c`), Go (`loot.go`), and Rust (`loot.rs`),
  each with its one-line build command at the top.

An external plugin has no loot-spec of its own, so it declares the form fields it
needs on its **config entry**, in the same shape as a loot-spec's `:inputs`; the
engine renders the form and sends the collected values as the request's `inputs`
(an `:int` or `:decimal` field arrives as a JSON number, not the form's string):

```json
{"type": "cli", "id": "insight", "label": "Insight Checks", "utility?": true,
 "inputs": [{"id": "socialBonus", "label": "Speaker's social bonus", "type": "int"}],
 "command": ["./5e-cli", "-data", "data", "insight"]}
```

The output is validated before it is mapped, so a contract breach fails with an
error in *your* keys (e.g. a missing `body`) rather than the namespaced view-model.
Both directions are modelled in `schemas.json` as `sns.sdk.schema.plugin-request`
and `sns.sdk.schema.plugin-output`, and emitted rooted as
`plugin-request.schema.json` / `plugin-output.schema.json` so codegen tools
(quicktype, typify, go-jsonschema) can generate request/output structs for a plugin
written in another language. The request is a union of the two modes — a generate
call (`inputs`) or an action call (`action` + `params`) — so codegen yields both,
and the generate-vs-action split is structural rather than a convention you infer.
In brief: output `title` is required; `subtitle`,
`sections`, and `actions` optional. Each section needs `items` (`heading` optional);
each item needs `body` (`title` and a `metadata` array of strings optional); each
action needs `label` and `action` (`params` object optional).

### Actions (stateful follow-ups)

A plugin can drive follow-up actions (e.g. "level up") in its own language. Emit
`actions` in the output:
```json
{"title": "Aegis", "actions": [{"label": "Sharpen", "action": "sharpen",
                                 "params": {"by": 1}}]}
```
The engine turns each into a button; clicking it re-invokes the **same command /
symbol** with an action request (note `action`/`params` instead of `inputs`):
```json
{"action": "sharpen", "params": {"by": 1}}
```
The plugin returns a fresh output (which may itself carry the next round of
`actions`). Branch on whether `action` is present in the request to tell a generate
from an action. A `:cli` plugin must persist any state itself (a file or an external
store) — the engine does not persist it; an `:ffi` plugin runs in-process and may
instead hold state in memory for the app's lifetime.

---

## Writing a `:jar` plugin

Depend on this module, implement `LootGenerator`, and expose a factory:

```clojure
(ns my.plugin
  (:require [sns.sdk.protocols :as p]))

(defn generator [_plugin-config]
  (reify p/LootGenerator
    (loot-spec [_] {:id :custom :label "Custom"})
    (generate  [_ ctx] {:loot/title "Hello from a jar"})))
```

Build a jar, point `:jar`/`:entrypoint` at it, and it loads at startup.

A plugin with no Clojure in it (e.g. pure Java/Kotlin) can skip the factory var:
implement the `sns.sdk.LootGenerator` interface on a class with a 0-arity
constructor and point `:jar`/`:class` at it instead:

```clojure
{:type :jar :id :custom :jar "plugins/custom.jar" :class "my.plugin.CustomLoot"}
```

