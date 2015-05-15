package ca.krasnay.panelized.datatable;

import org.apache.wicket.model.AbstractReadOnlyModel;

import ca.krasnay.panelized.TextPanel;

/**
 * Displays the text "Showing 1 to 10 of 100 items" in a data table toolbar.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class CurrentPagePanel extends TextPanel {

    private DataTablePanel<?> dataTablePanel;

    public CurrentPagePanel(String id, DataTablePanel<?> dataTablePanel) {

        super(id);

        this.dataTablePanel = dataTablePanel;

        setDefaultModel(new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {

//                IDataProvider<?> dataProvider = getDataTablePanel().getDataProvider();

                long rowCount = getDataTablePanel().getRowCount();
                long from = rowCount == 0 ? 0 : (getDataTablePanel().getCurrentPage() * getDataTablePanel().getRowsPerPage()) + 1;
                long to = Math.min(rowCount, from + getDataTablePanel().getRowsPerPage() - 1);

//                if (dataProvider instanceof FilterableDataProvider
//                        && ((FilterableDataProvider) dataProvider).isFiltered()) {
//
//                    int unfilteredCount = ((FilterableDataProvider) dataProvider).getUnfilteredCount();
//
//                    return String.format("Showing %d to %d of %d items (from %d)",
//                            from, to, rowCount, unfilteredCount);
//
//                } else {
                    if (getDataTablePanel().isPaginated()) {
                        return String.format("Showing %d to %d of %d items",
                                from, to, rowCount);
                    } else {
                        if (rowCount == 1) {
                            return String.format("%d item", rowCount);
                        } else {
                            return String.format("%d items", rowCount);
                        }
                    }
//                }
            }
        });
    }

    public DataTablePanel<?> getDataTablePanel() {
        return dataTablePanel;
    }


}
