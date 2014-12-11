package ca.krasnay.panelized;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Panel that's a simple list of buttons. Good for adding to BorderPanel headers
 * and footers.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ButtonsPanel extends Panel {

    private RepeatingView buttonRepeater;

    // Used to track when we add the first button, so we can highlight it,
    // e.g. on a PopUpFormPanel
    private boolean empty = true;

    public ButtonsPanel(String id) {

        super(id);

        setOutputMarkupId(true);

        buttonRepeater = new RepeatingView("button");
        add(buttonRepeater);
    }

    public ButtonsPanel addButton(Button button) {
        buttonRepeater.add(button);
        empty = false;
        return this;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String newButtonId() {
        return buttonRepeater.newChildId();
    }

    public void removeAllButtons() {
        buttonRepeater.removeAll();
        empty = true;
    }

}
