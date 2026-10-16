# Using the app

![The S&S Companion](images/companion.png)

## The rail

The column on the left lists every tool in your config, grouped under section
headings. Tools without a section sit under **Loot** at the top; the rest follow
in alphabetical order of section. Type into **Search tools…** to narrow the
list.

Each tool gets a page of its own, and [pages](pages.md) you configure show
several tools together. The page you're on is in the URL, so you can bookmark it
or use the browser's back button.

Some tools are [hidden](tools.md#hidden-tools): they only turn up from a loot
roll and appear on the rail while they're on screen.

## Generating loot

A tool's page has a form (if the tool asks for [inputs](inputs.md)) and a
generate button. The result appears as a card underneath, and the button
generates a fresh one each time.

A tool backed by a data file also gets a **Reload data before generating?**
checkbox, so you can edit the file mid-session and pick up the change without
restarting.

If a tool fails, the error appears over the card. Click it away and the previous
result is still there.

## The loot table

If your config has a [loot table](loot-table.md), **Loot table** at the top of
the rail opens it. It lists what each number on the die gives. Type in a roll
from a real die, or press **Roll d100**, and the matching entries light up; press
**Generate** (or Enter) to produce them. Pressing Enter with the box empty has
the app roll for you.

## Editing a result

Generated text often has values in it, such as a bonus, a damage type or a
creature. Those are [variables](variables.md), and **Edit item** turns the card
into a form where you can change them. Where a variable was drawn from a list,
you get a dropdown of the other options. The text around a value updates as you
type, so you never have to rewrite a sentence to change one number.

The editor also lets you change the title, subtitle and text of every item, add
or remove items and metadata, and mark a section **Secret** so it isn't sent to
Discord. **Done editing** returns to the card.

## Actions

Some results come with buttons of their own, such as **Level up** on a relic or
**Roll Deception** on the social tracker. These are [actions](actions.md). They
work on the result as it's currently shown, so any edits you've made carry
through. An action can have its own small form beside its button.

## DM-maintained tables

Some tools keep a table that you fill in yourself, like a list of characters and
their skill bonuses. It's shown above the tool's form: type a name into the
**Add…** box to add a row, and edit the fields in place. These tables are
saved with the rest of the app's [state](state.md).

## History

Results are kept in a list under the card. Click an entry to bring it back, hover
over one to preview it (hold Alt to keep the preview open), `✕` to drop it, or
**Clear** to empty the list. Whether results are saved automatically or only with
**Save to history** depends on the [history setting](history.md).

Every result is tagged with two random words, shown behind the **W** badge in
the corner of the card. They give each result a name you can refer to, for
example to match a history entry to a Discord message.

## Sending to Discord

With [reporting](reporting.md) configured, each result has a **Send to Discord**
button.

## Downloading state

**Download state** at the bottom of the rail saves everything the app has stored
(saved items, your tables and the history) as a ZIP of EDN files. See
[State](state.md#exporting).
