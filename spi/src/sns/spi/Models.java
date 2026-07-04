package sns.spi;

import java.util.List;
import java.util.Map;

/**
 * Typed data shapes exchanged with plugins, so JVM-language authors build and
 * receive records instead of untyped maps. The Clojure bridge in {@code
 * sns.spi.protocols} converts each record to/from the idiomatic namespaced
 * Clojure data the engine and UI use (see {@code sns.spi.schema}).
 */
public final class Models {
    private Models() {}

    // --- loot specification (drives the generic input forms) -----------------

    /**
     * Static description of a loot type. {@code id} becomes a keyword; {@code
     * inputs} may be null/empty. A {@code utility} is a session tool rather than
     * loot: grouped separately in the UI and barred from the loot-table.
     * Conforms to {@code sns.spi.schema/loot-spec}.
     */
    public record LootSpec(String id, String label, boolean stateful, List<Field> inputs, boolean utility) {
        public LootSpec(String id, String label) { this(id, label, false, null, false); }
        public LootSpec(String id, String label, boolean stateful, List<Field> inputs) {
            this(id, label, stateful, inputs, false);
        }
    }

    /** One declared input field. {@code type} is one of enum/int/text/bool. */
    public record Field(String id, String label, String type, Object defaultValue, List<Object> options) {
        public Field(String id, String label, String type) { this(id, label, type, null, null); }
    }

    // --- view-model (what a loot type returns) -------------------------------

    /** A rendered loot result. Conforms to {@code sns.spi.schema/view-model}. */
    public record ViewModel(String title, String subtitle, List<Section> sections, List<Action> actions) {
        public ViewModel(String title) { this(title, null, null, null); }
    }

    /** A titled group of items in a view-model. {@code heading} may be null. */
    public record Section(String heading, List<Item> items) { }

    /** A single line of loot detail. {@code title}/{@code metadata} may be null. */
    public record Item(String title, String body, List<String> metadata) { }

    /**
     * A stateful follow-up button. When surfaced, {@code action} (a keyword name)
     * and {@code params} are routed back to {@link LootAction#handleAction}.
     */
    public record Action(String label, String action, Map<String, Object> params) { }

    // --- action spec (static description of a loot type's actions) -----------

    /** One action's metadata; {@code params} names its parameters (as keywords). */
    public record ActionSpecValue(String label, List<String> params) { }
}
