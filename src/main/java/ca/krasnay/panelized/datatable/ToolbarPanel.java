package ca.krasnay.panelized.datatable;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

/**
 * Implementation of AbstractToolbar from the Wicket DataTable implementation.
 * Allows panels to be added to the left or right side in the Panelized style.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ToolbarPanel extends AbstractToolbar {

    private RepeatingView leftItemRepeater;

    private RepeatingView rightItemRepeater;

    public ToolbarPanel(DataTable<?, ?> table) {
        this(null, table);
    }

    public ToolbarPanel(IModel<?> model, DataTable<?, ?> table) {

        super(model, table);

        add(leftItemRepeater = new RepeatingView("leftItem"));
        add(rightItemRepeater = new RepeatingView("rightItem"));

    }

    public ToolbarPanel addLeftItem(Panel item) {
        leftItemRepeater.add(item);
        return this;
    }

    public ToolbarPanel addRightItem(Panel item) {
        rightItemRepeater.add(item);
        return this;
    }

    public String newLeftItemId() {
        return rightItemRepeater.newChildId();
    }

    public String newRightItemId() {
        return leftItemRepeater.newChildId();
    }
}
