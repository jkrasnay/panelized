package ca.krasnay.panelized.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;

import ca.krasnay.panelized.Panelized;

public class HomePage extends WebPage {

    public HomePage() {

        List<ITab> tabs = new ArrayList<>();
        tabs.add(new DataTableTab());
        tabs.add(new DatePickerTab());
        tabs.add(new DialogTab());
        tabs.add(new DropDownMenuTab());
        tabs.add(new LinkTab());
        tabs.add(new TabsTab());

        add(new TabbedPanel<ITab>("tabs", tabs));

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


