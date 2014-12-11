package ca.krasnay.panelized;

import org.apache.wicket.Component;

/**
 * Interface implemented by classes that can have child panels added to them.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface PanelContainer {

    /**
     * Adds a panel to the container. It's the caller's responsibility to ensure
     * that all panels added to a container have unique IDs. Returns this
     * container so the calls can be chained.
     *
     * @param panel
     *            Panel to be added. This is of type Component instead of Panel
     *            so that we can accept both Panels and FormComponentPanels.
     */
    public PanelContainer addPanel(Component panel);

    /**
     * Returns a new ID for a panel that is guaranteed to be unique within this
     * container. Users of containers need not use this method, but if they
     * don't, they take responsibility for ensuring that unique IDs are used.
     */
    public String newPanelId();

    /**
     * Removes all panels from the container. This is for dynamic forms whose
     * content changes
     */
    public void removeAllPanels();

}
