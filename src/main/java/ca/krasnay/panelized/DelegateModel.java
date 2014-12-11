package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * Model that delegates to the model in a particular component. This is used
 * when embedding a form component in a re-usable panel, so that model
 * resolution based on CompoundPropertyModel can work based on the panel's ID.
 * In this case the form component is given a DelegateModel that delegates to
 * the panel. If the panel's model is null, Wicket looks for a parent with a
 * CompoundPropertyModel and automatically creates a wrap model based on the
 * panel's ID.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DelegateModel<T> implements IModel<T> {

    private Component component;

    public static <Z> DelegateModel<Z> of(Component component) {
        return new DelegateModel<Z>(component);
    }

    public DelegateModel(Component component) {
        this.component = component;
    }

    @Override
    public void detach() {
        @SuppressWarnings("unchecked")
        IModel<T> model = (IModel<T>) component.getDefaultModel();
        if (model != null) {
            model.detach();
        }
    }

    @Override
    public T getObject() {
        @SuppressWarnings("unchecked")
        IModel<T> model = (IModel<T>) component.getDefaultModel();
        return model != null ? model.getObject() : null;
    }

    @Override
    public void setObject(T object) {
        @SuppressWarnings("unchecked")
        IModel<T> model = (IModel<T>) component.getDefaultModel();
        if (model != null) {
            model.setObject(object);
        }
    }

}
