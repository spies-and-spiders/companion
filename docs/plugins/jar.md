# JAR plugins

A JAR plugin is written in Clojure, Java, Kotlin or another JVM language, against
the Companion's SDK. It runs inside the app, reads [state](../state.md) directly,
and can extend the app's [randoms](../variables.md#randoms) in code.

It's the recommended way to write a plugin with real logic. JAR plugins only work
when the app is run as a JAR (`java -jar companion-<version>-standalone.jar`),
not with the native binary.

## The SDK

Add the SDK to your project. It's published to Maven Central as
[`tools.spies/sns-companion-sdk`](https://central.sonatype.com/artifact/tools.spies/sns-companion-sdk),
with the same version number as the app:

```clojure
;; deps.edn
{:deps {tools.spies/sns-companion-sdk {:mvn/version "<version>"}}}
```

```xml
<!-- Maven -->
<dependency>
  <groupId>tools.spies</groupId>
  <artifactId>sns-companion-sdk</artifactId>
  <version>VERSION</version>
</dependency>
```

The SDK is all a plugin needs; don't bundle the app itself into your JAR.

| Namespace / package | What's in it |
|---------------------|--------------|
| `sns.sdk.protocols` | The `Generator` and `Action` protocols a Clojure plugin implements. |
| `sns.sdk` (Java)    | The same as Java interfaces, plus the `Models` records for results. |
| `sns.sdk.schema`    | [malli](https://github.com/metosin/malli) schemas for everything a plugin sends or receives. Handy in tests. |
| `sns.sdk.vars`      | Resolving and re-drawing [variables](../variables.md). |
| `sns.sdk.randoms`   | Adding your own random lists. |
| `sns.sdk.rank`      | Working with [ranks](../ranks.md). |

## A Clojure plugin

Write a function that takes the tool's config entry and returns a `Generator`:

```clojure
(ns my.plugin
  (:require [sns.sdk.protocols :as p]))

(defn generator [_tool-config]
  (reify p/Generator
    (loot-spec [_] {})
    (generate [_ _ctx]
      {:loot/title "Hello from a JAR"})))
```

Build it into a JAR and point the config at the JAR and the function:

```clojure
{:type :jar
 :id   :hello
 :jar  {:path "plugins/my-plugin.jar" :entrypoint my.plugin/generator}}
```

`loot-spec` returns the plugin's own [settings](../tools.md#plugin-settings),
such as its `:inputs` or `:history`, or `{}` for none. Anything under
`:generator` in the config overrides them.

`generate` returns a [result](../results.md). Its `ctx` holds:

| Key       | What it is |
|-----------|------------|
| `:inputs` | The [form](../inputs.md) values, already converted to their types. |
| `:store`  | For reading [state](#state). |
| `:rng`    | The app's random number generator, a [randy](https://github.com/dekelpilli/randy) RNG. Use it rather than your own so draws are consistent with the rest of the app. |
| `:config` | The whole app config. |

## A Java plugin

Implement `sns.sdk.Generator` on a class with a no-argument constructor, and name
the class instead of an entrypoint:

```java
package my.plugin;

import java.util.Map;
import sns.sdk.Generator;
import sns.sdk.Models;

public class HelloLoot implements Generator {
    @Override
    public Models.ViewModel generate(Map<String, Object> ctx) {
        return new Models.ViewModel("Hello from Java");
    }
}
```

```clojure
{:type :jar :id :hello :jar {:path "plugins/hello.jar" :class "my.plugin.HelloLoot"}}
```

`ctx` has the same entries as above, keyed by strings. Override `lootSpec()` to
declare settings. The `Models` records (`ViewModel`, `Section`, `Item`,
`ItemVar`, `Action`, `Spec`, `Field`, `ManualState`) cover everything a result
can hold; null means "not set".

## Actions

Implement `Action` alongside `Generator` to handle [action buttons](../actions.md):

```clojure
(reify
  p/Generator
  (loot-spec [_] {})
  (generate [_ ctx]
    {:loot/title    "Whetstone Blade"
     :loot/sections [{:section/items [{:item/body "+{{ keen }} keener."
                                       :item/vars {:keen {:value 0}}}]}]
     :loot/actions  [{:action/label "Sharpen"
                      :action/event [:loot/action {:action :sharpen :params {:by 1}}]}]})
  p/Action
  (handle-action [this ctx action {:keys [by]}]
    (case action
      :sharpen (update-in (:view-model ctx)
                          [:loot/sections 0 :section/items 0 :item/vars :keen :value]
                          + by))))
```

`handle-action` gets the action name as a keyword, its params, and a `ctx` with
two extra keys: `:view-model`, the result as currently shown (with the DM's
edits), and `:inputs`, the values of the action's own fields. Build the new
result from `:view-model`.

In Java, implement `sns.sdk.Action` and add `Models.Action(label, action,
params)` entries to the result; the view-model arrives as
`ctx.get("view-model")`.

## State

Read a collection through the store:

```clojure
(p/read-collection (:store ctx) :relics)
;; => {"r1" {:name "Sunblade" :ranks {:ab 2}}}
```

Write by adding `:store/mutations` to the result, as described in
[State](../state.md#reading-and-writing-from-a-plugin):

```clojure
(assoc result :store/mutations {:relics {"r1" {:name "Sunblade" :ranks {:ab 3}}}})
```

The plugin reads the collection named after its id unless it declares others with
`:store/collections` in its loot-spec (or the config). Under the `:browser`
storage backend, collections it hasn't declared read as empty.

## Variables and randoms

Variables in a result can use the [shorthand forms](../variables.md#shorthand),
and the app resolves them. To resolve them yourself, for instance to read a drawn
value straight away:

```clojure
(require '[sns.sdk.vars :as vars])

(vars/resolve-vars rng {:x {:random :damage-types}})
;; => {:x {:value "fire" :random :damage-types :options [...]}}
```

`vars/redraw-distinct` draws a variable again, guaranteeing a different value,
which suits a "Re-roll" action:

```clojure
(vars/redraw-distinct rng item-vars :x)
```

A plugin can add its own random lists, including ones that take arguments from
the variable's declaration:

```clojure
(require '[sns.sdk.randoms :as randoms])

(defmethod randoms/preset :monster-types [_ _]
  ["aberration" "beast" "celestial" "construct"])

;; {:random :defences :type "non-armour"}
(defmethod randoms/preset :defences [_ {:keys [type]}]
  (cond-> ["Fortitude" "Reflexes" "Will"]
    (not= "non-armour" type) (conj "Armour")))
```

If a list in the config's `:randoms` has the same name, the config wins.

## Errors

Throw to report a failure; the exception's message becomes the error's title. To
control what's shown, put a result in the exception's data:

```clojure
(throw (ex-info "No relics left"
                {:view-model {:loot/title "No relics left"
                              :loot/subtitle "Every relic has been found"}}))
```

## Loading

JARs are loaded when the app starts. Plugins share one class loader, so several
tools can point at the same JAR, and JARs can use each other's classes.

The built-in plugins in [`src/sns/builtin/`](../../src/sns/builtin/) are written
the same way, and make good examples; `relics.clj` shows state, ranks and actions
together.
