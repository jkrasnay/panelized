package ca.krasnay.panelized;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Base class from which applications can load Panelized resources.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public final class Panelized {

    public static final CssHeaderItem CSS = CssHeaderItem.forReference(new PackageResourceReference(Panelized.class, "Panelized.css"));

    public static final CssHeaderItem FONT_AWESOME_CSS = CssHeaderItem.forReference(new PackageResourceReference(Panelized.class, "fontawesome/css/font-awesome.min.css"));

    public static final JavaScriptReferenceHeaderItem JS = JavaScriptHeaderItem.forReference(new PackageResourceReference(Panelized.class, "Panelized.js"));

    public static final OnDomReadyHeaderItem JS_INIT = OnDomReadyHeaderItem.forScript("Panelized.init()");

}
