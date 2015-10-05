package ca.krasnay.panelized.datatable.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.CheckBoxContainerPanel;
import ca.krasnay.panelized.CheckBoxPanel;
import ca.krasnay.panelized.EnumUtils;
import ca.krasnay.panelized.LabelledPanel;
import ca.krasnay.panelized.PanelContainer;

/**
 * Filter that filters on rows matching one or more enum values.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AbstractEnumFilter<E extends Enum<E>> implements DataTableFilter {

    private static final String SEPARATOR = ",";

    private String key;

    private String name;

    private Class<E> enumClass;

    private List<E> selectedValues = new ArrayList<E>();

    /**
     * Constructor for subclasses that have the SimpleFilter annotation.
     *
     * @param key
     *            Key by which the filter is known in URLs and persistent
     *            storage.
     * @param name
     *            Human-readable name for the filter, e.g. "Status"
     * @param enumClass
     *            Class of the enum.
     * @param values
     *            List of values to be pre-selected.
     */
    @SafeVarargs
    public AbstractEnumFilter(String key, String name, Class<E> enumClass, E... values) {
        this.key = key;
        this.name = name;
        this.enumClass = enumClass;
        this.selectedValues.addAll(Arrays.asList(values));
    }

    @Override
    public void buildEditor(PanelContainer popup) {

        LabelledPanel labelledPanel = new LabelledPanel(popup.newPanelId());
        popup.addPanel(labelledPanel);
        labelledPanel.setLabel(getName());

        CheckBoxContainerPanel checkBoxContainer = new CheckBoxContainerPanel(labelledPanel.newPanelId());
        labelledPanel.addPanel(checkBoxContainer);
        checkBoxContainer.setMinSelections(1, Model.of("Please make at least one selection."));

        for (final E value : enumClass.getEnumConstants()) {

            if (isVisible(value)) {

                IModel<Boolean> model = new AbstractCheckBoxModel() {
                    @Override
                    public boolean isSelected() {
                        return selectedValues.contains(value);
                    }
                    @Override
                    public void select() {
                        if (!isSelected()) {
                            selectedValues.add(value);
                        }
                    }
                    @Override
                    public void unselect() {
                        if (isSelected()) {
                            selectedValues.remove(value);
                        }
                    }
                };

                checkBoxContainer.addPanel(new CheckBoxPanel(checkBoxContainer.newPanelId(), model)
                .setLabel(EnumUtils.toString(value)));
            }
        }

    }

    @Override
    public String getDisplayText(Locale locale) {

        StringBuilder sb = new StringBuilder();
        sb.append(getName())
        .append(" is ");

        boolean first = true;
        for (E value : selectedValues) {
            if (!first) {
                sb.append(" or ");
            }
            sb.append(EnumUtils.toString(value));
            first = false;
        }

        return sb.toString();
    }

    @Override
    public String getKey() {
        return key;
    }

    protected List<E> getSelectedValues() {
        return selectedValues;
    }

    @Override
    public String getStateString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (E value : selectedValues) {
            if (!first) {
                sb.append(SEPARATOR);
            }
            first = false;
            sb.append(value);
        }
        return sb.toString();
    }

    @Override
    public void init(String stateString) {

        selectedValues.clear();

        if (stateString != null && !stateString.trim().equals("")) {
            for (String s : stateString.split(SEPARATOR)) {
                try {
                    selectedValues.add(Enum.valueOf(enumClass, s));
                } catch (IllegalArgumentException e) {
                    // Got an invalid status value, just ignore
                }
            }
        }
    }

    /**
     * Returns true (the default) if the value is to be shown. Subclasses can override this
     * to filter out some of the enum values.
     */
    protected boolean isVisible(E value) {
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public void validate() {
    }

}
