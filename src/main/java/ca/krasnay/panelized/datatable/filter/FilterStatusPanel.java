package ca.krasnay.panelized.datatable.filter;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AccessController;
import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogPanel.DialogFrame;
import ca.krasnay.panelized.DropDownMenuPanel;
import ca.krasnay.panelized.NamedAjaxAction;
import ca.krasnay.panelized.SpanContainerPanel;
import ca.krasnay.panelized.TextPanel;
import ca.krasnay.panelized.datatable.DataTablePanel;

/**
 * Panel that displays the filters on a data table as a sentence, such as
 * "Displaying items where {filter1} and {filter2}". Each of the filter strings
 * display the current state of the filter and functions as a drop-down menu
 * allowing you to edit or delete the filter.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class FilterStatusPanel extends SpanContainerPanel {

    private DataTablePanel<?> dataTablePanel;

    private FilterDialog dialog;

    public FilterStatusPanel(String id, DataTablePanel<?> dataTablePanel) {

        super(id);

        this.dataTablePanel = dataTablePanel;

        dialog = new FilterDialog(newPanelId());

    }

    private Panel createFilterDropdown(String panelId, final DataTableFilter filter) {

        AccessController acl = new AccessController() {
            @Override
            public boolean canAccess(Class<?> uiElementClass) {
                return true;
            }
        };

        DropDownMenuPanel menu = new DropDownMenuPanel(panelId, Model.of(filter.getDisplayText(getLocale())), acl);

        menu.addAction(new NamedAjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {

                DialogFrame frame = dialog.buildFrame("Edit");

                dialog.setModel(filter);

                filter.buildEditor(frame);

                AjaxAction saveAction = new AjaxAction() {
                    @Override
                    public void invoke(AjaxRequestTarget target) {
                        target.add(dataTablePanel);
                        dialog.hide(target);
                    }
                };

                dialog.show(target, "Edit Filter", filter, saveAction);

            }

            @Override
            public String getName(Locale locale) {
                return "Edit Filter";
            }
        });

        menu.addAction(new NamedAjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                getDataProvider().removeFilter(filter);
                target.add(dataTablePanel);
            }
            @Override
            public String getName(Locale locale) {
                return "Delete Filter";
            }
        });

        return menu;
    }

    private FilterableDataProvider getDataProvider() {

        IDataProvider<?> dataProvider = dataTablePanel.getDataProvider();

        if (dataProvider instanceof FilterableDataProvider) {
            return (FilterableDataProvider) dataProvider;
        } else {
            throw new RuntimeException("FilterStatusPanel must be used with a FilterableDataProvider");
        }

    }

    @Override
    protected void onBeforeRender() {

        removeAllPanels();

        addPanel(dialog);

        List<DataTableFilter> filters = getDataProvider().getFilters();

        if (filters.size() == 0) {

            addPanel(new TextPanel(newPanelId(), "Displaying all items"));

        } else {

            boolean first = true;
            for (DataTableFilter filter : filters) {

                if (first) {
                    addPanel(new TextPanel(newPanelId(), "Displaying items where "));
                } else {
                    addPanel(new TextPanel(newPanelId(), " and "));
                }
                first = false;

                addPanel(createFilterDropdown(newPanelId(), filter));

            }
        }

        super.onBeforeRender();

    }

}
