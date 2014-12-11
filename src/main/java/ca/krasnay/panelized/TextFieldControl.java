package ca.krasnay.panelized;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Form item panel that encapsulates a text field.
 *
 * @param <T> Type of the model object.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class TextFieldControl<T> extends AbstractControl<T> {

    public enum Size {
        LARGE,
        MEDIUM,
        SMALL,
        TINY;
    }

    private boolean autoFocus;

    private int maxLength = 255;

    private String placeholder;

    private IModel<String> prefix;

    private IModel<String> suffix;

    private TextField<T> textField;

    private Size size = Size.LARGE;

    public TextFieldControl(String id) {
        this(id, null);
    }

    public TextFieldControl(String id, IModel<T> model) {

        super(id, model);

        WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
        add(wrapper);

        wrapper.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return prefix != null ? "input-prepend" : null;
            }
        }, " "));

        wrapper.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return suffix != null ? "input-append" : null;
            }
        }, " "));


        IModel<String> prefixModel = new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return prefix != null ? prefix.getObject() : null;
            }
        };

        wrapper.add(new Label("prefix", prefixModel) {
            @Override
            protected void onConfigure() {
                setVisible(prefix != null);
            }
        });


        IModel<String> suffixModel = new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return suffix != null ? suffix.getObject() : null;
            }
        };

        wrapper.add(new Label("suffix", suffixModel) {
            @Override
            protected void onConfigure() {
                setVisible(suffix != null);
            }
        });


        textField = new TextField<T>("component", new DelegateModel<T>(this)) {

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return TextFieldControl.this.getConverter(type);
            }

            @Override
            public boolean isEnabled() {
                return isEnabledInternal();
            };

            @Override
            public boolean isRequired() {
                return TextFieldControl.this.isRequired();
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onTextFieldComponentTag(tag);
            }
        };

        wrapper.add(textField);

        textField.setOutputMarkupId(true);

        textField.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "textField-" + size.name();
            }
        }, " "));

        textField.add(new Behavior() {
            @Override
            public void onComponentTag(Component component, ComponentTag tag) {

                super.onComponentTag(component, tag);

                if (autoFocus) {
                    tag.put("autofocus", "");
                }

            }
        });

        textField.add(new AttributeModifier("maxLength", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return maxLength + "";
            }
        }));

        textField.add(new AttributeModifier("placeholder", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return placeholder;
            }
        }));

    }

    @Override
    public FormComponent<T> getFormComponent() {
        return textField;
    }

    public TextField<T> getTextField() {
        return textField;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    /**
     * Callback method called from the onComponentTag field of the TextField.
     * Here we can do last-minute changes to the tag.
     *
     * <p>This is called at the end of onComponentTag after the parent
     * implementations have been invoked, so this method can override those
     * tag changes.
     *
     * <p>The default implementation does nothing.
     */
    protected void onTextFieldComponentTag(ComponentTag tag) {

    }

    public TextFieldControl<T> setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
        return this;
    }

    public TextFieldControl<T> setLabel(IModel<String> label) {
        textField.setLabel(label);
        return this;
    }

    public TextFieldControl<T> setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public TextFieldControl<T> setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public TextFieldControl<T> setPrefix(IModel<String> prefix) {
        this.prefix = prefix;
        return this;
    }

    public TextFieldControl<T> setSize(Size size) {
        this.size = size;
        return this;
    }

    public TextFieldControl<T> setSuffix(IModel<String> suffix) {
        this.suffix = suffix;
        return this;
    }

}
