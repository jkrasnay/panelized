package ca.krasnay.panelized;

/**
 * Interface implemented by actions that may be conditionally hidden and/or disabled.
 *
 * Note that the method names are designed to avoid conflicts with Wicket components,
 * since many actions are also Wicket panels.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface ConditionalAction {

    public boolean isActionEnabled();

    public boolean isActionVisible();

}
