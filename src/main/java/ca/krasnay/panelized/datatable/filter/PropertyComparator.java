package ca.krasnay.panelized.datatable.filter;

import java.util.Comparator;

import org.apache.wicket.core.util.lang.PropertyResolver;

/**
 * Comparator that compares two objects based on the value of a property, using
 * Wicket's property resolution semantics.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class PropertyComparator<T> implements Comparator<T> {

    private String property;

    private boolean ascending;

    public PropertyComparator(String property) {
        this(property, true);
    }

    public PropertyComparator(String property, boolean ascending) {
        this.property = property;
        this.ascending = ascending;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(T o1, T o2) {

        int result = 0;

        Object v1 = PropertyResolver.getValue(property, o1);

        if (v1 instanceof Comparable) {
            result = ((Comparable<Object>) v1).compareTo(PropertyResolver.getValue(property, o2));
        }

        return ascending ? result : -result;
    }

}
