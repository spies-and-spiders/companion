package sns.sdk;

import java.util.List;
import java.util.Map;

/**
 * Records for the data exchanged with JVM plugins, converted to and from the
 * Clojure shapes in {@code sns.sdk.schema} by {@code sns.sdk.protocols}.
 *
 * <p>Keywords are written by bare name ({@code "int"}, not {@code ":int"}).
 * Optional components accept null.
 */
public final class Models {
    private Models() {}

    // --- loot specification (drives the generic input forms) -----------------

    /**
     * See {@code sns.sdk.schema/loot-spec}. Every component is optional, and the
     * tool's {@code :generator} config overrides any it sets. {@code history} is
     * always/on-report/button/never.
     */
    public record Spec(boolean hidden, String generateLabel, List<Field> inputs,
                           String history, List<String> storeCollections,
                           ManualState storeManual) {
        public Spec() { this(null); }
        public Spec(List<Field> inputs) { this(false, null, inputs, null, null, null); }
    }

    /** {@code type} is enum/int/decimal/text/bool; {@code list} collects several. */
    public record Field(String id, String label, String type, Object defaultValue,
                        List<?> options, boolean list) {
        public Field(String id, String label, String type) {
            this(id, label, type, null, null, false);
        }
        public Field(String id, String label, String type, Object defaultValue, List<?> options) {
            this(id, label, type, defaultValue, options, false);
        }
    }

    /**
     * A collection the DM edits by hand: each key is a row and {@code fields} its
     * columns. With {@code list}, a key owns several rows.
     */
    public record ManualState(String keyLabel, boolean list, List<Field> fields) {
        public ManualState(List<Field> fields) { this(null, false, fields); }
    }

    // --- view-model (what a loot type returns) -------------------------------

    /**
     * See {@code sns.sdk.schema/view-model}. {@code vars} is the title and
     * subtitle's template context. {@code words} is drawn by the engine, so leave
     * it null. {@code state} is opaque and handed back with the next action; keep
     * it to what can't be read off the item's (possibly DM-edited) vars.
     * {@code mutations} are store writes ({@code {collection: {key: value}}}, null
     * retracting), applied only once the view-model validates. An {@code error}
     * result shows as an overlay and isn't saved to history automatically.
     */
    public record ViewModel(String title, String subtitle, List<Section> sections, List<Action> actions,
                            Map<String, ItemVar> vars, List<String> words, Object state,
                            Map<String, Map<String, Object>> mutations, boolean error) {
        public ViewModel(String title) { this(title, null, null, null); }
        public ViewModel(String title, String subtitle, List<Section> sections, List<Action> actions) {
            this(title, subtitle, sections, actions, null, null, null, null);
        }
        public ViewModel(String title, String subtitle, List<Section> sections, List<Action> actions,
                         Map<String, ItemVar> vars, List<String> words, Object state,
                         Map<String, Map<String, Object>> mutations) {
            this(title, subtitle, sections, actions, vars, words, state, mutations, false);
        }
    }

    /** A {@code secret} section is shown and kept in history, but reporters leave it out by default. */
    public record Section(String heading, List<Item> items, boolean secret) {
        public Section(String heading, List<Item> items) { this(heading, items, false); }
    }

    /**
     * {@code title} and {@code body} are templates rendered in the browser
     * against {@code vars}: in {@code "+{{ dmg }} fire"}, the DM can edit
     * {@code dmg}, and follow-up actions see the edit.
     */
    public record Item(String title, String body, List<String> metadata, Map<String, ItemVar> vars) {
        public Item(String title, String body, List<String> metadata) {
            this(title, body, metadata, null);
        }
    }

    /**
     * A template variable. {@code type} is int/decimal/bool (null means text).
     * {@code random}/{@code args} record its source, so an action can reroll it.
     * A {@code context} var renders but isn't editable.
     *
     * <p>{@code step}/{@code max}/{@code rank} control ranking (see
     * {@code sns.sdk.rank}). Ranks are 1-based, so a null {@code rank} is the
     * declared value; a null {@code step} defaults to that value, and a
     * non-numeric var never ranks.
     */
    public record ItemVar(Object value, String type, String label, String random,
                          Map<String, Object> args, List<?> options, boolean context,
                          Object step, Integer max, Integer rank) {
        public ItemVar(Object value) { this(value, null, null, null, null, null, false, null, null, null); }
        public ItemVar(Object value, String type) { this(value, type, null, null, null, null, false, null, null, null); }
    }

    /**
     * A follow-up button routed back to {@link sns.sdk.Action#handleAction}.
     * {@code inputs} are form fields shown beside it, arriving as
     * {@code ctx.get("inputs")}. Their type may also be section or item, picking
     * part of the result as {@code {section: i}} or {@code {section: i, item: j}}.
     */
    public record Action(String label, String action, Map<String, Object> params, List<Field> inputs) {
        public Action(String label, String action, Map<String, Object> params) {
            this(label, action, params, null);
        }
    }
}
