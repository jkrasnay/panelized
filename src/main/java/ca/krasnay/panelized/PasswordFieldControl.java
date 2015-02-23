package ca.krasnay.panelized;

import org.apache.wicket.model.IModel;

public class PasswordFieldControl extends TextFieldControl<String> {

    public PasswordFieldControl(String id) {
        this(id, null);
    }

    public PasswordFieldControl(String id, IModel<String> model) {
        super(id, model, true);
    }

}
