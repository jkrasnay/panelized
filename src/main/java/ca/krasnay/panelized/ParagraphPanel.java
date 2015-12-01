package ca.krasnay.panelized;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ParagraphPanel extends Panel {

    public ParagraphPanel(String id, IModel<String> model) {
        this(id, model, true);
    }

    public ParagraphPanel(String id, IModel<String> model, boolean escapeHtml) {
        super(id, model);
        add(new Label("text", model).setEscapeModelStrings(escapeHtml));
    }

}
