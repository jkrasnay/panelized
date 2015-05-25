package ca.krasnay.panelized;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility methods for dealing with enums.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public final class EnumUtils {

    /**
     * Returns a human-readable name for the given enum value. Human-readable
     * names are obtained from a resource bundle in the same package with the
     * same name as the enum class. For example, consider the following class:
     *
     * <p><pre>
     * public class Widget {
     *     public enum Colour {
     *         RED, WHITE, BLUE;
     *     }
     * }
     * </pre></p>
     *
     * <p>
     * This method would look for the resource bundle called
     * Widget$Colour.properties in the same package, which might look like this:
     * </p>
     *
     * <p><pre>
     * RED=Red
     * WHITE=White
     * BLUE=Blue
     * </pre></p>
     *
     * @param value Enum value for which to return the string.
     */
    public static String toString(Enum<?> value) {
        return ResourceBundle.getBundle(value.getClass().getName()).getString(value.name());
    }

    /**
     * Returns a human-readable name for the given enum value in the given locale.
     * See {@link #toString(Enum)} for more how the string is determined.
     */
    public static String toString(Enum<?> value, Locale locale) {
        return ResourceBundle.getBundle(value.getClass().getName(), locale).getString(value.name());
    }

}
