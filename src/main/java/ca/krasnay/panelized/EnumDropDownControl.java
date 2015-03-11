package ca.krasnay.panelized;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;

public class EnumDropDownControl<E extends Enum<E>> extends DropDownControl<E> {

    public EnumDropDownControl(String id, Class<E> enumClass) {
        this(id, null, enumClass);
    }

    public EnumDropDownControl(String id, E... values) {
        this(id, null, Arrays.asList(values));
    }

    public EnumDropDownControl(String id, IModel<E> model, Class<E> enumClass) {
        this(id, model, Arrays.asList(enumClass.getEnumConstants()));
    }

    public EnumDropDownControl(String id, IModel<E> model, List<E> values) {

        super(id, model, values);

        setChoiceRenderer(new ChoiceRenderer<E>() {
            @Override
            public String getDisplayValue(E object) {
                return EnumUtils.toString(object, getLocale());
            }
        });

    }

    public EnumDropDownControl(String id, List<E> values) {
        this(id, null, values);
    }


}
