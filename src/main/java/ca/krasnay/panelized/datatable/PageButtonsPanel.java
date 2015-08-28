package ca.krasnay.panelized.datatable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.AjaxLinkPanel;
import ca.krasnay.panelized.SpanContainerPanel;
import ca.krasnay.panelized.ToolStyle;

/**
 * Displays buttons for paging through a data table's pages.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class PageButtonsPanel extends SpanContainerPanel {

    private DataTablePanel<?> dataTablePanel;

    public PageButtonsPanel(String id, DataTablePanel<?> dataTablePanel) {

        super(id);

        this.dataTablePanel = dataTablePanel;

        dataTablePanel.addForRefresh(this);

        AjaxAction prevAction = new AjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                getDataTablePanel().previousPage(target);
            }
        };

        addPanel(new AjaxLinkPanel(newPanelId(), "arrow-left", prevAction) {
            @Override
            public boolean isEnabled() {
                return !getDataTablePanel().isFirstPage();
            }
        }.setStyle(ToolStyle.HOVER_BUTTON)
        .setToolTip(Model.of("Previous Page")));

        AjaxAction nextAction = new AjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                getDataTablePanel().nextPage(target);
            }
        };

        addPanel(new AjaxLinkPanel(newPanelId(), "arrow-right", nextAction) {
            @Override
            public boolean isEnabled() {
                return !getDataTablePanel().isLastPage();
            }
        }.setStyle(ToolStyle.HOVER_BUTTON)
        .setToolTip(Model.of("Next Page")));

    }

    private DataTablePanel<?> getDataTablePanel() {
        return dataTablePanel;
    }

}
