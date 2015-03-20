package ca.krasnay.panelized;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class TableRowPanel extends Panel {

    private RepeatingView cellRepeater;

    public TableRowPanel(String id) {
        super(id);
        add(cellRepeater = new RepeatingView("cell"));
    }

    public TableRowPanel addCell(Panel cell) {
        cellRepeater.add(cell);
        return this;
    }

    public TableRowPanel addCell(Panel cell, CellStyle style) {
        cellRepeater.add(cell.add(style));
        return this;
    }

    public TableRowPanel addCell(IModel<String> text) {
        return addCell(new TextPanel(newCellId(), text));
    }

    public TableRowPanel addCell(String text) {
        return addCell(Model.of(text));
    }

    public TableRowPanel addCell(String text, CellStyle style) {
        return addCell(Model.of(text), style);
    }

    public TableRowPanel addCell(IModel<String> text, CellStyle style) {
        return addCell(new TextPanel(newCellId(), text), style);
    }

    public String newCellId() {
        return cellRepeater.newChildId();
    }

    public void removeAllCells() {
        cellRepeater.removeAll();
    }

}
