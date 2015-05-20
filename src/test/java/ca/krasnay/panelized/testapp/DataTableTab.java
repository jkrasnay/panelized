package ca.krasnay.panelized.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
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
import ca.krasnay.panelized.ToolStyle;
import ca.krasnay.panelized.datatable.CurrentPagePanel;
import ca.krasnay.panelized.datatable.DataTablePanel;
import ca.krasnay.panelized.datatable.PageButtonsPanel;
import ca.krasnay.panelized.datatable.PageSizePanel;
import ca.krasnay.panelized.datatable.ToolbarPanel;
import ca.krasnay.panelized.datatable.filter.AddFilterActionPanel;
import ca.krasnay.panelized.datatable.filter.FilterFactory;
import ca.krasnay.panelized.datatable.filter.FilterStatusPanel;
import ca.krasnay.panelized.datatable.filter.FilterableListDataProvider;
import ca.krasnay.panelized.datatable.filter.PrefixQuickFilter;
import ca.krasnay.panelized.datatable.filter.QuickFilterPanel;
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

        DataTablePanel<Widget> dataTablePanel = new DataTablePanel<Widget>(container.newPanelId());
        container.addPanel(dataTablePanel);

        //
        // Toolbars
        //

        ToolbarPanel toolbar = new ToolbarPanel(dataTablePanel.newTopToolbarId());

        FilterStatusPanel filterStatusPanel = new FilterStatusPanel(toolbar.newLeftItemId(), dataTablePanel);

        DropDownMenuPanel addFilterMenu = new DropDownMenuPanel(toolbar.newLeftItemId(), "filter", new DummyAccessController());
        addFilterMenu.setStyle(ToolStyle.HOVER_BUTTON);

        for (FilterFactory factory : filterFactories) {
            addFilterMenu.addAction(new AddFilterActionPanel(addFilterMenu.newPanelId(), dataTablePanel, factory, new RefreshAction(filterStatusPanel)));
        }

        toolbar.addLeftItem(new QuickFilterPanel(toolbar.newLeftItemId(), dataTablePanel).setSearchDelay(500));
        toolbar.addLeftItem(addFilterMenu);
        toolbar.addLeftItem(filterStatusPanel);

        toolbar.addRightItem(new PageSizePanel(toolbar.newRightItemId(), dataTablePanel));
        toolbar.addRightItem(new CurrentPagePanel(toolbar.newRightItemId(), dataTablePanel));
        toolbar.addRightItem(new PageButtonsPanel(toolbar.newRightItemId(), dataTablePanel));

        dataTablePanel.addTopToolbar(toolbar);


        //
        // Data Provider
        //

        List<Widget> widgets = new ArrayList<>();
        widgets.add(new Widget(1, "Macintosh Apple", Widget.Color.RED));
        widgets.add(new Widget(2, "Banana", Widget.Color.YELLOW));
        widgets.add(new Widget(3, "Blueberry", Widget.Color.BLUE));
        widgets.add(new Widget(4, "Radish", Widget.Color.RED));
        widgets.add(new Widget(5, "Red Pepper", Widget.Color.RED));
        widgets.add(new Widget(6, "Yellow Pepper", Widget.Color.YELLOW));
        widgets.add(new Widget(7, "Orange Pepper", Widget.Color.ORANGE));
        widgets.add(new Widget(8, "Orange", Widget.Color.ORANGE));
        widgets.add(new Widget(9, "Carrot", Widget.Color.ORANGE));
        widgets.add(new Widget(10, "Zuccini", Widget.Color.GREEN));
        widgets.add(new Widget(11, "Spinach", Widget.Color.GREEN));
        widgets.add(new Widget(12, "Granny Smith Apple", Widget.Color.GREEN));
        widgets.add(new Widget(13, "Eggplant", Widget.Color.PURPLE));
        widgets.add(new Widget(14, "Lemon", Widget.Color.YELLOW));
        widgets.add(new Widget(15, "Lime", Widget.Color.GREEN));
        widgets.add(new Widget(16, "Pineapple", Widget.Color.YELLOW));

        FilterableListDataProvider<Widget> dataProvider = new FilterableListDataProvider<Widget>(widgets);

        dataTablePanel.setDataProvider(dataProvider);

        dataProvider.addQuickFilter(new PrefixQuickFilter<Widget>("name"));


        //
        // Columns
        //

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

        dataTablePanel.setPageSize(10);

        return container;

    }
}
