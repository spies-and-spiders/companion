# Data plugins

A data plugin is loot written entirely as data: some tables to draw from, and a
description of the [result](../results.md) to build from the draws. No
programming involved.

```clojure
{:type :data :id :rings :label "Rings" :data {:source "data/rings.edn"}}
```

`:source` is a `.edn` or `.json` file, relative to the directory the app runs
from. For a short table, write the spec into the config instead with
`:data {:inline {...}}`.

Data plugins loaded from a file get a **Reload data before generating?**
checkbox, so you can fix a typo in the file mid-session without restarting.

## A first example

Draw two rings from a table and show each as an item:

```clojure
{:tables     {:rings [{:name "Band of the Sentinel"
                       :effect "Use your reaction to impose disadvantage on an attack against an adjacent ally."}
                      {:name "Loop of Embers"
                       :effect "Once per short rest, deal an extra 1d6 fire damage on a hit."}
                      {:name "Ring of Quiet Steps"
                       :effect "You have advantage on Stealth checks made to move silently."}]}
 :let        {:rings {:draw :rings :n 2}}
 :view-model {:loot/title    "Two rings"
              :loot/sections [{:section/heading "Rings"
                               :section/items   {:data/each :rings
                                                 :data/as   {:item/title {:data/field :name}
                                                             :item/body  {:data/field :effect}}}}]}}
```

There are three parts:

- **`:tables`**: named lists of entries. An entry is a map with whatever fields
  you like.
- **`:let`**: the draws to make, each given a name.
- **`:view-model`**: the [result](../results.md) to return, written out as it
  should look, with `:data/...` placeholders where the draws go.

The spec may also set [`:inputs`](../inputs.md) and [`:history`](../history.md),
which the config's `:generator` overrides.

## Drawing

Each entry in `:let` makes one draw from a table:

| Draw                   | Gives |
|------------------------|-------|
| `{:choose :uniques}`   | one entry |
| `{:draw :rings :n 2}`  | a list of `n` different entries (or the whole table, if it's smaller) |
| `{:take :rings :n 2}`  | a list of `n` entries, which may repeat |

## Filling in the result

The `:view-model` is used as written, apart from three placeholders:

| Placeholder                             | Becomes |
|-----------------------------------------|---------|
| `{:data/field <path>}`                  | the value at `<path>` |
| `{:data/each <path> :data/as <shape>}`  | `<shape>`, once for each entry in the list at `<path>` |
| `{:data/var <path>}`                    | a [variable](../variables.md) holding the value at `<path>` |

A **path** is a key like `:rings`, or a list of keys like `[:unique :mods]` to
reach inside an entry. Paths are looked up in the entry currently being
repeated by `:data/each`, then the draws, then the form [inputs](../inputs.md).
A path that leads nowhere gives nothing, and a key with nothing in it is left
out, so `{:data/field :metadata}` simply doesn't appear on an entry without
metadata.

A `:data/each` inside a list, such as a section's items, adds its items to the
list rather than nesting a list inside it, so you can mix fixed and repeated
items:

```clojure
:section/items [{:item/body "Always shown first."}
                {:data/each :rings :data/as {:item/body {:data/field :effect}}}]
```

## Using fields in templates

Fields can also be used in [templates](../templates.md) directly:

- A `:choose` draw's fields are available everywhere by name, such as
  `"{{unique.name}}"` in the title.
- Inside a `:data/each`, the current entry's fields are available to the items it
  produces, such as `"{{name}}"`.
- Form inputs are available everywhere by their id.

Section headings can only see the first and last of those, so a heading that
changes per entry needs `{:data/field :name}`.

A list of draws isn't available to templates unless you make it a variable:

```clojure
:loot/vars {:drawn {:data/var :rings}}
;; then "{{join (pluck drawn "name") ", "}}"
```

### `{:data/field}` or `"{{ }}"`?

They differ when the field holds a template itself. Given an entry with
`:effect "+4 {{ ability }}."`, `{:data/field :effect}` passes that text on to be
filled in, while `"{{ effect }}"` would insert it as-is and leave `{{ ability }}`
unfilled. When a field might contain placeholders, use `:data/field`.

## Variables

An entry can declare [variables](../variables.md) under `:data/vars`. This is how
a data plugin gets a random value the DM can edit:

```clojure
{:name      "Researcher's Power"
 :data/vars {:ability {:random :literal :options ["Intelligence" "Wisdom"]}}
 :mods      [{:effect "+4 {{ ability }}."}
             {:effect "You cannot use {{ ability }} to cast spells."}]}
```

The variables are drawn once each time the entry is drawn, and every item made
from that entry shares them. For a `:choose` draw they become shared values for
the whole result, so changing "Wisdom" to "Intelligence" in the editor updates
both mods.

Variables written straight into the `:view-model`, under `:loot/vars` or
`:item/vars`, are drawn too.

Data plugins can't keep [state](../state.md) or offer [actions](../actions.md).
When your loot needs either, move to a [JAR](jar.md) or [CLI](cli.md) plugin.

## No tables at all

A spec with nothing to draw only needs a `:view-model`, which is handy for a
fixed reminder that comes up on the loot table:

```clojure
{:view-model {:loot/title    "Tarot cards"
              :loot/sections [{:section/items [{:item/body "Draw 1 tarot card"}]}]}}
```

## In JSON

The same spec works as JSON. Keywords become strings and the keys keep their
slashes:

```json
{
  "tables": {"uniques": [{"name": "Only One", "mods": [{"effect": "Effect A", "tags": ["accuracy"]}]}]},
  "let": {"unique": {"choose": "uniques"}},
  "view-model": {
    "loot/title": "{{unique.name}}",
    "loot/sections": [
      {"section/heading": "Mods",
       "section/items": {"data/each": ["unique", "mods"],
                         "data/as": {"item/body": {"data/field": "effect"},
                                     "item/metadata": {"data/field": "tags"}}}}]}
}
```

[`data/uniques.edn`](../../data/uniques.edn) and
[`data/rings.edn`](../../data/rings.edn) are complete examples.
