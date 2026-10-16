# S&S Companion documentation

## Running the app

- [Getting started](getting-started.md): install, write a first config, and run it.
- [Using the app](using-the-app.md): a tour of the UI during a session.

## Configuring it

- [Configuration](configuration.md): the config file and its top-level settings.
- [Tools](tools.md): declaring loot types, and choosing the right kind of plugin.
- [Pages](pages.md): showing several tools side by side.
- [The loot table](loot-table.md): rolling a die to decide what drops.
- [Form inputs](inputs.md): fields the DM fills in before generating.
- [Result history](history.md): which results get kept.
- [State](state.md): where stateful loot and DM-maintained tables are stored.
- [Reporting to Discord](reporting.md): sending results to a channel.

## Writing loot

- [Results](results.md): the shape every plugin returns.
- [Templates](templates.md): how result text is written and rendered.
- [Variables and randoms](variables.md): editable values inside the text, and
  drawing them from named lists.
- [Ranks](ranks.md): values that grow as an item is upgraded.
- [Actions](actions.md): follow-up buttons on a result, such as "Level up".

## Plugin types

- [Built-in plugins](plugins/builtin.md): the ones that ship with the app.
- [Data plugins](plugins/data.md): loot written as EDN or JSON, no code needed.
- [JAR plugins](plugins/jar.md): Clojure, Java or another JVM language, using
  the SDK. Standalone JAR only.
- [External plugins](plugins/external.md): the JSON contract shared by the three
  below, for plugins in any other language.
  - [CLI plugins](plugins/cli.md): any program that reads stdin and writes stdout.
  - [WASM plugins](plugins/wasm.md): a sandboxed WebAssembly module. Native
    binary only.
  - [FFI plugins](plugins/ffi.md): a function in a shared library.

## Working on the app

- [Development](development.md): building from source, tests, and releases.
