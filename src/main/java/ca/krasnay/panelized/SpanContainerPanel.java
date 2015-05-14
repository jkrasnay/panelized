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
public class SpanContainerPanel extends Panel implements PanelContainer {

    private RepeatingView panelRepeater;

    private String cssClass;

    private String itemCssClass;

    public SpanContainerPanel(String id) {
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
    public SpanContainerPanel(String id, FormComponentPanel<?> formComponentPanel) {
        this(id);
        panelRepeater.add(formComponentPanel);
    }

    public SpanContainerPanel(String id, IModel<?> model) {

        super(id, model);

        setOutputMarkupId(true);

        add(panelRepeater = new RepeatingView("panel"));

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return getCssClass();
            }
        }, " "));

    }

    public SpanContainerPanel addPanel(Component panel) {

        panel.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return getItemCssClass();
            }
        }, " "));

        panelRepeater.add(panel);
        return this;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getItemCssClass() {
        return itemCssClass;
    }

    public String newPanelId() {
        return panelRepeater.newChildId();
    }

    @Override
    public void removeAllPanels() {
        panelRepeater.removeAll();
    }

    public SpanContainerPanel setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public SpanContainerPanel setItemCssClass(String itemCssClass) {
        this.itemCssClass = itemCssClass;
        return this;
    }

}
