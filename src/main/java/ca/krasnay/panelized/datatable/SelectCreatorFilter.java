package ca.krasnay.panelized.datatable;

import java.io.Serializable;

import ca.krasnay.sqlbuilder.SelectCreator;

/**
 * Filter that can modify the query of a SelectCreatorDataProvider. An array
 * of these filters can be provided to the data provider, which uses them
 * to modify the query before it is executed.
 *
 * @author John Krasnay
 */
public interface SelectCreatorFilter extends Serializable {

    /**
     * Applies the filter to a {@link SelectCreator}.
     */
    public void apply(SelectCreator selectCreator);

}
