package sns.sdk;

import java.util.Map;

/**
 * A loot type, for JVM-language plugins; the SDK extends the
 * {@code sns.sdk.protocols/Generator} protocol onto it.
 *
 * <p>{@code ctx} is keyed by {@code rng}, {@code store}, {@code config},
 * {@code registry} and {@code inputs}.
 */
public interface Generator {

    /** A static description of this loot type. */
    default Models.Spec lootSpec() {
        return new Models.Spec();
    }

    Models.ViewModel generate(Map<String, Object> ctx);
}
