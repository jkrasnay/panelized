package ca.krasnay.panelized.datatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

import ca.krasnay.panelized.BorderPanel;

/**
 * Common base class for DataTablePanel and GroupingDataTablePanel.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AbstractDataTablePanel<T> extends BorderPanel {

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

//    private AccessControlList accessControlList;

    private AccessController accessController;

    private List<DataTableAction<T>> actions = new ArrayList<DataTableAction<T>>();

    private List<IColumn<T, String>> columns;

    private IDataProvider<T> dataProvider;

    private DataTableAction<T> deleteAction;

    private DataTableAction<T> editAction;

    private String rowIdProperty = "id";

    private String rowNameProperty = "name";

    private DataTableAction<T> viewAction;


    /**
     * Popup shown if a multi action is about to be invoked, but can only be invoked
     * on a subset of the selected rows.
     */
//    private ConfirmationPopUpPanel<Void> multiActionConfirmPopup;

    /**
     * Holds the multi-action to be invoked while the multiActionConfirmPopup is
     * being displayed.
     */
//    private DataTableAction<T> multiAction;

    /**
     * Holds the list of row IDs to be passed to the multi-action while the
     * multiActionConfirmPopup is being displayed.
     */
//    private List<String> multiActionRowIds;


    public AbstractDataTablePanel(String id) {
        super(id);

        ajaxBehavior = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                onAction(target);
            }
        };
        add(ajaxBehavior);

