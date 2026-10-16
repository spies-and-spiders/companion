# FFI plugins

An FFI plugin is a function in a shared library (`.so`, `.dylib` or `.dll`) that
the app loads and calls directly. It takes the request as a JSON string and
returns the result as one:

```c
char *generate(const char *request_json);
```

```clojure
{:type :ffi
 :id   :ffi-loot
 :ffi  {:library     "plugins/libloot.dylib"
        :symbol      "generate"
        :free-symbol "loot_free"}}
```

| Key            | What it does |
|----------------|--------------|
| `:library`     | The shared library. Required. |
| `:symbol`      | The function to call. Required. |
| `:free-symbol` | A `void free_fn(char *)` the app calls on the returned string once it has read it. Without one, the library keeps ownership of the memory. |

The request and result are described in [External plugins](external.md).

## When to use it

Unlike CLI plugins, the library stays loaded for as long as the app runs, so it
can keep things in memory between calls and there's no program to start on each
click. The costs:

- You need a separate build for each platform you want to run on.
- A crash in the library takes the whole app down with it.
- The library runs with the same access to your machine as the app.

If you don't need what FFI offers, a [WASM plugin](wasm.md) is usually the
better choice when running the native binary.

## Building

Any language that can export a C function works. Each file in
[`examples/ffi-plugin/`](../../examples/ffi-plugin/) has its build command at the
top:

```sh
cc -shared -fPIC -o libloot.dylib loot.c                         # C
go build -buildmode=c-shared -o libloot.dylib loot.go             # Go
rustc --crate-type cdylib -o libloot.dylib loot.rs                # Rust
```

Use `.so` on Linux and `.dll` on Windows.

## Errors

There's no stderr channel, so report a failure by returning a result with
`"loot/error?": true`.
