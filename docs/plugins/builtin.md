# Built-in plugins

A few plugins ship with the app. Add one by its id:

```clojure
{:type :builtin :id :social :label "Group Social" :section "Utilities"}
```

Like any tool, it takes a `:label`, `:section` and [`:generator`](../tools.md#plugin-settings)
settings.

## `:social`

A group Deception and Persuasion tracker. It keeps a
[table](../state.md#dm-maintained-tables) of characters, with each one's
Deception and Persuasion bonus and whether they're present this session. The
result shows the group's bonus for each skill, and **Roll Deception** and
**Roll Persuasion** buttons that roll a d20 plus that bonus.

The group bonus is the average of the present characters' bonuses, with the two
highest counted twice.

## `:relics`

Relics that are saved when generated and upgraded later. Each relic has an effect
with [ranks](../ranks.md), and the result has an **Upgrade** button for each part
of the effect that can still improve. It's a small worked example of
[state](../state.md) and [actions](../actions.md) more than a usable loot table:
there are only two relics.

## `:divine-dust`

Always returns "Divine Dust". It's there for trying the app out.
