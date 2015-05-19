package ca.krasnay.panelized.datatable.filter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import ca.krasnay.panelized.SearchFieldPanel;
import ca.krasnay.panelized.datatable.DataTablePanel;

/**
 * A specialization of SearchFieldPanel designed to work with data table panels.
 * Upon search, sets the quick filter string on the data provider associated
 * with the data table and adds the data table to the AjaxRequestTarget for refresh.
 * This field also makes itself invisible if the associated data provider does
 * not implement {@link QuickFilterDataProvider}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class QuickFilterPanel extends SearchFieldPanel {

    private DataTablePanel<?> dataTablePanel;

    public QuickFilterPanel(String id, DataTablePanel<?> dataTablePanel) {
        super(id);
        this.dataTablePanel = dataTablePanel;
    }

    private QuickFilterDataProvider getDataProvider() {
        IDataProvider<?> dataProvider = dataTablePanel.getDataProvider();
        return dataProvider instanceof QuickFilterDataProvider ? (QuickFilterDataProvider) dataProvider : null;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(getDataProvider() != null);
    }

    @Override
    protected void onSearch(AjaxRequestTarget target, String searchString) {

        QuickFilterDataProvider dataProvider = getDataProvider();

        if (dataProvider != null) {
            dataProvider.setQuickFilterString(searchString);
            dataTablePanel.refresh(target);
        }

    }


}
