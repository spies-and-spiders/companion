#!/usr/bin/env python3
"""Example sns-companion :cli loot plugin.

The engine passes the request context as JSON on stdin:
    {"inputs": {...}, "session": {...}}
and expects a friendly (un-namespaced) view-model as JSON on stdout:
    {"title", "subtitle", "sections": [{"heading", "items": [{"title","body","tags"}]}]}

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
    title, body, tags = random.choice(CONDITIONS)
    json.dump({
        "title": title,
        "subtitle": "Weather",
        "sections": [{"heading": "Conditions",
                      "items": [{"body": body, "tags": tags}]}],
    }, sys.stdout)


if __name__ == "__main__":
    main()
