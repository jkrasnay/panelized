package ca.krasnay.panelized.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.ClientTabbedPanel;
import ca.krasnay.panelized.TextPanel;

public class TabsTab extends AbstractTab {

    public TabsTab() {
        super(Model.of("Tabs"));
    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

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

        return new ClientTabbedPanel(panelId, tabs);

    }
}
