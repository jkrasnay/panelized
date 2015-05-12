package ca.krasnay.panelized;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Container panel with special validation to enforce minimum/maximum number of
 * selections.
 *
 * Note that for this to work it must be under a form that supports the
 * {@link FormValidatable} interface.
 *
 * This panel currently doesn't work with nested checkboxes, i.e. checkboxes
 * underneath a {@link CheckBoxPanel}. It treats all checkboxes under this panel
 * as equivalent.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class CheckBoxContainerPanel extends ContainerPanel implements FormValidatable {

    private int maxSelections = Integer.MAX_VALUE;

    private IModel<String> maxSelectionsError;

    private int minSelections;

    private IModel<String> minSelectionsError;

    public CheckBoxContainerPanel(String id) {
        this(id, null);
    }

    public CheckBoxContainerPanel(String id, IModel<?> model) {
        super(id, model);
    }

    private CheckBox findFirstCheckBox() {
        return visitChildren(CheckBox.class, new IVisitor<CheckBox, CheckBox>() {
            @Override
            public void component(CheckBox object, IVisit<CheckBox> visit) {
                visit.stop(object);
            }
        });
    }

    @Override
    public IFormValidator getFormValidator() {

        final CheckBox firstCheckBox = findFirstCheckBox();

        if (firstCheckBox != null && (minSelections > 0 || maxSelections > 0)) {

            return new AbstractFormValidator() {

                @Override
                public FormComponent<?>[] getDependentFormComponents() {
                    return null;
                }

                @Override
                public void validate(Form<?> form) {

                    if (!CheckBoxContainerPanel.this.isEnabled()) {
                        return;
                    }

                    int count = getSelectionCount();

                    if (isVisibleInHierarchy() && count > maxSelections) {
                        firstCheckBox.error(maxSelectionsError.getObject());
                    }

                    if (isVisibleInHierarchy() && count < minSelections) {
                        firstCheckBox.error(minSelectionsError.getObject());
                    }

                }
            };

        } else {
            return null;
        }

    }

    private int getSelectionCount() {

        final int[] checkedCount = new int[1];

        visitChildren(CheckBox.class, new IVisitor<CheckBox, Void>() {
            @Override
            public void component(CheckBox checkBox, IVisit<Void> visit) {
                if (Strings.isTrue(checkBox.getValue())) {
                    checkedCount[0]++;
                }
                visit.dontGoDeeper();
            }
        });

        return checkedCount[0];
    }

    public CheckBoxContainerPanel setMaxSelections(int maxSelections, IModel<String> errorMessage) {
        this.maxSelections = maxSelections;
        this.maxSelectionsError = errorMessage;
        // TODO I think we need to do something more here to localize the message,
        // e.g. bind it to this component. See ResourceModel for more info.
        return this;
    }

    public CheckBoxContainerPanel setMinSelections(int minSelections, IModel<String> errorMessage) {
        this.minSelections = minSelections;
        this.minSelectionsError = errorMessage;
        // TODO I think we need to do something more here to localize the message,
        // e.g. bind it to this component. See ResourceModel for more info.
        return this;
    }

}
