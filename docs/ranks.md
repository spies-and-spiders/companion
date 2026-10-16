# Ranks

Some loot gets stronger over time: a relic that starts at +1 and can be upgraded
to +4. Rather than storing the current number and working out the next one
yourself, a numeric [variable](variables.md) can carry a **rank**.

```clojure
{:ab    {:value 1 :rank 3}               ; 1, 2, 3, … → shows 3
 :range {:value 30 :step 15 :rank 2}}    ; 30, 45, 60, … → shows 45
```

| Key      | What it does |
|----------|--------------|
| `:rank`  | How many ranks the variable has. Starts at 1, which is the plain `:value`. |
| `:step`  | How much each rank above 1 adds. Defaults to the value itself. |
| `:max`   | The highest rank it can reach. No limit by default. |

The template still just says `{{ ab }}`, and gets the value for the current rank.
The editor shows the base value and the rank side by side, and the card shows
what they add up to. Because the base value never changes, a rank can be taken
back just by lowering it.

Only numbers can rank. A variable holding text, or one marked `:context?`, never
does.

## Text that unlocks at a rank

Base the condition on the number itself, rather than adding a second variable
that could get out of step with it:

```clojure
"+{{ab}} AB.{{#gte ab 3}} Deals 6 fire damage on hit.{{/gte}}"
```

An item whose whole text depends on a rank disappears from the card until it's
reached (it stays in the editor).

## One rank, several numbers

Templates can do arithmetic, so a rank worth "+1 AB and +3 fire damage" is one
variable:

```clojure
"+{{multiply rank 3}} fire damage, +{{rank}} AB"    ; at rank 3: +9 and +3
```

[`{{die-size n}}`](templates.md#extra-helpers) does the same for damage dice.

## Ranking up from a plugin

The ranks themselves are just data, so any plugin can set `:rank` on a variable.
JAR plugins also get helpers from the SDK's `sns.sdk.rank` namespace, for the
common case of a mod with a total number of upgrades shared across its
variables:

```clojure
(require '[sns.sdk.rank :as rank])

(def vars {:ab {:value 1} :range {:value 30 :step 15}})

(rank/available vars 4)   ; => [:ab :range], the variables that can still go up,
                          ;    or nil once 4 upgrades have been spent
(rank/rank-up vars :ab)   ; => vars with :ab one rank higher
(rank/mod-rank vars)      ; => 1, plus the upgrades spent so far
(rank/stepped {:value 1 :step 2 :rank 3})   ; => 5
```

The built-in [relics plugin](plugins/builtin.md#relics) is a full example, using
an [action](actions.md) per variable that can still be upgraded.
