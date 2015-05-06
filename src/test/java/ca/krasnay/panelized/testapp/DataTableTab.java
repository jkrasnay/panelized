package ca.krasnay.panelized.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.ContainerPanel;
import ca.krasnay.panelized.datatable.DataTablePanel;

public class DataTableTab extends AbstractTab {

    public DataTableTab() {
        super(Model.of("Data Table"));
    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

        ContainerPanel container = new ContainerPanel(panelId);

        DataTablePanel<Widget> dataTablePanel = new DataTablePanel<>(container.newPanelId());
        container.addPanel(dataTablePanel);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(new Widget(1, "Apple", Widget.Color.RED));
        widgets.add(new Widget(2, "Banana", Widget.Color.YELLOW));
        widgets.add(new Widget(3, "Blueberry", Widget.Color.BLUE));

        IDataProvider<Widget> dataProvider = new ListDataProvider<Widget>(widgets);

        dataTablePanel.setDataProvider(dataProvider);

        List<IColumn<Widget, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<Widget, String>(Model.of("Name"), "name"));

        dataTablePanel.setColumns(columns);

        return container;

    }
}
