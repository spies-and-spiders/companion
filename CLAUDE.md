This application is to be used by a DM during a D&D session. Its role is to generate loot items during the session based on pre-defined parameters and rules, all via a browser-based UI. 

There will be several different types of loot, with different mechanisms for generating them. Some loot types will be stateless, requiring only randomisation when generating them, and some will be stateful, requiring the loot generation to call or potentially modify a persistent state. Loot generation should be sufficiently modular such that new loot types can be added via plugins. The loot rules and types will be defined on the backend.

The application should be sufficiently configurable such that a new DM can come along and use it with a new set of loot types and rules. Not all DMs will want to use Clojure or even Java, so we will need to have built-in plugins for handling JSON as a plugin, and for handling CLI as a plugin. This will be a pre-compiled application given to players, so all plugin configuration/registration will need to be done via config, not via Clojure code. Optionally, this repository can also house a small library with Java interfaces and/or Clojure protocols that others can extend, in which case we'll need to be able to read external library JARs based on config.

Avoid excessively logging all decisions/removals
