package ca.krasnay.panelized.datatable.sqlbuilder;

import java.io.Serializable;

import ca.krasnay.sqlbuilder.Predicate;

/**
 * Interface implemented by quick filters that can be applied to a SelectCreator.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface SelectCreatorQuickFilter extends Serializable {

    public Predicate createPredicate(String quickFilterString);

}
