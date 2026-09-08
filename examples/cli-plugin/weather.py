#!/usr/bin/env python3
"""Example sns-companion :cli loot plugin.

The engine passes the request context as JSON on stdin:
    {"inputs": {...}}
and expects a view-model as JSON on stdout — the same one a :jar plugin returns:
    {"loot/title", "loot/subtitle",
     "loot/sections": [{"section/heading", "section/items": [
       {"item/title", "item/body", "item/metadata"}]}]}

Register it in config.edn with:
    {:type :cli :id :weather :label "Weather"
     :command ["python3" "examples/cli-plugin/weather.py"]}
"""
import json
import random
import sys

CONDITIONS = [
    ("Fogfall", "A thick fog rolls in; sight beyond 30 feet is obscured.", ["obscured"]),
    ("Bloodrain", "A warm red drizzle falls. Beasts grow restless.", ["omen"]),
    ("Stillair", "The wind dies completely. Sound carries unnaturally far.", ["quiet"]),
    ("Emberfall", "Cinders drift from a distant fire on the horizon.", ["fire"]),
]


def main():
    _ctx = json.load(sys.stdin) if not sys.stdin.isatty() else {}
    title, body, metadata = random.choice(CONDITIONS)
    json.dump({
        "loot/title": title,
        "loot/subtitle": "Weather",
        "loot/sections": [{"section/heading": "Conditions",
                           "section/items": [{"item/body": body,
                                              "item/metadata": metadata}]}],
    }, sys.stdout)


if __name__ == "__main__":
    main()
