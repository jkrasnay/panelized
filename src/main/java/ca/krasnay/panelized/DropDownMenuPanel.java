package ca.krasnay.panelized;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes.EventPropagation;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A drop-down menu in the Bootstrap style, consisting of a link to trigger
 * the menu and a list of links that are the menu items.
 *
 * Page links and actions can be added as menu items, and are subject to access
 * control checks.
 *
 * Note that if this panel is in a button group with other controls, we have to
 * attach it to a &lt;wicket:container&gt; instead of a real element. In this case
 * the menu is not AJAX-refreshable. You need to refresh a parent component instead.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DropDownMenuPanel extends Panel {

    private static class ExternalLinkAction implements Serializable {

        private IModel<String> url;

        private IModel<String> linkTextModel;

        private ExternalLinkAction(IModel<String> url, IModel<String> linkTextModel) {
            this.url = url;
            this.linkTextModel = linkTextModel;
        }

    }

    private static class PageLinkAction implements Serializable {

        private PageRef pageRef;

        private IModel<String> linkTextModel;

        private String popupWindowId;

        private PageLinkAction(PageRef pageRef, IModel<String> linkTextModel, String popupWindowId) {
            this.pageRef = pageRef;
            this.linkTextModel = linkTextModel;
            this.popupWindowId = popupWindowId;
        }

    }

    // Note we used to just use a String instance here, but it
    // broke in Wicket 1.5 serialization.
    private enum DummyAction {
        SEPARATOR
    }

    private AccessController accessController;

    /**
     * List of actions to be added to the menu. Each item in this list can
     * be one of the following:
     *
     *  - a NamedAjaxAction
     *  - the SEPARATOR_ACTION constant
     *  - a PageLinkAction
     *  - an ExternalLinkAction
     *  - a DropDownMenuPanel representing a sub-menu
     *
     * We use this list to re-generate the menu each time it is rendered, to
     * take into account changing visibility, enablement, and permissions.
     *
     */
    private List<Object> actions = new ArrayList<Object>();

    private boolean alignRight;

    private Label label;

    private String icon = "icon-cog";

    private RepeatingView itemRepeater;

    private RepeatingView panelRepeater;

    private boolean visible = true;

    /**
     * True if we want to add a separator. In order to avoid doubled-up
     * separators or separators at the end of the menu, addSeparator() just
     * sets this flag. The separator is only really added via checkSeparator()
     * when adding a "real" menu item.
     */
    private boolean pendingSeparator;

    public DropDownMenuPanel(String id, AccessController accessController) {
        this(id, accessController, false);
    }

    public DropDownMenuPanel(String id, AccessController accessController, boolean mini) {
        this(id, (String) null, accessController);
        label.setDefaultModel(new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return String.format("<i class='%s'></i>", icon);
            }
        });
        label.setEscapeModelStrings(false);
        label.add(new AttributeAppender("class", Model.of(mini ? "btn btn-mini" : "btn"), " "));
    }

    public DropDownMenuPanel(String id, IModel<String> name, AccessController accessController) {

        super(id);

        this.accessController = accessController;

        setOutputMarkupId(true);

        add(label = new Label("name", name));

        WebMarkupContainer menu = new WebMarkupContainer("menu");
        add(menu);
        menu.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return alignRight ? "pull-right" : null;
            }
        }, " "));

        menu.add(itemRepeater = new RepeatingView("item"));
        add(panelRepeater = new RepeatingView("panel"));
    }

    public DropDownMenuPanel(String id, String name, AccessController acl) {
        this(id, Model.of(name), acl);
    }

    public void addAction(final NamedAjaxAction action) {
        actions.add(action);
    }

    public void addActionPanel(Panel panel) {

        assert panel instanceof NamedAjaxAction : "Panel must implement NamedAjaxAction";

        if (accessController != null && accessController.canAccess(panel.getClass())) {
            addAction((NamedAjaxAction) panel);
            panelRepeater.add(panel);
        }

    }

    public void addExternalLink(IModel<String> url, String linkText) {
        actions.add(new ExternalLinkAction(url, Model.of(linkText)));
    }

