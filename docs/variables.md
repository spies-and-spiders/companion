# Variables and randoms

A **variable** is a value that a [template](templates.md) fills in, such as the
`2` and `"fire"` in "+2 fire damage on hit". Keeping values apart from the text
means the DM can edit them in the UI, and a plugin can read them back later
without parsing a sentence.

## Where variables go

Variables are keyed by the name the template uses, and live in one of two places
on a [result](results.md):

- `:item/vars` on an item, for that item's text.
- `:loot/vars` on the result, for the title, subtitle and every item. The editor
  shows these under **Shared values**, so changing one updates every place it's
  used.

```clojure
{:loot/title    "{{ name }}"
 :loot/vars     {:name {:value "Researcher's Power"}
                 :ability {:value "Wisdom" :options ["Intelligence" "Wisdom"]}}
 :loot/sections [{:section/items [{:item/body "+4 {{ ability }}."}
                                  {:item/body "You cannot use {{ ability }} to cast spells."}]}]}
```

## A variable's settings

| Key         | What it does |
|-------------|--------------|
| `:value`    | The value. Required. |
| `:label`    | The name shown in the editor. Defaults to one made from the key, so `:damage-type` shows as "Damage type". Name variables after what they hold and you rarely need this. |
| `:options`  | Other values the DM might choose. The editor suggests them as they type. |
| `:type`     | `:int`, `:decimal` or `:bool`, which picks the editor's control and keeps an edited value a number. Worked out from `:value` when left out; anything else is text. |
| `:context?` | Makes the variable available to templates without showing it in the editor. |
| `:step`, `:max`, `:rank` | Used by [ranks](ranks.md). |

## Shorthand

Plugins can write a variable in a shorter form, which the app turns into the full
form above before the result reaches the browser:

```clojure
{:ability "Wisdom"                          ; just a value
 :damage  {:random :damage-types}           ; drawn from a list (see below)
 :odd     {:literal {:random :something}}}  ; a value that happens to look like the line above
```

A map counts as a draw if it has a `:random` key, and `:literal` is the escape
hatch for a value that really is a map with `:random` in it. Anything else is
taken as the value itself.

## Randoms

Often you want a value picked at random, like a feat or a damage type. Rather
than every plugin carrying its own copy of the list, name the lists once in the
config under `:randoms`:

```clojure
:randoms {:feats        ["Alert" "Athlete" "Brawler" "Charger" "Durable"]
          :skills       ["Athletics" "Deception" "Insight" "Stealth"]
          :damage-types ["acid" "cold" "fire" "lightning" "necrotic" "radiant"]}
```

Then any plugin can draw from them with `{:random <list>}`:

```clojure
{:item/body "You gain the {{ feat }} feat."
 :item/vars {:feat {:random :feats}}}
```

The drawn value arrives in the browser with the whole list as its `:options`, so
the DM can swap it for another from the same list. Draws use the app's random
number generator, whatever language the plugin is written in.

Two lists are always available, even with no `:randoms` configured:

```clojure
;; pick from values written right here
{:random :literal :options ["harm" "damage"]}

;; pick 2 different values from another list; the result is a list
{:random :without-replacement :amount 2 :preset :skills}
```

A list of values is indexed in the template with `.[n]`:

```clojure
{:item/body "Proficiency in {{ skills.[0] }} and {{ skills.[1] }}."
 :item/vars {:skills {:random :without-replacement :amount 2 :preset :skills}}}
```

## Writing them from each plugin type

- [Data plugins](plugins/data.md#variables) declare variables on table entries
  under `:data/vars`, or in the result they describe.
- [External plugins](plugins/external.md) write them in JSON, such as
  `"item/vars": {"dmg": {"random": "damage-types"}}`.
- [JAR plugins](plugins/jar.md#variables-and-randoms) can also add lists in code,
  including ones that take arguments, and re-draw a value in an action.
