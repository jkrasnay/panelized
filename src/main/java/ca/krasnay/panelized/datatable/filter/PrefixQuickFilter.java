package ca.krasnay.panelized.datatable.filter;

import org.apache.wicket.core.util.lang.PropertyResolver;

/**
 * Quick filter that matches if the quick filter string matches the prefix of
 * the value of the given property on the row.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class PrefixQuickFilter<T> implements ListQuickFilter<T> {

    private String property;

    public PrefixQuickFilter(String property) {
        this.property = property;
    }

    public boolean include(T row, String quickFilterString) {
        if (quickFilterString == null) {
            return true;
        } else {
            String s = quickFilterString.toLowerCase();
            Object value = PropertyResolver.getValue(property, row);
            return value != null && value.toString().toLowerCase().startsWith(s);
        }
    };
}
