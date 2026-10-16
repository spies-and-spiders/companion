# Pages

Every plugin gets a page of its own on the rail. When you use a few tools
together, such as something rolled every combat round alongside something rolled
on a single character's turn, put them on a **page** and they'll sit side by
side, each with its own form and result.

```clojure
{:tools [{:type :data :id :lair-actions :data {:source "data/lair.edn"}
          :generator {:hidden? true}}
         {:type :cli  :id :weather :cli {:command ["python3" "weather.py"]}}
         {:type :page :id :combat :label "Combat round" :section "Combat"
          :page {:tools [:lair-actions :weather]}}]}
```

A page is a tool like any other, so it takes `:id`, `:label` and `:section`, and
appears on the rail where you list it. Its `:page {:tools [...]}` lists plugin
ids, in the order they should be shown.

- A page can list plugins, but not other pages.
- A plugin can be on several pages. Its inputs, result and history are the same
  wherever it's shown.
- Plugins on a page still have their own page too. Make a plugin
  [hidden](tools.md#hidden-tools) to show it only on its pages.
- Pages can't go on the loot table.

## Loot rolls and pages

When a [loot roll](loot-table.md) produces a result, the app shows it on the
page you're already on if that page holds the rolled tool. Otherwise it switches
to the tool's own page. A roll that lands on several tools at once goes to the
first page holding all of them, or to a temporary page of its own if none does.
