package ca.krasnay.panelized.datatable.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

import ca.krasnay.panelized.AccessController;
import ca.krasnay.panelized.datatable.AbstractDataTablePanel;

/**
 * Renders a column with buttons to invoke actions on data table rows.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ActionsColumn<T> extends AbstractColumn<T, String> {

    /**
     * Dummy action that acts as a placeholder for a separator in the action
     * menu.
     */
    public class SeparatorAction implements DataTableAction<T> {

        @Override
        public boolean acceptsMultiples() {
            return false;
        }
        @Override
        public String getName(Locale locale) {
            throw new RuntimeException("Separator action mistakenly invoked");
        }
        @Override
        public void invoke(AjaxRequestTarget target, List<String> rowIds) {
            throw new RuntimeException("Separator action mistakenly invoked");
        }
        @Override
        public boolean isEnabled(T row) {
            return true;
        }
        @Override
        public boolean isVisible(T row) {
            return true;
        }
    }

    private AbstractDefaultAjaxBehavior ajaxBehavior;

    private AccessController accessController;

    private List<DataTableAction<T>> actions = new ArrayList<DataTableAction<T>>();

    private DataTableAction<T> deleteAction;

    private DataTableAction<T> editAction;

    private DataTableAction<T> viewAction;

    private AbstractDataTablePanel<T> dataTablePanel;


//    /**
//     * Popup shown if a multi action is about to be invoked, but can only be invoked
//     * on a subset of the selected rows.
//     */
//    private ConfirmationPopUpPanel<Void> multiActionConfirmPopup;

//    /**
//     * Holds the multi-action to be invoked while the multiActionConfirmPopup is
//     * being displayed.
//     */
//    private DataTableAction<T> multiAction;

//    /**
//     * Holds the list of row IDs to be passed to the multi-action while the
//     * multiActionConfirmPopup is being displayed.
//     */
//    private List<String> multiActionRowIds;





    public ActionsColumn(AbstractDataTablePanel<T> dataTablePanel, AccessController accessController) {

        super(Model.of(""));

        this.dataTablePanel = dataTablePanel;
        this.accessController = accessController;

        ajaxBehavior = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                onAction(target);
            }
        };

        dataTablePanel.add(ajaxBehavior);

        dataTablePanel.addHeaderContributor(new IHeaderContributor() {
            @Override
            public void renderHead(IHeaderResponse response) {
                String script = String.format("Panelized.DataTable.initActions('#%s', '%s');", ActionsColumn.this.dataTablePanel.getMarkupId(), getCallbackUrl());
                response.render(OnDomReadyHeaderItem.forScript(script));
            }
        });


