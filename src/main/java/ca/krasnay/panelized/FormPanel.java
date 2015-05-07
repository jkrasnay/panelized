package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

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

        setOutputMarkupId(true);

        form = new Form<Void>("form") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
            @Override
            protected void onValidate() {
                FormPanel.this.onValidateInternal();
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

    /**
     * Called from the Form component's onComponentTag method so that the
     * subclass can override attributes.
     */
    protected void onFormComponentTag(ComponentTag tag) {
    }

    /**
     * Called as part of the form's validation process. By default, looks for
     * child components implementing the {@link FormValidatable} interface and
     * validating the returned form validators.
     */
    private void onValidateInternal() {

        validate();

        form.visitChildren(Component.class, new IVisitor<Component, Void>() {
            @Override
            public void component(Component component, IVisit<Void> visit) {
                if (component instanceof FormValidatable) {
                    IFormValidator childValidator = ((FormValidatable) component).getFormValidator();
                    if (childValidator != null) {
                        // TODO replicate functionality from Form.validateFormValidator
                        // (or just pass the child validator to that method)
                        // (since its protected final, we'd have to provide a wrapper method)
                        childValidator.validate(form);
                    }
                }
            }
        });

    }

    /**
     * Hook method that subclasses can override to perform form-level validation.
     * Note that child components implementing FormValidatable are automatically
     * called and need not be validated here.
     */
    protected void validate() {

    }

    @Override
    public void removeAllPanels() {
        panelRepeater.removeAll();
    }

}
