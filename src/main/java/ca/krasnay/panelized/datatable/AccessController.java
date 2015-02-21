package ca.krasnay.panelized.datatable;

/**
 * Interface through which we can determine whether the current user can access
 * the given object. For data tables, the given object can be a {@link DataTableAction}
 * or a Filter.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface AccessController {

    /**
     * Returns true if the current user can access the given object.
     */
    public boolean canAccess(Object o);

}
