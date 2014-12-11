package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Abstract form item panel that displays a label to the left or top of its
 * input control.
 *
 * This component will add the CSS class "required" if it's first FormComponent
 * child is required.
 *
 * @author john
 */
public class LabelledPanel extends Panel implements PanelContainer {

    private String cssClass;

    private Label label;

    private ContainerPanel childItems;

    private boolean wide;

    public LabelledPanel(String id) {
        this(id, null);
    }

    public LabelledPanel(String id, IModel<?> model) {

        super(id, model);

        setOutputMarkupId(true);

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {

                String result = "labelled";

                if (wide) {
                    result += " labelled-wide";
                }

                if (cssClass != null) {
                    result += " " + cssClass;
                }

                return result;

            }
        }, " "));

        label = new Label("label", new ResourceModel(id));
        add(label);

        label.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return LabelledPanel.this.isRequired() ? "required" : null;
            }
        }, " "));

        label.add(new AttributeAppender("for", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                FormComponent<?> fc = getFormComponent();
                return fc == null ? null : fc.getMarkupId();
            }
        }, " "));

        childItems = new ContainerPanel("childItems");
        add(childItems);

    }

    public LabelledPanel addPanel(Component item) {
        childItems.addPanel(item);
        return this;
    }

    /**
     * Returns the main FormComponent for this panel, or null if there is no
     * such component. By default, returns the first child component that
     * implements FormComponent.
     *
     * <p>
     * The returned component is used for a few things:
     *
     * <ul>
     * <li>We add the "required" CSS class if the form component is required.
     * <li>It becomes the target of the "for" attribute for the label.
     * <li>The label model is injected into the form component for correct error
     * message display.
     * </ul>
     *
     * @return
     */
    public FormComponent<?> getFormComponent() {
        return visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, FormComponent<?>>() {
            @Override
            public void component(FormComponent<?> object, IVisit<FormComponent<?>> visit) {
                visit.stop(object);
            }
        });
    }

    public String getLabel() {
        return label.getDefaultModelObjectAsString();
    }

    /**
     * Returns true if this component is required. If so, the "required" star
     * is displayed, and the label is given the CSS class "required". By
     * default, if {@link #getFormComponent()} returns a non-null value, calls
     * isRequired() on that component, else returns false. Subclasses may
     * override this method to explicitly return true or false as required.
     */
    protected boolean isRequired() {
        FormComponent<?> fc = getFormComponent();
        if (fc != null) {
            return fc.isRequired();
        } else {
            return false;
        }
    }

    public boolean isWide() {
        return wide;
    }

    @Override
    public String newPanelId() {
        return childItems.newPanelId();
    }

    @Override
    protected void onBeforeRender() {

        if (!hasBeenRendered()) {

            //
            // Inject the label into the input. Use a model to support
            // localization.
            //

            FormComponent<?> fc = getFormComponent();

            if (fc != null && fc.getLabel() == null) {
                fc.setLabel(new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                        return getLabel();
                    }
                });
            }

        }

        super.onBeforeRender();
    }

    @Override
    public void removeAllPanels() {
        childItems.removeAllPanels();
    }

    public LabelledPanel setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public LabelledPanel setLabel(IModel<String> labelModel) {
        this.label.setDefaultModel(labelModel);
        return this;
    }

    public LabelledPanel setLabel(String label) {
        setLabel(Model.of(label));
        return this;
    }

    /**
     * Sets whether this is a wide field. Wide fields are given the full width
     * of the containing window.
     */
    public LabelledPanel setWide(boolean wide) {
        this.wide = wide;
        return this;
    }

}
