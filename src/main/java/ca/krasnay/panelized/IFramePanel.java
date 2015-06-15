package ca.krasnay.panelized;

import java.lang.reflect.Method;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.link.ILinkListener;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;

/**
 * Panel that displays a resource in an iframe. See <a href=
 * "https://cwiki.apache.org/WICKET/displaying-content-eg-pdf-excel-word-in-an-iframe.html"
 * >this page</a> on the Wicket wiki.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class IFramePanel extends Panel implements ILinkListener {

    private IResource resource;


    /**
     * Constructor. If using this constructor, you *must* call
     * {@link #setResource(IResource)} before the panel is rendered.
     */
    public IFramePanel(String id) {
        this(id, null);
    }

    public IFramePanel(String id, IResource resource) {
        super(id);
        this.resource = resource;

        WebComponent iframe = new WebComponent("iframe");
        add(iframe);

        iframe.add(new AttributeModifier("src", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return urlFor(ILinkListener.INTERFACE, new PageParameters()).toString();
            }
        }));
    }

    /**
     * Override and always return true, else Wicket throws an error when the
     * panel is hidden, e.g. on a popup that hasn't yet been displayed.
     */
    @Override
    public boolean canCallListenerInterface(Method method) {
        return true;
    }

    @Override
    protected boolean getStatelessHint() {
        return false;
    }

    @Override
    public void onLinkClicked() {
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

            @Override
            public void respond(IRequestCycle requestCycle) {
                IResource.Attributes a = new IResource.Attributes(requestCycle.getRequest(), requestCycle.getResponse());
                resource.respond(a);
            }

            @Override
            public void detach(IRequestCycle requestCycle) {
            }

        });

    }

    public IFramePanel setResource(IResource resource) {
        this.resource = resource;
        return this;
    }

}
