# Getting started

## Install

Each [release](https://github.com/spies-and-spiders/companion/releases) has two
kinds of build:

- **A native binary** for Linux (x64 and arm64), macOS (Apple silicon) and
  Windows (x64). It starts instantly and needs nothing else installed. It's the
  only build that can run [WASM plugins](plugins/wasm.md).
- **A standalone JAR** (`companion-<version>-standalone.jar`), run with
  `java -jar` on Java 22 or newer. It's the only build that can load
  [JAR plugins](plugins/jar.md).

The install scripts download the right native binary into the current directory
(or the one you pass them):

```sh
# macOS / Linux
curl -fsSL https://raw.githubusercontent.com/spies-and-spiders/companion/master/scripts/install.sh | bash
```

```powershell
# Windows
irm https://raw.githubusercontent.com/spies-and-spiders/companion/master/scripts/install.ps1 | iex
```

## Write a config

The app reads its loot types from a config file. Create `config.edn` next to the
binary:

```clojure
{:tools [{:type :data
          :id   :trinkets
          :data {:inline {:tables     {:trinkets [{:name "A brass key that fits no lock"}
                                                  {:name "A glass eye that blinks"}
                                                  {:name "A map of a town that burned down"}]}
                          :let        {:trinket {:choose :trinkets}}
                          :view-model {:loot/title "{{trinket.name}}"}}}}]}
```

That's one tool, `trinkets`, which picks a random entry from a table. It's a
[data plugin](plugins/data.md), so there's no code involved.
[`examples/config.edn`](../examples/config.edn) is a fuller example with every
setting annotated.

## Run it

```sh
./companion                          # reads ./config.edn, or ./config.json
./companion --config my-campaign.edn # or name the file
```

```sh
java -jar companion-<version>-standalone.jar --config my-campaign.edn
```

Open <http://localhost:8080> and press **Generate Trinkets**.

Relative paths in the config, such as a data plugin's `:source`, are resolved
against the directory you run the app from, so it's simplest to keep the config,
its data files and any plugins together and run the app from there.

If the config is invalid, the app refuses to start and prints what's wrong with
it.

## Next steps

- [Using the app](using-the-app.md) shows what the UI can do.
- [Tools](tools.md) explains the kinds of plugin you can add.
- [Configuration](configuration.md) covers the remaining settings, such as the
  port and where state is saved.
