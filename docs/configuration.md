# Configuration

Everything about your game lives in one config file. The app looks for
`config.edn`, then `config.json`, in the directory it's run from, or reads the
file named by `--config <path>`.

## EDN or JSON

The examples in these docs are EDN, but JSON works just as well. Keywords
(`:weight`) and symbols (`my.plugin/generator`) become plain strings, and
namespaced keys keep their slash:

```clojure
{:type :cli :id :weather :label "Weather" :cli {:command ["python3" "weather.py"]}}
```

```json
{"type": "cli", "id": "weather", "label": "Weather", "cli": {"command": ["python3", "weather.py"]}}
```

EDN configs are read with [aero](https://github.com/juxt/aero#tag-literals), so
you can use its tags, such as `#env` to read a secret from the environment or
`#include` to split a large config into several files:

```clojure
{:reporting #include "reporting.edn"}
```

Each release includes `schemas.json`, a JSON Schema for the config, which most
editors can use to autocomplete and check a JSON config.

## Settings

Only `:tools` is required.

| Key              | What it does                                                        | Default            |
|------------------|---------------------------------------------------------------------|--------------------|
| `:tools`         | The loot types and pages in the UI. See [Tools](tools.md).           | (required)         |
| `:loot-table`    | Weighted or ranged entries for a die roll. See [The loot table](loot-table.md). | none     |
| `:loot-die-size` | The die the loot table is rolled on.                                 | `100`              |
| `:storage`       | Where state is kept. See [State](state.md).                          | in memory          |
| `:history`       | When results are added to the history. See [Result history](history.md). | `:button`      |
| `:reporting`     | Where **Send** posts results. See [Reporting](reporting.md).         | no button          |
| `:randoms`       | Named lists that variables draw from. See [Variables and randoms](variables.md#randoms). | none |
| `:words`         | Replaces the word list results are tagged from.                      | a built-in list    |
| `:extra-words`   | Adds to the built-in word list.                                     | none               |
| `:server`        | `{:host "127.0.0.1" :port 8080}`                                    | as shown           |

## Letting players connect

By default the app only listens on `127.0.0.1`, so only your own machine can
reach it. To open it to other devices on your network, such as a tablet at the
table, listen on every interface:

```clojure
:server {:host "0.0.0.0" :port 8080}
```

There's no login, so anyone who can reach the port can use the app. Only do this
on a network you trust.

## Validation

The config is checked when the app starts. A missing required setting, a value
of the wrong type, a loot-table entry pointing at a tool that doesn't exist, or
two tools sharing an id all stop the app with an error describing the problem,
rather than failing later at the table. Keys the app doesn't recognise are
ignored, so check the spelling of an optional setting that seems to have no
effect.
