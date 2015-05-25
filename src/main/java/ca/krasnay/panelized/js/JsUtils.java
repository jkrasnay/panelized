package ca.krasnay.panelized.js;

public class JsUtils {

    public static String doubleQuote(String s) {
        return "\"" + s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "") + "\"";
    }

    public static String singleQuote(String s) {
        return "'" + s.replace("'", "\\'").replace("\n", "\\n").replace("\r", "") + "'";
    }
}
