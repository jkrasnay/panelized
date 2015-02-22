package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * AjaxAction that refreshes one or more components by adding them to the
 * AjaxRequestTarget.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class RefreshAction implements AjaxAction {

    private Component[] components;

    public RefreshAction(Component... components) {
        this.components = components;
    }

    @Override
    public void invoke(AjaxRequestTarget target) {
        for (Component component : components) {
            target.add(component);
        }
    }

}
