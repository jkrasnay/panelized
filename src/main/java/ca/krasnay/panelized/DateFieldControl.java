package ca.krasnay.panelized;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;


public class DateFieldControl<T> extends TextFieldControl<T> {

    private String format = "yyyy-MM-dd";

    /**
     * Format used for the model when the model type is String.
     */
    private String modelFormat = "yyyy-MM-dd HH:mm:ss";

    private LocalDate endDate;

    private LocalDate startDate;

    public DateFieldControl(String id, Class<T> type) {
        this(id, type, null);
    }

    public DateFieldControl(String id, Class<T> type, IModel<T> model) {

        super(id, model);

        // TODO check if the given type is supported
        setType(type);

        setSize(Size.SMALL);

        addValidator(new IValidator<T>() {
            @Override
            public void validate(IValidatable<T> validatable) {

                if (startDate == null && endDate == null) {
                    return;
                }

                T value = validatable.getValue();

                if (value != null) {

                    LocalDate date;

                    if (value instanceof String) {
                        date = DateTime.parse((String) value, DateTimeFormat.forPattern(modelFormat)).toLocalDate();
                    } else if (value instanceof ReadableInstant || value instanceof ReadablePartial) {
                        date = new LocalDate(value);
                    } else if (value instanceof Date) {
                        date = new LocalDate(value);
                    } else {
                        throw new RuntimeException("Invalid model value of type " + value.getClass().getName());
                    }


                    if (startDate != null && startDate.isAfter(date)) {
                        validatable.error(new ValidationError()
                        .addKey("validation.startDate")
                        .setVariable("date", startDate.toString(format)));
                    }

                    if (endDate != null && endDate.isBefore(date)) {
                        validatable.error(new ValidationError()
                        .addKey("validation.endDate")
                        .setVariable("date", endDate.toString(format)));
                    }

                }
            }
        });
    }

    @Override
    public <C> IConverter<C> getConverter(final Class<C> type) {
        return new IConverter<C>() {

            @SuppressWarnings("unchecked")
            @Override
            public C convertToObject(String value, Locale locale) {

                try {
                    LocalDate date = LocalDate.parse(value, DateTimeFormat.forPattern(format));

                    if (type == Date.class) {
                        return (C) date.toDate();
                    } else if (type == LocalDate.class) {
                        return (C) date;
                    } else if (type == String.class) {
                        return (C) date.toString(modelFormat);
                    } else {
                        throw new RuntimeException("Unsupported type for date field control: " + type);
                    }
                } catch (Exception e) {
                    ConversionException cx = new ConversionException("");
                    cx.setResourceKey("dateDropDowns.InvalidDate");
                    throw cx;
                }
            }

            @Override
            public String convertToString(C value, Locale locale) {

                LocalDate date;

                if (type == String.class) {
                    date = LocalDate.parse((String) value, DateTimeFormat.forPattern(modelFormat));
                } else {
                    date = new LocalDate(value);
                }

                return date.toString(format);
            }
        };

    }

    @Override
    protected void onBeforeRender() {
        if (!hasBeenRendered()) {
            getFormComponent().add(new DatePicker(format)
            .setStartDate(startDate)
            .setEndDate(endDate));
        }
        super.onBeforeRender();
    }


    public DateFieldControl<T> setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public DateFieldControl<T> setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Sets the format used for the model when the model type is String.
     */
    public DateFieldControl<T> setModelFormat(String modelFormat) {
        this.modelFormat = modelFormat;
        return this;
    }

    public DateFieldControl<T> setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

}
