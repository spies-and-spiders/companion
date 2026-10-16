# Result history

Each plugin keeps a list of past results under its card, so you can bring back
something you generated earlier in the session. The `:history` setting decides
which results go into it:

| `:history`   | A result is saved… |
|--------------|--------------------|
| `:button`    | only when you press **Save to history**. This is the default. |
| `:always`    | every time it's generated, rolled or changed by an action. If you edit a result by hand, a **Save to history** button appears, since only you know when you've finished editing. |
| `:on-report` | when it's [sent to Discord](reporting.md). |
| `:never`     | never. |

Set it for every plugin at once, and override it for individual plugins under
`:generator`:

```clojure
{:history :always
 :tools   [{:type :data :id :tarot :data {:source "data/tarot.edn"}
            :generator {:history :never}}]}
```

A data plugin can also set `:history` in its spec file.

Failed results are never saved.

## Where it's kept

The history is stored like the rest of the app's [state](state.md), in a
collection called `history`, so it's saved and exported along with everything
else. Don't give a plugin's own collection that name.

Separately, the app remembers each plugin's last result for as long as the
browser tab is open, so switching between pages doesn't lose what was on screen.
