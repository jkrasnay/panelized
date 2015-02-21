package ca.krasnay.panelized.datatable;

import java.util.Date;

import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * Type of map returned by SelectCreatorDataProvider. This provides methods
 * for accessing the map without casting the result.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class RowMap extends LinkedCaseInsensitiveMap<Object> {

    public Date getDate(String column) {
        return (Date) get(column);
    }

    public int getInt(String column) {
        return Integer.parseInt(getString(column));
    }

    public String getString(String column) {
        Object value = get(column);
        return value == null ? null : value.toString();
    }


}
