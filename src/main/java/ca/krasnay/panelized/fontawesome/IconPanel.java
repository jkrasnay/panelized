package ca.krasnay.panelized.fontawesome;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class IconPanel extends Panel {

    private String iconName;

    public IconPanel(String panelId, String iconName) {

        super(panelId);

        WebMarkupContainer icon = new WebMarkupContainer("icon");
        add(icon);

        this.iconName = iconName;

        icon.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "fa-" + IconPanel.this.iconName;
            }
        }, " "));
    }
}
