package sns.sdk;

import java.util.Map;

/**
 * Optional. Stateful follow-up operations on previously generated loot (e.g.
 * levelling a relic up), surfaced to the UI via a view-model's {@code
 * :loot/actions}. Mirrors the {@code sns.sdk.protocols/LootAction} protocol.
 */
public interface LootAction {

    /**
     * Apply {@code action} (the keyword's name) with {@code params}. Returns an
     * updated view-model. {@code ctx} is the opaque engine context map.
     */
    Models.ViewModel handleAction(Map<String, Object> ctx, String action, Map<String, Object> params);
}
