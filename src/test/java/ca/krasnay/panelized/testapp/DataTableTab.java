package ca.krasnay.panelized.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.ContainerPanel;
import ca.krasnay.panelized.DropDownMenuPanel;
import ca.krasnay.panelized.DummyAccessController;
import ca.krasnay.panelized.EnumUtils;
import ca.krasnay.panelized.RefreshAction;
import ca.krasnay.panelized.datatable.DataTablePanel;
import ca.krasnay.panelized.datatable.ToolbarPanel;
import ca.krasnay.panelized.datatable.filter.AddFilterActionPanel;
import ca.krasnay.panelized.datatable.filter.FilterFactory;
import ca.krasnay.panelized.datatable.filter.FilterStatusPanel;
import ca.krasnay.panelized.datatable.filter.FilterableListDataProvider;
import ca.krasnay.panelized.testapp.Widget.Color;

public class DataTableTab extends AbstractTab {

    public DataTableTab() {
        super(Model.of("Data Table"));
    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

        ContainerPanel container = new ContainerPanel(panelId);

        final List<FilterFactory> filterFactories = new ArrayList<>();
        filterFactories.add(new WidgetColorFilterFactory());

        DataTablePanel<Widget> dataTablePanel = new DataTablePanel<Widget>(container.newPanelId()) {
            @Override
            protected void addTopToolbars(DataTable<Widget, String> dataTable) {

                ToolbarPanel toolbar = new ToolbarPanel(dataTable);

                FilterStatusPanel filterStatusPanel = new FilterStatusPanel(toolbar.newLeftItemId(), this);

                DropDownMenuPanel addFilterMenu = new DropDownMenuPanel(toolbar.newLeftItemId(), "filter", new DummyAccessController());
                addFilterMenu.setButtonLike(true);

                for (FilterFactory factory : filterFactories) {
                    addFilterMenu.addAction(new AddFilterActionPanel(addFilterMenu.newPanelId(), this, factory, new RefreshAction(filterStatusPanel)));
                }

                toolbar.addLeftItem(addFilterMenu);
                toolbar.addLeftItem(filterStatusPanel);

                dataTable.addTopToolbar(toolbar);

            }
        };
        container.addPanel(dataTablePanel);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(new Widget(1, "Apple", Widget.Color.RED));
        widgets.add(new Widget(2, "Banana", Widget.Color.YELLOW));
        widgets.add(new Widget(3, "Blueberry", Widget.Color.BLUE));

        FilterableListDataProvider<Widget> dataProvider = new FilterableListDataProvider<Widget>(widgets);

        dataTablePanel.setDataProvider(dataProvider);



        List<IColumn<Widget, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<Widget, String>(Model.of("Name"), "name", "name"));
        columns.add(new AbstractColumn<Widget, String>(Model.of("Color")) {
            @Override
            public void populateItem(Item<ICellPopulator<Widget>> cellItem, String componentId, IModel<Widget> rowModel) {
                Color color = rowModel.getObject().getColor();
                cellItem.add(new Label(componentId, EnumUtils.toString(color)));
            }
        });

        dataTablePanel.setColumns(columns);

        return container;

    }
}
