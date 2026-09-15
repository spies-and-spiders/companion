package sns.sdk;

import java.util.List;
import java.util.Map;

/**
 * Typed data shapes exchanged with plugins, so JVM-language authors build and
 * receive records instead of untyped maps. The Clojure bridge in {@code
 * sns.sdk.protocols} converts each to/from the namespaced Clojure data the
 * engine and UI use (see {@code sns.sdk.schema}), losing nothing either way.
 *
 * <p>A component naming a Clojure keyword carries its bare name ({@code "int"},
 * not {@code ":int"}). Optional components accept null.
 */
public final class Models {
    private Models() {}

    // --- loot specification (drives the generic input forms) -----------------

    /**
     * Conforms to {@code sns.sdk.schema/loot-spec}. Every component is optional, and
     * the tool's {@code :generator} config overrides each one it sets; the tool's id,
     * label and section come from config alone. {@code storeCollections} defaults to
     * one named after the tool's id. {@code history} is always/on-report/button/never.
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
     * A DM-maintained collection the UI renders an editor for: each key is a row,
     * {@code fields} its columns. With {@code list} a key owns several rows.
     */
    public record ManualState(String keyLabel, boolean list, List<Field> fields) {
        public ManualState(List<Field> fields) { this(null, false, fields); }
    }

    // --- view-model (what a loot type returns) -------------------------------

    /**
     * Conforms to {@code sns.sdk.schema/view-model}. {@code vars} is the context
     * the title and subtitle render against. {@code words} is drawn by the
     * engine, so a generator leaves it null. {@code state} is opaque bookkeeping
     * carried untouched and handed back with the next action — keep it to what
     * cannot be read off the item, since a DM may have edited the displayed
     * {@link Item#vars()}. {@code mutations} are store writes
     * ({@code {collection: {key: value}}}, null retracting) applied only once
     * this view-model validates, so a failing call changes nothing.
     */
    public record ViewModel(String title, String subtitle, List<Section> sections, List<Action> actions,
                            Map<String, ItemVar> vars, List<String> words, Object state,
                            Map<String, Map<String, Object>> mutations) {
        public ViewModel(String title) { this(title, null, null, null); }
        public ViewModel(String title, String subtitle, List<Section> sections, List<Action> actions) {
            this(title, subtitle, sections, actions, null, null, null, null);
        }
    }

    /** A {@code secret} section is shown and kept in history, but reporters leave it out by default. */
    public record Section(String heading, List<Item> items, boolean secret) {
        public Section(String heading, List<Item> items) { this(heading, items, false); }
    }

    /**
     * {@code title} and {@code body} are templates rendered in the browser
     * against {@code vars}: {@code "+{{ dmg }} fire"} shows the value as its own
     * editable control, and that edit is what a follow-up action reads. Text
     * with no tags renders as itself.
     */
    public record Item(String title, String body, List<String> metadata, Map<String, ItemVar> vars) {
        public Item(String title, String body, List<String> metadata) {
            this(title, body, metadata, null);
        }
    }

    /**
     * One variable a template interpolates, keyed by the name it is referred to
     * by. {@code type} is int/decimal/bool and picks the browser's control (null
     * means text). {@code random}/{@code args} record what it was drawn from, so
     * an action can reroll it. With {@code context} it renders but is not
     * offered for editing.
     *
     * <p>{@code step}/{@code max}/{@code rank} are how the var ranks up (see
     * {@code sns.sdk.rank}). Ranks are 1-based, so a null {@code rank} is the
     * declared value itself; a null {@code step} defaults to that value, and a
     * var holding anything but a number never ranks.
     */
    public record ItemVar(Object value, String type, String label, String random,
                          Map<String, Object> args, List<?> options, boolean context,
                          Object step, Integer max, Integer rank) {
        public ItemVar(Object value) { this(value, null, null, null, null, null, false, null, null, null); }
        public ItemVar(Object value, String type) { this(value, type, null, null, null, null, false, null, null, null); }
    }

    /**
     * A follow-up button routed back to {@link sns.sdk.Action#handleAction}. These
     * three fields rather than an event vector: the engine fills in the id this
     * plugin was registered under.
     */
    public record Action(String label, String action, Map<String, Object> params) { }
}
