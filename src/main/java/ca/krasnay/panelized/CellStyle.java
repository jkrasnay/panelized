package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.parser.XmlTag;

/**
 * Behaviour that modifies the styling of a table cell. Typically applied to
 * cell panels that have been added to TableRowPanel and TableHeaderRowPanel.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class CellStyle extends Behavior {

    private enum Align {
        LEFT,
        CENTER,
        RIGHT;
    }

    private enum Valign {
        TOP,
        MIDDLE,
        BOTTOM;
    }

    private boolean bold;

    private Align align = Align.LEFT;

    private Valign valign;

    private boolean indent;

    private boolean narrow;

    public CellStyle() {
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        if (tag.getType() != XmlTag.TagType.CLOSE) {
            String cssClass = tag.getAttribute("class");
            String newClass = cssClass == null ? getCssClasses() : cssClass + " " + getCssClasses();
            tag.put("class", newClass);
        }
    }

    public CellStyle bold() {
        this.bold = true;
        return this;
    }

    public CellStyle alignCenter() {
        this.align = Align.CENTER;
        return this;
    }

    public CellStyle alignRight() {
        this.align = Align.RIGHT;
        return this;
    }

    public CellStyle valignTop() {
        this.valign = Valign.TOP;
        return this;
    }

    public CellStyle valignBottom() {
        this.valign = Valign.BOTTOM;
        return this;
    }

    public CellStyle indent() {
        this.indent = true;
        return this;
    }

    public CellStyle narrow() {
        this.narrow = true;
        return this;
    }

    private String getCssClasses() {

        StringBuilder sb = new StringBuilder();

        if (bold) {
            sb.append("cell-bold ");
        }

        if (align == Align.CENTER) {
            sb.append("cell-alignCenter ");
        }

        if (align == Align.RIGHT) {
            sb.append("cell-alignRight ");
        }

        if (valign == Valign.TOP) {
            sb.append("cell-valignTop ");
        }

        if (valign == Valign.BOTTOM) {
            sb.append("cell-valignBottom ");
        }

        if (indent) {
            sb.append("cell-indent ");
        }

        if (narrow) {
            sb.append("cell-narrow");
        }

        return sb.toString();
    }
}
