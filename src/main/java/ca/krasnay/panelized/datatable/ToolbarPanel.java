package ca.krasnay.panelized.datatable;

import org.apache.wicket.markup.html.panel.Panel;

import ca.krasnay.panelized.SpanContainerPanel;

/**
 * Implementation of AbstractToolbar from the Wicket DataTable implementation.
 * Allows panels to be added to the left or right side in the Panelized style.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ToolbarPanel extends Panel {

    private SpanContainerPanel leftItems;

    private SpanContainerPanel rightItems;

    public ToolbarPanel(String id) {

        super(id);

        add(leftItems = new SpanContainerPanel("left").setItemCssClass("pnl-ToolbarPanel-leftItem"));
        add(rightItems = new SpanContainerPanel("right").setItemCssClass("pnl-ToolbarPanel-rightItem"));

    }

    public ToolbarPanel addLeftItem(Panel item) {
        leftItems.addPanel(item);
        return this;
    }

    public ToolbarPanel addRightItem(Panel item) {
        rightItems.addPanel(item);
        return this;
    }

    public String newLeftItemId() {
        return rightItems.newPanelId();
    }

    public String newRightItemId() {
        return leftItems.newPanelId();
    }
}
