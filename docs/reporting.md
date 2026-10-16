# Reporting to Discord

With reporting configured, every result gets a **Send to Discord** button that
posts it to a channel through a webhook. Players can see what they found without
you reading it out, and the channel doubles as a record of the campaign's loot.

```clojure
:reporting {:backend :discord
            :discord {:webhook-url #env DISCORD_WEBHOOK_URL
                      :username    "Loot Goblin"
                      :avatar-url  "https://example.com/goblin.png"}}
```

| Key                | What it does |
|--------------------|--------------|
| `:webhook-url`     | The webhook to post to. Required. |
| `:username`        | The name the message is posted under. |
| `:avatar-url`      | The image shown beside it. |
| `:include-secret?` | Send secret sections too. Off by default. |

To get a webhook URL, open the channel's settings in Discord, go to
**Integrations → Webhooks** and create one.

Anyone with the URL can post to your channel, so keep it out of files you share.
In an EDN config, `#env DISCORD_WEBHOOK_URL` reads it from an environment
variable instead. JSON configs don't support `#env`, so the URL has to be
written in. Alternatively, keep `:reporting` in a separate file and pull it in
with `#include "reporting.edn"`.

## What gets sent

The result is posted as it's shown on the card, edits included, with its two
[words](using-the-app.md#history) hidden behind a spoiler. Long results are split
over several messages.

Some things are left out:

- Sections marked **Secret**, either by the plugin or in the editor. Use these
  for DM-only notes, such as a cursed item's curse.
- Items whose text is empty, and sections with no items left.

With `:history :on-report`, sending a result is also what saves it to the
[history](history.md).
