package ca.krasnay.panelized.datatable.filter;

/**
 * Quick filter for the {@link FilterableListDataProvider}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface ListQuickFilter<T> {

    /**
     * Returns true if the given row should be included given the provided quick
     * filter string.
     *
     * @param row
     *            Row that we are checking.
     * @param quickFilterString
     *            Filter string provided by the user. Will never be passed null.
     */
    public boolean include(T row, String quickFilterString);

}
