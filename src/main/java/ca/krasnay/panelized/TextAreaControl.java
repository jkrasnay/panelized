package ca.krasnay.panelized;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Form item panel that encapsulates a text area.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class TextAreaControl extends AbstractControl<String> {

    private boolean required;

    private int rows = 6;

    private TextArea<String> textArea;

    public TextAreaControl(String id) {
        this(id, null);
    }

    public TextAreaControl(String id, IModel<String> model) {

        super(id, model);

        textArea = new TextArea<String>("field", new DelegateModel<String>(this)) {
            @Override
            public boolean isRequired() {
                return TextAreaControl.this.isRequired();
            }
        };

        add(textArea);

        textArea.setType(String.class);
        textArea.setOutputMarkupId(true);

        textArea.add(new AttributeModifier("rows", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "" + rows;
            }
        }));

    }

    public TextArea<String> getTextArea() {
        return textArea;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    public TextAreaControl setLabel(IModel<String> label) {
        textArea.setLabel(label);
        return this;
    }

    @Override
    public TextAreaControl setRequired(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public FormComponent<String> getFormComponent() {
        return textArea;
    }

    public TextAreaControl setRows(int rows) {
        this.rows = rows;
        return this;
    }
}
