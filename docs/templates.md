# Templates

The text in a [result](results.md) is a [Handlebars](https://handlebarsjs.com)
template. Instead of writing out finished prose, a plugin writes the sentence
with placeholders and supplies the values separately as
[variables](variables.md):

```clojure
{:item/body "+{{ damage }} {{ type }} damage on hit."
 :item/vars {:damage {:value 2} :type {:value "fire"}}}
```

The browser fills in the placeholders, and refills them whenever a value
changes. That's what lets the DM change "fire" to "cold" in the editor without
retyping the sentence, and lets a plugin read the value back later without
having to pick it out of the text.

If there's nothing to fill in, just write the text. A template with no
placeholders is shown as it is.

## What you can write

Templates are plain [Handlebars](https://handlebarsjs.com/guide/), with most of
the [handlebars-helpers](https://github.com/helpers/handlebars-helpers) library
available (its array, collection, comparison, inflection, math, misc, number,
object, regex and string helpers). One thing to watch for: index a list with
`{{ skills.[0] }}`, since `{{ skills.0 }}` doesn't parse.

## Extra helpers

| Template | Gives |
|----------|-------|
| `{{die-size n}}` | The `n`th step up a die ladder: 1 → 1d4, 2 → 1d6 … 5 → 1d12, 6 → 3d4, 7 → 1d6+2d4, and so on. Handy for damage that grows with [rank](ranks.md). |
| `{{sns-spell "Earth Tremor"}}` | A link to the spell on spies.tools. Hovering it in the UI shows the spell's description. |
| `{{sns-maneuver "…"}}`, `{{sns-condition "…"}}` | The same for maneuvers and conditions. |

The `sns-` helpers take an optional second argument for the link text:
`{{sns-spell "Earth Tremor" "a tremor"}}`.

## Links

Markdown-style links, `[text](https://…)`, are shown as clickable links on the
card and stay links when [sent to Discord](reporting.md).

## Mistakes

A template that Handlebars can't parse, such as one with an unclosed `{{#if}}`,
is shown as raw text rather than causing an error, so you'll see the problem on
the card.
