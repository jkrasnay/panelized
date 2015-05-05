package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.AjaxLinkPanel;
import ca.krasnay.panelized.ContainerPanel;

public class DialogTab extends AbstractTab {

    public DialogTab() {
        super(Model.of("Dialog"));
    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

        ContainerPanel container = new ContainerPanel(panelId);

        AjaxAction onSaveAction = new AjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                // TODO update something
            }
        };

        final ShowTestDialogActionPanel showDialogPanel = new ShowTestDialogActionPanel(container.newPanelId(), onSaveAction);
        container.addPanel(showDialogPanel);

        AjaxAction showDialogAction = new AjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                showDialogPanel.invoke(target);
            }
        };

        container.addPanel(new AjaxLinkPanel(container.newPanelId(), Model.of("Show Test Dialog"), showDialogAction));

        return container;

    }
}
