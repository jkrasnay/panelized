package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.ValidationError;

/**
 * Transparent radio group panel. Radio buttons and other panels can
 * be added as children to this panel.
 *
 * By default this component is set as required. If you want to have a
 * null option, you must explicitly call <code>setRequired(false)</code>.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class RadioGroupControl<T> extends Panel implements PanelContainer {

    private RadioGroup<T> radioGroup;

    private RepeatingView childRepeater;

    private boolean isRequired = true;

    private String requiredMessage;

    public RadioGroupControl(String id) {
        this(id, null);
    }

    public RadioGroupControl(String id, IModel<T> model) {

        super(id, model);

        setOutputMarkupId(true);

        radioGroup = new RadioGroup<T>("radioGroup", new DelegateModel<T>(this)) {

            @Override
            public boolean isRequired() {
                return RadioGroupControl.this.isRequired();
            };

//            @Override
//            public void validate() {
//
//                // Copied from FormComponent.validate()
//                // See https://issues.apache.org/jira/browse/WICKET-4853
//
//                if (!checkRequired())
//                {
//                        reportRequiredError();
//                }
//
//                if (isValid())
//                {
//                        convertInput();
//                        if (isValid())
//                        {
//                                if (isRequired() && getConvertedInput() == null && isInputNullable())
//                                {
//                                    reportRequiredError();
//                                }
//                                else
//                                {
//                                        validateValidators();
//                                }
//                        }
//                }
//            };

            @Override
            protected void reportRequiredError() {
                if (requiredMessage == null) {
                    error(new ValidationError().addKey("Required"));
                } else {
                    error(requiredMessage);
                }
            }
        };
        add(radioGroup);
        radioGroup.setRequired(true);

        childRepeater = new RepeatingView("child");
        radioGroup.add(childRepeater);

    }

    @Override
    public RadioGroupControl<T> addPanel(Component panel) {
        childRepeater.add(panel);
        return this;
    }

    public boolean isRequired() {
        return isRequired;
    }

    @Override
    public String newPanelId() {
        return childRepeater.newChildId();
    }

    @Override
    public void removeAllPanels() {
        childRepeater.removeAll();
    }

    public RadioGroup<T> getRadioGroup() {
        return radioGroup;
    }

    public String getRequiredMessage() {
        return requiredMessage;
    }

    public RadioGroupControl<T> setRequired(boolean required) {
        this.isRequired = required;
        return this;
    }

    public RadioGroupControl<T> setRequiredMessage(String requiredMessage) {
        this.requiredMessage = requiredMessage;
        return this;
    }


}
