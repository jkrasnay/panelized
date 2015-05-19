package ca.krasnay.panelized.datatable;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import ca.krasnay.panelized.PanelContainer;

/**
 * Panel that displays a data table.
 *
 * Features:
 * - is a panel, need not be attached to a table element
 * - has top/bottom toolbars
 *      - note that we don't use DataTable's toolbar mechanism; too limiting for AJAX refresh,
 *        This means that if you don't make the table width 100%, the toolbars will be wider
 *        than the data table
 *      - part of this component, rather than just siblings, so an app
 *        can define a common "standard" data table complete with required
 *        toolbars
 * - adds "data-" attributes to tr elements indicating row ID and row name, to be used
 *   by actions
 * - is a panel container, so that actions can
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DataTablePanel<T> extends AbstractDataTablePanel<T> {

//    private static final Logger log = Logger.getLogger(DataTablePanel.class);

    private static final int DEFAULT_PAGE_SIZE = 50;

    private RepeatingView bottomToolbarRepeater;

    private int columnAliasIndex;

    // Current page of a paginated table
    // We can't just keep this in the underlying DataTable since we can
    // re-build it, e.g. to add a new column
    private long currentPage = 0;

    private DataTable<T, String> dataTable;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private boolean initialized;

    private InternalDataTablePanel<T, String> internalDataTable;

    private boolean paginated = true;

    // Repeater for invisible "utility" panels such as actions
    private RepeatingView panelRepeater;

    // List of components to be AJAX-refreshed. Toolbar panels such as the
    // CurrentPagePanel that depend on the state of the data table and/or
    // it's data provider add themselves to this list
    private List<Component> refreshComponents = new ArrayList<>();

    private boolean reorderable;

    private RepeatingView topToolbarRepeater;

    public DataTablePanel(String id) {

        super(id);

        setOutputMarkupId(true);

        add(topToolbarRepeater = new RepeatingView("topToolbar"));
        add(panelRepeater = new RepeatingView("panel"));
        add(bottomToolbarRepeater = new RepeatingView("bottomToolbar"));

    }

//    /**
//     * Adds any desired top toolbars to the data table. Due to the way toolbars
//     * are implemented in Wicket, toolbars must be added by overriding this
//     * method so that we have access to the DataTable in the toolbar
//     * constructor.
//     *
//     * <p>See {@link ToolbarPanel} for an AbstractToolbar implementation in the
//     * Panelized style.
//     */
//    protected void addTopToolbars(DataTable<T, String> dataTable) {
//    }

    public DataTablePanel<T> addBottomToolbar(Component toolbar) {
        bottomToolbarRepeater.add(toolbar);
        return this;
    }

    /**
     * Adds the given component to be refreshed when
     * {@link #refresh(AjaxRequestTarget)} is called. These are usually toolbar
     * child panels whose state depends on the state of the data table panel.
     * Typically, such panels accept the DataTablePanel in their constructors
     * and call this method themselves.
     */
    public DataTablePanel<T> addForRefresh(Component component) {
        refreshComponents.add(component);
        return this;
    }

    @Override
    public PanelContainer addPanel(Component panel) {
        panelRepeater.add(panel);
        return this;
    }

    public DataTablePanel<T> addTopToolbar(Component toolbar) {
        topToolbarRepeater.add(toolbar);
        return this;
    }

    private String allocateColumnAlias() {
        return "c" + columnAliasIndex++;
    }

    /**
     * Builds the data table component. We may need to do this occasionally,
     * since filters can add columns to the result, and the only way to change
     * the columns is to re-create the data table.
     */
    @SuppressWarnings("unchecked")
    private void buildDataTable() {

        List<IColumn<T, String>> allColumns = new ArrayList<IColumn<T, String>>(getColumns());

        dataTable = new DataTable<T, String>("table", allColumns, getDataProvider(), pageSize) {
            @Override
            protected Item<T> newRowItem(String id, int index, IModel<T> model) {

                Item<T> item = super.newRowItem(id, index, model);
                T row = model.getObject();

                String rowId = getRowId(row);
                if (rowId != null) {
                    item.add(new AttributeModifier("data-row-id", rowId));
                }

                String rowName = getRowName(row);
                if (rowName != null) {
                    item.add(new AttributeModifier("data-row-name", rowName));
                }

//                RowStyle rowStyle = getRowStyle(row);
//                if (rowStyle != null) {
//                    item.add(new AttributeAppender("class", Model.of(rowStyle.name()), " "));
//                }

                return item;
            }
        };

        dataTable.setOutputMarkupId(true);

        addOrReplace(dataTable);

//        internalDataTable = new InternalDataTablePanel<T, String>(border.newPanelId(), this, allColumns, getDataProvider(), paginated ? pageSize : Integer.MAX_VALUE);
//        internalDataTable.setOutputMarkupId(true);
//
//        border.removeAllPanels();
//        border.addPanel(internalDataTable);

        ISortStateLocator<String> sortStateLocator = null;
        if (getDataProvider() instanceof ISortStateLocator) {
            sortStateLocator = (ISortStateLocator<String>) getDataProvider();
        }

//        DataTable<T, String> dataTable = internalDataTable.getDataTable();
//
//        addTopToolbars(dataTable);

        dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, sortStateLocator) {
            @Override
            protected WebMarkupContainer newSortableHeader(String headerId, String property, ISortStateLocator<String> locator) {
                return new AjaxFallbackOrderByBorder<String>(headerId, property, locator) {
                    @Override
                    protected void onAjaxClick(AjaxRequestTarget target) {
                        // TODO (lib) need some sort of callback, so the page URL can be updated by the app
                        //target.add(dataTable); // Seems to make our headers repeat (but not others?!)
                        //target.add(DataTablePanel.this);
                        refresh(target);
                    }

                    @Override
                    protected void onSortChanged() {
                        super.onSortChanged();
                        DataTablePanel.this.setCurrentPage(null, 0);
                    }

                };
            }

        });

        dataTable.setCurrentPage(currentPage);

        // TODO (lib) probably belongs here, but later
