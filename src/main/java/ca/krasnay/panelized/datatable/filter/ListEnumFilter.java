package ca.krasnay.panelized.datatable.filter;

public abstract class ListEnumFilter<T, E extends Enum<E>> extends AbstractEnumFilter<E> implements ListFilter<T> {

    @SafeVarargs
    public ListEnumFilter(String key, String name, Class<E> enumClass, E... values) {
        super(key, name, enumClass, values);
    }

    public abstract E getValue(T row);

    @Override
    public boolean include(T row) {
        return getSelectedValues().contains(getValue(row));
    }

}
