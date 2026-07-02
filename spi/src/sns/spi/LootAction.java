package sns.spi;

import java.util.Map;

/**
 * Optional. Stateful follow-up operations on previously generated loot (e.g.
 * levelling a relic up), surfaced to the UI via a view-model's {@code
 * :loot/actions}. Mirrors the {@code sns.spi.protocols/LootAction} protocol.
 */
public interface LootAction {

    /**
     * Data-only description of the actions this generator supports, keyed by
     * action name (which becomes a keyword).
     */
    Map<String, Models.ActionSpecValue> actionSpec();

    /**
     * Apply {@code action} (the keyword's name) with {@code params}. Returns an
     * updated view-model. {@code ctx} is the opaque engine context map.
     */
    Models.ViewModel handleAction(Object ctx, String action, Map<?, ?> params);
}
