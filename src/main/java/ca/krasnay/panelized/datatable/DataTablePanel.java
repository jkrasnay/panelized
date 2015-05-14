package ca.krasnay.panelized.datatable;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValueConversionException;

import ca.krasnay.panelized.BorderPanel;

/**
 * Panel that displays a data table.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
/**
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DataTablePanel<T> extends AbstractDataTablePanel<T> implements IHeaderContributor {

//    private static final Logger log = Logger.getLogger(DataTablePanel.class);

    private static final int DEFAULT_PAGE_SIZE = 50;

    /**
     * Value of PARAM_PAGE_SIZE indicating to show all items.
     */
    private static final String PAGE_SIZE_ALL = "all";

    /**
     * URL parameter used to store a filter value.
     */
    private static final String PARAM_FILTER = "f";

    /**
     * URL parameter used to store the current page.
     */
    private static final String PARAM_PAGE = "p";

    /**
     * URL parameter used to store the page size.
     */
    private static final String PARAM_PAGE_SIZE = "z";

    /**
     * URL parameter used to store the quick filter.
     */
    private static final String PARAM_QUICK_FILTER = "q";


    /**
     * URL parameter used to store the sort state.
     */
    private static final String PARAM_SORT = "s";

    /**
     * String that separates the filter key from the state string in a URL
     * parameter.
     */
    private static final String FILTER_KEY_SEPARATOR = ":";

//    public static void addFilterToPageParameters(Filter filter, PageParameters pageParameters) {
//        String value = filter.getKey() + FILTER_KEY_SEPARATOR + filter.getStateString();
//        pageParameters.add(PARAM_FILTER, value);
//    }

    private boolean initialized;

//    private AdvancedFilterPanel advancedFilterPanel;

    private int columnAliasIndex;

//    private CurrentPagePanel currentPageTop;
//    private CurrentPagePanel currentPageBottom;

//    private boolean filtersReadOnly;

//    private String fullUrl;

//    private PageButtonsPanel pageButtonsTop;
//    private PageButtonsPanel pageButtonsBottom;

//    private PageSizePanel pageSizePanel;

//    /**
//     * Result of buildFilterColumnState() as of the last time the data table
//     * was built.
//     */
//    private String lastFilterColumnState;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private boolean paginated = true;

    private InternalDataTablePanel<T, String> internalDataTable;

    private boolean reorderable;

//    private AjaxAction favouriteAction;

//    private List<FilterFactory> filterFactories = new ArrayList<FilterFactory>();

//    private List<Filter> filters = new ArrayList<Filter>();

    private SortParam<String> defaultSort;

    private SortParam<String> secondarySort;

//    private List<FilterFactory> minorFilterFactories = new ArrayList<FilterFactory>();

//    private EditCommentActionPanel<?> commentActionPanel;

//    private boolean urlUpdatedOnChange = true;

    private BorderPanel border;

    public DataTablePanel(String id) {

        super(id);

        setOutputMarkupId(true);

        addPanel(border = new BorderPanel(newPanelId()));

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return reorderable ? "dt-reorderable" : null;
            }
        }, " "));


//        addHeaderRightItem(pageButtonsTop = new PageButtonsPanel("pageButtons", this) {
//            @Override
//            public boolean isVisible() {
//                return isPaginated();
//            }
//        });
//        pageButtonsTop.setOutputMarkupId(true);
//
//        addHeaderRightItem(currentPageTop = new CurrentPagePanel("currentPage", this));
//        currentPageTop.setOutputMarkupId(true);
//
//        addFooterRightItem(pageButtonsBottom = new PageButtonsPanel("pageButtonsBottom", this) {
//            @Override
//            public boolean isVisible() {
//                return isPaginated();
//            }
//        });
//        pageButtonsBottom.setOutputMarkupId(true);
//
//        addFooterRightItem(currentPageBottom = new CurrentPagePanel("currentPageBottom", this));
//        currentPageBottom.setOutputMarkupId(true);
//
//        addHeaderRightItem(pageSizePanel = new PageSizePanel("pageSize", this) {
//            @Override
//            public boolean isVisible() {
//                return isPaginated();
//            }
//        });

    }

