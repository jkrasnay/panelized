package ca.krasnay.panelized;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Label that displays a human-readable string for an enum value. Uses
 * {@link EnumUtils#toString(Enum, java.util.Locale)} to determine the
 * human-readable string.
 *
 * This class can accept model objects of an enum type or of strings; however,
 * if you're expecting string values, you must call one of the constructors
 * that specify the enum type.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class EnumLabel<E extends Enum<E>> extends Label {

    private Class<E> enumType;

    public EnumLabel(String id, IModel<?> model) {
        super(id, model);
    }

    public EnumLabel(String id, Class<E> enumType, IModel<?> model) {
        super(id, model);
        this.enumType = enumType;
    }

    public EnumLabel(String id) {
        super(id);
    }

    public EnumLabel(String id, Class<E> enumType) {
        super(id);
        this.enumType = enumType;
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {

        Object e = getDefaultModelObject();

        String s;
        if (e == null) {
            s = "";
        } else if (e instanceof String) {
            assert enumType != null : "For String model objects you must specify the enum type in the constructor";
            s = EnumUtils.toString(Enum.valueOf(enumType, (String) e));
        } else if (e instanceof Enum<?>){
            s = EnumUtils.toString((Enum<?>) e, getLocale());
        } else {
            throw new RuntimeException("EnumLabel can't handle model objects of type " + e.getClass().getName());
        }

        replaceComponentTagBody(markupStream, openTag, s);

    }
}
