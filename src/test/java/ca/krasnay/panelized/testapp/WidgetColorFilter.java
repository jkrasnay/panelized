package ca.krasnay.panelized.testapp;

import ca.krasnay.panelized.datatable.filter.ListEnumFilter;

public class WidgetColorFilter extends ListEnumFilter<Widget, Widget.Color> {

    public WidgetColorFilter(Widget.Color... values) {
        super("color", "Color", Widget.Color.class, values);
    }

    @Override
    public Widget.Color getValue(Widget row) {
        return row.getColor();
    }

    @Override
    public void validate() {
    }

}
