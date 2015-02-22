package ca.krasnay.panelized;

import java.io.Serializable;

/**
 * Interface implemented by access control implementations. Used when adding
 * actions to page menus and data tables. Assumes that access can be inferred
 * from the class of the action or page being added to the menu, e.g. by a
 * class-level annotation.
 *
 * <p>Security concerns are *not* deeply embedded in Panelized; however, we
 * strongly believe that applications should default to denying access, and
 * that the developer should have to take explicit action to grant access.
 * By forcing the developer to provide an access controller to page menus
 * and data tables, and then have those components filter actions as they are
 * added, we allow apps to adopt this "safe by default" stance.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface AccessController extends Serializable {

    /**
     * Returns true if the current user can access the element with the given
     * class. In most cases, the class represents a page or action.
     */
    public boolean canAccess(Class<?> uiElementClass);

}
