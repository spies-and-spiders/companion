# The loot table

The loot table decides *what kind* of loot drops. You roll a die (a d100 unless
you say otherwise), and the number picks one or more of your tools to generate.

There are two ways to write one: with **weights**, where the app works out the
numbers for you, or with **ranges**, where you assign the numbers yourself.

## Weights

```clojure
:loot-table [{:id :divine-dust :weight 40}
             {:id :uniques     :weight 25}
             {:id :rings       :weight 20}
             {:id :relics      :weight 15}]
```

Each entry names a tool by its `:id`. Weights are relative, so an entry with
weight 40 comes up twice as often as one with weight 20. Leave `:weight` out and
it counts as 1, which makes a table with no weights at all uniform.

The app spreads the entries over the die in order: here 1–40 is divine dust,
41–65 uniques, and so on. The **Loot table** page shows the exact numbers.

## Ranges

When you're copying a table out of a book, or want some numbers to give more
than one thing, assign the numbers yourself:

```clojure
:loot-table [{:id :uniques :ranges [[1 60] [99 100]]}
             {:id :relics  :ranges [[61 98]]}
             {:id :rings   :ranges [[95 100]]}]
```

Each range is an inclusive `[from to]` pair, and an entry can have as many as it
likes. A roll of 95–98 above gives a relic *and* rings, since both cover those
numbers.

The rules:

- Once one entry uses `:ranges`, every entry has to. An entry can't have both
  `:weight` and `:ranges`.
- Every number on the die must belong to at least one entry.

## Die size

```clojure
:loot-die-size 20
```

Changes the die from a d100. With weights, the table can't have more entries than
the die has sides.

## Rolling

Open **Loot table** at the top of the rail. Type in the number you rolled, or
press **Roll d100**; the entries it lands on are highlighted. **Generate** (or
Enter) produces them. Pressing Enter with the box empty lets the app roll for
you.

Any tool can go on the loot table, including [hidden](tools.md#hidden-tools)
ones. [Pages](pages.md) can't.
