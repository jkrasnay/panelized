package ca.krasnay.panelized.datatable;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Displays a dropdown for selecting the size a data table's pages.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class PageSizePanel extends Panel {

    private DataTablePanel<?> dataTablePanel;

    public PageSizePanel(String id, DataTablePanel<?> dataTablePanel) {

        super(id);

        this.dataTablePanel = dataTablePanel;

        setOutputMarkupId(true);

        List<Integer> choices = Arrays.asList(10, 20, 50, 100, Integer.MAX_VALUE);

        DropDownChoice<Integer> pageSize = new DropDownChoice<Integer>("pageSize", new PropertyModel<Integer>(dataTablePanel, "pageSize"), choices, new ChoiceRenderer<Integer>() {
            @Override
            public Object getDisplayValue(Integer object) {
                if (object == Integer.MAX_VALUE) {
                    return "All items";
                } else {
                    return object + " items";
                }
            }
        });

        add(pageSize);

        pageSize.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                getDataTablePanel().refresh(target);
            }
        });

    }

    public DataTablePanel<?> getDataTablePanel() {
        return dataTablePanel;
    }

}