//    public void addPageLink(Class<? extends Page> pageClass) {
//        addPageLink(new PageRef(pageClass));
//    }

//    public void addPageLink(Class<? extends Page> pageClass, int entityId) {
//        addPageLink(new PageRef(pageClass, entityId));
//    }

//    public void addPageLink(PageRef pageRef) {
//        addPageLink(pageRef, Model.of(BasePage.getPageTitle(pageRef.getPageClass())));
//    }

    public void addPageLink(final PageRef pageRef, IModel<String> linkTextModel) {
        addPageLink(pageRef, linkTextModel, null);
    }

    public void addPageLink(PageRef pageRef, IModel<String> linkTextModel, String popupWindowId) {
        actions.add(new PageLinkAction(pageRef, linkTextModel, popupWindowId));
    }

    /**
     * Adds a registration link to the page-level action list. Similar to
     * {@link #addPageLink(PageRef)}, but adds an onclick handler
     * to show the link in a popup.
     *
     * @param pageRef
     *            PageRef of the page to be displayed.
     * @param requiredPermission
     *            Required EventPermission for this link. We can't derive this
     *            from the page, since RegistrationPage is @PublicAccess
     * @param linkText
     *            Text of the link to display, e.g. "Register Another Attendee".
     */
//    public void addRegistrationLink(PageRef pageRef, EventPermission requiredPermission, String linkText) {
//        if (CurrentEffregUser.isSuperUser() || acl.hasPermission(User.getCurrentUser(), requiredPermission.getMask())) {
//            addPageLink(pageRef, Model.of(linkText), "register");
//        }
//    }

    public void addSeparator() {
        actions.add(DummyAction.SEPARATOR);
    }

