package ca.krasnay.panelized.datatable.filter;

/**
 * Interface implemented by a data table filter that works with a
 * {@link FilterableListDataProvider}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface ListFilter<T> {

    /**
     * Returns true if the given row passes the filter.
     */
    public boolean include(T row);

}
