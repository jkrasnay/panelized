package ca.krasnay.panelized.datatable.sqlbuilder;

import static ca.krasnay.sqlbuilder.Predicates.in;

import java.util.ArrayList;
import java.util.List;

import ca.krasnay.panelized.datatable.filter.AbstractEnumFilter;
import ca.krasnay.sqlbuilder.SelectCreator;

public class SelectCreatorEnumFilter<E extends Enum<E>> extends AbstractEnumFilter<E> implements SelectCreatorFilter {

    private String dbExpr;

    /**
     * Constructor for subclasses that have the SimpleFilter annotation.
     *
     * @param key
     *            Key by which the filter is known in URLs and persistent
     *            storage.
     * @param name
     *            Human-readable name for the filter, e.g. "Status"
     * @param dbExpr
     *            SQL expression that returns the value against which the enum
     *            value is compared. Note that this is interpolated directly
     *            into the SQL, so please DO NOT ACCEPT USER PROVIDED VALUES
     *            HERE, lest ye be smitten by a SQL injection vulnerability.
     * @param enumClass
     *            Class of the enum.
     * @param values
     *            List of values to be pre-selected.
     */
    @SafeVarargs
    public SelectCreatorEnumFilter(String key, String name, String dbExpr, Class<E> enumClass, E... values) {
        super(key, name, enumClass, values);
        this.dbExpr = dbExpr;
    }

    @Override
    public void apply(SelectCreator selectCreator) {
        List<String> strings = new ArrayList<>();
        for (E value : getSelectedValues()) {
            strings.add(value.name());
        }
        selectCreator.where(in(dbExpr, strings));
    }


}


