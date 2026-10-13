package sns.sdk;

/**
 * Sends a generated view-model somewhere external, like a Discord webhook.
 * Configured under {@code :reporting}. Mirrors {@code sns.sdk.protocols/Reporter}.
 */
public interface Reporter {

    /** The report button's label, e.g. "Send to Discord". */
    String reportLabel();

    void report(Models.ViewModel viewModel);
}
