package sns.sdk;

import java.util.Map;

/**
 * Optional. Stateful follow-up operations on previously generated loot (e.g.
 * ranking a relic up), surfaced to the UI via a view-model's {@code
 * :loot/actions}. Mirrors the {@code sns.sdk.protocols/Action} protocol.
 */
public interface Action {

    /**
     * Apply {@code action} (the keyword's name) with {@code params}. Returns an
     * updated view-model.
     *
     * <p>{@code ctx.get("view-model")} is the result the UI had on screen as a
     * {@link Models.ViewModel}, DM edits included. Rebuild from it rather than
     * from {@code params}, so the displayed values are what the action operates
     * on; it is null when the caller sent nothing. {@code ctx.get("inputs")}
     * holds the values entered for the action's {@link Models.Action#inputs()}.
     */
    Models.ViewModel handleAction(Map<String, Object> ctx, String action, Map<String, Object> params);
}