//    public DropDownMenuPanel addSubMenu(String name) {
//        DropDownMenuPanel subMenu = new DropDownMenuPanel("submenu", name, acl);
//        actions.add(subMenu);
//        return subMenu;
//    }

    private void checkSeparator() {
        if (pendingSeparator && itemRepeater.size() > 0) {
            WebMarkupContainer item = new WebMarkupContainer(itemRepeater.newChildId());
            itemRepeater.add(item);
            item.add(new AttributeModifier("class", "divider"));
            WebMarkupContainer link = new WebMarkupContainer("link");
            item.add(link);
            link.setVisible(false);
            link.add(new WebMarkupContainer("text"));
            item.add(new EmptyPanel("submenu"));
        }
        pendingSeparator = false;
    }

    /**
     * Clears the menu, removing all actions.
     */
    public void clearMenu() {
        actions.clear();
        itemRepeater.removeAll();
        panelRepeater.removeAll();
    }

    private void createLink(DropDownMenuPanel subMenu) {

        visible = true;

        checkSeparator();

        WebMarkupContainer item = new WebMarkupContainer(itemRepeater.newChildId());
        itemRepeater.add(item);
        item.add(new AttributeModifier("class", "dropdown-submenu"));

        item.add(new EmptyPanel("link").setVisible(false));

        item.add(subMenu);

        // Remove class="dropdown-toggle", which interferes with the styling
        subMenu.label.add(new AttributeModifier("class", ""));

    }

    private void createLink(ExternalLinkAction action) {

        visible = true;

        checkSeparator();

        WebMarkupContainer item = new WebMarkupContainer(itemRepeater.newChildId());
        itemRepeater.add(item);

        WebMarkupContainer link = new WebMarkupContainer("link");
        item.add(link);
        link.add(new AttributeModifier("href", action.url));
        link.add(new Label("text", action.linkTextModel).setRenderBodyOnly(true));

        item.add(new EmptyPanel("submenu"));

    }

    private void createLink(final NamedAjaxAction action) {

        boolean actionVisible = action instanceof ConditionalAction ? ((ConditionalAction) action).isActionVisible() : true;

        if (accessController != null && accessController.canAccess(action.getClass()) && actionVisible) {

            visible = true;

            checkSeparator();

            WebMarkupContainer item = new WebMarkupContainer(itemRepeater.newChildId());
            itemRepeater.add(item);

            AjaxLink<Void> link = new AjaxLink<Void>("link") {

                @Override
                protected void disableLink(ComponentTag tag) {

                    //
                    // NOTE: we set force-disabled here to prevent a data table on the page
                    // from re-enabling us. That's because the data table will disable page-level
                    // menus if any of the rows are checked.
                    //

                    if (tag.getAttributes().containsKey("class")) {
                        tag.put("class", tag.getAttribute("class") + " disabled force-disabled");
                    } else {
                        tag.put("class", "disabled force-disabled");
                    }
                }

                @Override
                public boolean isEnabled() {
                    if (action instanceof ConditionalAction) {
                        return ((ConditionalAction) action).isActionEnabled();
                    } else {
                        return super.isEnabled();
                    }
                }

                @Override
                public boolean isVisible() {
                    if (action instanceof ConditionalAction) {
                        return ((ConditionalAction) action).isActionVisible();
                    } else {
                        return super.isVisible();
                    }
                }

                @Override
                public void onClick(AjaxRequestTarget target) {
                    action.invoke(target);
                }

                @Override
                protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                    // Needed so Bootstrap hides the menu
                    attributes.setEventPropagation(EventPropagation.BUBBLE);
                }

            };
            item.add(link);

            IModel<String> linkTextModel = new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return action.getName(getLocale());
                }
            };

            link.add(new Label("text", linkTextModel).setRenderBodyOnly(true));

            item.add(new EmptyPanel("submenu"));

        }

    }

    private void createLink(final PageLinkAction action) {

        if (true /*action.pageRef.canBeAccessedBy(acl)*/) {

            visible = true;

            checkSeparator();

            WebMarkupContainer item = new WebMarkupContainer(itemRepeater.newChildId());
            itemRepeater.add(item);

            BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", action.pageRef.getPageClass(), action.pageRef.getPageParameters()) {
                @Override
                protected void disableLink(ComponentTag tag) {
                    tag.put("class", "disabled");
                }
                @Override
                public boolean isEnabled() {
                    return action.pageRef.isEnabled();
                }
            };
            item.add(link);

            if (action.popupWindowId != null) {
                link.add(new AttributeModifier("onclick", String.format("return effreg.popup(this, '%s')", action.popupWindowId)));
            }

            link.add(new Label("text", action.linkTextModel).setRenderBodyOnly(true));

            item.add(new EmptyPanel("submenu"));

        }
    }

    @Override
    public boolean isVisible() {
        return visible; //itemRepeater.size() > 0;
    }

    public String newPanelId() {
        return panelRepeater.newChildId();
    }

    @Override
    protected void onBeforeRender() {
        rebuildMenu();
        super.onBeforeRender();
    }

    /**
     * Rebuilds the menu. Called each time the component is rendered.
     */
    private void rebuildMenu() {

        itemRepeater.removeAll();
        pendingSeparator = false;
        visible = false;

        for (Object action : actions) {
            if (action instanceof NamedAjaxAction) {
                createLink((NamedAjaxAction) action);
            } else if (action instanceof PageLinkAction) {
                createLink((PageLinkAction) action);
            } else if (action instanceof ExternalLinkAction) {
                createLink((ExternalLinkAction) action);
            } else if (action instanceof DropDownMenuPanel) {
                createLink((DropDownMenuPanel) action);
            } else if (action == DummyAction.SEPARATOR) {
                pendingSeparator = true;
            } else {
                throw new RuntimeException("Unrecognized action class: " + action.getClass() + ", value '" + action + "'");
            }
        }

    }

    public DropDownMenuPanel setAlignRight() {
        alignRight = true;
        return this;
    }

    public DropDownMenuPanel setIcon(String icon) {
        this.icon = icon;
        return this;
    }


}
