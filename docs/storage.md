# Storage backends

Stateful loot reads and writes through the `Store` SPI. The backend is chosen in
`resources/config.edn` under `:storage` and is fully swappable — nothing else in
the app changes when you switch.

| `:backend` | What it is                                   | Needs a process? | Durable? |
|------------|----------------------------------------------|------------------|----------|
| `:memory`  | Atom-backed; the default when none is set    | no               | no       |
| `:file`    | One transit-encoded file per loot-type, `:dir` | no             | yes      |
| `:mysql`   | Any MySQL-compatible SQL server, over JDBC   | yes              | yes      |

```clojure
;; resources/config.edn
:storage {:backend :file  :dir "./state"}                      ; or
:storage {:backend :mysql :url "jdbc:mariadb://localhost:3306/sns"}
```

## `:mysql` — any MySQL-compatible server

The app talks only over the MySQL wire protocol via JDBC (using the MariaDB
driver), so the server is interchangeable: **Dolt**, MySQL, MariaDB, etc. all
work. Point `:url` at your server; it must contain the database named in the URL
(here, `sns`). On first connect the app creates a single
`documents(collection, id, doc JSON)` table if it doesn't exist.

### Quickstart with Dolt (recommended — git-versioned, Apache 2.0)

[Dolt](https://github.com/dolthub/dolt) is a MySQL-compatible database with
git-style versioning, which is handy for inspecting/rolling back session state.
With Dolt installed, in a separate terminal:

```bash
mkdir -p /tmp/sns-dolt && cd /tmp/sns-dolt && dolt init
dolt sql -q "CREATE DATABASE sns;"
dolt sql-server          # serves the MySQL protocol on :3306
```

Then set `:storage {:backend :mysql :url "jdbc:mariadb://localhost:3306/sns"}`
and start the app. Any other MySQL/MariaDB server works the same way — just
create the `sns` database and point `:url` at it.
