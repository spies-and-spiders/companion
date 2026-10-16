# CLI plugins

A CLI plugin is any program that reads a JSON request on stdin and prints a JSON
result on stdout. It's the easiest kind of plugin to write: use whatever language
you're comfortable with, and test it by running it in a terminal.

```clojure
{:type :cli
 :id   :weather
 :label "Weather"
 :cli  {:command ["python3" "plugins/weather.py"]}}
```

`:command` is the program and its arguments, as a list. It's run from the
directory the app was started in, once per generate or action.

The request and result are described in [External plugins](external.md). Here's
a complete plugin:

```python
#!/usr/bin/env python3
import json, random, sys

request = json.load(sys.stdin)
title, body = random.choice([
    ("Fogfall", "A thick fog rolls in; sight beyond 30 feet is obscured."),
    ("Stillair", "The wind dies completely. Sound carries unnaturally far."),
])
json.dump({"loot/title": title,
           "loot/subtitle": "Weather",
           "loot/sections": [{"section/items": [{"item/body": body}]}]},
          sys.stdout)
```

## Trying it by hand

```sh
echo '{"inputs": {}}' | python3 plugins/weather.py
```

## Errors

Exit with a non-zero status to report a failure. Whatever the program wrote to
stderr is shown to the DM as the error, so make it readable.

## Things to know

- Each request starts the program afresh, so anything it remembers between runs
  has to go through [state](external.md#state).
- The program has the same access to your machine as the app does. Only run
  plugins you trust; [WASM plugins](wasm.md) are the sandboxed alternative
  when running the native binary.
- Startup time adds to every click. Interpreted languages are fine for this,
  but avoid anything that takes seconds to start.

See [`examples/cli-plugin/`](../../examples/cli-plugin/) for runnable examples.
