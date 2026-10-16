# WASM plugins

A WASM plugin is a [WASI](https://wasi.dev) WebAssembly module, run inside the
app. It reads the request on stdin and writes the result to stdout, just like a
[CLI plugin](cli.md), so the same source can usually be built as either.

WASM plugins only work with the native binary, not when the app is run as a JAR.

Choose WASM when you want to share a plugin with other DMs:

- **One file runs everywhere.** The same `.wasm` works on Windows, macOS and
  Linux, so there's nothing to build per platform.
- **It's sandboxed.** A module can't touch your files or network, except for
  directories you explicitly give it.

```clojure
{:type :wasm
 :id   :enchantments
 :wasm {:module "plugins/enchantments.wasm"
        :args   ["enchant"]
        :dirs   {"/data" "./data"}}}
```

| Key       | What it does |
|-----------|--------------|
| `:module` | The `.wasm` file. Required. |
| `:args`   | Command-line arguments passed to the module. |
| `:dirs`   | Directories the module may see, as guest path → host path. Without this, it has no filesystem at all. |

The request and result are described in [External plugins](external.md).

## Building a module

Any language that compiles to WASI preview 1 (`wasip1`) works. A command module,
the kind with a `main` function, is what's needed. For example, with Go:

```sh
GOOS=wasip1 GOARCH=wasm go build -o loot.wasm loot.go
```

Rust (`--target wasm32-wasip1`), C via wasi-sdk, and Zig can all do the same.

## Errors

Exit with a non-zero status to report a failure; anything written to stderr is
shown to the DM as the error.

## Example

[`examples/wasm-plugin/loot.go`](../../examples/wasm-plugin/loot.go) is a
complete plugin with an editable variable, `loot/state`, and a "Sharpen"
action that reads the DM's edits back.
