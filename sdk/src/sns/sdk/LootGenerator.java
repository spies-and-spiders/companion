package sns.sdk;

import java.util.Map;

/**
 * A loot type. Java/Kotlin/Scala plugins may implement this interface instead of
 * reifying the {@code sns.sdk.protocols/LootGenerator} Clojure protocol; the SDK
 * extends the protocol onto this interface, so implementing it is sufficient.
 *
 * <p>Data shapes are exchanged as {@link Models} records; the engine context
 * ({@code ctx}) is passed through as a map keyed by the bare names of
 * {@code {:rng :store :config :inputs}}.
 */
public interface LootGenerator {

    /** Static, data-only description of this loot type. */
    Models.LootSpec lootSpec();

    /** Produce loot. */
    Models.ViewModel generate(Map<String, Object> ctx);
}
