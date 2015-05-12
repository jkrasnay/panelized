package ca.krasnay.panelized.datatable.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.repeater.data.ListDataProvider;

/**
 * Implementation of {@link ListDataProvider} that supports filters. Filters
 * must implement {@link ListFilter}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class FilterableListDataProvider<T extends Serializable> extends ListDataProvider<T> implements FilterableDataProvider {

    private List<DataTableFilter> filters = new ArrayList<>();

    public FilterableListDataProvider() {
        this(null);
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

    @SuppressWarnings("unchecked")
    @Override
    protected final List<T> getData() {

        List<T> result = new ArrayList<>();

        for (T row : super.getData()) {

            boolean include = true;

            for (DataTableFilter filter : filters) {
                if (!((ListFilter<T>) filter).include(row)) {
                    include = false;
                    break;
                }
            }

            if (include) {
                result.add(row);
            }
        }

        return result;

    }

    @Override
    public List<DataTableFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    @Override
    public void removeFilter(DataTableFilter filter) {
        filters.remove(filter);
    }



}
