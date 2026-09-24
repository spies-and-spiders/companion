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
| **`:wasm`**    | Run a WASI module on GraalWASM, exchanging the same JSON as `:cli` over its stdio. In-process and works in the native image like `:ffi`, but sandboxed (it sees only the directories you map) and one portable build for every platform. |
| **`:jar`**     | An external JAR implementing the protocols or interfaces provided. This provides the best integration into the Companion, but requires the use of a JVM language (e.g. Java/Kotlin/Clojure/Scala) **and a JVM build (not the native image)**. |

All five are listed under `:tools` in `config.edn`, alongside `:page` entries (see
[Pages](#pages)). Keys every tool shares (`:type`, `:id`, `:label`, `:section`) sit at
the top level; what every plugin shares nests under `:generator`, and what only one
`:type` understands nests under that type's own key — the same rule `:storage` and
`:reporting` follow for their backend:
```clojure
{:storage    {:backend :file :file {:dir "./state"}}
 :tools      [{:type :data    :id :uniques :data {:source "data/uniques.edn"}}
              {:type :cli     :id :weather :label "Weather"
                              :cli {:command ["python3" "examples/cli-plugin/weather.py"]}}
              {:type :ffi     :id :ffi-loot
                              :ffi {:library "plugins/libloot.dylib"
                                    :symbol "generate" :free-symbol "loot_free"}}
              {:type :jar     :id :custom
                              :jar {:path "plugins/custom.jar"
                                    :entrypoint my.plugin/generator}}
              {:type :builtin :id :relics}
              {:type :builtin :id :social}]
 :loot-table [{:id :uniques :weight 30} {:id :relics :weight 10}]}
```

`:loot-table` is the weighted die "roll". Entries may omit `:weight` (defaults to 1, i.e. uniform).
The die defaults to a d100; set `:loot-die-size` to roll a different one. The table cannot have
more entries than the die has sides — the app refuses to start if it does.

Instead of weights, entries may pin themselves to sides of the die with `:ranges` (inclusive
`[from to]` pairs, not necessarily consecutive). An entry takes `:weight` or `:ranges`, not both,
and once one entry has `:ranges` they all need them. Every side of the die must belong to some
entry; where ranges overlap, a roll there generates every entry it lands on.

```clojure
:loot-table [{:id :uniques :ranges [[1 60] [99 100]]}
             {:id :relics  :ranges [[61 98]]}
             {:id :rings   :ranges [[95 100]]}]   ; 95-98 rolls a relic and rings
```

**Loot table**, at the top of the rail, lists every entry against its numbers; entering a
roll highlights what it lands on, and generates it on Enter (or **Generate all** when it
lands on several).

A tool's `:id`, `:label` and `:section` come from its config entry alone; a plugin cannot
set them for itself. `:label` defaults to one derived from the id (`:divine-dust` shows as
"Divine dust").

The UI rail lists tools under **sections**, named with `:section "Utilities"` (any
string). Tools with no section are listed under `Loot`, which always comes first; other
sections follow in alphabetical order. Within a section, tools keep their order in
`:tools`.

Everything else a plugin's UI needs sits under **`:generator`** on its config entry, for
every plugin type:

| Key                  | What it does |
|----------------------|--------------|
| `:hidden?`           | Keeps it off the rail (see below). |
| `:inputs`            | The form fields whose values are sent as `inputs`. |
| `:history`           | Overrides the global `:history` setting. |
| `:generate-label`    | The generate button's text. |
| `:store/collections` | The state collections it reads or writes. |
| `:store/manual`      | A DM-maintained table (see below). |

Builtin and `:jar` plugins may declare the same keys in their loot-spec, and `:data`
specs may declare `:inputs` and `:history` in the spec file; anything set under
`:generator` overrides them key by key.

A plugin may be marked **hidden** with `:generator {:hidden? true}` — as with every
`:generator` key, a compiled `:jar` plugin can be hidden without touching its code. A
hidden type is kept out of the UI picker, so the only way to reach it is by rolling the
`:loot-table`, from a page, or from another loot type's action — useful for loot that
should only ever turn up by chance. Whichever hidden type is on screen appears on the
picker while it is there, so the rail always reflects what you're looking at. A hidden
type that is in no `:loot-table` or page is unreachable; nothing stops you configuring
that.

A plugin may declare **manual state** with `:store/manual`: a table the DM fills in by
hand rather than one the app generates. The UI renders a generic editor for it above
the plugin's own form, and the plugin reads it back through the store like any other
collection.

```clojure
{:type      :cli :id :social :label "Group Social" :section "Utilities"
 :cli       {:command ["./social"]}
 :generator {:store/manual {:key-label "Character"
                            :fields    [{:id :deception :label "Deception" :type :decimal :default 0}
                                        {:id :persuasion :label "Persuasion" :type :decimal :default 0}
                                        {:id :present? :label "Present" :type :bool :default true}]}}}
```

Each key is a row and `:fields` are its columns, declared as ordinary input fields and
coerced to their types on the way in (a blank falls back to `:default`). With
`:list? true` a row holds a *sequence* of those field maps instead of one, for a key
that owns several records:

```clojure
:store/manual {:key-label "Character" :list? true
               :fields [{:id :soul :label "Soul" :type :text}
                        {:id :proc-chance :label "Proc chance" :type :decimal :default 0}]}
```

The state lives in the first of the plugin's `:store/collections` (its `:id` by
default) and is stored, exported and hand-editable exactly like any other collection.

The shipped **`:social` builtin** — the Group Deception & Persuasion tracker — is
exactly this: a manual character table plus one action that rolls 1d20 + the group
bonus over whoever is present, declaring its table in its own loot-spec. Add
`{:type :builtin :id :social}` to `:tools` to use it.

### Pages

By default every plugin gets a page of its own in the UI. To use several plugins
together (say, something rolled every combat round alongside something rolled on one
character's turn), group them on a **page** and each gets its own form and result
side by side:

```clojure
{:tools [{:type :data :id :lair-actions :generator {:hidden? true} :data {:source "data/lair.edn"}}
         {:type :cli :id :weather :cli {:command ["python3" "weather.py"]}}
         {:type :page :id :combat :label "Combat round" :section "Combat"
          :page {:tools [:lair-actions :weather]}}]}
```

A page is an entry in `:tools` like any other, so it sits on the rail wherever you list
it. `:label` defaults to the id, and `:section` works as it does for a plugin (`Loot`
when absent). A page lists plugins only, not other pages. A plugin may appear on
several pages; its inputs, result and history are shared between them. Every plugin
also keeps its own page in its section; set `:generator {:hidden? true}` to show it
only on its pages.

Ids must be unique across every tool, pages included, and every listed plugin must
exist — the app refuses to start otherwise. A page cannot appear on the `:loot-table`.
Rolling the `:loot-table` shows the result on the page already on screen if it holds
every rolled type, otherwise on the type's own page. A roll landing on several types goes to
the first page holding them all, or to a page of its own when none does.

### Result history

Generated results are kept per plugin, with the timestamp they were made, in a
`history` collection — so they live wherever `:storage` puts everything else (a
`history.edn` file, memory, or the browser's IndexedDB) and travel with an export.
The list appears under the result: click an entry to put it back on the bench, `✕` to
drop one, **Clear** to empty the plugin's history. When `:history` says so, a
**Save to history** button sits alongside *Edit item*.

| `:history`   | When a result is stored                       |
|--------------|-----------------------------------------------|
| `:always`    | Every generation, loot-table roll, and action that changes the item. A hand-edited result adds a **Save to history** button, since only you know when an edit is finished. |
| `:on-report` | Only when the result is reported.              |
| `:button`    | Only when **Save to history** is pressed. The default. |
| `:never`     | Not at all.                                    |

Set it globally in config, and override it per plugin under `:generator`:

```clojure
{:history :on-report
 :tools   [{:type :data :id :uniques :data {:source "data/uniques.edn"} :generator {:history :always}}]}
```

`history` is reserved: don't name a plugin's own `:store/collections` after it.

The separate "last result" the UI restores when you come back to a plugin is not
configurable — it is always remembered for as long as the tab is open.

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
                  :section/secret? false         ; optional — true keeps it out of reports
                  :section/items [{:item/title nil               ; optional
                                   :item/body  "+{{ ab }} {{ x }} damage" ; required
                                   :item/metadata ["accuracy"]   ; optional
                                   :item/vars  {:ab {:value 1}
                                                :x  {:value   "fire"
                                                     :label   "Damage types"
                                                     :random  :damage-types
                                                     :options ["fire" "cold"]}}}]}]
 :loot/actions  [{:action/label "Level up"
                  :action/event [:loot/action {:id :relics :action :rank-up}]}]
 :loot/state    {…}                              ; optional, opaque
 :loot/error?   false                            ; optional — true marks a failure
 :store/mutations {:relics {"r1" {…}}}}          ; optional — writes to apply
