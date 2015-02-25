package ca.krasnay.panelized;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class BookmarkablePageLinkPanel extends Panel {

    private static IModel<String> getDefaultPageTitle(PageRef pageRef) {

        if (pageRef.getPageClass().isAnnotationPresent(PageTitle.class)) {
            return Model.of(pageRef.getPageClass().getAnnotation(PageTitle.class).value());
        } else {
            throw new RuntimeException("Page " + pageRef.getPageClass().getName() + " does not have a @PageRef annotation");
        }
    }

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
