package ca.krasnay.panelized.datatable.filter;

import java.io.Serializable;
import java.util.Locale;

import ca.krasnay.panelized.PanelContainer;

/**
 * Term in the advanced filter for a data table.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface DataTableFilter extends Serializable {

    /**
     * Adds fields to the given container suitable for editing the details of the
     * filter. The filter itself is the entity model for the container.
     */
    public void buildEditor(PanelContainer container);

    /**
     * Returns a human-readable representation for the filter, including the
     * current state. For example, a text filter might return something like
     * "First Name starts with 'abc'"
     */
    public String getDisplayText(Locale locale);

    /**
     * Returns the key by which this filter is known in URLs and persistent
     * storage.
     */
    public String getKey();

    /**
     * Returns the state of the filter as a string. For example, the state of a
     * text filter might be something like "startsWith,abc". The
     * {@link #init(String)} function must be able to initialize the filter from
     * such a string.
     */
    public String getStateString();

    /**
     * Initializes the filter from a state string previously returned from
     * {@link #getStateString()}.
     */
    public void init(String stateString);

}
