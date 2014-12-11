package ca.krasnay.panelized;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * Base panel for components that are "checkable": check boxes and radio
 * buttons. This base class implements the label, note, and child panels.
 * Subclasses implement the actual HTML input of the correct type.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class CheckablePanel extends Panel implements IHeaderContributor, PanelContainer {

    private boolean heading;

    private Label label;

    private RepeatingView inlinePanelRepeater;

    private ContainerPanel childItems;

    private boolean tall;

    public CheckablePanel(String id) {
        this(id, null);
    }

    public CheckablePanel(String id, IModel<Boolean> model) {

        super(id, model);

        add(new AttributeAppender("class", Model.of("checkable"), " "));

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return tall ? "checkable-tall" : null;
            }
        }, " "));

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return heading ? "checkable-heading" : null;
            }
        }, " "));

        label = new Label("label", new ResourceModel(id));
        add(label);

        inlinePanelRepeater = new RepeatingView("inlinePanel");
        add(inlinePanelRepeater);

        childItems = new ContainerPanel("childItems") {
            public boolean isEnabled() {
                // Disable validations for children when we're not checked
                return isChecked();
            };
        };
        add(childItems);

    }

    /**
     * Adds an inline panel to the checkable. Inline panels are displayed to the right of the label.
     * Use {@link #newPanelId()} if you need a unique ID for the inline panel.
     */
    public CheckablePanel addInlinePanel(Panel panel) {
        inlinePanelRepeater.add(panel);
        return this;
    }

    public PanelContainer addPanel(Panel item) {
        return childItems.addPanel(item);
    }

    public abstract Component getCheckable();

    public abstract boolean isChecked();

    @Override
    public String newPanelId() {
        return childItems.newPanelId();
    }

    @Override
    protected void onBeforeRender() {

        if (!hasBeenRendered()) {
            Component checkable = getCheckable();
            checkable.setOutputMarkupId(true);
            label.add(new AttributeModifier("for", checkable.getMarkupId()));
            checkable.add(new AttributeAppender("class", Model.of("checkable"), " "));
        }

        super.onBeforeRender();
    }

    @Override
    public void removeAllPanels() {
        childItems.removeAllPanels();
    }

    /**
     * Sets whether this checkable functions as a heading for a group of subordinate controls.
     * This simply affects the styling of the control.
     */
    public CheckablePanel setHeading(boolean heading) {
        this.heading = heading;
        return this;
    }

    public CheckablePanel setLabel(IModel<String> labelModel) {
        label.setDefaultModel(labelModel);
        return this;
    }

    public CheckablePanel setLabel(String label) {
        return setLabel(Model.of(label));
    }

    /**
     * Sets whether this checkable is "tall". Tall checkables have an input box to the
     * right of their labels. We need this hint so that we can push the checkbox/radio
     * down a little bit with CSS.
     */
    public CheckablePanel setTall(boolean tall) {
        this.tall = tall;
        return this;
    }

}