//    @SuppressWarnings("unchecked")
//    public DataTablePanel<T> addFilter(Filter filter) {
//
//        if (!(getDataProvider() instanceof SelectCreatorDataProvider)) {
//            throw new RuntimeException("Filters only supported for instances of SelectCreatorDataProvider");
//        }
//
//        if (filter instanceof ColumnContributor) {
//            ((ColumnContributor<T>) filter).setColumnAlias(allocateColumnAlias());
//        }
//
//        filters.add(filter);
//
//        ((SelectCreatorDataProvider) getDataProvider()).addFilter(filter);
//
//        return this;
//    }

//    public DataTablePanel<T> addFilterFactory(FilterFactory factory) {
//
//        if (getAccessControlList() == null) {
//            throw new RuntimeException("You must set the access control list on the data table before adding filter factories");
//        }
//
//        if (CurrentEffregUser.canAccess(factory.getFilterClass(), getAccessControlList())) {
//            filterFactories.add(factory);
//        }
//
//        return this;
//    }

//    /**
//     * Adds a filter factory to be shown under the "More" cascading menu.
//     */
//    public DataTablePanel<T> addMinorFilterFactory(FilterFactory factory) {
//
//        if (getAccessControlList() == null) {
//            throw new RuntimeException("You must set the access control list on the data table before adding filter factories");
//        }
//
//        if (CurrentEffregUser.canAccess(factory.getFilterClass(), getAccessControlList())) {
//            minorFilterFactories.add(factory);
//        }
//
//        return this;
//    }

    /**
     * Adds any desired top toolbars to the data table. Due to the way toolbars
     * are implemented in Wicket, toolbars must be added by overriding this
     * method so that we have access to the DataTable in the toolbar
     * constructor.
     *
     * <p>See {@link ToolbarPanel} for an AbstractToolbar implementation in the
     * Panelized style.
     */
    protected void addTopToolbars(DataTable<T, String> dataTable) {
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

        internalDataTable = new InternalDataTablePanel<T, String>(border.newPanelId(), this, allColumns, getDataProvider(), paginated ? pageSize : Integer.MAX_VALUE);
        internalDataTable.setOutputMarkupId(true);

        border.removeAllPanels();
        border.addPanel(internalDataTable);

        ISortStateLocator<String> sortStateLocator = null;
        if (getDataProvider() instanceof ISortStateLocator) {
            sortStateLocator = (ISortStateLocator<String>) getDataProvider();
        }

        DataTable<T, String> dataTable = internalDataTable.getDataTable();

        addTopToolbars(dataTable);

        dataTable.addTopToolbar(new HeadersToolbar<String>(dataTable, sortStateLocator) {
            @Override
            protected WebMarkupContainer newSortableHeader(String headerId, String property, ISortStateLocator<String> locator) {
                return new AjaxFallbackOrderByBorder<String>(headerId, property, locator) {
                    @Override
                    protected void onAjaxClick(AjaxRequestTarget target) {
                        // TODO (lib) need some sort of callback, so the page URL can be updated by the app
                        //target.add(dataTable); // Seems to make our headers repeat (but not others?!)
                        target.add(DataTablePanel.this);
                    }

                    @Override
                    protected void onSortChanged() {
                        super.onSortChanged();
                        getTable().setCurrentPage(0);
                    }

                };
            }

        });

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

//    /**
//     * Builds a string from the states of all filters that implement
//     * ColumnContributor. If this state changes between renders, we know that
//     * we have to re-build the columns.
//     */
//    private String buildFilterColumnState() {
//        StringBuilder sb = new StringBuilder();
//        for (Filter filter : filters) {
//            if (filter instanceof ColumnContributor) {
//                sb.append(filter.getStateString()).append("~~~");
//            }
//        }
//        return sb.toString();
//    }
//
//
//    public List<FilterFactory> getAllFilterFactories() {
//        List<FilterFactory> allFilterFactories = new ArrayList<FilterFactory>();
//        allFilterFactories.addAll(filterFactories);
//        allFilterFactories.addAll(minorFilterFactories);
//        return allFilterFactories;
//    }

    @Override
    public long getCurrentPage() {
        return getDataTable().getCurrentPage();
    }

    private DataTable<T, String> getDataTable() {
        return internalDataTable != null ? internalDataTable.getDataTable() : null;
    }

//    public AjaxAction getFavouriteAction() {
//        return favouriteAction;
//    }

//    /**
//     * Returns the full URL of the page that reflects the state of this data table panel.
//     * Used, for example, when building favourites.
//     */
//    public String getFullUrl() {
//        return fullUrl;
//    }

//    public List<FilterFactory> getFilterFactories() {
//        return Collections.unmodifiableList(filterFactories);
//    }
//
//    public List<Filter> getFilters() {
//        return Collections.unmodifiableList(filters);
//    }

    /**
     * Returns the plural term for items in the list. Returns "items" by
     * default. Used by the advanced filter panel to describe what is being
     * shown.
     */
    public String getItemNamePlural() {
        return "items";
    }

//    public List<FilterFactory> getMinorFilterFactories() {
//        return minorFilterFactories;
//    }

    public long getPageCount() {
        return getDataTable().getPageCount();
    }

    public int getPageSize() {
        return pageSize;
    }

//    public String getQuickFilter() {
//        if (getDataProvider() instanceof FilterableDataProvider) {
//            return ((FilterableDataProvider) getDataProvider()).getQuickFilterString();
//        } else {
//            return null;
//        }
//    }

    @Override
    public long getRowsPerPage() {
        return paginated ? pageSize : Integer.MAX_VALUE;
    }

//    @SuppressWarnings("unchecked")
//    private void initFilters(PageParameters params) {
//
//        if (params == null) {
//            return;
//        }
//
//        if (filtersReadOnly) {
//            return;
//        }
//
//        for (StringValue sv : params.getValues(PARAM_FILTER)) {
//            String s = sv.toString();
//            int index = s.indexOf(FILTER_KEY_SEPARATOR);
//            if (index > 0) {
//                String key = s.substring(0, index);
//                String value = s.substring(index + FILTER_KEY_SEPARATOR.length());
//                for (FilterFactory factory : getAllFilterFactories()) {
//                    if (factory.getKey().equals(key)) {
//                        Filter filter = factory.createFilter();
//                        try {
//                            filter.init(value);
//                            addFilter(filter);
//                        } catch (Exception e) {
//                            // Can't initialize filter, just skip it.
//                            log.info("Can't initialize filter %s with value %s: %s", filter.getClass(), value, e.getMessage());
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (getDataProvider() instanceof FilterableDataProvider) {
//            setQuickFilter(params.get(PARAM_QUICK_FILTER).toString());
//        }
//
//        String sort = params.get(PARAM_SORT).toString();
//        if (sort != null && getDataProvider() instanceof SortableDataProvider) {
//            String[] parts = sort.split(",", 2);
//            if (parts.length == 2) {
//
//                try {
//
//                    int colIndex = Integer.parseInt(parts[0]);
//                    SortOrder sortOrder = Boolean.valueOf(parts[1]) ? SortOrder.ASCENDING : SortOrder.DESCENDING;
//                    ((SortableDataProvider<T, String>) getDataProvider()).setSort(getColumns().get(colIndex).getSortProperty(), sortOrder);
//
//                } catch (NumberFormatException e) {
//                    // Ignore, going with default sort order
//                } catch (IndexOutOfBoundsException e) {
//                    // Ignore, going with default sort order
//                }
//
//            }
//        }
//
//        String pageSize = params.get(PARAM_PAGE_SIZE).toString();
//        if (pageSize == null) {
//            // ignore
//        } else if (PAGE_SIZE_ALL.equals(pageSize)) {
//            setPageSize(Integer.MAX_VALUE);
//        } else {
//            try {
//                setPageSize(Integer.parseInt(pageSize));
//            } catch (NumberFormatException e) {
//            }
//        }
//
//    }

//    /**
//     * If the current configuration of filters has been recorded as a
//     * "favourite" by the user, returns the name of the favourite. Returns null
//     * otherwise. By default returns null. Subclasses that implement favourites
//     * should override this method and return an appropriate value.
//     */
//    public String getFavouriteName() {
//        return null;
//    }

//    /**
//     * Returns a string of HTML that describes the filters in place on this
//     * data table. Example: "Showing people where &lt;b&gt;Registration
//     * Status is Registered&lt;/b&gt;"
//     * @return
//     */
//    public String getFilterStringHtml() {
//        StringBuilder sb = new StringBuilder();
//        if (getFilters().size() == 0) {
//            sb.append("Showing all " + getItemNamePlural());
//        } else {
//            sb.append("Showing " + getItemNamePlural() + " where ");
//        }
//
//        boolean first = true;
//        for (Filter filter : getFilters()) {
//            if (!first) {
//                sb.append(" and");
//            }
//            sb.append(" <b>")
//            .append(StringEscapeUtils.escapeHtml(filter.getDisplayText(Locale.ENGLISH)))
//            .append("</b>");
//            first = false;
//        }
//        return sb.toString();
//    }

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

    public void nextPage(AjaxRequestTarget target) {
        setCurrentPage(target, getCurrentPage() + 1);
    }

//    public void onAction(AjaxRequestTarget target) {
//
//        IRequestParameters req = RequestCycle.get().getRequest().getRequestParameters();
//
//        String filter = req.getParameterValue("filter").toString();
//        String move = req.getParameterValue("move").toString();
//        String comment = req.getParameterValue("comment").toString();
//
//        if (filter != null) {
//
//            if (StringUtils.isNotBlank(filter)) {
//                setQuickFilter(filter);
//            } else {
//                setQuickFilter(null);
//            }
//            updatePage(target);
//
//        } else if (move != null) {
//
//            String[] indexes = move.split(",");
//            onMove(target, Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]));
//
//        } else if (comment != null) {
//
//            String rowId = req.getParameterValue("rowId").toString();
//            commentActionPanel.invoke(target, rowId);
//        }
//
//    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onBeforeRender() {

        assert getDataProvider() != null : "You must call setDataProvider before this component is rendered";
        assert getColumns() != null : "You must call setColumns before this component is rendered";

        if (!initialized) {

            //
            // Initialize default sorting. Note that the call to initFilters can override this
            //

            // TODO (lib): set sorting on data provider
//            if (getDataProvider() instanceof SortableDataProvider && defaultSort != null) {
//                ((SortableDataProvider<T, String>) getDataProvider()).setSort(defaultSort);
//            }
//
//            if (getDataProvider() instanceof SelectCreatorDataProvider && secondarySort != null) {
//                ((SelectCreatorDataProvider) getDataProvider()).setSecondarySort(secondarySort);
//            }


            // TODO (lib) filters belong to filter bar, talk to data provider, perhaps refreshing this panel
//            if (urlUpdatedOnChange) {
//                initFilters(getPage().getPageParameters());
//            }

            buildDataTable();

            // Note: set the page *after* building the data table
            try {
                setCurrentPage(null, getPage().getPageParameters().get(PARAM_PAGE).toInt() - 1);
            } catch (StringValueConversionException e) {
            }


            // TODO (lib) added by application
//            SearchFieldPanel quickFilterPanel = new SearchFieldPanel("quickFilter", new PropertyModel<String>(this, "quickFilter")) {
//                @Override
//                protected boolean isClearIconVisible() {
//                    return true;
//                }
//            };
//            addHeaderLeftItem(quickFilterPanel);
//            quickFilterPanel.setVisible(getDataProvider() instanceof FilterableDataProvider && ((FilterableDataProvider) getDataProvider()).hasQuickFilters());
//
//            addHeaderLeftItem(advancedFilterPanel = new AdvancedFilterPanel("filter", this).setReadOnly(filtersReadOnly), true);

            // TODO (lib) added by application
//            updateUrl(null);

            initialized = true;

        }

        super.onBeforeRender();

    }

    /**
     * Called when the data table is reorderable and the user drags a row to a
     * new position in the data table. The default implementation does nothing.
     *
     * @param target
     *            AjaxRequestTarget under which the item was moved.
     * @param fromIndex
     *            Current index of the item to be moved.
     * @param toIndex
     *            Desired index of the item to be moved.
     */
    protected void onMove(AjaxRequestTarget target, int fromIndex, int toIndex) {

    }

    public void previousPage(AjaxRequestTarget target) {
        setCurrentPage(target, getCurrentPage() - 1);
    }

//    /**
//     * Update portions of the UI that are sensitive to favourites.
//     */
//    public void refreshFavourites(AjaxRequestTarget target) {
//        advancedFilterPanel.refreshFavourites(target);
//    }

//    public DataTablePanel<T> removeFilter(Filter filter) {
//        filters.remove(filter);
//        ((SelectCreatorDataProvider) getDataProvider()).removeFilter(filter);
//        return this;
//    }

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

//    public DataTablePanel<T> setCommentActionPanel(EditCommentActionPanel<?> commentActionPanel) {
//
//        if (getAccessControlList() == null) {
//            throw new RuntimeException("You must set the access control list on the data table before adding actions");
//        }
//
//        if (CurrentEffregUser.canAccess(commentActionPanel.getClass(), getAccessControlList())) {
//            this.commentActionPanel = commentActionPanel;
//            addPanel(commentActionPanel);
//        }
//
//        return this;
//
//    }

    public void setCurrentPage(AjaxRequestTarget target, long pageNum) {

        if (pageNum < 0 || pageNum > getPageCount() - 1) {
            return;
        }

        getDataTable().setCurrentPage(pageNum);

//        if (target != null) {
//            updatePage(target);
//        }

    }

    @Override
    public DataTablePanel<T> setDataProvider(IDataProvider<T> dataProvider) {
        super.setDataProvider(dataProvider);
        return this;
    }

    /**
     * Sets the default sort order for the table. You can set this directly on
     * the data provider, but setting it here hides the sort page parameter if
     * it matches the default.
     * @param property
     * @param ascending
     * @return
     */
    public DataTablePanel<T> setDefaultSort(String property, boolean ascending) {
        defaultSort = new SortParam<String>(property, ascending);
        return this;
    }

//    /**
//     * Sets the action to be invoked when the "favourite" icon (a heart) is clicked.
//     * If null (the default), the favourite icon is not displayed.
//     */
//    public DataTablePanel<T> setFavouriteAction(AjaxAction favouriteAction) {
//        this.favouriteAction = favouriteAction;
//        return this;
//    }
//
//    public DataTablePanel<T> setFiltersReadOnly(boolean filtersReadOnly) {
//        this.filtersReadOnly = filtersReadOnly;
//        return this;
//    }

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

//    public DataTablePanel<T> setQuickFilter(String quickFilter) {
//        ((FilterableDataProvider) getDataProvider()).setQuickFilterString(quickFilter);
//        return this;
//    }

    public DataTablePanel<T> setReorderable(boolean reorderable) {

        this.reorderable = reorderable;

        if (reorderable) {
            paginated = false;
        }

        return this;
    }

    public DataTablePanel<T> setSecondarySort(String property, boolean ascending) {
        this.secondarySort = new SortParam<String>(property, ascending);
        return this;
    }

    public DataTablePanel<T> setTitle(IModel<String> titleModel) {
        border.setTitle(titleModel);
        return this;
    }

//    /**
//     * Sets whether changes to paging, sorting, and filtering should update the
//     * URL to preserve the data table state. Defaults to true. Set to false if
//     * the data table is not the main object on the page, or if there is more
//     * than one data table on the page.
//     */
//    public DataTablePanel<T> setUrlUpdatedOnChange(boolean urlUpdatedOnChange) {
//        this.urlUpdatedOnChange = urlUpdatedOnChange;
//        return this;
//    }

//    public void sortFilterFactories() {
//
//        Comparator<FilterFactory> comparator = new Comparator<FilterFactory>() {
//            @Override
//            public int compare(FilterFactory o1, FilterFactory o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        };
//
//        Collections.sort(filterFactories, comparator);
//        Collections.sort(minorFilterFactories, comparator);
//
//    }


//    /**
//     * Updates the components of the data table that are sensitive to which is
//     * the current page. Updates the data table itself and the current page
//     * components. This is so that the quick filter text box remains stable
//     * when we are typing the quick filter or paging through the data.
//     */
//    void updatePage(AjaxRequestTarget target) {
//
//        if (!buildFilterColumnState().equals(lastFilterColumnState)) {
//            buildDataTable(true);
//        }
//
//        target.add(dataTable);
//        target.add(currentPageTop);
//        target.add(currentPageBottom);
//        target.add(pageButtonsTop);
//        target.add(pageButtonsBottom);
//        target.add(pageSizePanel);
//
//        if (urlUpdatedOnChange) {
//            updateUrl(target);
//        }
//    }

//    private void updateUrl(AjaxRequestTarget target) {
//
//        if (filtersReadOnly) {
//            return;
//        }
//
//        // Update the page URL
//
//        PageParameters oldParams = getPage().getPageParameters();
//        PageParameters newParams = new PageParameters();
//
//        if (oldParams != null) {
//
//            // Copy over parameters that we don't control
//
//            for (String key : oldParams.getNamedKeys()) {
//                if (key.equals(PARAM_FILTER) || key.equals(PARAM_PAGE) || key.equals(PARAM_PAGE_SIZE) || key.equals(PARAM_QUICK_FILTER) || key.equals(PARAM_SORT)) {
//                    // ignore
//                } else {
//                    newParams.add(key, oldParams.get(key).toString());
//                }
//            }
//        }
//
//        if (getPageSize() != DEFAULT_PAGE_SIZE) {
//            String value = PAGE_SIZE_ALL;
//            if (getPageSize() < Integer.MAX_VALUE) {
//                value = "" + getPageSize();
//            }
//            newParams.add(PARAM_PAGE_SIZE, value);
//        }
//
//
//        //
//        // Sort filter strings so the URL always renders in a predictable order.
//        // This simplifies detecting when a URL is a favourite.
//        //
//        List<String> filterStrings = new ArrayList<String>();
//        for (Filter filter : filters) {
//            filterStrings.add(filter.getKey() + FILTER_KEY_SEPARATOR + filter.getStateString());
//        }
//        Collections.sort(filterStrings);
//        for (String stateString : filterStrings) {
//            newParams.add(PARAM_FILTER, stateString);
//        }
//
//        if (getDataProvider() instanceof SortableDataProvider) {
//            @SuppressWarnings("unchecked")
//            SortParam<String> sortParam = ((SortableDataProvider<T, String>) getDataProvider()).getSort();
//            if (sortParam != null) {
//                if (defaultSort != null && defaultSort.equals(sortParam)) {
//                    // skip
//                } else {
//                    // Rather than expose the sort parameter directly, which would open us
//                    // to a SQL injection attack, we expose the index of the column that
//                    // we are currently sorted by.
//                    List<? extends IColumn<?, String>> columns = getColumns();
//                    for (int i = 0; i < columns.size(); i++) {
//                        IColumn<?, String> column = columns.get(i);
//                        if (sortParam.getProperty().equals(column.getSortProperty())) {
//                            newParams.add(PARAM_SORT, i + "," + sortParam.isAscending());
//                        }
//                    }
//                }
//            }
//        };
//
//
//        //
//        // Calculate the full "favoriteable" URL w/o quick search and page
//        //
//
//        String url = urlFor(getPage().getClass(), newParams).toString();
//
//        fullUrl = App.get().getBaseUrl() + RequestCycle.get().mapUrlFor(getPage().getClass(), newParams);
//
//        if (target != null) {
//            refreshFavourites(target);
//        }
//
//
//        if (getQuickFilter() != null) {
//            newParams.add(PARAM_QUICK_FILTER, getQuickFilter());
//        }
//
//        if (getCurrentPage() > 0) {
//            newParams.add(PARAM_PAGE, "" + (getCurrentPage() + 1));
//        }
//
//        url = urlFor(getPage().getClass(), newParams).toString();
//
//        if (target != null) {
//            // target is null on initial call, when this panel is first rendered
//
//            // TODO proper URL escaping required?
//            // TODO History.js for cross-browser support?
//            target.appendJavaScript(String.format("window.history.replaceState(null, '', '%s');", StringEscapeUtils.escapeJavaScript(url)));
//        }
//
//    }

}
