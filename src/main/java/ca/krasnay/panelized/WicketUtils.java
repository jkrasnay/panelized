package ca.krasnay.panelized;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.util.string.Strings;

public class WicketUtils {

    /**
     * Returns true if the given checkbox is checked. Works whether or not
     * the form has been submitted.
     */
    public static boolean isCheckBoxChecked(CheckBox checkBox) {

        Form<?> form = checkBox.findParent(Form.class);
        if (form != null && form.isSubmitted()) {
            // After form post
            return Strings.isTrue(checkBox.getInput());
        } else {
            // Initial display, before the form is posted
            return Strings.isTrue(checkBox.getValue());
        }

    }

    /**
     * Returns true if the given checkbox is checked. Works whether or not
     * the form has been submitted.
     */
    public static boolean isRadioChecked(Radio<?> radio) {
        Form<?> form = radio.findParent(Form.class);
        RadioGroup<?> radioGroup = radio.findParent(RadioGroup.class);
        if (form != null && form.isSubmitted()) {
            // After form post
            return Strings.isEqual(radioGroup.getInput(), radio.getValue());
        } else {
            // Initial display, before the form is posted
            Object o1 = radioGroup.getDefaultModelObject();
            return o1 != null && o1.equals(radio.getDefaultModelObject());
        }

    }

}
