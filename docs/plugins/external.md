# External plugins

[CLI](cli.md), [WASM](wasm.md) and [FFI](ffi.md) plugins are written in whatever
language you like, and talk to the app in JSON. They differ only in how the JSON
gets there:

| Type    | The plugin is            | Request arrives on | Result goes to |
|---------|--------------------------|--------------------|----------------|
| `:cli`  | a program the app runs   | stdin              | stdout |
| `:wasm` | a WebAssembly module     | stdin              | stdout |
| `:ffi`  | a function in a shared library | its argument | its return value |

Everything else on this page applies to all three, so a plugin can move between
them without changing its logic. A CLI plugin and a WASM plugin can even be built
from the same source.

If you're happy writing Clojure, Java or another JVM language, a
[JAR plugin](jar.md) is the better fit.

## The exchange

Each time the DM presses generate, the app sends a **request** and the plugin
replies with a [result](../results.md):

```json
// request
{"inputs": {"rounds": 5}}
```

```json
// result
{"loot/title": "Fogfall",
 "loot/subtitle": "Weather",
 "loot/sections": [{"section/heading": "Conditions",
                    "section/items": [{"item/body": "Sight beyond 30 feet is obscured.",
                                       "item/metadata": ["obscured"]}]}]}
```

`inputs` holds the values of the plugin's [form fields](../inputs.md), with
numbers as JSON numbers. It's `{}` for a plugin with no fields.

The result is everything described in [Results](../results.md), written in JSON,
including [variables](../variables.md):

```json
{"loot/title": "Ember Ring",
 "loot/sections": [{"section/items": [
   {"item/body": "Deals +{{ dmg }} {{ type }} damage.",
    "item/vars": {"dmg": {"value": 3},
                  "type": {"random": "damage-types"}}}]}]}
```

A variable can name one of the config's [randoms](../variables.md#randoms), as
`type` does above, and the app draws the value. Your plugin doesn't need its own
copy of the list.

## Configuring the form

An external plugin can't describe itself, so its form fields and other settings
go under [`:generator`](../tools.md#plugin-settings) in the config:

```json
{"type": "cli", "id": "insight", "label": "Insight Checks", "section": "Utilities",
 "cli": {"command": ["./insight"]},
 "generator": {"inputs": [{"id": "bonus", "label": "Speaker's bonus", "type": "int", "default": 0}]}}
```

## Actions

To add [action buttons](../actions.md), list them under `loot/actions` with a
`label`, an `action` name, and optionally `params` and `inputs`:

```json
{"loot/title": "Whetstone Blade",
 "loot/sections": [{"section/items": [
   {"item/body": "A plain blade, +{{ keen }} keener.",
    "item/vars": {"keen": {"value": 0}}}]}],
 "loot/actions": [{"label": "Sharpen", "action": "sharpen", "params": {"by": 1}}],
 "loot/state": {"tier": 1}}
```

Pressing the button calls the same plugin again, with an action request:

```json
{"action": "sharpen",
 "params": {"by": 1},
 "view-model": {"loot/title": "Whetstone Blade", "loot/state": {"tier": 1}, "...": "..."}}
```

- `view-model` is the result as it's currently shown, including the DM's edits
  and your `loot/state`. Read values from here rather than from `params`.
- `inputs` holds the values of the action's own fields, and is only present
  when it has some.

The plugin replies with a new result, which can offer further actions. To tell a
generate request from an action request, check whether `action` is present.

## State

External plugins don't read or write files to keep [state](../state.md).
Instead, they declare which collections they use, and the app sends those with
each request as `state`:

```json
{"type": "cli", "id": "tally",
 "cli": {"command": ["python3", "tally.py"]},
 "generator": {"store/collections": ["tally"]}}
```

```json
// request
{"inputs": {"who": "Quincy"},
 "state": {"tally": {"Quincy": {"count": 4}, "Viktor": {"count": 1}}}}
```

To change state, the result lists the changes under `store/mutations`:
`{collection: {key: value}}`, with `null` deleting a key.

```json
// result
{"loot/title": "Quincy × 5",
 "store/mutations": {"tally": {"Quincy": {"count": 5}, "Viktor": null}}}
```

This works the same whichever storage backend is configured, and the changes are
only saved if the rest of the result is valid.

A [DM-maintained table](../state.md#dm-maintained-tables) declared with
`store/manual` is sent the same way, in the collection named after the plugin's
id:

```json
"generator": {"store/manual": {"key-label": "Name",
                               "fields": [{"id": "count", "label": "Count", "type": "int", "default": 0}]}}
```

A plugin that declares neither gets no `state`.

## Errors

If your plugin can't produce a result, it can report why. A CLI or WASM plugin
can write a message to stderr and exit with a non-zero status, which the DM sees
as the error. Any plugin can also return a result with `"loot/error?": true`.

If the result itself is malformed, the error names the problem in the result's
own keys, such as a missing `item/body`.

## JSON schemas

Every release includes `plugin-request.schema.json` and
`plugin-output.schema.json`, JSON Schemas for the request and the result (run
`make schemas` to generate them from source). Code generators such as
[quicktype](https://quicktype.io) can turn them into types for your language. The
request schema has two variants, generate and action, so the generated code
gets a type for each.

## Examples

- [`examples/cli-plugin/weather.py`](../../examples/cli-plugin/weather.py): the
  simplest possible plugin.
- [`examples/cli-plugin/tally.py`](../../examples/cli-plugin/tally.py): state
  and a DM-maintained table.
- [`examples/wasm-plugin/loot.go`](../../examples/wasm-plugin/loot.go): variables,
  `loot/state` and an action.
- [`examples/ffi-plugin/`](../../examples/ffi-plugin/): the same plugin in C, Go
  and Rust.
