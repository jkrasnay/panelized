package ca.krasnay.panelized.testapp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

import ca.krasnay.panelized.AjaxAction;
import ca.krasnay.panelized.AjaxLinkPanel;
import ca.krasnay.panelized.TablePanel;
import ca.krasnay.panelized.TableRowPanel;
import ca.krasnay.panelized.ToolStyle;

public class LinkTab extends AbstractTab {

    public LinkTab() {

        super(Model.of("Links"));

    }

    @Override
    public WebMarkupContainer getPanel(String panelId) {

        TablePanel tablePanel = new TablePanel(panelId);

        tablePanel.setHeadings("Link", "Description");

        AjaxAction action = new AjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
            }
        };

        TableRowPanel row;

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action));
        row.addCell("Link");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setStyle(ToolStyle.BUTTON));
        row.addCell("Button");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setStyle(ToolStyle.BUTTON).setDefaultButton(true));
        row.addCell("Default Button");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setStyle(ToolStyle.HOVER_BUTTON));
        row.addCell("Hover Button");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setEnabled(false));
        row.addCell("Disabled Link");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setStyle(ToolStyle.BUTTON).setEnabled(false));
        row.addCell("Disabled Button");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setStyle(ToolStyle.BUTTON).setDefaultButton(true).setEnabled(false));
        row.addCell("Disabled Default Button");

        tablePanel.addRow(row = new TableRowPanel(tablePanel.newRowId()));
        row.addCell(new AjaxLinkPanel(row.newCellId(), "star-o", Model.of("Hello"), action).setStyle(ToolStyle.HOVER_BUTTON).setEnabled(false));
        row.addCell("Disabled Hover Button");

        return tablePanel;

    }
}
