package ca.krasnay.panelized.js;

/**
 * Builder that represents a string of Javascript code. This is output
 * verbatim and no escaping is done.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class JsCode extends JsBuilder {

    private String code;

    public JsCode(String code) {
        this.code = code;
    }


    @Override
    void buildString(StringBuilder sb, String indent, boolean json) {

        if (json) {
            throw new RuntimeException("JSON does not support JavaScript code");
        }

        sb.append(indent).append(code);

    }

}
