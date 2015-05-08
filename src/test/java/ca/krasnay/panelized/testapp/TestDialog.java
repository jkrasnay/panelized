package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogPanel;
import ca.krasnay.panelized.LabelledPanel;
import ca.krasnay.panelized.TextAreaControl;
import ca.krasnay.panelized.TextFieldControl;

public class TestDialog extends DialogPanel {

    public TestDialog(String id) {
        super(id);
    }

    public void show(AjaxRequestTarget target, String title, AjaxAction saveAction) {

        DialogFrame frame = buildFrame(title);

//        frame.addPanel(new TipPanel(frame.newPanelId(), "This is a test"));

        frame.addPanel(new LabelledPanel(frame.newPanelId(), "Name")
        .addPanel(new TextFieldControl("name", Model.of(""))
        .setRequired(true)));

        frame.addPanel(new LabelledPanel(frame.newPanelId(), "Life Story")
        .addPanel(new TextAreaControl("name", Model.of(""))));

        frame.addSaveButton(saveAction);
        frame.addCancelButton();

        super.show(target);

    }
}