//        multiActionConfirmPopup = new ConfirmationPopUpPanel<Void>(newPanelId(), "Confirm Action");
//        addPanel(multiActionConfirmPopup);
//        multiActionConfirmPopup.setEscapeHtml(false);
//        multiActionConfirmPopup.setSize(PopUpFormPanel.Size.LARGE);
//
//        multiActionConfirmPopup.addSaveButton("Continue", new AjaxAction() {
//            @Override
//            public void invoke(AjaxRequestTarget target) {
//                multiAction.invoke(target, multiActionRowIds);
//                multiActionConfirmPopup.hide(target);
//            }
//        });
//
//        multiActionConfirmPopup.addCancelButton();


    }

    public AbstractDataTablePanel<T> addAction(DataTableAction<T> action) {

        if (accessController == null) {
            throw new RuntimeException("You must set the access controller on the data table before adding actions");
        }

        if (accessController.canAccess(action)) {
            actions.add(action);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public AbstractDataTablePanel<T> addActionPanel(Panel actionPanel) {

        if (!(actionPanel instanceof DataTableAction)) {
            throw new RuntimeException("actionPanel must implement DataTableAction");
        }

        addPanel(actionPanel);

        addAction((DataTableAction<T>) actionPanel);

        return this;
    }

    /**
     * Adds a separator to the action menu. The panel ensures that a separator
     * will not appear at the start or end of the menu, nor will two separators
     * appear together, so caller can conditionally add other actions without
     * worrying about these cases for separators.
     */
    public final AbstractDataTablePanel<T> addSeparator() {
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

    public List<IColumn<T, String>> getColumns() {
        return columns;
    }

    @Override
    protected String getCssClass() {
        return "bdr-dataTable";
    }

    public abstract long getCurrentPage();

    public final IDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public DataTableAction<T> getDeleteAction() {
        return deleteAction;
    }

    public DataTableAction<T> getEditAction() {
        return editAction;
    }

    public final long getRowCount() {
        return dataProvider.size();
    }


    /**
     * Returns the identifier of the row for the purposes of identifying the
     * row to actions. By default, retrieves the value from the row
     * corresponding to the property name returned by {@link #getRowIdProperty()}.
     */
    protected String getRowId(T row) {
        Object value = PropertyResolver.getValue(getRowIdProperty(), row);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    /**
     * Returns the name of the property in the row object that returns the row
     * ID for the purpose of identifying the row to actions. Used by the default
     * implementation of {@link #getRowId(IModel)}. By default returns "id".
     */
    public String getRowIdProperty() {
        return rowIdProperty;
    }

    /**
     * Returns the name of the row. This is used to identify which rows cannot
     * be acted upon by a particular multi action. By default, returns the
     * value from the row corresponding to the property name returned
     * by {@link #getRowNameProperty()}.
     */
    protected String getRowName(T row) {
        Object value = PropertyResolver.getValue(getRowNameProperty(), row);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    protected String getRowNameProperty() {
        return rowNameProperty;
    }

    public abstract long getRowsPerPage();

    /**
     * Returns the RowStyle for the given row. By default returns null,
     * meaning that no special styling is applied to the row.
     */
//    protected RowStyle getRowStyle(IModel<T> rowModel) {
//        return null;
//    }

    public DataTableAction<T> getViewAction() {
        return viewAction;
    }

    boolean hasActions() {
        return getActions().size() > 0 || viewAction != null || editAction != null || deleteAction != null;
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

//    boolean hasTools() {
//        return isReorderable() || hasActions();
//    }

    /**
     * Initialize the given row container to populate the row ID and name
     * attributes and to apply the requested row style.
     */
    protected void initRowContainer(WebMarkupContainer rowContainer, T row) {

        if (hasActions()) {
            String rowId = getRowId(row);
            if (rowId != null) {
                rowContainer.add(new AttributeModifier("data-row-id", rowId));
            }

            String rowName = getRowName(row);
            if (rowName != null) {
                rowContainer.add(new AttributeModifier("data-row-name", rowName));
            }
        }

//        RowStyle rowStyle = getRowStyle(row);
//        if (rowStyle != null) {
//            rowContainer.add(new AttributeAppender("class", Model.of(rowStyle.name()), " "));
//        }

    }

    public abstract boolean isPaginated();

    /**
     * Returns true if this data table is re-orderable.
     * @return
     */
    protected boolean isReorderable() {
        return false;
    }

    public void onAction(AjaxRequestTarget target) {

        IRequestParameters req = RequestCycle.get().getRequest().getRequestParameters();

        String action = req.getParameterValue("action").toString();

        if (action != null) {

            int actionIndex = Integer.parseInt(action);
            DataTableAction<T> actionObj = getAllActions().get(actionIndex);

            String rowId = req.getParameterValue("rowId").toString();
            List<String> rowIds = Arrays.asList(rowId.split(","));
//            String checkedCount = req.getParameterValue("checkedCount").toString();
//            if (checkedCount != null && Integer.valueOf(checkedCount) > rowIds.size()) {
//
////                String[] badRows = req.getParameters("badRow"); // Limited to 10 in Javascript
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
//
//            } else {
                actionObj.invoke(target, rowIds);
//            }

        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {

//        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(AbstractDataTablePanel.class, "DataTable.js")));
//
//        String script = String.format("dt_init('#%s', %s, '%s');", getMarkupId(), isReorderable(), getCallbackUrl());
//        response.render(OnDomReadyHeaderItem.forScript(script));

    }

    public AbstractDataTablePanel<T>  setAccessController(AccessController accessController) {
        this.accessController = accessController;
        return this;
    }

    public AbstractDataTablePanel<T> setColumns(IColumn<T, String>... columns) {
        this.columns = new ArrayList<IColumn<T, String>>(Arrays.asList(columns));
        return this;
    }

    public AbstractDataTablePanel<T> setColumns(List<IColumn<T, String>> columns) {
        this.columns = columns;
        return this;
    }

    public AbstractDataTablePanel<T> setDataProvider(IDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
        return this;
    }

    public AbstractDataTablePanel<T> setDeleteAction(DataTableAction<T> deleteAction) {

        if (accessController == null) {
            throw new RuntimeException("You must set the access controller on the data table before adding actions");
        }

        if (accessController.canAccess(deleteAction)) {
            this.deleteAction = deleteAction;
        }

        return this;
    }


    @SuppressWarnings("unchecked")
    public AbstractDataTablePanel<T> setDeleteActionPanel(Panel deleteActionPanel) {

        if (!(deleteActionPanel instanceof DataTableAction)) {
            throw new RuntimeException("deleteActionPanel must implement DataTableAction");
        }

        addPanel(deleteActionPanel);

        setDeleteAction((DataTableAction<T>) deleteActionPanel);

        return this;
    }

    public AbstractDataTablePanel<T> setEditAction(DataTableAction<T> editAction) {

        if (accessController == null) {
            throw new RuntimeException("You must set the access controller on the data table before adding actions");
        }

        if (accessController.canAccess(editAction)) {
            this.editAction = editAction;
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public AbstractDataTablePanel<T> setEditActionPanel(Panel editActionPanel) {

        if (!(editActionPanel instanceof DataTableAction)) {
            throw new RuntimeException("editActionPanel must implement DataTableAction");
        }

        addPanel(editActionPanel);

        setEditAction((DataTableAction<T>) editActionPanel);

        return this;
    }

    public AbstractDataTablePanel<T> setRowIdProperty(String rowIdProperty) {
        this.rowIdProperty = rowIdProperty;
        return this;
    }

    public AbstractDataTablePanel<T> setRowNameProperty(String rowNameProperty) {
        this.rowNameProperty = rowNameProperty;
        return this;
    }

    public AbstractDataTablePanel<T> setViewAction(DataTableAction<T> viewAction) {

        if (accessController == null) {
            throw new RuntimeException("You must set the access controller on the data table before adding actions");
        }

        if (accessController.canAccess(viewAction)) {
            this.viewAction = viewAction;
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public AbstractDataTablePanel<T> setViewActionPanel(Panel viewActionPanel) {

        if (!(viewActionPanel instanceof DataTableAction)) {
            throw new RuntimeException("viewActionPanel must implement DataTableAction");
        }

        addPanel(viewActionPanel);

        setViewAction((DataTableAction<T>) viewActionPanel);

        return this;
    }


}
