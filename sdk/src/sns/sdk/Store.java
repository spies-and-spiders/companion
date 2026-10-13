package sns.sdk;

/**
 * Read access to persisted state: named collections, each a map of key to value,
 * reading as empty until written. Mirrors {@code sns.sdk.protocols/Store}.
 *
 * <p>Plugins write by returning {@code :store/mutations} on their view-model,
 * which the engine applies once it validates.
 */
public interface Store {

    /**
     * Called once at startup, before any other method, so construction can stay
     * side-effect-free.
     */
    default void setup() {}

    /** The whole collection {@code collId} (a keyword), or an empty map. */
    Object readCollection(Object collId);
}
