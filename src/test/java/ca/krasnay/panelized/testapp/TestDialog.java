package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogPanel;
import ca.krasnay.panelized.LabelledPanel;
import ca.krasnay.panelized.RadioButtonPanel;
import ca.krasnay.panelized.RadioGroupControl;
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
        .addPanel(new TextFieldControl<String>("name", Model.of(""))
        .setRequired(true)));

        frame.addPanel(new LabelledPanel(frame.newPanelId(), "Life Story")
        .addPanel(new TextAreaControl("name", Model.of(""))));

        LabelledPanel trialOutcome = new LabelledPanel(frame.newPanelId(), "Trial Outcome");
        frame.addPanel(trialOutcome);

        RadioGroupControl<Boolean> radioGroup = new RadioGroupControl<>(trialOutcome.newPanelId(), Model.of(false));
        trialOutcome.addPanel(radioGroup);

        radioGroup.addPanel(new RadioButtonPanel<Boolean>(radioGroup.newPanelId(), Model.of(false), Model.of("Not guilty")));

        RadioButtonPanel<Boolean> guilty = new RadioButtonPanel<Boolean>(radioGroup.newPanelId(), Model.of(true), Model.of("Guilty"));
        radioGroup.addPanel(guilty);

        guilty.addPanel(new LabelledPanel(guilty.newPanelId(), "Sentence")
        .addPanel(new TextFieldControl<String>("sentence", Model.of(""))));

        frame.addSaveButton(saveAction);
        frame.addCancelButton();

        super.show(target);

    }
}
