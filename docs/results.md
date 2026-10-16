# Results

Every plugin, whatever it's written in, returns a **result** in the same shape.
The UI knows how to show that shape, so a new plugin never needs any UI code.

Here's a result as a JAR plugin writes it:

```clojure
{:loot/title    "{{ name }}"
 :loot/subtitle "Unique · {{ base }}"
 :loot/vars     {:name {:value "Pacifist's Vow"}
                 :base {:value "armour"}}
 :loot/sections [{:section/heading "Mods"
                  :section/items   [{:item/body     "+{{ ab }} AB with effects that cannot deal damage."
                                     :item/metadata ["accuracy"]
                                     :item/vars     {:ab {:value 1}}}
                                    {:item/body "Your weapons gain the 'non-lethal' trait."}]}
                 {:section/heading "Curse"
                  :section/secret? true
                  :section/items   [{:item/body "You cannot knowingly deal the killing blow."}]}]}
```

And the same thing as JSON, which is what [external plugins](plugins/external.md)
print. The keys are identical, slashes included:

```json
{"loot/title": "{{ name }}",
 "loot/subtitle": "Unique · {{ base }}",
 "loot/vars": {"name": {"value": "Pacifist's Vow"}, "base": {"value": "armour"}},
 "loot/sections": [
   {"section/heading": "Mods",
    "section/items": [
      {"item/body": "+{{ ab }} AB with effects that cannot deal damage.",
       "item/metadata": ["accuracy"],
       "item/vars": {"ab": {"value": 1}}},
      {"item/body": "Your weapons gain the 'non-lethal' trait."}]},
   {"section/heading": "Curse",
    "section/secret?": true,
    "section/items": [{"item/body": "You cannot knowingly deal the killing blow."}]}]}
```

Only `loot/title` is required, so `{"loot/title": "A pinch of divine dust"}` is a
complete result.

## The parts

A result is a title and subtitle, then a list of **sections**, each with an
optional heading and a list of **items**.

| Key               | What it is |
|-------------------|------------|
| `:loot/title`     | The card's heading. Required. |
| `:loot/subtitle`  | A line above the title. |
| `:loot/vars`      | [Variables](variables.md) used by the title, subtitle and every item. |
| `:loot/sections`  | The body of the card. |
| `:loot/actions`   | Buttons under the result. See [Actions](actions.md). |
| `:loot/state`     | Anything the plugin wants handed back with the next action. See [Actions](actions.md#keeping-track-of-things). |
| `:loot/error?`    | Marks the result as a failure. See below. |
| `:store/mutations`| Changes to save. See [State](state.md#reading-and-writing-from-a-plugin). |

| Section key        | What it is |
|--------------------|------------|
| `:section/heading` | The section's heading. |
| `:section/items`   | Its items. Required. |
| `:section/secret?` | Shown on the card, but not [sent to Discord](reporting.md). |

| Item key         | What it is |
|------------------|------------|
| `:item/body`     | The item's text. Required. |
| `:item/title`    | A bold heading for the item. |
| `:item/metadata` | Short tags shown as chips under the item. |
| `:item/vars`     | [Variables](variables.md) for this item's text. |

Titles, bodies, headings and metadata are all [templates](templates.md), so they
can contain `{{ }}` placeholders. Text without placeholders is shown as it is.

An item whose text comes out empty isn't shown. That's useful for text that only
appears under some condition, such as at a higher [rank](ranks.md); the item
stays in the editor, so it reappears as soon as it has something to say.

## Errors

When something goes wrong, such as there being no relics left to draw, return a
result with `:loot/error? true`. It's shown over the card, and the DM can click
it away to get back to the previous result.
Error results are never saved to the history.

A plugin that crashes gets the same treatment automatically, with the error
message as the title. External plugins can print a message to stderr and exit
with a non-zero status; JAR plugins can throw.

## Validation

Every result is checked before it's shown. If one is malformed, such as an item
missing its `:item/body`, the DM sees an error naming the problem instead, and
none of its [state changes](state.md#reading-and-writing-from-a-plugin) are
saved. External plugins are checked against
[`plugin-output.schema.json`](plugins/external.md#json-schemas).
