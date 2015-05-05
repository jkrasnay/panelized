package ca.krasnay.panelized;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for action panels that display a dialog.
 *
 * This class enforces policy for this type of action:
 *
 * <ul>
 * <li>tracks and invokes an "onSaveAction" object, which is normally used so
 * the caller can refresh UI elements when the action is completed
 * <li>adds an ellipsis to the action name when it is displayed in a menu
 * <li>implements a standard popup title, incorporating both the action name and
 * the name of the entity being acted upon
 * <li>triggers a user notification when the action is complete
 * </ul>
 *
 * The class implements neither NamedAjaxAction nor DataTableAction. The
 * subclass must implement one or both of these interfaces as necessary.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class DialogActionPanel extends ContainerPanel implements ConditionalAction {

    private static final Logger log = LoggerFactory.getLogger(DialogActionPanel.class);

//    @SpringBean
//    private ExceptionNotifier exceptionNotifier;

    private boolean isSaveAndNew;

//    private AlertPopUpPanel notificationPopup;

    private AjaxAction onSaveAction;

    private AjaxAction saveAction = new AjaxAction() {
        @Override
        public void invoke(AjaxRequestTarget target) {
//            log.info("Invoked action %s", DialogActionPanel.this.getClass().getSimpleName());
            try {
                isSaveAndNew = false;
                doSave(target);
                DialogPanel dialog = getDialog();
                if (dialog != null) {
                    dialog.hide(target);
                }
            } catch (Exception e) {
                handleSaveException(target, e);
            }
        }
    };

    /**
     * Like the save action, but instead of hiding the dialog it re-invokes
     * the action, which re-shows the dialog with a new record.
     */
    private AjaxAction saveAndNewAction = new AjaxAction() {
        public void invoke(AjaxRequestTarget target) {

            log.info("Invoked action %s", DialogActionPanel.this.getClass().getSimpleName());

            try {
                isSaveAndNew = true;
                doSave(target);
                ((NamedAjaxAction) DialogActionPanel.this).invoke(target);
            } catch (Exception e) {
                handleSaveException(target, e);
            }

        };
    };

    public DialogActionPanel(String id, AjaxAction onSaveAction) {

        super(id);

        this.onSaveAction = onSaveAction;

    }

    private void doSave(AjaxRequestTarget target) {

        save(target);

        if (onSaveAction != null) {
            onSaveAction.invoke(target);
        }
    }

    /**
     * Returns the name of the action to be shown in menus and
     * as the popup title.
     */
    protected abstract String getActionName();

    /**
     * Returns the title of the popup. By default returns the action name, but
     * subclasses can override this to return a different dialog title.
     */
    protected String getDialogTitle() {
        return getActionName();
    }

    /**
     * Returns the dialog title for a popup involving a named entity. Returns
     * the regular dialog title and the given entity name separated by a dash.
     */
    protected String getDialogTitle(String entityName) {
        return String.format("%s - %s", getDialogTitle(), entityName);
    }

    /**
     * Returns the action name when implementing NamedAjaxAction. By default,
     * returns the action name followed by ellipsis.
     */
    public String getName(Locale locale) {
        return getActionName() + "...";
    }

    /**
     * Returns the save action to use when building the dialog.
     */
    protected AjaxAction getSaveAction() {
        return saveAction;
    }

    /**
     * Returns the "save and new" action to use when building the dialog.
     */
    protected AjaxAction getSaveAndNewAction() {
        if (this instanceof NamedAjaxAction) {
            return saveAndNewAction;
        } else {
            // Dialogs should expect a null save and new action
            return null;
        }
    }

    protected abstract void handleSaveException(AjaxRequestTarget target, Exception e);

    /**
     * Returns the currently displayed dialog. Used by the save action
     * to hide the dialog. May also be used by sub-classes, for example,
     * to refresh the feedback panel to display an error.
     */
    protected abstract DialogPanel getDialog();

    /**
     * Default enablement for named ajax actions. Returns true.
     */
    @Override
    public final boolean isActionEnabled() {
        return true;
    }

    /**
     * Default visiblity for named ajax actions. Returns true.
     */
    @Override
    public boolean isActionVisible() {
        return true;
    }

    /**
     * Default enablement for data table actions. Returns true.
     */
    public boolean isEnabled(IModel<Map<String, Object>> rowModel) {
        return true;
    }

    /**
     * Returns whether the most recent save action was Save and New. Only valid
     * from within {@link #save()}. The save method can use this to, for
     * example, direct the user to a new page representing the newly created
     * entity if the user clicked Save rather than Save and New.
     */
    protected boolean isSaveAndNew() {
        return isSaveAndNew;
    }

    /**
     * Default visibility for data table actions. Returns true.
     */
    public boolean isVisible(IModel<Map<String, Object>> rowModel) {
        return true;
    }

    /**
     * Performs the expected action when the user clicks the save button.
     */
    protected abstract void save(AjaxRequestTarget target);


}