//        boolean showTotals = false;
//        for (IColumn<?, String> column : allColumns) {
//            if (column instanceof TotalledColumn && ((TotalledColumn<T>) column).isTotalled()) {
//                showTotals = true;
//            }
//        }
//
//        if (showTotals) {
//            dataTable.addBottomToolbar(new TotalsToolbar<T>(dataTable));
//        }

    }

    @Override
    public long getCurrentPage() {
        return currentPage;
    }

    private DataTable<T, String> getDataTable() {
        return dataTable;
    }

    /**
     * Returns the plural term for items in the list. Returns "items" by
     * default. Used by the advanced filter panel to describe what is being
     * shown.
     */
    public String getItemNamePlural() {
        return "items";
    }

    public long getPageCount() {
        return getDataTable().getPageCount();
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getRowsPerPage() {
        return paginated ? pageSize : Integer.MAX_VALUE;
    }

    public boolean isFirstPage() {
        return getCurrentPage() == 0;
    }

    public boolean isLastPage() {
        return getCurrentPage() >= getPageCount() - 1;
    }

    @Override
    public boolean isPaginated() {
        return paginated; // && pageSize < Integer.MAX_VALUE;
    }

    @Override
    protected boolean isReorderable() {
        return reorderable;
    }

    public String newBottomToolbarId() {
        return bottomToolbarRepeater.newChildId();
    }

    // TODO (lib) move to action column
//    /**
//     * Called when the data table is reorderable and the user drags a row to a
//     * new position in the data table. The default implementation does nothing.
//     *
//     * @param target
//     *            AjaxRequestTarget under which the item was moved.
//     * @param fromIndex
//     *            Current index of the item to be moved.
//     * @param toIndex
//     *            Desired index of the item to be moved.
//     */
//    protected void onMove(AjaxRequestTarget target, int fromIndex, int toIndex) {
//
//    }

    @Override
    public String newPanelId() {
        return panelRepeater.newChildId();
    }

    public String newTopToolbarId() {
        return topToolbarRepeater.newChildId();
    }

    public void nextPage(AjaxRequestTarget target) {
        setCurrentPage(target, getCurrentPage() + 1);
        refresh(target);
    }

    @Override
    protected void onBeforeRender() {

        assert getDataProvider() != null : "You must call setDataProvider before this component is rendered";
        assert getColumns() != null : "You must call setColumns before this component is rendered";

        if (!initialized) {
            buildDataTable();
            initialized = true;
        }

        super.onBeforeRender();

    }

    public void previousPage(AjaxRequestTarget target) {
        setCurrentPage(target, getCurrentPage() - 1);
        refresh(target);
    }

    /**
     * Refresh the data table via AJAX. Takes care to avoid refreshing toolbar
     * controls that do not work properly if refreshed, e.g. the QuickFilterPanel.
     */
    public void refresh(AjaxRequestTarget target) {

        target.add(getDataTable());

        for (Component component : refreshComponents) {
            target.add(component);
        }

    }

    @Override
    public void removeAllPanels() {
        panelRepeater.removeAll();
    }

    @Override
    public DataTablePanel<T> setColumns(IColumn<T, String>... columns) {
        super.setColumns(columns);
        return this;
    }

    @Override
    public DataTablePanel<T> setColumns(List<IColumn<T, String>> columns) {
        super.setColumns(columns);
        return this;
    }

    public void setCurrentPage(AjaxRequestTarget target, long currentPage) {

        if (currentPage < 0 || currentPage > getPageCount() - 1) {
            return;
        }

        this.currentPage = currentPage;
        getDataTable().setCurrentPage(currentPage);

    }

    @Override
    public DataTablePanel<T> setDataProvider(IDataProvider<T> dataProvider) {
        super.setDataProvider(dataProvider);
        return this;
    }

    public DataTablePanel<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (getDataTable() != null) {
            getDataTable().setItemsPerPage(pageSize);
        }
        return this;
    }

    public DataTablePanel<T> setPaginated(boolean paginated) {

        this.paginated = paginated;

        if (paginated) {
            reorderable = false;
        }

        if (getDataTable() != null) {
            getDataTable().setItemsPerPage(getRowsPerPage());
        }

        return this;
    }

    public DataTablePanel<T> setReorderable(boolean reorderable) {

        this.reorderable = reorderable;

        if (reorderable) {
            paginated = false;
        }

        return this;
    }

}
