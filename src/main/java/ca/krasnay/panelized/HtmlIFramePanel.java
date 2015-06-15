package ca.krasnay.panelized;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.IResource;

public class HtmlIFramePanel extends IFramePanel {

    public HtmlIFramePanel(String id, final IModel<String> htmlModel) {

        super(id);

        IResource resource = new IResource() {
            @Override
            public void respond(Attributes attributes) {
                WebResponse response = (WebResponse) attributes.getResponse();
                response.setContentType("text/html; charset=utf-8");
                response.write(htmlModel.getObject());
            }
        };

        setResource(resource);

    }

}
