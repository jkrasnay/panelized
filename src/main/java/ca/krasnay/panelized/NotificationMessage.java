package ca.krasnay.panelized;

/**
 * Encapsulates a notification message to be displayed by to the user when
 * an action is complete.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class NotificationMessage {

    private String message;

    private boolean showAsPopup;

    /**
     * Constructor. Calls {@link String#format(String, Object...)} with the
     * given arguments.
     */
    public NotificationMessage(String s, Object... args) {
        message = String.format(s, args);
    }

    public boolean isShowAsPopup() {
        return showAsPopup;
    }

    /**
     * Sets whether to show the message as a popup that the user must explicitly
     * dismiss. By default this is false, and the message is shown as a
     * temporary message that disappears after a few seconds.
     */
    public NotificationMessage setShowAsPopup(boolean showAsPopup) {
        this.showAsPopup = showAsPopup;
        return this;
    }

    @Override
    public String toString() {
        return message;
    }

}
