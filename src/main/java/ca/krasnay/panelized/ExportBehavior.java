package ca.krasnay.panelized;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * Behavior that can be attached to a panel to perform a binary
 * file download. The download must be queued by a previous AJAX
 * request via {@link #queueExport(AjaxRequestTarget)}.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ExportBehavior extends AbstractAjaxBehavior {

    private ExportAction exportAction;

    public ExportBehavior(ExportAction exportAction) {
        this.exportAction = exportAction;
    }

    @Override
    public void onRequest() {

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
            @Override
            public String getContentType() {
                return exportAction.getContentType();
            }
            @Override
            public void write(OutputStream output) throws IOException {
                exportAction.writeContent(output);
            }

        };

        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream, exportAction.getFileName()));

    }

    /**
     * Queues the export to happen shortly after the current AJAX call completes.
     */
    protected void queueExport(AjaxRequestTarget target) {

        String url = getCallbackUrl().toString();

        url = url + (url.contains("?") ? "&" : "?");
        url = url + "antiCache=" + System.currentTimeMillis();

        // the timeout is needed to let Wicket release the channel
        target.appendJavaScript("setTimeout(\"window.location.href='" + url + "'\", 100);");

    }

}
