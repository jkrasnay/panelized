package ca.krasnay.panelized;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel containing an icon and a link. Used in links, buttons, and drop-down menus.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class IconLabelPanel extends Panel {

    public IconLabelPanel(String id, String iconName, IModel<String> textModel) {

        super(id);

        WebMarkupContainer icon = new WebMarkupContainer("icon");
        add(icon);
        icon.setVisible(iconName != null);
        icon.add(new AttributeAppender("class", Model.of("fa fa-" + iconName), " "));

        Label text = new Label("text", textModel);
        add(text);
        text.setVisible(textModel != null);

    }
}
