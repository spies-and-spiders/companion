# State

Some loot needs to remember things: a relic that's upgraded over several
sessions, a tally that goes up each time, a table of characters the DM keeps up
to date. That's the app's **state**.

State is a set of named **collections**. Each collection is a map from a key to
a value, like `"Vex" → {:deception 7 :persuasion 3}`. A collection doesn't need
to be created; it's simply empty until something is written to it.

## Storage backends

Where state is kept is set with `:storage`:

```clojure
:storage {:backend :file :file {:dir "./state"}}
```

| `:backend` | Where state lives | Survives a restart | Edit by hand |
|------------|-------------------|--------------------|--------------|
| `:memory`  | In the app. The default. | no | no |
| `:file`    | One EDN file per collection, in `:dir` (default `./state`). | yes | yes, even while the app is running |
| `:browser` | The DM's browser (IndexedDB). | yes, on that device | via export |

For anything you'll use across sessions, `:file` is the safest choice.

### `:file`

Each collection is a file named after it, such as `state/relics.edn`. The files
are the source of truth: edit one by hand while the app is running, and the next
read picks up the change. Writes go to a temporary file first and are then moved
into place, so a crash mid-write can't leave a file half-written.

Only run one copy of the app against a state directory at a time.

### `:browser`

The server stores nothing, and state lives in the browser that's using the app.
The drawback is that it's tied to one browser on one device, and clearing the
site's data deletes it. Download your state regularly.

## Exporting

**Download state** at the bottom of the rail saves every collection as a ZIP of
EDN files, one per collection: the same files the `:file` backend uses. To move
from `:browser` or `:memory` to `:file`, unzip the download into a directory and
point `:storage` at it. There's no way to import a ZIP back into the browser.

## DM-maintained tables

A plugin can declare a table for the DM to fill in, such as a roster of
characters. The UI shows an editor for it above the plugin's form, and the
plugin reads it like any other collection.

```clojure
{:type      :cli
 :id        :social
 :cli       {:command ["./social"]}
 :generator {:store/manual {:key-label "Character"
                            :fields    [{:id :deception  :label "Deception"  :type :decimal :default 0}
                                        {:id :persuasion :label "Persuasion" :type :decimal :default 0}
                                        {:id :present?   :label "Present"    :type :bool    :default true}]}}}
```

Each row is a key (here, a character's name) and `:fields` are its columns.
Fields are declared like [form inputs](inputs.md), and values are converted to
their type when saved, with a blank field falling back to its `:default`.
`:key-label` names the key column.

With `:list? true`, each key holds a list of records instead of one, for
something like a character who owns several items:

```clojure
:store/manual {:key-label "Character" :list? true
               :fields    [{:id :soul :label "Soul" :type :text}
                           {:id :proc-chance :label "Proc chance" :type :decimal :default 0}]}
```

The table is stored in the plugin's first collection (by default, the one named
after its id), so with the `:file` backend the `:social` table above is
`state/social.edn`:

```clojure
{"Vex"   {:deception 7M :persuasion 3M :present? true}
 "Ilrec" {:deception 1M :persuasion -2M :present? false}}
```

## Which collections a plugin uses

A plugin uses one collection named after its id unless it says otherwise. To use
different or additional collections, such as reading another plugin's table,
list them under `:generator`:

```clojure
:generator {:store/collections [:souls :social]}
```

Under the `:browser` backend this matters: only the declared collections are
sent with a request, so a plugin reading a collection it didn't declare sees
nothing. `history` is reserved for the [result history](history.md).

## Reading and writing from a plugin

How a plugin reads state depends on its type. [JAR](plugins/jar.md#state) and
built-in plugins read collections directly; [external plugins](plugins/external.md#state)
are sent the collections they declared along with each request.

Writing works the same way for all of them: the plugin doesn't write anything
itself, but lists the changes it wants in its result under `:store/mutations`:

```clojure
{:loot/title      "Sunblade"
 :store/mutations {:relics {"r1" {:name "Sunblade" :ranks {:ab 2}}
                            "r9" nil}}}
```

That's `{collection {key value}}`. Each key given is replaced, `nil` deletes a
key, and keys not mentioned are left alone. One result can change several
collections.

The app only applies the changes once it has checked that the rest of the result
is valid. If a plugin fails or returns something broken, nothing is written, so
an error at the table never leaves your state half-updated.
