# Actions

An **action** is a button on a result that sends it back to the plugin to do
something more: upgrade it, re-roll one of its values, roll a check using it.
The plugin returns a new result, which replaces the old one on the card.

## Offering an action

How a plugin adds a button depends on its type. External plugins and JAR plugins
using the Java interfaces give a label, an action name and optional parameters:

```json
{"loot/title": "Aegis",
 "loot/actions": [{"label": "Sharpen", "action": "sharpen", "params": {"by": 1}}]}
```

Clojure JAR plugins and built-ins write the event the button sends directly:

```clojure
{:loot/title   "Aegis"
 :loot/actions [{:action/label "Sharpen"
                 :action/event [:loot/action {:action :sharpen :params {:by 1}}]}]}
```

Either way, clicking the button calls the plugin again with the action's name and
parameters. See [external plugins](plugins/external.md#actions) and
[JAR plugins](plugins/jar.md#actions) for what that call looks like.

Actions only go back to the plugin that offered them.

## Working from what's on screen

The DM may have [edited the result](using-the-app.md#editing-a-result) before
pressing the button. So that the action works on what the DM can see, the
plugin is sent the result as it currently stands, edits included, and should
rebuild its item from that rather than from a copy saved in the parameters.

For example, if the result says "+{{ keen }} keener" with `keen` at 2 and the DM
changes it to 5, a "Sharpen" action should read `keen` from the result it's given
and return 6.

## Keeping track of things

Some things the plugin needs aren't shown on the card, such as the id an item is
stored under. Put those under `:loot/state`. The app never looks at it, and hands
it back with the next action.

```json
{"loot/title": "Whetstone Blade",
 "loot/state": {"relic-id": "r1", "tier": 2}}
```

Keep this to bookkeeping. Anything the DM should be able to see or change belongs
in a [variable](variables.md), because edits can't reach `:loot/state`.

## Action inputs

An action can ask for values, shown as a small form beside its button. They're
declared like [form inputs](inputs.md):

```json
{"label": "Replace a mod", "action": "replace",
 "inputs": [{"id": "mod", "label": "Mod", "type": "enum", "options": ["Fire", "Cold"]}]}
```

One "Replace a mod" button with a dropdown is tidier than a button per mod.

Actions also have two extra field types for pointing at part of the result:

| Type       | The DM picks | The plugin gets |
|------------|--------------|-----------------|
| `:section` | a section of the result | `{"section": 1}` |
| `:item`    | an item of the result   | `{"section": 1, "item": 0}` |

The numbers are positions in `loot/sections` and that section's `section/items`,
counting from 0.
