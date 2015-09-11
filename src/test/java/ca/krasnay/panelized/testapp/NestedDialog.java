package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogPanel;
import ca.krasnay.panelized.TextPanel;

public class NestedDialog extends DialogPanel {

    public NestedDialog(String id) {
        super(id);
    }

    public void show(AjaxRequestTarget target, String title, AjaxAction saveAction) {

        DialogFrame frame = buildFrame(title);

        frame.addPanel(new TextPanel(frame.newPanelId(), "This is a nested dialog. It's here to confirm the form tag fix. See Modal.js for more information."));

        frame.addSaveButton(saveAction);
        frame.addCancelButton();

        super.show(target);

    }
}
