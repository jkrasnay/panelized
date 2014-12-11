package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel that contains a label to be used as the title of a border panel.
 * Contains a CSS class that styles the title specially.
 *
 * @author john
 */
public class BorderTitlePanel extends Panel {

    private Label label;

    public BorderTitlePanel(String id, IModel<String> model) {
        super(id);
        add(label = new Label("label", model));
    }

    public BorderTitlePanel(String id, String s) {
        this(id, Model.of(s));
    }

    public Component setTitleModel(IModel<String> model) {
        label.setDefaultModel(model);
        return this;
    }
}
