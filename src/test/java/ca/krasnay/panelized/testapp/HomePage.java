package ca.krasnay.panelized.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.ClientTabbedPanel;
import ca.krasnay.panelized.Panelized;
import ca.krasnay.panelized.TextPanel;

public class HomePage extends WebPage {

    public HomePage() {

        List<ITab> tabs = new ArrayList<>();

        tabs.add(new AbstractTab(Model.of("Tab 1")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new TextPanel(panelId, "Tab 1 content");
            }
        });

        tabs.add(new AbstractTab(Model.of("Tab 2")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new TextPanel(panelId, "Tab 2 content");
            }
        });

        add(new ClientTabbedPanel("tabs", tabs));

        add(new TabbedPanel<ITab>("wtabs", tabs));
    }

    @Override
    public void renderHead(IHeaderResponse response) {

        super.renderHead(response);

        response.render(Panelized.CSS);
        response.render(Panelized.FONT_AWESOME_CSS);
        response.render(Panelized.JS);
        response.render(Panelized.JS_INIT);

    }

}


