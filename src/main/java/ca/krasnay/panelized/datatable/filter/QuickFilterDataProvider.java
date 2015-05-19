package ca.krasnay.panelized.datatable.filter;

/**
 * Interface supported by data providers that support "quick filters". A quick
 * filter is a filter based on a short string entered by the user.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface QuickFilterDataProvider {

    /**
     * Returns the quick filter string currently in effect.
     */
    public String getQuickFilterString();

    /**
     * Returns true if this data provider has been configured with quick
     * filters. The UI should not render the quick filter components if this
     * method returns false.
     */
    public boolean hasQuickFilters();

    /**
     * Sets the quick filter string to the given string. If the given search
     * string is null, quick filtering is removed.
     */
    public void setQuickFilterString(String s);


}
