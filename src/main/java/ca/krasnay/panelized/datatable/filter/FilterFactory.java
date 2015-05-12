package ca.krasnay.panelized.datatable.filter;

import java.io.Serializable;
import java.util.Locale;

/**
 * Factory for data table filter objects. Factories are used
 * when initializing filter sets from strings, e.g. from URL parameters,
 * and also to populate a list of new filters to add to a query.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface FilterFactory extends Serializable {

    /**
     * Creates a new instance of the filter.
     */
    public DataTableFilter createFilter();

    /**
     * Returns the key for the filter.
     */
    public String getKey();

    /**
     * Returns the name of the filter.
     */
    public String getName(Locale locale);

}
