package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * Base class for panels that simply contain a list of other panels, each in
 * its own div.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ContainerPanel extends Panel implements PanelContainer {

    private RepeatingView panelRepeater;

    private String cssClass;

    private boolean renderDivs;

    public ContainerPanel(String id) {
        this(id, (IModel<?>) null);
    }

    /**
     * Constructor that takes adds a single FormComponentPanel to this
     * container. This allows ContainerPanel to act as an adapter, allowing
     * FormComponentPanel to be added to PanelContainers. This is required
     * because FormComponentPanel does not extend Panel.
     *
     * @param id
     *            Panel ID.
     * @param formComponentPanel
     *            FormComponentPanel to be added.
     */
    public ContainerPanel(String id, FormComponentPanel<?> formComponentPanel) {
        this(id);
        panelRepeater.add(formComponentPanel);
    }

    public ContainerPanel(String id, IModel<?> model) {

        super(id, model);

        setOutputMarkupId(true);

        add(panelRepeater = new RepeatingView("panel") {
            @Override
            protected void renderChild(Component child) {
                child.setRenderBodyOnly(!renderDivs);
                super.renderChild(child);
            }
        });

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return getCssClass();
            }
        }, " "));

    }

    public ContainerPanel addPanel(Component panel) {
        panelRepeater.add(panel);
        return this;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String newPanelId() {
        return panelRepeater.newChildId();
    }

    @Override
    public void removeAllPanels() {
        panelRepeater.removeAll();
    }

    public ContainerPanel setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    /**
     * Sets whether the container panel renders a div around each child.
     */
    public ContainerPanel setRenderDivs(boolean renderDivs) {
        this.renderDivs = renderDivs;
        return this;
    }
}
