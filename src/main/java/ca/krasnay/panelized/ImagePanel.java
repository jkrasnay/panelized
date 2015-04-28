package ca.krasnay.panelized;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class ImagePanel extends Panel {

    public enum Overflow {
        HIDDEN,
        SCROLL,
        SHRINK_TO_FIT;
    }

    private Overflow overflow = Overflow.SHRINK_TO_FIT;

    public ImagePanel(String id, IModel<String> srcModel, IModel<String> altModel) {

        super(id);

        setOutputMarkupId(true);

        WebMarkupContainer img = new WebMarkupContainer("img");
        add(img);
        img.add(new AttributeAppender("src", srcModel));
        img.add(new AttributeAppender("alt", altModel));

        img.add(new AttributeAppender("style", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                switch (overflow) {
                case HIDDEN:
                    return "overflow:hidden";
                case SCROLL:
                    return "scroll:auto";
                case SHRINK_TO_FIT:
                    return "max-width:100%";
                default:
                    return null;
                }
            }
        }, ";"));

    }

    public ImagePanel setOverflow(Overflow overflow) {
        this.overflow = overflow;
        return this;
    }
}
