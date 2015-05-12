package ca.krasnay.panelized.datatable.filter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogActionPanel;
import ca.krasnay.panelized.DialogPanel;
import ca.krasnay.panelized.NamedAjaxAction;
import ca.krasnay.panelized.datatable.DataTablePanel;

public class AddFilterActionPanel extends DialogActionPanel implements NamedAjaxAction {

    private DataTablePanel<?> dataTablePanel;

    private FilterDialog dialog;

    private DataTableFilter filter;

    private FilterFactory filterFactory;

    public AddFilterActionPanel(String id, DataTablePanel<?> dataTablePanel, FilterFactory filterFactory, AjaxAction onSaveAction) {

        super(id, onSaveAction);

        this.dataTablePanel = dataTablePanel;
        this.filterFactory = filterFactory;

        addPanel(dialog = new FilterDialog(newPanelId()));

    }

    @Override
    protected String getActionName() {
        return filterFactory.getName(getLocale());
    }

    @Override
    protected DialogPanel getDialog() {
        return dialog;
    }

    @Override
    protected void handleSaveException(AjaxRequestTarget target, Exception e) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            RuntimeException re = new RuntimeException(e.getMessage());
            re.setStackTrace(e.getStackTrace());
            throw re;
        }
    }

    @Override
    public void invoke(AjaxRequestTarget target) {
        filter = filterFactory.createFilter();
        dialog.show(target, "Add Filter", filter, getSaveAction());
    }

    @Override
    protected void save(AjaxRequestTarget target) {
        getDataProvider().addFilter(filter);
        target.add(dataTablePanel);
    }

    private FilterableDataProvider getDataProvider() {

        IDataProvider<?> dataProvider = dataTablePanel.getDataProvider();

        if (dataProvider instanceof FilterableDataProvider) {
            return (FilterableDataProvider) dataProvider;
        } else {
            throw new RuntimeException("FilterStatusPanel must be used with a FilterableDataProvider");
        }

    }

}
