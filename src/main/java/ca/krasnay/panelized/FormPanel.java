package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Panel consisting of a form with a child panel repeater.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class FormPanel extends Panel implements PanelContainer {

    private Form<Void> form;

    private RepeatingView panelRepeater;

    public FormPanel(String id) {

        super(id);

        form = new Form<Void>("form") {
            protected void onValidate() {
                FormPanel.this.onValidate();
            };
        };
        add(form);

        panelRepeater = new RepeatingView("panel");
        form.add(panelRepeater);

    }

    @Override
    public PanelContainer addPanel(Component panel) {
        panelRepeater.add(panel);
        return this;
    }

    public Form<Void> getForm() {
        return form;
    }

    @Override
    public String newPanelId() {
        return panelRepeater.newChildId();
    }

    protected void onValidate() {

    }

    @Override
    public void removeAllPanels() {
        panelRepeater.removeAll();
    }

}
