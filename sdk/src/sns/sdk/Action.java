package sns.sdk;

import java.util.Map;

/**
 * Optional follow-up operations on generated loot, like ranking a relic up,
 * offered through a view-model's actions. Mirrors {@code sns.sdk.protocols/Action}.
 */
public interface Action {

    /**
     * Apply {@code action} with {@code params}, returning an updated view-model.
     *
     * <p>{@code ctx.get("view-model")} is the result on screen, DM edits included,
     * or null. Rebuild from it rather than from {@code params}, so the action
     * works on what the DM sees. {@code ctx.get("inputs")} holds the values
     * entered for the action's {@link Models.Action#inputs()}.
     */
    Models.ViewModel handleAction(Map<String, Object> ctx, String action, Map<String, Object> params);
}
