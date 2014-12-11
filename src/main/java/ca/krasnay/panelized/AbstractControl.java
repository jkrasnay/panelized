package ca.krasnay.panelized;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;

/**
 * Base class for controls. A control is a panel that encapsulates single
 * FormComponent or FormComponentPanel.
 *
 * Subclasses must call setFormComponent to set the component for this control
 * before the control is first rendered. The component passed to
 * setFormComponent must have the id from the COMPONENT_ID field.
 *
 * In general, the form component should be constructed as follows:
 *
 * <ol>
 * <li>It must be constructed with the ID COMPONENT_ID ("component").
 * <li>It should be constructed with a DelegateModel that delegates to this
 * component. This allows automatic model creation via CompoundPropertyModel to
 * work as expected.
 * <li>It should override its isRequired() method and delegate to this
 * component's isRequired() method.
 * <li>It should override its isEnabled() method and delegate to this
 * component's isEnabledInternal() method. This allows the component to be
 * disabled by the framework when it is the child of an unchecked
 * CheckablePanel.
 * </ol>
 *
 * The default markup for this panel has a single span to which the form
 * component is bound. Subclasses may need to implement their own markup in
 * order to get the correct element type (for example, TextFieldControl
 * implements its own markup with a &lt;span&gt; element.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AbstractControl<T> extends Panel {

    private boolean required;

    public AbstractControl(String id) {
        this(id, null);
    }

    public AbstractControl(String id, IModel<?> model) {
        super(id, model);
    }

    /**
     * An internal implementation of isEnabled(). Form components wrapped by
     * controls should delegate their isEnabled() method here. This method
     * checks to see if the control is a child of a checkable and disables the
     * control if the checkable is not checked. Otherwise, it delegates to this
     * classes isEnabled() method.
     */
    protected boolean isEnabledInternal() {

        CheckablePanel checkableParent = findParent(CheckablePanel.class);

        if (checkableParent != null) {

            return checkableParent.isChecked() ? isEnabled() : false;

        } else {

            return isEnabled();

        }
    }

    public boolean isRequired() {
        return required;
    }

    public AbstractControl<T> setRequired(boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Adds the given behavior to the text field embedded in this item.
     */
    public AbstractControl<T> addBehavior(Behavior behavior) {
        getFormComponent().add(behavior);
        return this;
    }

    /**
     * Adds the given validator to the text field embedded in this item.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public AbstractControl addValidator(IValidator<?> validator) {
        getFormComponent().add((IValidator) validator);
        return this;
    }

    public abstract FormComponent<T> getFormComponent();

    public AbstractControl<T> setType(Class<?> type) {
        getFormComponent().setType(type);
        return this;
    }


}
