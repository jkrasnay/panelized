package ca.krasnay.panelized.testapp;

import java.util.Locale;

import ca.krasnay.panelized.datatable.filter.DataTableFilter;
import ca.krasnay.panelized.datatable.filter.FilterFactory;

public class WidgetColorFilterFactory implements FilterFactory {

    @Override
    public DataTableFilter createFilter() {
        return new WidgetColorFilter();
    }

    @Override
    public String getKey() {
        return "color";
    }

    @Override
    public String getName(Locale locale) {
        return "Color";
    }

}
