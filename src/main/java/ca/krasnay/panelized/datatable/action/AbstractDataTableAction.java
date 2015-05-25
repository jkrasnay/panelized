package ca.krasnay.panelized.datatable.action;

import java.util.Locale;

public abstract class AbstractDataTableAction<T> implements DataTableAction<T> {

    private String name;

    public AbstractDataTableAction(String name) {
        this.name = name;
    }

    @Override
    public boolean acceptsMultiples() {
        return false;
    }

    @Override
    public boolean isEnabled(T row) {
        return true;
    }

    @Override
    public boolean isVisible(T row) {
        return true;
    }

    @Override
    public String getName(Locale locale) {
        return name;
    }

}
