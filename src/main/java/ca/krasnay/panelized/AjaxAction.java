package ca.krasnay.panelized;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Action invoked by AJAX.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface AjaxAction extends Serializable {

    /**
     * Invokes the action.
     *
     * @param target
     *            AjaxRequestTarget for the action.
     */
    public void invoke(AjaxRequestTarget target);

}
