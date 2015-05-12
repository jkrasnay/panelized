package ca.krasnay.panelized.datatable.filter;

import java.util.List;

import ca.krasnay.panelized.datatable.sqlbuilder.SelectCreatorDataProvider;
import ca.krasnay.panelized.datatable.sqlbuilder.SelectCreatorFilter;

/**
 * Interface implemented by data providers that can be filtered. Filters given
 * to the data provider must be aware of the specific type of data provider to
 * which they are being added. For example, filters given to a
 * {@link SelectCreatorDataProvider} must implement {@link SelectCreatorFilter}.
 *
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface FilterableDataProvider {

    /**
     * Adds the given filter to the data provider.
     */
    public void addFilter(DataTableFilter filter);

    /**
     * Returns a list of filters currently applied to the data provider.
     */
    public List<DataTableFilter> getFilters();

    /**
     * Removes the given filter from the data provider.
     */
    public void removeFilter(DataTableFilter filter);

}
