package sns.sdk;

/**
 * How a single mod evolves. The default implementation interprets the
 * upgrade-graph DSL ({@code sns.sdk.schema/mod}); plugins may supply bespoke
 * logic by implementing this interface. Mirrors the {@code
 * sns.sdk.protocols/Progression} protocol.
 */
public interface Progression {

    /**
     * Derive {@code mod}'s variables at the progression described by {@code path}
     * (a vector of {@code {:id ...}} steps). Returns the resolved
     * {@code sns.sdk.schema/item-vars}, which the browser renders the template against.
     */
    Object currentState(Object mod, Object path);

    /** Return the upgrade options available as the next step from {@code path}. */
    Object levelOptions(Object mod, Object path);
}
