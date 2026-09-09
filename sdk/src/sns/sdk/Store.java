package sns.sdk;

/**
 * Reads over persisted state, for stateful plugins. State is a set of named
 * collections, each a map of key to value; a collection needs no declaration and
 * reads as an empty map until something is written to it. Available to
 * in-process plugins only; CLI, FFI and WASM plugins persist their own state. Mirrors
 * the {@code sns.sdk.protocols/Store} protocol.
 *
 * <p>Writing is declarative and not part of this interface: put the changes on
 * your view-model under {@code :store/mutations} and the engine applies them
 * once the view-model has validated, so a plugin cannot leave state changed by a
 * call that then fails. Declare the collections you use with {@code
 * :store/collections} in your loot-spec; it defaults to a single collection
 * named after the plugin's {@code :id}.
 */
public interface Store {

    /**
     * Prepare the backend for use. Called once at startup, before any other
     * method; construction itself must stay side-effect-free. Default is a no-op.
     */
    default void setup() {}

    /**
     * Return the whole collection {@code collId} (a keyword) as a map, or an empty
     * map when absent.
     */
    Object readCollection(Object collId);
}
