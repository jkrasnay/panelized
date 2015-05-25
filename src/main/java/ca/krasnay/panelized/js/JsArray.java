package ca.krasnay.panelized.js;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building Javascript arrays.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class JsArray extends JsBuilder {

    private List<Object> items = new ArrayList<Object>();

    public JsArray add(Boolean value) {
        items.add(value);
        return this;
    }

    public JsArray add(JsBuilder value) {
        items.add(value);
        return this;
    }

    public JsArray add(Number value) {
        items.add(value);
        return this;
    }

    public JsArray add(String value) {
        items.add(value);
        return this;
    }

    @Override
    void buildString(StringBuilder sb, String indent, boolean json) {

        sb.append("[");
        boolean first = true;
        for (Object value : items) {
            if (!first) {
                sb.append(",\n");
            }
            first = false;
            appendValue(sb, value, indent, json);
        }
        sb.append("\n").append(indent).append("]");
    }

}
