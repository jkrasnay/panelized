package ca.krasnay.panelized.datatable;

import java.util.Locale;

import ca.krasnay.panelized.ContainerPanel;


public abstract class DataTableActionPanel<T> extends ContainerPanel implements DataTableAction<T> {

    private boolean acceptsMultiples;

    private String name;

    public DataTableActionPanel(String id, String name) {
        this(id, name, false);
    }

    public DataTableActionPanel(String id, String name, boolean acceptsMultiples) {
        super(id);
        this.name = name;
        this.acceptsMultiples = acceptsMultiples;
    }

    public boolean acceptsMultiples() {
        return acceptsMultiples;
    }

    public String getName(Locale locale) {
        return name;
    }

    @Override
    public boolean isEnabled(T row) {
        return true;
    }

    @Override
    public boolean isVisible(T row) {
        return true;
    }

}
