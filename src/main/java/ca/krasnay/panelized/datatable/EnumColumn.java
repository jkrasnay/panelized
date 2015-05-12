package ca.krasnay.panelized.datatable;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ca.krasnay.panelized.EnumLabel;
import ca.krasnay.panelized.EnumUtils;

/**
 * Column displaying an enum value using an EnumLabel component. See
 * {@link EnumLabel} and {@link EnumUtils} for more information about how the
 * column value is determined.
 *
 * @author john
 */
public class EnumColumn<T, E extends Enum<E>> extends AbstractColumn<T, String> {

    private Class<E> enumClass;

    private String propertyExpression;

    public EnumColumn(IModel<String> displayModel, Class<E> enumClass, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty);
        this.enumClass = enumClass;
        this.propertyExpression = propertyExpression;
    }

    public EnumColumn(IModel<String> displayModel, Class<E> enumClass, String propertyExpression) {
        this(displayModel, enumClass, null, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new EnumLabel<E>(componentId, enumClass, new PropertyModel<Object>(rowModel, propertyExpression)));
    }

}
