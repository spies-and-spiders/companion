package sns.sdk;

/**
 * Persistence for stateful plugins. State is a set of named collections, each a
 * map of key to value; a collection needs no declaration and reads as an empty
 * map until something is written to it. Available to in-process plugins only;
 * CLI and FFI plugins persist their own state. Mirrors the {@code
 * sns.sdk.protocols/Store} protocol.
 *
 * <p>Both methods take and return plain data, so the same calls work against a
 * local store or one whose state lives in the DM's browser. Declare the
 * collections you use with {@code :store/collections} in your loot-spec; it
 * defaults to a single collection named after the plugin's {@code :id}.
 */
public interface Store {

    /**
     * Prepare the backend for use. Called once at startup, before any other
     * method; construction itself must stay side-effect-free. Default is a no-op.
     */
    default void setup() {}

    /**
     * Return the whole collection {@code coll} (a keyword) as a map, or an empty
     * map when absent.
     */
    Object readCollection(Object coll);

    /**
     * Apply {@code mutations}, shaped {@code {<collection> {<key> <value>}}}. A
     * null value retracts that key; keys left out are untouched.
     */
    void mutate(Object mutations);
}
