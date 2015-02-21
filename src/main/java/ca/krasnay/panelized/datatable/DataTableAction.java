package ca.krasnay.panelized.datatable;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Interface for an action that can be invoked for a row on a data table.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface DataTableAction<T> extends Serializable {

    /**
     * Returns true if this action is applicable to multiple rows. If not, the
     * UI will not offer this action to the user if multiple rows are selected.
     */
    public boolean acceptsMultiples();

    /**
     * Returns true if this action should be enabled in the menu for the given
     * row.
     */
    public boolean isEnabled(T row);

    /**
     * Returns true if this action should be displayed in the menu for the given
     * row.
     */
    public boolean isVisible(T row);

    /**
     * Returns the display name of the action.
     *
     * @param locale
     *            Locale indictation the desired language.
     */
    public String getName(Locale locale);

    /**
     * Invokes the action.
     */
    public void invoke(AjaxRequestTarget target, List<String> rowIds);

}
