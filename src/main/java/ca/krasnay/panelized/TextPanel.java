package ca.krasnay.panelized;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel that is a simple block of text.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class TextPanel extends Panel {

    private boolean bold;

    private String cssClass;

    private boolean dim;

    private Label label;

    private boolean large;

    private boolean multiLine;

    public TextPanel(String id) {
        this(id, (IModel<String>) null);
    }

    public TextPanel(String id, IModel<String> textModel) {

        super(id);

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                StringBuilder sb = new StringBuilder();
                if (cssClass != null) {
                    sb.append(cssClass).append(" ");
                }
                if (bold) {
                    sb.append("txt-bold ");
                }
                if (dim) {
                    sb.append("txt-dim ");
                }
                if (large) {
                    sb.append("txt-large ");
                }
                if (multiLine) {
                    sb.append("txt-multi ");
                }
                return sb.toString().trim();
            }
        }, " "));

        label = new Label("text", textModel != null ? textModel : new DelegateModel<String>(this));
        add(label);

    }

    public TextPanel(String id, String text) {
        this(id, Model.of(text));
    }

    public String getCssClass() {
        return cssClass;
    }

    public Label getLabel() {
        return label;
    }

    public TextPanel setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public TextPanel setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    /**
     * Sets whether this text is shown as diminished, that is, whether it has
     * been made smaller and/or dimmer than regular text.
     */
    public TextPanel setDim(boolean dim) {
        this.dim = dim;
        return this;
    }

    public TextPanel setEscapeHtml(boolean escapeMarkup) {
        label.setEscapeModelStrings(escapeMarkup);
        return this;
    }

    public TextPanel setLarge(boolean large) {
        this.large = large;
        return this;
    }

    public TextPanel setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
        return this;
    }

    public TextPanel setText(IModel<String> textModel) {
        label.setDefaultModel(textModel);
        return this;
    }

    public TextPanel setText(String text) {
        return setText(Model.of(text));
    }

}
