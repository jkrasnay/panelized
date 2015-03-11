package ca.krasnay.panelized;

/**
 * Access controller that returns true for all cases.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DummyAccessController implements AccessController {
    @Override
    public boolean canAccess(Class<?> uiElementClass) {
        return true;
    }
}
