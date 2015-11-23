package ca.krasnay.panelized;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Panel containing a link to a bookmarkable page.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class BookmarkablePageLinkPanel extends Panel {

    private static IModel<String> getDefaultPageTitle(PageRef pageRef) {

        if (pageRef.getPageClass().isAnnotationPresent(PageTitle.class)) {
            String value = pageRef.getPageClass().getAnnotation(PageTitle.class).value();
            return new ResourceModel(value, value);
        } else {
            throw new RuntimeException("Page " + pageRef.getPageClass().getName() + " does not have a @PageTitle annotation");
        }
    }

    /**
     * Constructor that uses the page's {@link PageTitle} annotation to determine
     * link text. The value of the {@link PageTitle} annotation is used to look
     * up a string resource with that key. If not found, the value itself is
     * displayed, so the value can contain the page title directly for monolingual apps.
     */
    public BookmarkablePageLinkPanel(String panelId, PageRef pageRef) {
        this(panelId, getDefaultPageTitle(pageRef), pageRef);
    }

    public BookmarkablePageLinkPanel(String panelId, IModel<String> linkText, PageRef pageRef) {

        super(panelId);

        BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", pageRef.getPageClass(), pageRef.getPageParameters());
        add(link);

        link.add(new Label("text", linkText));

    }
}
