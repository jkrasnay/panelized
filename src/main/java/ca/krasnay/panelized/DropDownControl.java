package ca.krasnay.panelized;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Control that encapsulates a DropDownChoice component.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DropDownControl<T> extends AbstractControl<T> {

    private DropDownChoice<T> dropDownChoice;

    private boolean required;

    private IModel<String> nullOptionText;

    public DropDownControl(String id) {
        this(id, (List<T>) null);
    }

    public DropDownControl(String id, IModel<List<T>> choices) {
        this(id, null, choices);
    }

    public DropDownControl(String id, IModel<T> model, IModel<List<T>> choices) {

        super(id, model);

        dropDownChoice = new DropDownChoice<T>("component", new DelegateModel<T>(this), choices) {

            @Override
            protected CharSequence getDefaultChoice(final String selected) {

                if (nullOptionText == null) {

                    return super.getDefaultChoice(selected);

                } else {

                    boolean noSelection = selected == null || selected.equals("");

                    if (isNullValid() || noSelection) {

                        StringBuilder sb = new StringBuilder();

                        sb.append("\n<option");

                        if (noSelection) {
                            sb.append(" selected=\"selected\"");
                        }

                        sb.append(" value=\"\">").append(nullOptionText.getObject()).append("</option>");
                        return sb;

                    } else {

                        return "";

                    }
                }
            }

            @Override
            public boolean isEnabled() {
                return isEnabledInternal();
            };

            @Override
            public boolean isRequired() {
                return DropDownControl.this.isRequired();
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onDropDownComponentTag(tag);
            }
        };

        add(dropDownChoice);

    }

    public DropDownControl(String id, IModel<T> model, List<T> choices) {
        this(id, model, Model.ofList(choices));
    }

    public DropDownControl(String id, List<T> choices) {
        this(id, null, Model.ofList(choices));
    }

    @Override
    public DropDownChoice<T> getFormComponent() {
        return dropDownChoice;
    }

    public DropDownChoice<T> getDropDownChoice() {
        return dropDownChoice;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

//    public DropDownControl<T> onChange(final AjaxAction action) {
//        getFormComponent().add(new AjaxFormComponentUpdatingBehavior("onchange") {
//            @Override
//            protected void onUpdate(AjaxRequestTarget target) {
//                action.invoke(target);
//            }
//        });
//        return this;
//    }

    /**
     * Calls the provided action when the dropdown changes. Note that the model
     * object is updated when changed, as per Wicket's
     * AjaxFormComponentUpdatingBehavior. This means that action code can find
     * out the new value for this dropdown by inspecting the model object;
     * however, it means that even if the form is cancelled the model object
     * will have the new value.
     */
    public DropDownControl<T> onChange(final AjaxAction action) {
        dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                action.invoke(target);
            }
        });
        return this;
    }


    /**
     * Callback method called from the onComponentTag field of the DropDownControl.
     * Here we can do last-minute changes to the tag.
     *
     * <p>This is called at the end of onComponentTag after the parent
     * implementations have been invoked, so this method can override those
     * tag changes.
     *
     * <p>The default implementation does nothing.
     */
    protected void onDropDownComponentTag(ComponentTag tag) {

    }

    public DropDownControl<T> setChoiceRenderer(IChoiceRenderer<T> renderer) {
        getDropDownChoice().setChoiceRenderer(renderer);
        return this;
    }

    public DropDownControl<T> setChoices(IModel<? extends List<? extends T>> choices) {
        getDropDownChoice().setChoices(choices);
        return this;
    }

    public DropDownControl<T> setChoices(List<? extends T> choices) {
        return setChoices(Model.ofList(choices));
    }

    public DropDownControl<T> setNullOptionText(IModel<String> nullOptionText) {
        if (nullOptionText instanceof IComponentAssignedModel) {
            this.nullOptionText = ((IComponentAssignedModel<String>) nullOptionText).wrapOnAssignment(this);
        } else {
            this.nullOptionText = nullOptionText;
        }
        return this;
    }

    public DropDownControl<T> setNullValid(boolean nullValid) {
        getDropDownChoice().setNullValid(nullValid);
        return this;
    }

    @Override
    public DropDownControl<T> setRequired(boolean required) {
        this.required = required;
        return this;
    }

}
