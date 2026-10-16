# Development

This page is for working on the Companion itself. To write plugins, you only need
the [plugin docs](README.md#plugin-types).

## Layout

| Path        | What's there |
|-------------|--------------|
| `src/sns/server/` | The backend: config loading, the generation engine, storage, reporting and the HTTP API. |
| `src/sns/builtin/` | The built-in plugins, and the adapters for data, CLI, FFI and WASM plugins. |
| `src/sns/ui/` | The browser UI, in ClojureScript ([Replicant](https://github.com/cjohansen/replicant) and [Nexus](https://github.com/cjohansen/nexus)). |
| `sdk/` | The plugin SDK, published separately to Maven Central. |
| `examples/` | An annotated config and example external plugins. |
| `data/` | Example data plugins used by the example config. |

## Prerequisites

- The [Clojure CLI](https://clojure.org/guides/install_clojure) and a JDK (22 or
  newer).
- Node.js, for building the UI.
- [GraalVM](https://www.graalvm.org) with `native-image`, only for building the
  native binary.

Run `make deps` once to install the npm packages.

## Common tasks

`make` on its own lists every target. The ones you'll use most:

| Command        | What it does |
|----------------|--------------|
| `make run`     | Runs the backend, serving the API and UI on port 8080. |
| `make watch`   | Rebuilds the UI on change, alongside `make run`. |
| `make repl`    | Starts a backend REPL. |
| `make check`   | Runs the Clojure and ClojureScript tests, and fails if anything uses reflection. |
| `make uber`    | Builds `target/companion.jar`. |
| `make graalvm` | Builds the native binary. |
| `make schemas` | Writes `schemas.json` and the two plugin JSON schemas. |
| `make sdk-jar` | Builds the SDK JAR. |

The SDK's Java interfaces have to be compiled before the app can load; `make
prep` does this, and the targets above run it for you. If you change a `.java`
file under `sdk/` and the change doesn't seem to take, delete `sdk/target` and
prep again.

## How a request flows

1. The UI posts to `/api/generate` with a tool id and its form inputs.
2. The engine fills in input defaults and calls the plugin's `generate`.
3. The result's [variables](variables.md) are resolved (randoms drawn), the
   result is validated against `sns.sdk.schema/view-model`, and only then are its
   `:store/mutations` applied.
4. The UI renders the result's [templates](templates.md) in the browser.

Actions take the same path through `/api/action`, with the on-screen result
attached. Under the `:browser` storage backend, the UI sends the relevant
collections with each request and applies the returned mutations to IndexedDB
itself.

## The SDK

`sdk/` is the only part of the repository with a stable contract, since external
JARs compile against it. Everything plugins see (protocols, Java interfaces,
schemas, vars, randoms and ranks) lives there, and the app depends on it like a
plugin would.

## Releases

Pushing a tag runs the release workflow, which builds the native binaries for
each platform, the standalone JAR and the schemas, and attaches them to a GitHub
release. If anything under `sdk/` has changed since the previous release, the
SDK is also published to Maven Central under the same version.