```

**Titles and bodies are templates, not finished text.** Nothing is rendered on
the server — a template travels to the browser beside the variables it
interpolates, and [Handlebars](https://handlebarsjs.com) renders it there:
`{{ name }}`, `{{ x.[0] }}` to index a drawn collection, `{{#if flag}}…{{/if}}`.
The [handlebars-helpers](https://github.com/helpers/handlebars-helpers) string,
array, object, collection, comparison, math, number, inflection, regex and misc
groups are registered, so `{{titleize name}}`, `{{add gold 5}}` and
`{{#gt gold 2}}…{{/gt}}` work too.
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
bookkeeping that has no place in the rendered item — a stored id, the data entry
a mod came from. Everything the DM can *see* should be read back off the
view-model itself, a var's rank included.

`:loot/error?` marks a result as a failure to report rather than loot (no
relics left to draw, say). The UI shows it as an overlay over the card, which a
click dismisses, leaving the previous result where it was; it is never saved to
history, whatever the `:history` mode. A generator or action that
throws gets one for free: the `:view-model` in the exception's `ex-data` if it
carries one, otherwise the exception's message as the title and its class as the
subtitle.

`:store/mutations` is how a stateful type writes: `{<collection> {<key>
<value>}}`, a nil value retracting that key. The engine applies it *after* this
view-model validates, so a plugin that returns something unusable changes
nothing. See [docs/storage.md](docs/storage.md).

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
      "action": "rank-up",
      "params": {
        "relic-id": "…"
      }
    }
  ]
}
```


`:action/event` is dispatched verbatim by the frontend; for stateful follow-ups it
should be `[:loot/action {:id <loot-type> :action <kw> :params <map>}]`, which the
backend routes to your `Action/handle-action`.

---
### TODO: Update the below slop


## The plugin SDK (`sns.sdk.*`)

Everything below lives in the `sdk/` module, published as
**`tools.spies/sns-companion-sdk`** — the one dependency a plugin needs, and the only
part of this repo with a stable contract.

| Namespace | What it gives you |
|---|---|
| `sns.sdk.protocols` | the protocols you implement (`Generator`, `Action`, `Reporter`, `Store`), and the Java interfaces they are bridged onto |
| `sns.sdk.schema` | the malli schemas for every shape crossing the boundary (loot-spec, view-model, mod, config) |
| `sns.sdk.randoms` | the `random` template filter and its preset vocabulary |
| `sns.sdk.rank` | ranks: what a var's rank means, and which vars can still take one |

The first two are contracts you implement; the last two are shared logic you can use
and extend without depending on the app.

### `Generator` (required)
```clojure
(loot-spec [this])   ; => {:inputs [...]}, or {} — the :generator keys, all optional
(generate  [this ctx]) ; => a view-model
```

`ctx` is `{:rng :store :config :inputs}`:
- `:rng` — a randy RNG (or use randy's default-rng functions).
- `:store` — the `Store` (see below) for stateful loot.
- `:inputs` — values collected from the declared `:inputs`.

There is no renderer on the context, because there is no rendering on the
server: return templates plus their vars, and the browser renders them.

### `Action` (optional — stateful follow-ups)
```clojure
(handle-action [this ctx action params]) ; => an updated view-model
```

`ctx` additionally carries **`:view-model`** — the view-model the UI had on
screen, DM edits included. Reconstruct your item from it rather than trusting a
copy frozen into `params`, so an action operates on what is actually displayed:

```clojure
(handle-action [_ ctx action params]
  (-> (view-model->relic (:view-model ctx))   ; your own inverse of the below
      (rank-up)
      (relic->view-model ctx)))
