package ca.krasnay.panelized.js;

import java.io.Serializable;

/**
 * Utility methods used by builders.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class JsBuilder implements Serializable {

    protected static final String INDENT = "  ";

    protected void appendValue(StringBuilder sb, Object value, String indent, boolean json) {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof Boolean) {
            sb.append(value.toString());
        } else if (value instanceof Number) {
            sb.append(value.toString());
        } else if (value instanceof String) {
            sb.append(JsUtils.doubleQuote((String) value));
        } else if (value instanceof JsBuilder) {
            sb.append("\n").append(indent).append(INDENT);
            ((JsBuilder) value).buildString(sb, indent + INDENT, json);
        } else {
            throw new RuntimeException("Unsupported type: " + value.getClass());
        }
    }

    abstract void buildString(StringBuilder sb, String indent, boolean json);

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        buildString(sb, "", true);
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        buildString(sb, "", false);
        return sb.toString();
    }

}
