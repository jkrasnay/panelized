package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Panel that shows an edit button to the left and one more child panels to
 * the right. Clicking the edit button invokes the given AjaxAction.
 *
 * @author john
 */
public class EditablePanel extends Panel implements PanelContainer {

    private AjaxAction action;

    private ContainerPanel actionPanels;

    private ContainerPanel childItems;

    private boolean leaveSpace;

    public EditablePanel(String id) {
        this(id, null);
    }

    public EditablePanel(String id, IModel<?> model) {

        super(id, model);

        setOutputMarkupId(true);

        actionPanels = new ContainerPanel("actionPanels");
        add(actionPanels);

        WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
        add(wrapper);

        wrapper.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return (action == null && !leaveSpace) ? "pnl-Editable--disabled" : "pnl-Editable";
            }
        }, " "));

        wrapper.add(new AjaxLink<Void>("link") {

            @Override
            public boolean isVisible() {
                return action != null;
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                action.invoke(target);
            }
        });

        childItems = new ContainerPanel("childItems");
        wrapper.add(childItems);

    }

    @Override
    public PanelContainer addPanel(Component panel) {
        childItems.addPanel(panel);
        return this;
    }

    @Override
    public String newPanelId() {
        return childItems.newPanelId();
    }

    @Override
    public void removeAllPanels() {
        childItems.removeAllPanels();
    }

    public EditablePanel setAction(AjaxAction action) {

        assert this.action == null : "EditablePanel can only accept one action";

        this.action = action;

        if (action instanceof Panel) {
            actionPanels.addPanel((Panel) action);
        }

        return this;
    }

    /**
     * Sets whether to leave space to the left of the panel even if there's no
     * action. This allows the panel to line up with others that do have
     * actions.
     */
    public EditablePanel setLeaveSpace(boolean leaveSpace) {
        this.leaveSpace = leaveSpace;
        return this;
    }

}
