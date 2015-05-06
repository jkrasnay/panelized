package ca.krasnay.panelized.testapp;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AccessController;
import ca.krasnay.panelized.ContainerPanel;
import ca.krasnay.panelized.DropDownMenuPanel;
import ca.krasnay.panelized.NamedAjaxAction;

public class DropDownMenuTab extends AbstractTab {

    public DropDownMenuTab() {
        super(Model.of("Drop Down Menu"));
    }

    private DropDownMenuPanel buildMenu(DropDownMenuPanel menu) {

        menu.addSeparator(); // confirm that leading separator is removed

        menu.addAction(new ShowTestDialogActionPanel(menu.newPanelId(), null));

        menu.addAction(new NamedAjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                System.out.println("Bar clicked");
            }
            @Override
            public String getName(Locale locale) {
                return "Bar";
            }
        });

        menu.addSeparator();

        menu.addSeparator(); // confirm that repeated separators are removed

        menu.addAction(new NamedAjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                System.out.println("Baz clicked");
            }
            @Override
            public String getName(Locale locale) {
                return "Baz";
            }
        });

        menu.addSeparator(); // confirm that trailing separator is removed

        return menu;

    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

        ContainerPanel container = new ContainerPanel(panelId);
        container.setRenderDivs(false);

        AccessController allAccess = new AccessController() {
            @Override
            public boolean canAccess(Class<?> uiElementClass) {
                return true;
            }
        };

        container.addPanel(buildMenu(new DropDownMenuPanel(container.newPanelId(), "cog", allAccess)));
        container.addPanel(buildMenu(new DropDownMenuPanel(container.newPanelId(), Model.of("Menu"), allAccess)));
        container.addPanel(buildMenu(new DropDownMenuPanel(container.newPanelId(), "cog", Model.of("Menu"), allAccess)));

        container.addPanel(buildMenu(new DropDownMenuPanel(container.newPanelId(), "cog", allAccess).setButtonLike(true)));
        container.addPanel(buildMenu(new DropDownMenuPanel(container.newPanelId(), Model.of("Menu"), allAccess).setButtonLike(true)));
        container.addPanel(buildMenu(new DropDownMenuPanel(container.newPanelId(), "cog", Model.of("Menu"), allAccess).setButtonLike(true)));


        return container;

    }
}
