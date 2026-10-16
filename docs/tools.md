# Tools

A **tool** is anything listed on the UI's rail. Most tools are **plugins**: a
loot type with a generate button. The other kind is a [page](pages.md), which
shows several plugins together.

Tools are listed under `:tools`, in the order they should appear:

```clojure
{:tools [{:type :data    :id :rings   :data {:source "data/rings.edn"}}
         {:type :cli     :id :weather :label "Weather" :section "Utilities"
                         :cli {:command ["python3" "weather.py"]}}
         {:type :builtin :id :social  :label "Group Social" :section "Utilities"}]}
```

## Choosing a plugin type

| Type                             | Write it in                    | Can keep state | Works with          | Good for |
|----------------------------------|--------------------------------|----------------|---------------------|----------|
| [`:builtin`](plugins/builtin.md) | (ships with the app)           | yes            | binary and JAR      | The tools that come with the app. |
| [`:data`](plugins/data.md)       | EDN or JSON                    | no             | binary and JAR      | Picking from tables. The place to start. |
| [`:jar`](plugins/jar.md)         | Clojure, Java, Kotlin…         | yes            | JAR only            | Anything with logic. The richest integration, using the SDK directly. |
| [`:cli`](plugins/cli.md)         | any language                   | yes            | binary and JAR      | Anything a script can do, in a non-JVM language. |
| [`:wasm`](plugins/wasm.md)       | any language that targets WASI | yes            | native binary only  | Code you want to share with other DMs: one file runs everywhere, sandboxed. |
| [`:ffi`](plugins/ffi.md)         | C, Rust, Go, Zig…              | yes            | binary and JAR      | Native code loaded into the app itself. |

If your loot is a matter of "pick one of these, then show it", use a data plugin.
If it needs logic, write a JAR plugin: it reads state directly, can add its own
randoms, and needs no JSON round trip. If you'd rather not use a JVM language, a
CLI plugin is the easiest alternative, since you can run it by hand in a terminal.
The CLI, WASM and FFI types all speak the same
[JSON contract](plugins/external.md), so a plugin can move between them later.

## Settings every tool has

| Key        | What it does |
|------------|--------------|
| `:type`    | The kind of tool: one of the plugin types above, or `:page`. |
| `:id`      | A unique keyword naming the tool. The loot table and pages refer to tools by id. |
| `:label`   | The name shown in the UI. Defaults to one made from the id, so `:divine-dust` shows as "Divine dust". |
| `:section` | The rail heading to list it under. Tools without one go under **Loot**, which always comes first; other sections are sorted alphabetically. |

Settings that only one type understands go under a key named after the type,
such as `:cli {:command [...]}` or `:data {:source "..."}`.

A plugin decides what it generates, but never its own id, label or section.
Those only come from the config, so the same plugin can be listed twice under
different names.

## Plugin settings

Every plugin can also take a `:generator` map, which adjusts how the app presents
it:

| Key                  | What it does |
|----------------------|--------------|
| `:inputs`            | Form fields shown above the generate button. See [Form inputs](inputs.md). |
| `:generate-label`    | Replaces the button's "Generate &lt;label&gt;" text. |
| `:hidden?`           | Keeps it off the rail. See below. |
| `:history`           | Overrides the global [history setting](history.md) for this plugin. |
| `:store/collections` | The [state](state.md) collections it uses. |
| `:store/manual`      | A table the DM fills in by hand. See [State](state.md#dm-maintained-tables). |

```clojure
{:type      :cli
 :id        :insight
 :cli       {:command ["./insight"]}
 :generator {:generate-label "Roll Insight"
             :inputs         [{:id :bonus :label "Speaker's bonus" :type :int :default 0}]}}
```

Plugins can declare these settings for themselves (a JAR plugin in code, a data
plugin in its spec file). Anything under `:generator` in the config wins, key by
key, so you can adjust a plugin you didn't write.

## Hidden tools

A plugin with `:generator {:hidden? true}` doesn't appear on the rail. You can
only reach it by rolling the [loot table](loot-table.md), from a [page](pages.md),
or through another tool's [action](actions.md). It's useful for loot that should
only ever come up by chance:

```clojure
{:tools      [{:type :data :id :omens :generator {:hidden? true}
               :data {:source "data/omens.edn"}}
              ...]
 :loot-table [... {:id :omens :weight 5}]}
```

While a hidden tool's result is on screen, it shows on the rail so you can see
where you are. A hidden tool that isn't on the loot table or a page can't be
reached at all, so remember to put it somewhere.
