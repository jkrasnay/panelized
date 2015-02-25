package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.model.IModel;

/**
 * Panel encapsulating a radio button, its label, and an note.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class RadioButtonPanel<T> extends CheckablePanel {

    private Radio<T> radio;

    public RadioButtonPanel(String id, IModel<T> valueModel, IModel<String> labelModel) {

        super(id);

        setLabel(labelModel);

        radio = new Radio<T>("field", valueModel) {
            @Override
            public boolean isEnabled() {
                return RadioButtonPanel.this.isEnabled();
            }
        };
        add(radio);

    }

    @Override
    public Component getCheckable() {
        return radio;
    }

    public Radio<T> getRadio() {
        return radio;
    }

    @Override
    public boolean isChecked() {
        return WicketUtils.isRadioChecked(radio);
    }

}
