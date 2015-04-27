package ca.krasnay.panelized;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class AjaxLinkPanel extends Panel {

    public AjaxLinkPanel(String panelId, IModel<String> labelModel, final AjaxAction action) {

        super(panelId);

        AjaxLink<Void> link = new AjaxLink<Void>("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                action.invoke(target);
            }
        };
        add(link);

        link.add(new Label("label", labelModel));

    }
}
