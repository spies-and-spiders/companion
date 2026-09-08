#!/usr/bin/env python3
"""A stateful `:cli` plugin: a tally the DM keeps, incremented by the plugin.

Shows both directions of the state contract. The engine reads the collections
declared on the config entry and sends them as `state`; the `mutations` sent
back are applied on its side, so this script never touches a file and never
learns which storage backend is configured.

    {"type": "cli", "id": "tally",
     "command": ["python3", "examples/cli-plugin/tally.py"],
     "inputs": [{"id": "who", "label": "Who", "type": "text"}],
     "store/manual": {"key-label": "Name",
                      "fields": [{"id": "count", "label": "Count",
                                  "type": "int", "default": 0}]}}
"""
import json
import sys

request = json.load(sys.stdin)
tally = (request.get("state") or {}).get("tally") or {}
who = (request.get("inputs") or {}).get("who") or "someone"
count = (tally.get(who) or {}).get("count", 0) + 1

rows = sorted({**tally, who: {"count": count}}.items())
print(json.dumps({
    "loot/title": f"{who} × {count}",
    "loot/sections": [{"section/heading": "Tally",
                       "section/items": [{"item/title": name,
                                          "item/body": str(row.get("count", 0))}
                                         for name, row in rows]}],
    # Declared, not written: the engine applies this once the output validates.
    "store/mutations": {"tally": {who: {"count": count}}},
}))