//      multiActionConfirmPopup = new ConfirmationPopUpPanel<Void>(newPanelId(), "Confirm Action");
//      addPanel(multiActionConfirmPopup);
//      multiActionConfirmPopup.setEscapeHtml(false);
//      multiActionConfirmPopup.setSize(PopUpFormPanel.Size.LARGE);
//
//      multiActionConfirmPopup.addSaveButton("Continue", new AjaxAction() {
//          @Override
//          public void invoke(AjaxRequestTarget target) {
//              multiAction.invoke(target, multiActionRowIds);
//              multiActionConfirmPopup.hide(target);
//          }
//      });
//
//      multiActionConfirmPopup.addCancelButton();

    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {

        T row = rowModel.getObject();

        StringBuilder html = new StringBuilder();

        if (hasMultiActions()) {
            html.append("<input type='checkbox'>");
        }

        String actionHtml = buildActionMenu(true, row);
        if (actionHtml != null) {
            html.append(actionHtml);
        }

        int directActionIndex = 0;

        if (viewAction != null) {
            html.append(String.format("<a href='#' class='pnl-Button pnl-Button--hover %s' title='View' data-action='%d'><i class='fa fa-eye'></i></a>",
                    viewAction.isEnabled(row) ? "" : "disabled",
                            directActionIndex));
            directActionIndex++;
        }

        if (editAction != null) {
            html.append(String.format("<a href='#' class='pnl-Button pnl-Button--hover %s' title='Edit' data-action='%d'><i class='fa fa-edit'></i></a>",
                    editAction.isEnabled(row) ? "" : "disabled",
                            directActionIndex));
            directActionIndex++;
        }

        if (deleteAction != null) {
            html.append(String.format("<a href='#' class='pnl-Button pnl-Button--hover %s' title='Delete' data-action='%d'><i class='fa fa-trash'></i></a>",
                    deleteAction.isEnabled(row) ? "" : "disabled",
                            directActionIndex));
        }

        cellItem.add(new Label(componentId, html.toString()).setEscapeModelStrings(false));
    }

    @Override
    public String getCssClass() {
        return "pnl-DataTable-actionsColumn";
    }

    @Override
    public Component getHeader(String componentId) {
        StringBuilder html = new StringBuilder();

        if (hasMultiActions()) {

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

        sb.append("<div class='pnl-DropDownMenu'>")
        .append("<a class='pnl-Button pnl-Button--hover pnl-DropDownMenu-toggle' title='Actions' href='#'><i class='fa fa-cog'></i></a>")
        .append("<ul class='pnl-DropDownMenu-menu'>");

        List<DataTableAction<T>> allActions = getAllActions();

        boolean pendingSeparator = false;
        boolean actionsAdded = false;

        for (int i = 0; i < allActions.size(); i++) {

            DataTableAction<T> action = allActions.get(i);

            if (action instanceof ActionsColumn.SeparatorAction) {
                if (actionsAdded) {
                    pendingSeparator = true;
                }
            } else {
                if (action == editAction) {
                    // skip
                } else if (action == deleteAction) {
                    // skip
                } else if (action == viewAction) {
                    // skip
                } else if ((singleRow && action.isVisible(row))
                        || (!singleRow && action.acceptsMultiples())) {

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

        return actionsAdded ? sb.toString() : null;
    }

    /**
     * Adds the given action. If the action is a panel, it is added as a child
     * to this container.
     */
    public ActionsColumn<T> addAction(DataTableAction<T> action) {

        if (accessController == null) {
            throw new RuntimeException("You must set the access controller on the data table before adding actions");
        }

        if (accessController.canAccess(action.getClass())) {
            actions.add(action);
        }

        if (action instanceof Panel) {
            dataTablePanel.addPanel((Panel) action);
        }

        return this;
    }

    /**
     * Adds a separator to the action menu. The panel ensures that a separator
     * will not appear at the start or end of the menu, nor will two separators
     * appear together, so caller can conditionally add other actions without
     * worrying about these cases for separators.
     */
    public final ActionsColumn<T> addSeparator() {
        actions.add(new SeparatorAction());
        return this;
    }

    /**
     * Returns the list of actions added via {@link #addAction(DataTableAction)}.
     * Does not include the edit and delete actions.
     * @return
     */
    public final List<DataTableAction<T>> getActions() {
        return actions;
    }

    /**
     * Returns all actions, including edit, delete, and view actions that are
     * not returned by {@link #getActions()}.
     */
    public final List<DataTableAction<T>> getAllActions() {

        List<DataTableAction<T>> allActions = new ArrayList<DataTableAction<T>>();

        if (viewAction != null) {
            allActions.add(viewAction);
        }

        if (editAction != null) {
            allActions.add(editAction);
        }

        if (deleteAction != null) {
            allActions.add(deleteAction);
        }

        allActions.add(new SeparatorAction());

        allActions.addAll(getActions());

        return allActions;
    }


    public String getCallbackUrl() {
        return ajaxBehavior.getCallbackUrl().toString();
    }

    public DataTableAction<T> getDeleteAction() {
        return deleteAction;
    }

    public DataTableAction<T> getEditAction() {
        return editAction;
    }

    public DataTableAction<T> getViewAction() {
        return viewAction;
    }

    /**
     * Returns true if the data table has any configured actions that apply to
     * multiple rows.
     */
    boolean hasMultiActions() {
        for (DataTableAction<T> action : getAllActions()) {
            if (action.acceptsMultiples()) {
                return true;
            }
        }
        return false;
    }

    public String newPanelId() {
        return dataTablePanel.newPanelId();
    }

    public ActionsColumn<T> setDeleteAction(DataTableAction<T> deleteAction) {

        this.deleteAction = deleteAction;

        if (deleteAction instanceof Panel) {
            dataTablePanel.addPanel((Panel) deleteAction);
        }

        return this;

    }

    public ActionsColumn<T> setEditAction(DataTableAction<T> editAction) {

        this.editAction = editAction;

        if (editAction instanceof Panel) {
            dataTablePanel.addPanel((Panel) editAction);
        }

        return this;

    }

    public ActionsColumn<T> setViewAction(DataTableAction<T> viewAction) {

        this.viewAction = viewAction;

        if (viewAction instanceof Panel) {
            dataTablePanel.addPanel((Panel) viewAction);
        }

        return this;

    }

    public void onAction(AjaxRequestTarget target) {

        IRequestParameters req = RequestCycle.get().getRequest().getRequestParameters();

        String action = req.getParameterValue("action").toString();

        if (action != null) {

            int actionIndex = Integer.parseInt(action);
            DataTableAction<T> actionObj = getAllActions().get(actionIndex);

            String rowId = req.getParameterValue("rowId").toString();
            List<String> rowIds = Arrays.asList(rowId.split(","));
            String checkedCount = req.getParameterValue("checkedCount").toString();
            if (checkedCount != null && Integer.valueOf(checkedCount) > rowIds.size()) {

                //                //              String[] badRows = req.getParameters("badRow"); // Limited to 10 in Javascript
                //                int badRowCount = Integer.valueOf(checkedCount) - rowIds.size();
                //
                //                multiAction = actionObj;
                //                multiActionRowIds = rowIds;
                //                multiActionConfirmPopup.setTitle("Confirm Action - " + actionObj.getName(Locale.ENGLISH).replace("...", ""));
                //
                //                StringBuilder message = new StringBuilder();
                //
                //                List<StringValue> badRows = req.getParameterValues("badRow"); // Limited to 10 in Javascript
                //
                //                if (badRows.size() == 0) {
                //
                //                    message.append(String.format("Can only invoke this action on %d of %s selected items. Would you like to continue?", rowIds.size(), checkedCount));
                //
                //                } else {
                //
                //                    message.append("<p>Cannot apply this action to the following items:</p><ul>");
                //
                //                    if (badRowCount > 10) {
                //                        int index = 0;
                //                        for (StringValue sv : badRows) {
                //                            message.append("<li>").append(StringEscapeUtils.escapeXml(sv.toString())).append("</li>");
                //                            index++;
                //                            if (index == 5) {
                //                                break;
                //                            }
                //                        }
                //
                //                        message.append(String.format("<li>(%d more)</li>", badRowCount - index));
                //
                //                    } else {
                //                        for (StringValue sv : badRows) {
                //                            message.append("<li>").append(StringEscapeUtils.escapeXml(sv.toString())).append("</li>");
                //                        }
                //                    }
                //
                //                    message.append(String.format("</ul><p>Would you like to apply the action to the remaining %d items?</p>", rowIds.size()));
                //
                //                }
                //
                //                multiActionConfirmPopup.setText(message.toString());
                //                multiActionConfirmPopup.show(target);

            } else {
                actionObj.invoke(target, rowIds);
            }

        }

    }

}
