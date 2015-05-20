package ca.krasnay.panelized.testapp;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import ca.krasnay.panelized.Panelized;

public class PanelizedTestApp extends WebApplication {

    public static void main(String[] args) throws Exception {

        Server server = new Server(5000);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS|ServletContextHandler.NO_SECURITY);
        server.setHandler(handler);

        FilterHolder wicketFilter = new FilterHolder(WicketFilter.class);
        wicketFilter.setInitParameter(WicketFilter.APP_FACT_PARAM, ContextParamWebApplicationFactory.class.getName());
        wicketFilter.setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, PanelizedTestApp.class.getName());

        // This line is the only surprise when comparing to the equivalent
        // web.xml. Without some initialization seems to be missing.
        wicketFilter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");

        handler.addFilter(wicketFilter, "/*", EnumSet.of(DispatcherType.REQUEST));

        server.start();
        System.out.println("Panelized test app started on port 5000");

        server.join();

    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {

        super.init();

        Panelized.init(this);

        getMarkupSettings().setStripWicketTags(true);
        getMarkupSettings().setDefaultBeforeDisabledLink("");
        getMarkupSettings().setDefaultAfterDisabledLink("");

    }

}