```

Write that pair as inverses over everything the UI can change. Whatever the
view-model cannot express — a stored id, the data entry a mod came from —
travels in `:loot/state`; keep it small, since anything read back from there is a
value the DM's edits cannot reach. A var's level is not one of them: it rides on
the var itself, so it round-trips with the item like any other edit.

### `Reporter` (optional — send loot to an external destination)
```clojure
(report-label [this])        ; => button label, e.g. "Send to Discord"
(report! [this view-model])  ; => {:ok true}; throws on failure
```
Config-driven via `:reporting` (mirrors `:storage`), and **optional** — when none
is configured the UI hides its per-item report button. One built-in backend:

```clojure
:reporting {:backend :discord :discord {:webhook-url #env DISCORD_WEBHOOK_URL}}
```

Sections marked `:section/secret?` are left out of Discord reports; set
`:include-secret? true` under `:discord` to send them anyway. An item whose
body renders empty is left out too, along with any section that has no items
left — both stay in the result editor, where they can be filled in.

`POST /api/report` `{:view-model …}` forwards the (validated) view-model to the
reporter; `GET /api/capabilities` tells the UI whether to show the button.

### `Store` (optional — custom persistence)
```clojure
(read-collection [this coll])   ; writes are declared, see below
```
State is a set of named collections, each a map of key to value; a collection
needs no declaration and reads as `{}` until written to. Three backends, chosen
by config `:storage {:backend ...}`: `:memory` (the default), `:file` (one EDN
file per collection under `:file {:dir ...}`, default `./state`, and the files are the
source of truth — hand edits propagate live) and `:browser` (IndexedDB, with the
state travelling on each request).

Reads take and return plain data, so plugins work unchanged whichever backend is
configured. Writes are not a method: a plugin puts them on its view-model under
`:store/mutations` (`{<collection> {<key> <value>}}`, a nil value retracting) and
the engine applies them once that view-model has validated — so a call that ends
in an error cannot leave state changed behind it.

A loot type's `:store/collections` declares what it uses, defaulting to
`[<plugin-id>]`. Available to `:builtin` and `:jar` plugins;
`:cli`, `:ffi` and `:wasm` persist their own state.
See [docs/storage.md](docs/storage.md).

---

## `:inputs` (drive the generic form)

Declared under a tool's `:generator`, or in a plugin's loot-spec:

```clojure
{:inputs [{:id :character :label "Character" :type :enum :options ["Thoros" "Simo"]}
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

## Ranks (mod state & progression)

A mod carries `:vars` and a `:template` interpolating them. **A var carries its
own progression** — there is no separate upgrade graph and no path to replay:
the rank is one more key on the var the template already reads.

```clojure
{:vars      {:ab 1                          ; steps by 1: 1, 2, 3, …
             :range {:value 30 :step 15}}   ; 30, 45, 60, …
 :template  "+{{ab}} AB within {{range}}ft."
 :max-ranks 4}                              ; total ranks across every var
```

Ranks are **1-based**: a var's declared `:value` is rank 1, so an item nobody
has upgraded is rank 1 and needs no bookkeeping at all. One upgrade makes it
`{:rank 2}`.

| key | meaning |
|---|---|
| `:step` | how far one rank moves the value; defaults to the value itself |
| `:max` | most ranks this var may take; uncapped by default |
| `:rank` | ranks taken; absent means 1 |

A var holding anything but a number has nowhere to step to and never ranks.
Text that should appear only once a var is high enough keys off the number
rather than off a second flag var that could disagree with it:

```clojure
"+{{ab}} AB.{{#gte ab 3}} Deals 6 fire damage on hit.{{/gte}}"
```

Templates do arithmetic, so one var can drive several numbers at once — a rank
that is worth +1 AB and +3 fire damage is one var, not two:

```clojure
"+{{multiply rank 3}} fire damage, +{{rank}} AB"   ; at rank 3: +9 and +3
```

The vocabulary is **closed**, deliberately: the browser folds ranks in as it
renders, so the same `sns.sdk.rank` runs on both sides of the wire.

```clojure
(rank/stepped {:value 1 :step 2 :rank 3})   ; => 5
(rank/upgradeable? {:value 1})              ; => true  (a number, uncapped)
(rank/available vars 4)                     ; => (:ab :range), or nil when capped
(rank/rank-up vars :ab)                     ; => vars with :ab one rank higher
(rank/mod-rank vars)                        ; => the mod's own rank
```

Nothing is stamped on a var to set this up — the defaults are read, not
written — so a plugin's vars travel exactly as it wrote them, and a rank
round-trips with the item like any other DM edit. The editor shows the rank
beside the value, and the value it shows stays the rank-1 base: the card shows
what the two come to.

An item whose body renders to nothing is left out of the view — a mod the DM
emptied, or one whose text only starts at a higher rank. It stays in the
editor, and comes back the moment it renders something again.

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

### A var's rank and its value are one map

A var's progression addresses it by the same id the template interpolates, so
ranking a mod up and drawing its randoms touch one map rather than two:

```clojure
{:vars     {:ab 1 :x {:random :damage-types}}
 :template "+{{ ab }} AB, {{ x }} damage"}
```

`(rank/rank-up vars :ab)` twice gives `:ab` `{:value 1 :rank 3}`, which renders
as 3; `:x` is untouched. The declared value never moves, so a rank can be taken
back, and there is exactly one place a rank lives — no second copy to fall out
of step with the first.

A resolved var records the `:type` it was declared as (`:int`, `:decimal`,
`:bool` — anything else is text). The editor picks its control from that — and
puts the value back in that type, so a DM can retype `:ab` and it still ranks
up as a number. A numeric field left blank comes back as nil: it renders as
nothing, and ranking leaves it alone.

---

## The data DSL (`:data`)

A code-free loot type in one EDN (or `.json`) file, loaded from `:source` (a
filesystem path) — or written straight into the config under `:inline`, which
takes precedence over `:source` when both are given:

```clojure
{:inputs   []                              ; optional; :generator {:inputs …} overrides
 :history  :always                         ; optional; :generator {:history …} overrides
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

(The data DSL renders mods at the rank their vars declare; nothing ranks them
up — use `:builtin`/`:jar` for stateful loot.)

---

## The external plugin contract (`:cli`, `:ffi`, `:wasm`)

External plugins exchange the **same view-model** a `:jar` plugin returns, as
JSON — only the transport differs. Namespaced keys survive JSON as they are, so
`"loot/title"` reads back as `:loot/title` and there is one output contract to
learn, not two. The engine sends a **request** and reads back a **view-model**:

```json
// request → plugin        // view-model ← plugin
{"inputs": {...}}           {"loot/title": "Fogfall", "loot/subtitle": "Weather",
                             "loot/sections": [{"section/heading": "Sky",
                               "section/items": [
                                 {"item/body": "…",
                                  "item/metadata": ["obscured"]}]}]}
```

Everything [the view-model](#the-view-model-what-every-loot-type-returns)
supports is available, including `item/vars` — so an external plugin emits
*templates* with editable, typed values, exactly as a builtin does, rather than
finished prose:

```json
{"loot/title": "Ember Ring",
 "loot/sections": [{"section/items": [
   {"item/body": "Deals +{{ dmg }} fire damage.",
    "item/vars": {"dmg": {"value": 3, "type": "int"}}}]}]}
```

A var may also be **declared rather than resolved** — `{"random": "<preset>"}`,
the same shape a `:data` entry or a mod's `:vars` use — and the engine draws it
with the request's rng before validating, so an external plugin names a preset
from `:randoms` instead of shipping its own copy of the vocabulary:

```json
{"item/body": "+2 {{ dmg-type }} damage",
 "item/vars": {"dmg-type": {"random": "dmg-type"}}}
```

The drawn value comes back with the preset's `:options` attached, so the DM edits
it as a combobox exactly as for an in-process plugin.

The one exception is `loot/actions`, covered below.

- **`:cli`** runs your `:cli {:command [...]}`, writing the request to **stdin** and reading the
  view-model from **stdout**. A non-zero exit is an error, and whatever the command
  wrote to **stderr** becomes the error the DM sees. See
  `examples/cli-plugin/weather.py`.
- **`:ffi`** calls `:ffi`'s `:symbol` in its `:library` — a C-ABI function
  `char* generate(const char* request_json)` returning a malloc'd output string.
  If `:free-symbol` is set (e.g. `loot_free`) it is called on the returned pointer
  once the bytes are read; otherwise the library owns that memory. The same C ABI
  is reachable from any language that can export it — see `examples/ffi-plugin/`
  for equivalent plugins in C (`loot.c`), Go (`loot.go`), and Rust (`loot.rs`),
  each with its one-line build command at the top.
- **`:wasm`** runs a WASI command module on GraalWASM, in-process: the request goes
  to its **stdin** and the view-model comes back on **stdout**, as for `:cli`, but
  with no subprocess and no ABI to hand-roll. Under `:wasm`, `:module` is the file,
  `:args` are its program arguments and
  `:dirs` maps guest paths to host directories — a module with no `:dirs` gets no
  filesystem at all. Unlike `:jar` it runs in the native image too, and unlike
  `:ffi` one module runs on every platform. See `examples/wasm-plugin/loot.go`,
  which shows `item/vars` and `loot/state` across an action.

An external plugin has no loot-spec of its own, so it declares the form fields it
needs under its config entry's **`:generator`**, in the same shape as a loot-spec's
`:inputs`; the engine renders the form and sends the collected values as the request's `inputs`
(an `:int` or `:decimal` field arrives as a JSON number, not the form's string):

```json
{"type": "cli", "id": "insight", "label": "Insight Checks", "section": "Utilities",
 "generator": {"inputs": [{"id": "socialBonus", "label": "Speaker's social bonus", "type": "int"}]},
 "cli": {"command": ["./5e-cli", "-data", "data", "insight"]}}
```

The output is validated before it is mapped, so a contract breach fails with an
error in *your* keys (e.g. a missing `item/body`). Both directions are modelled in
`schemas.json` as `sns.sdk.schema.plugin-request` and
`sns.sdk.schema.plugin-output`, and emitted rooted as
`plugin-request.schema.json` / `plugin-output.schema.json` so codegen tools
(quicktype, typify, go-jsonschema) can generate request/output structs for a plugin
written in another language. The request is a union of the two modes — a generate
call (`inputs`) or an action call (`action` + `params` + `view-model`) — so codegen
yields both, and the generate-vs-action split is structural rather than a
convention you infer.

### Actions (stateful follow-ups)

A plugin can drive follow-up actions (e.g. "level up") in its own language. Emit
`loot/actions` in the output. This is the one part of the view-model you do **not**
write in full: `:action/event` is a UI action vector the browser dispatches as-is,
and it carries the `id` your plugin was registered under — which the plugin has no
way to know, since the same command can be registered several times. So you supply
the three fields the SDK's `Models.Action` gives a `:jar` author, and the adapter
builds the event:

```json
{"loot/title": "Aegis",
 "loot/actions": [{"label": "Sharpen", "action": "sharpen", "params": {"by": 1}}]}
```

The engine turns each into a button; clicking it re-invokes the **same command /
symbol / module** with an action request (note `action`/`params` instead of
`inputs`):

```json
{"action": "sharpen", "params": {"by": 1},
 "view-model": {"loot/title": "Aegis", "…": "…"}}
```

`view-model` is the result the UI had on screen, **DM edits included**. Rebuild
your item from that rather than from a copy frozen into `params`, so what the DM
can see is what the action operates on — and put anything the item cannot express
(a stored id, say) under `loot/state`, which the engine and UI carry
through untouched. The plugin returns a fresh view-model (which may itself carry
the next round of `loot/actions`). Branch on whether `action` is present in the
request to tell a generate from an action.

### State (`store/collections`, `store/manual`)

An external plugin never touches the store. It **declares** the collections it
uses on its config entry; the engine reads them and sends them as the request's
`state`, and applies the `store/mutations` the plugin returns:

```json
// request → plugin                    // view-model ← plugin
{"inputs": {"rounds": 5},               {"loot/title": "3 flares over 5 rounds",
 "state": {"crystals": {                 "store/mutations": {"crystals": {
   "Quincy": {"chance": 27}}}}             "Quincy": {"chance": 32},
                                           "Viktor": null}}}
```

`store/mutations` is `{<collection>: {<key>: <value>}}`, a `null` retracting that
key. The engine applies it only once the output has validated, so a plugin that
errors changes nothing. Every backend works the same way, including `:browser` —
the plugin never learns which is configured, and never parses or writes EDN.

Declare state one of two ways under the config entry's `generator`:

- **`store/collections`** — the collections to read, e.g. `["crystals"]`.
- **`store/manual`** — a DM-maintained table the UI renders an editor for, held
  in the collection named after the plugin's `id`. Same shape as a builtin's
  `:store/manual`: a `key-label`, the `fields` of a row, and `list?` when a key
  owns several records.

```json
{"type": "cli", "id": "crystals", "label": "Crystal Flares", "section": "Utilities",
 "cli": {"command": ["./5e-cli", "crystal", "procs"]},
 "generator": {"inputs": [{"id": "rounds", "label": "Combat rounds", "type": "int", "default": 10}],
               "store/manual": {"key-label": "Character",
                                "fields": [{"id": "chance", "label": "Flare chance (%)", "type": "int",
                                            "default": 10}]}}}
```

`examples/cli-plugin/tally.py` is a runnable version of both directions.

A plugin that declares neither is sent no `state` and costs no reads.

---


## Writing a `:jar` plugin

Depend on this module, implement `Generator`, and expose a factory:

```clojure
(ns my.plugin
  (:require [sns.sdk.protocols :as p]))

(defn generator [_plugin-config]
  (reify p/Generator
    (loot-spec [_] {})                    ; or any :generator keys, e.g. {:inputs [...]}
    (generate  [_ ctx] {:loot/title "Hello from a jar"})))
```

Build a jar, point `:jar {:path ... :entrypoint ...}` at it, and it loads at startup.
Its id, label and section come from the config entry; its loot-spec only declares
`:generator` keys, and the config entry's `:generator` overrides those.

A plugin with no Clojure in it (e.g. pure Java/Kotlin) can skip the factory var:
implement the `sns.sdk.Generator` interface on a class with a 0-arity
constructor and name it with `:class` instead. `lootSpec()` defaults to an empty
`Models.Spec`, so override it only to declare `:generator` keys:

```clojure
{:type :jar :id :custom :jar {:path "plugins/custom.jar" :class "my.plugin.CustomLoot"}}
```

The `Models` records cover the schemas in full, so the Java path loses nothing a
Clojure one can express: `Models.Item` carries `vars`, and `Models.ViewModel`
carries `vars`, `state` and `mutations`. Implement `sns.sdk.Action` for
follow-ups — its `ctx.get("view-model")` is the displayed, possibly DM-edited
result as a `Models.ViewModel`.

