package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.joda.time.LocalDate;

import ca.krasnay.panelized.js.JsObject;

/**
 * Adds a date picker component to a text field.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DatePicker extends Behavior {

    private String format;

    private LocalDate endDate;

    private LocalDate startDate;

    public DatePicker() {
        this("yyyy-MM-dd");
    }

    public DatePicker(String format) {
        this.format = format;
    }


    @Override
    public void renderHead(Component component, IHeaderResponse response) {

        component.setOutputMarkupId(true);

//        JsObjectBuilder opts = new JsObjectBuilder();
//        opts.put("autoclose", true);
//        opts.put("format", format.replace('M', 'm'));
//        opts.put("language", component.getLocale().toString());

        JsObject opts = new JsObject();
        opts.put("format", format.toUpperCase());

        if (startDate != null) {
            opts.put("minDate", startDate.toString(format));
        }

        if (endDate != null) {
            opts.put("maxDate", endDate.toString(format));
        }

        System.out.println(String.format("Panelized.DatePicker.init('#%s', %s)", component.getMarkupId(), opts));
        response.render(OnDomReadyHeaderItem.forScript(String.format("Panelized.DatePicker.init('#%s', %s)", component.getMarkupId(), opts)));
    }

    public DatePicker setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public DatePicker setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }


}
