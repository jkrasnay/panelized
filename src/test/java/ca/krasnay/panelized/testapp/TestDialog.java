package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogPanel;
import ca.krasnay.panelized.TextPanel;

public class TestDialog extends DialogPanel {

    public TestDialog(String id) {
        super(id);
    }

    public void show(AjaxRequestTarget target, String title, AjaxAction saveAction) {

        DialogFrame frame = buildFrame(title);

        frame.addPanel(new TextPanel(frame.newPanelId(), "Hello, world"));

        frame.addSaveButton(saveAction);
        frame.addCancelButton();

        super.show(target);

    }
}
