package ca.krasnay.panelized;

import java.util.Locale;

/**
 * Action invoked by AJAX.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface NamedAjaxAction extends AjaxAction {

    /**
     * Returns the name of the action.
     *
     * @param locale
     *            Locale for which to return the name.
     */
    public String getName(Locale locale);

}
