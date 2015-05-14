package ca.krasnay.panelized.datatable;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Simple panel wrapper for Wicket's DataTable component. Used internally
 * by DataTablePanel.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class InternalDataTablePanel<T, S> extends Panel {

    private DataTable<T, S> dataTable;

    public InternalDataTablePanel(String id, final DataTablePanel<T> dataTablePanel, List<? extends IColumn<T, S>> columns, IDataProvider<T> dataProvider, long rowsPerPage) {

        super(id);

        add(dataTable = new DataTable<T, S>("table", columns, dataProvider, rowsPerPage) {
            @Override
            protected Item<T> newRowItem(String id, int index, IModel<T> model) {

                Item<T> item = super.newRowItem(id, index, model);
                T row = model.getObject();

                String rowId = dataTablePanel.getRowId(row);
                if (rowId != null) {
                    item.add(new AttributeModifier("data-row-id", rowId));
                }

                String rowName = dataTablePanel.getRowName(row);
                if (rowName != null) {
                    item.add(new AttributeModifier("data-row-name", rowName));
                }

//                RowStyle rowStyle = getRowStyle(row);
//                if (rowStyle != null) {
//                    item.add(new AttributeAppender("class", Model.of(rowStyle.name()), " "));
//                }

                return item;
            }
        });

        dataTable.setOutputMarkupId(true);


    }

    public DataTable<T, S> getDataTable() {
        return dataTable;
    }

}
