package ca.krasnay.panelized;

/**
 * Interface implemented by access control implementations. Access is controlled
 * at the Java class level.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface AccessController {

    /**
     * Returns true if the current user can access the element with the given
     * class. In most cases, the class represents a page or action.
     */
    public boolean canAccess(Class<?> uiElementClass);

}
