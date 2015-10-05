package ca.krasnay.panelized.datatable.filter;

import org.apache.wicket.ajax.AjaxRequestTarget;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.DialogPanel;

/**
 * Dialog for editing the state of a filter. The heavy lifting is done by
 * {@link DataTableFilter#buildEditor(ca.krasnay.panelized.PanelContainer)}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class FilterDialog extends DialogPanel {

    public FilterDialog(String id) {
        super(id);
    }

    public void show(AjaxRequestTarget target, String title, DataTableFilter filter, AjaxAction saveAction) {

        setModel(filter);

        DialogFrame frame = buildFrame(title);

        filter.buildEditor(frame);

        frame.addSaveButton(saveAction);
        frame.addCancelButton();

        super.show(target);

    }

    @Override
    protected void validate() {
        DataTableFilter filter = (DataTableFilter) getModelObject();
        filter.validate();
    }
}
