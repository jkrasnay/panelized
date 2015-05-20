package ca.krasnay.panelized;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
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

    public static void init(Application app) {

        IPackageResourceGuard packageResourceGuard = app.getResourceSettings().getPackageResourceGuard();

        if (packageResourceGuard instanceof SecurePackageResourceGuard) {
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
            guard.addPattern("+*.woff");
            guard.addPattern("+*.woff2");
            guard.addPattern("+*.eot");
            guard.addPattern("+*.svg");
            guard.addPattern("+*.ttf");
        }
    }

}
