package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;


/**
 * Form item panel that encapsulates a check box.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class CheckBoxPanel extends CheckablePanel {

    private CheckBox checkBox;

//    private boolean updateChildrenOnClick;

    public CheckBoxPanel(String id) {
        this(id, null);
    }

    public CheckBoxPanel(String id, IModel<Boolean> model) {

        super(id, model);

        checkBox = new CheckBox("field", new DelegateModel<Boolean>(this)) {
            public boolean isEnabled() {
                return CheckBoxPanel.this.isEnabled();
            };
        };
        add(checkBox);

        checkBox.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                // Sets the 'disabled' class, which interacts with auto-disabling
                // the children of checkables
                return isEnabled() ? null : "disabled";
            }
        }, " "));


        checkBox.setOutputMarkupId(true);

    }

    @Override
    public Component getCheckable() {
        return checkBox;
    }

    @Override
    public boolean isChecked() {
        return WicketUtils.isCheckBoxChecked(checkBox);
    }

//    public CheckBoxPanel setUpdateChildrenOnClick(boolean updateChildrenOnClick) {
//        this.updateChildrenOnClick = updateChildrenOnClick;
//        return this;
//    }

}
