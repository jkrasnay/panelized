package ca.krasnay.panelized;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Encapsulation of a page class and corresponding parameters. Filters can be added to a PageRef so that the
 * page comes up pre-filtered.
 *
 * PageRefs can be used as an AjaxAction to load the page when the action is invoked.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class PageRef implements AjaxAction, Serializable {

    private boolean enabled = true;

    /**
     * Keep track of added filters for canAccess()
     */
//    private List<Filter> filters = new ArrayList<Filter>();

    private Class<? extends Page> pageClass;

    private PageParameters pageParameters = new PageParameters();

    public PageRef(Class<? extends Page> pageClass) {
        this.pageClass = pageClass;
    }

//    public PageRef(Class<? extends Page> pageClass, int entityId) {
//        this.pageClass = pageClass;
//        add(EntityPageUtils.PARAM_ID, entityId);
//    }
//
//    public PageRef add(Filter filter) {
//        filters.add(filter);
//        DataTablePanel.addFilterToPageParameters(filter, pageParameters);
//        return this;
//    }

    public PageRef add(String key, boolean value) {
        pageParameters.add(key, Boolean.toString(value));
        return this;
    }

    public PageRef add(String key, int value) {
        pageParameters.add(key, Integer.toString(value));
        return this;
    }

    public PageRef add(String key, String value) {
        pageParameters.add(key, value);
        return this;
    }

    /**
     * Returns true if the subject with the given ACL can access this page
     * reference. This method checks the authz annotations on the page class
     * and any filters.
     */
//    public boolean canBeAccessedBy(AccessControlList acl) {
//
//        if (!CurrentEffregUser.canAccess(pageClass, acl)) {
//            return false;
//        }
//
//        for (Filter filter : filters) {
//            if (!CurrentEffregUser.canAccess(filter.getClass(), acl)) {
//                return false;
//            }
//        }
//
//        return true;
//
//    }

    public Class<? extends Page> getPageClass() {
        return pageClass;
    }

    public PageParameters getPageParameters() {
        return pageParameters;
    }

    /**
     * Sets the response page to the one referred to by this reference.
     */
    public void go() {
        RequestCycle.get().setResponsePage(pageClass, pageParameters);
    }

    @Override
    public void invoke(AjaxRequestTarget target) {
        go();
    }

    /**
     * Returns true if links targeting this PageRef should be enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    public PageRef setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void throwRestartException() {
        throw new RestartResponseException(pageClass, pageParameters);
    }

}
