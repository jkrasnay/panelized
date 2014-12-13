package ca.krasnay.panelized;

import org.apache.wicket.markup.html.form.validation.IFormValidator;

/**
 * Interface implemented by components that can provide a form validator. This
 * allows a component to implement form validation (i.e. validation across
 * multiple form components) without having access to the form proper. It also
 * simplifies removal of such a component without worrying about removing the
 * validator from the form.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface FormValidatable {

    /**
     * Returns the form validator used by this component, or null if the component
     * has no such validator.
     */
    public IFormValidator getFormValidator();

}
