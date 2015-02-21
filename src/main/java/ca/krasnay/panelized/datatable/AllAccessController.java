package ca.krasnay.panelized.datatable;

/**
 * Access controller that always grants access. Used when access control is not
 * a concern for the application, or if access control is controlled at a
 * higher-level, such as for the page or a containing panel.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class AllAccessController implements AccessController {
    @Override
    public boolean canAccess(Object o) {
        return true;
    }
}
