package ca.krasnay.panelized.testapp;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.LocalDate;

import ca.krasnay.panelized.ContainerPanel;
import ca.krasnay.panelized.DateFieldControl;

public class DatePickerTab extends AbstractTab {

    private LocalDate date;

    public DatePickerTab() {
        super(Model.of("Date Picker"));
    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

        ContainerPanel container = new ContainerPanel(panelId);
        container.addPanel(new DateFieldControl<LocalDate>(container.newPanelId(), LocalDate.class, new PropertyModel<LocalDate>(this, "date")));
        return container;
    }
}
