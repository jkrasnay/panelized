package ca.krasnay.panelized;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class TableHeaderRowPanel extends Panel {

    private RepeatingView cellRepeater;

    public TableHeaderRowPanel(String id) {
        super(id);
        add(cellRepeater = new RepeatingView("cell"));
    }

    public TableHeaderRowPanel addCell(Panel cell) {
        cellRepeater.add(cell);
        return this;
    }

    public TableHeaderRowPanel addCell(Panel cell, CellStyle style) {
        cellRepeater.add(cell.add(style));
        return this;
    }

    public TableHeaderRowPanel addCell(IModel<String> text) {
        return addCell(text, null);
    }

    public TableHeaderRowPanel addCell(String text) {
        return addCell(text, null);
    }

    public TableHeaderRowPanel addCell(String text, CellStyle style) {
        return addCell(Model.of(text), style);
    }

    public TableHeaderRowPanel addCell(IModel<String> text, CellStyle style) {
        TextPanel cell = new TextPanel(newCellId(), text);
        if (style != null) {
            cell.add(style);
        }
        cellRepeater.add(cell);
        return this;
    }

    public String newCellId() {
        return cellRepeater.newChildId();
    }

    public void removeAllCells() {
        cellRepeater.removeAll();
    }

}
