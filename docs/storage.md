# Storage

Stateful plugins read and write through the `Store` protocol. State is a set of
named **collections**, each a map of key to value; a collection needs no
declaration and reads as `{}` until something is written to it. The backend is
chosen in `config.edn` under `:storage`.

| `:backend` | Where state lives                          | Hand-editable | Survives a restart |
|------------|--------------------------------------------|---------------|--------------------|
| `:memory`  | An atom; the default when unset            | no            | no                 |
| `:file`    | One EDN file per collection under `:dir`   | yes, live     | yes                |
| `:browser` | IndexedDB, one object store per collection | via export    | yes, per device    |

```clojure
;; config.edn
:storage {:backend :file :dir "./state"}   ; ./state/relics.edn, ./state/social.edn, …
:storage {:backend :memory}
:storage {:backend :browser}
```

## Who can use it

In-process plugins only — `:builtin` and `:jar`. `:cli` and `:ffi` plugins
exchange JSON over a process/ABI boundary and persist their own state.

## Reading and writing

Two methods, both taking and returning plain data:

```clojure
(p/read-collection store :relics)
;; => {"r1" {:name "Sunblade" :base "longsword" :path [{:id :sharp}]}}

(p/mutate! store {:relics {"r1" {:name "Sunblade" :path [{:id :sharp} {:id :flaming}]}
                           "r9" nil}})   ; nil retracts that key
```

A mutation merges: keys you don't mention are untouched, and one call can span
several collections. Values are ordinary Clojure data — vectors keep their
order, keywords stay keywords, and nothing needs a schema.

## Declaring collections

A loot type's `:store/collections` says which collections it uses. It defaults
to a single collection named after the plugin's `:id`, so most plugins declare
nothing:

```clojure
{:id :relics :label "Relic"}                              ; uses :relics
{:id :souls :label "Soul" :store/collections [:souls :social]}
```

Under `:browser` this is load-bearing: the client ships exactly these
collections with each request and applies the returned writes, so a collection
you read without declaring will be empty. Declaring several is also how a plugin
relates its own state to another's.

The exception is a loot-table roll, where the type is chosen server-side and the
client cannot know which declaration applies — it ships every declared
collection instead.

## The `:file` backend

The files are the source of truth. An edit made by hand while the app is running
is picked up by the next read — each file's parsed value is cached and re-read
only when its modification time or size changes. Writes re-read first, so a
write never resurrects state from before an external edit, and each file is
written to a temporary name and renamed into place so an interrupted write
cannot leave a half-written file behind.

```clojure
;; state/relics.edn
{"r1" {:name "Sunblade"
       :base "longsword"
       :path [{:id :sharp} {:id :flaming}]}}
```

Two processes writing the same directory are not coordinated; one app, one
directory.

## The `:browser` backend

State lives in the DM's IndexedDB and the server holds nothing. The client sends
the collections a plugin declared with each request; the plugin runs on the
server as usual, and its writes come back on the response under
`:store/mutations` for the client to apply.

Plugins are unaffected by this — the same `read-collection` and `mutate!` calls
work either way.

The consequence to plan around is that state is **device-local**: a laptop and a
tablet are two separate databases, and clearing site data destroys it. Use the
export button to download a ZIP of every collection; unzip it into a directory
and point a local deployment at it with `{:backend :file :dir "…"}` to carry on
from there. There is no import back into the browser.

The button builds the archive wherever the state is — in the page for
`:browser`, and from `GET /api/export` for `:file` and `:memory` — but both
write the same thing: one pretty-printed `<collection>.edn` per collection, the
same files the `:file` backend keeps.

Because manual state — a character table, a soul ledger — is data the DM typed
rather than data the app generated, `:file` is the safer default for anything
long-lived.
