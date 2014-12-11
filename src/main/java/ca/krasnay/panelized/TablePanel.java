package ca.krasnay.panelized;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Lightweight table.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class TablePanel extends Panel {

    private boolean clickable;

    private RepeatingView headerRowRepeater;

    private RepeatingView rowRepeater;

    public TablePanel(String id) {

        super(id);
        setOutputMarkupId(true);

        WebMarkupContainer table = new WebMarkupContainer("table");
        add(table);

        table.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return clickable ? "lightTable-clickable" : null;
            }
        }, " "));

        table.add(headerRowRepeater = new RepeatingView("headerRow"));
        table.add(rowRepeater = new RepeatingView("row"));
    }

    public void addHeaderRow(TableHeaderRowPanel row) {
        headerRowRepeater.add(row);
    }

    public void addRow(TableRowPanel row) {
        rowRepeater.add(row);
    }

    public String newHeaderRowId() {
        return headerRowRepeater.newChildId();
    }

    public String newRowId() {
        return rowRepeater.newChildId();
    }

    public void removeAllRows() {
        rowRepeater.removeAll();
    }

    public TablePanel setClickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    /**
     * Utility method that adds a {@link TableHeaderRowPanel} and adds each
     * of the given strings as a cell in the header.
     */
    public TablePanel setHeadings(String... headings) {
        TableHeaderRowPanel header = new TableHeaderRowPanel(newHeaderRowId());
        addHeaderRow(header);
        for (String heading : headings) {
            header.addCell(new TextPanel(header.newCellId(), heading));
        }
        return this;
    }

}
