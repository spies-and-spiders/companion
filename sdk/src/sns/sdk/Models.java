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
     * Conforms to {@code sns.sdk.schema/loot-spec}. A {@code utility} is a
     * session tool rather than loot: grouped separately and barred from the
     * loot-table. {@code storeCollections} defaults to one named after
     * {@code id}. {@code history} is always/on-report/button/never.
     *
     * <p>No {@code hidden}: that is set on the plugin's config entry, not here.
     */
    public record LootSpec(String id, String label, List<Field> inputs, boolean utility,
                           String generateLabel, List<String> storeCollections,
                           ManualState storeManual, String history) {
        public LootSpec(String id, String label) { this(id, label, null, false); }
        public LootSpec(String id, String label, List<Field> inputs) {
            this(id, label, inputs, false);
        }
        public LootSpec(String id, String label, List<Field> inputs, boolean utility) {
            this(id, label, inputs, utility, null, null, null, null);
        }
    }

    /** {@code type} is enum/int/decimal/text/bool; {@code list} collects several. */
    public record Field(String id, String label, String type, Object defaultValue,
                        List<Object> options, boolean list) {
        public Field(String id, String label, String type) {
            this(id, label, type, null, null, false);
        }
        public Field(String id, String label, String type, Object defaultValue, List<Object> options) {
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

    public record Section(String heading, List<Item> items) { }

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
     */
    public record ItemVar(Object value, String type, String label, String random,
                          Map<String, Object> args, List<Object> options, boolean context) {
        public ItemVar(Object value) { this(value, null, null, null, null, null, false); }
        public ItemVar(Object value, String type) { this(value, type, null, null, null, null, false); }
    }

    /**
     * A follow-up button routed back to {@link LootAction#handleAction}. These
     * three fields rather than an event vector: the engine fills in the id this
     * plugin was registered under.
     */
    public record Action(String label, String action, Map<String, Object> params) { }
}
