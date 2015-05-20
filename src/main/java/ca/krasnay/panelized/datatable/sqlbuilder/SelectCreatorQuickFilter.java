package ca.krasnay.panelized.datatable.sqlbuilder;

import java.io.Serializable;

import ca.krasnay.sqlbuilder.Predicate;

/**
 * Interface implemented by quick filters that can be applied to a SelectCreator.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface SelectCreatorQuickFilter extends Serializable {

    /**
     * Returns a predicate that filters the result set to rows matching the
     * given quick filter string. May return null, in which case the filter is
     * not applied. This is handy if you only want to apply the filter if the
     * quick filter string matches a particular pattern.
     */
    public Predicate createPredicate(String quickFilterString);

}
