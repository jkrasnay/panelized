package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogActionPanel;
import ca.krasnay.panelized.NamedAjaxAction;

public class ShowTestDialogActionPanel extends DialogActionPanel implements NamedAjaxAction {

    private TestDialog dialog;

    public ShowTestDialogActionPanel(String id, AjaxAction onSaveAction) {

        super(id, onSaveAction);

        addPanel(dialog = new TestDialog(newPanelId()));

    }

    @Override
    protected String getActionName() {
        return "Show Test Dialog";
    }

    @Override
    protected void handleSaveException(AjaxRequestTarget target, Exception e) {
        // Here we log the exception and notify the user an app-specific manner
    }

    @Override
    protected void hideDialog(AjaxRequestTarget target) {
        dialog.hide(target);
    }

    @Override
    protected void save(AjaxRequestTarget target) {
        // Here we save the entity
    }

    @Override
    public void invoke(AjaxRequestTarget target) {
        // Here we would load the entity to be edited and pass it to the dialog
        // Typically, the entity would be kept in a local field
        dialog.show(target, getDialogTitle(), getSaveAction());
    }

}
