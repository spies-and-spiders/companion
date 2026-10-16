# Form inputs

A plugin can ask the DM for values before it generates, such as which character
the loot is for, or how many rounds have passed. Declare the fields under the
plugin's `:generator`:

```clojure
{:type      :cli
 :id        :crystals
 :cli       {:command ["./crystals"]}
 :generator {:inputs [{:id :rounds :label "Combat rounds" :type :int :default 10}
                      {:id :character :label "Character" :type :enum
                       :options ["Thoros" "Simo" "Vex"]}
                      {:id :lucky? :label "Lucky" :type :bool}]}}
```

The form appears above the generate button, and the values are sent to the plugin
with each request, keyed by `:id`.

## Fields

| Key        | What it does |
|------------|--------------|
| `:id`      | The key the value arrives under. |
| `:label`   | The text beside the field. |
| `:type`    | One of the types below. |
| `:default` | Used when the field is left blank. |
| `:options` | The choices for an `:enum`. |
| `:list?`   | Collects several values instead of one. |

| Type       | Shown as                     | Arrives as |
|------------|------------------------------|------------|
| `:text`    | a text box                   | a string |
| `:int`     | a number box                 | a whole number |
| `:decimal` | a number box                 | an exact decimal, so `1.3` stays `1.3` |
| `:bool`    | a checkbox                   | `true` or `false` |
| `:enum`    | a text box that suggests `:options` as you type | a string |

An `:enum` isn't a strict dropdown. Long option lists are easier to filter by
typing, so the field accepts anything and just flags a value that isn't one of
the options. If your plugin needs one of the options exactly, it should check.

Similarly, a number that won't parse is passed to the plugin as the text that was
typed, so the plugin can report the problem in its own words.

## Lists

With `:list? true`, each value gets its own row, and there's always a blank row at
the bottom: fill it in to add another. Rows can be removed or dragged into a
different order. (Checkbox lists use an **Add** button instead.) The plugin receives a
list, which is empty if nothing was entered and there's no `:default`.

```clojure
{:id :party :label "Party member" :type :text :list? true}
```

## Inputs on actions

[Actions](actions.md#action-inputs) can have fields too, shown beside their
button. They're declared the same way.
