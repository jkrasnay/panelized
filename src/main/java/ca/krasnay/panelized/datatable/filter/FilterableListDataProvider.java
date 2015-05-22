package ca.krasnay.panelized.datatable.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

/**
 * Implementation of {@link ListDataProvider} that supports filters. Filters
 * must implement {@link ListFilter}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class FilterableListDataProvider<T extends Serializable> extends ListDataProvider<T> implements FilterableDataProvider, ISortableDataProvider<T, String>, QuickFilterDataProvider {

    private List<DataTableFilter> filters = new ArrayList<>();

    private final SingleSortState<String> sortState = new SingleSortState<String>();

    private List<ListQuickFilter<T>> quickFilters = new ArrayList<>();

    private String quickFilterString;

    public FilterableListDataProvider() {
        this(Collections.<T> emptyList());
    }

    public FilterableListDataProvider(List<T> list) {
        super(list);
    }

    @Override
    public void addFilter(DataTableFilter filter) {
        if (filter instanceof ListFilter) {
            filters.add(filter);
        } else {
            throw new RuntimeException("Filter must implement ListFilter");
        }
    }

    public void addQuickFilter(ListQuickFilter<T> filter) {
        quickFilters.add(filter);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final List<T> getData() {

        List<T> result = new ArrayList<>();

        for (T row : getRawData()) {

            boolean include = true;

            for (DataTableFilter filter : filters) {
                if (!((ListFilter<T>) filter).include(row)) {
                    include = false;
                    break;
                }
            }

            if (include && quickFilterString != null) {
                for (ListQuickFilter<T> filter : quickFilters) {
                    if (!filter.include(row, quickFilterString)) {
                        include = false;
                        break;
                    }
                }
            }

            if (include) {
                result.add(row);
            }
        }

        SortParam<String> sort = sortState.getSort();
        if (sort != null) {
            Collections.sort(result, new PropertyComparator<T>(sort.getProperty(), sort.isAscending()));
        }

        return result;

    }

    @Override
    public List<DataTableFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    /**
     * Returns the raw, unfiltered, unsorted list of items. By default calls
     * super.getData().
     */
    public List<T> getRawData() {
        return super.getData();
    }

    @Override
    public ISortState<String> getSortState() {
        return sortState;
    }

    @Override
    public void removeFilter(DataTableFilter filter) {
        filters.remove(filter);
    }

    public void setSort(String property, SortOrder order){
        sortState.setPropertySortOrder(property, order);
    }

    @Override
    public String getQuickFilterString() {
        return quickFilterString;
    }

    @Override
    public boolean hasQuickFilters() {
        return quickFilters.size() > 0;
    }

    @Override
    public void setQuickFilterString(String s) {
        this.quickFilterString = s;
    }


}
