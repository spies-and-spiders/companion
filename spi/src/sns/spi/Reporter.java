package sns.spi;

/**
 * Optional. Sends a generated loot view-model to an external destination (e.g. a
 * Discord webhook). Config-driven via {@code :reporting}. Mirrors the {@code
 * sns.spi.protocols/Reporter} protocol.
 */
public interface Reporter {

    /** Human label for the report button, e.g. "Send to Discord". */
    String reportLabel();

    /**
     * Send {@code viewModel} to the destination
     */
    void report(Models.ViewModel viewModel);
}
