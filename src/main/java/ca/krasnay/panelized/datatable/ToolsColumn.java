package ca.krasnay.panelized.datatable;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Renders the "tools" column, the first column in a datatable that facilitates
 * re-ordering and invoking row-specific actions.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ToolsColumn<T> extends AbstractColumn<T, String> {

    private AbstractDataTablePanel<T> dataTablePanel;

    public ToolsColumn(AbstractDataTablePanel<T> dataTablePanel) {
        super(Model.of(""));
        this.dataTablePanel = dataTablePanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {

        T row = rowModel.getObject();

        StringBuilder html = new StringBuilder();

        if (dataTablePanel.isReorderable()) {
            html.append("<span class='drag-handle' title='Drag to re-order'></span>");
        }

        if (dataTablePanel.hasMultiActions()) {
            html.append("<input type='checkbox'>");
        }

        String actionHtml = buildActionMenu(true, row);
        if (actionHtml != null) {
            html.append(actionHtml);
        }

        int directActionIndex = 0;

        if (dataTablePanel.getViewAction() != null) {
            html.append(String.format("<div class='btn-group'><a href='#' class='btn btn-mini %s' title='View' data-action='%d'><i class='icon-eye-open'></i></a></div>",
                    dataTablePanel.getViewAction().isEnabled(row) ? "" : "disabled",
                    directActionIndex));
            directActionIndex++;
        }

        if (dataTablePanel.getEditAction() != null) {
            html.append(String.format("<div class='btn-group'><a href='#' class='btn btn-mini %s' title='Edit' data-action='%d'><i class='icon-edit'></i></a></div>",
                    dataTablePanel.getEditAction().isEnabled(row) ? "" : "disabled",
                    directActionIndex));
            directActionIndex++;
        }

        if (dataTablePanel.getDeleteAction() != null) {
            html.append(String.format("<div class='btn-group'><a href='#' class='btn btn-mini %s' title='Delete' data-action='%d'><i class='icon-trash'></i></a></div>",
                    dataTablePanel.getDeleteAction().isEnabled(row) ? "" : "disabled",
                    directActionIndex));
        }

        cellItem.add(new Label(componentId, html.toString()).setEscapeModelStrings(false));
    }

    @Override
    public String getCssClass() {
        return "dt-tools";
    }

    @Override
    public Component getHeader(String componentId) {
        StringBuilder html = new StringBuilder();

        if (dataTablePanel.isReorderable()) {
            html.append("<span class='drag-handle'></span>");
        }

        if (dataTablePanel.hasMultiActions()) {

            html.append("<input type='checkbox'>");

            String actionHtml = buildActionMenu(false, null);
            if (actionHtml != null) {
                html.append(actionHtml);
            }
        }

        return new Label(componentId, html.toString())
        .setEscapeModelStrings(false);
    }

    /**
     * Returns the HTML for an action menu for this data table.
     *
     * @param singleRow
     *            True if the action menu is for a single row, false if the
     *            action menu is for multiple rows via the checkboxes.
     * @param row
     *            Row for which to generate actions.
     */
    String buildActionMenu(boolean singleRow, T row) {

        StringBuilder sb = new StringBuilder();
//        sb.append("<div class='btn-group'>");

        sb.append("<div class='pnl-DropDownMenu'>")
        .append("<a class='pnl-Button pnl-DropDownMenu-toggle' title='Actions' href='#'><i class='fa fa-cog'></i></a>")
        .append("<ul class='pnl-DropDownMenu-menu'>");

        List<DataTableAction<T>> allActions = dataTablePanel.getAllActions();

        boolean pendingSeparator = false;
        boolean actionsAdded = false;

        for (int i = 0; i < allActions.size(); i++) {

            DataTableAction<T> action = allActions.get(i);

            if (action instanceof AbstractDataTablePanel.SeparatorAction) {
                if (actionsAdded) {
                    pendingSeparator = true;
                }
            } else {
                if ((singleRow && action.isVisible(row)) || (!singleRow && action.acceptsMultiples())) {

                    if (pendingSeparator) {
                        sb.append("<li class='divider'></li>\n");
                        pendingSeparator = false;
                    }

                    String cssClass = "";
                    if (!singleRow) {
                        cssClass = "disabled";
                    } else if (!action.isEnabled(row)) {
                        cssClass = "disabled";
                    }

                    sb.append("<li>");
                    sb.append(String.format("<a href='#' data-action='%d' class='%s'>%s</a>",
                            i, cssClass, action.getName(Locale.ENGLISH)));
                    sb.append("</li>\n");

                    actionsAdded = true;
                }
            }
        }

        sb.append("</ul>");
        sb.append("</div>");

        return sb.toString();
    }


}
