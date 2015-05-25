package ca.krasnay.panelized.js;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for building Javascript objects.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class JsObject extends JsBuilder {

    private static final Pattern REGEX_IDENT = Pattern.compile("[_a-z0-9]+", Pattern.CASE_INSENSITIVE);

    private Map<String, Serializable> fields = new LinkedHashMap<String, Serializable>();

    private JsObject parent;

    public JsObject() {

    }

    private JsObject(JsObject parent) {
        this.parent = parent;
    }

    public JsObject put(String key, Boolean value) {
        fields.put(key, value);
        return this;
    }

    public JsObject put(String key, JsBuilder value) {
        fields.put(key, value);
        return this;
    }

    public JsObject put(String key, Number value) {
        fields.put(key, value);
        return this;
    }

    public JsObject put(String key, String s) {
        fields.put(key, s);
        return this;
    }

    public JsObject remove(String key) {
        fields.remove(key);
        return this;
    }

    public JsObject beginObject(String key) {
        JsObject child = new JsObject(this);
        put(key, child);
        return child;
    }

    public JsObject endObject() {
        if (parent != null) {
            return parent;
        } else {
            throw new RuntimeException("Called endObject() without a corresponding beginObject call");
        }
    }

    @Override
    void buildString(StringBuilder sb, String indent, boolean json) {

        sb.append("{");

        boolean first = true;
        for (String key : fields.keySet()) {

            if (!first) {
                sb.append(",");
            }
            first = false;

            sb.append("\n").append(indent).append(INDENT);

            if (json || !REGEX_IDENT.matcher(key).matches()) {
                sb.append(JsUtils.doubleQuote(key));
            } else {
                sb.append(key);
            }

            sb.append(": ");

            appendValue(sb, fields.get(key), indent, json);

        }

        sb.append("\n").append(indent).append("}");

    }
}
