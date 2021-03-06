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
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IComponentAssignedModel;
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

        private PopupSettings popupSettings;

        private PageLinkAction(PageRef pageRef, IModel<String> linkTextModel, PopupSettings popupSettings) {
            this.pageRef = pageRef;
            this.linkTextModel = linkTextModel;
            this.popupSettings = popupSettings;
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

    private ToolStyle style = ToolStyle.LINK;

    private RepeatingView itemRepeater;

    private RepeatingView panelRepeater;

    private IModel<String> toolTipModel;

    private boolean visible = true;

    /**
     * True if we want to add a separator. In order to avoid doubled-up
     * separators or separators at the end of the menu, addSeparator() just
     * sets this flag. The separator is only really added via checkSeparator()
     * when adding a "real" menu item.
     */
    private boolean pendingSeparator;

    public DropDownMenuPanel(String id, String iconName, IModel<String> textModel, AccessController accessController) {

        super(id);

        this.accessController = accessController;

        setOutputMarkupId(true);

        WebMarkupContainer link = new WebMarkupContainer("link");
        add(link);

        link.add(new IconLabelPanel("label", iconName, textModel));

        link.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                switch (style) {
                case BUTTON:
                    return "pnl-Button";
                case HOVER_BUTTON:
                    return "pnl-Button pnl-Button--hover";
                default:
                    return "pnl-Link";
                }
            }
        }, " "));

        link.add(new AttributeModifier("title", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return toolTipModel != null ? toolTipModel.getObject() : null;
            }
        }));

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

    public DropDownMenuPanel(String id, String iconName, AccessController acl) {
        this(id, iconName, null, acl);
    }

    public DropDownMenuPanel(String id, IModel<String> textModel, AccessController acl) {
        this(id, null, textModel, acl);
    }

    public void addAction(final NamedAjaxAction action) {
        if (accessController != null && accessController.canAccess(action.getClass())) {
            actions.add(action);
            if (action instanceof Panel) {
                panelRepeater.add((Panel) action);
            }
        }
    }

    public void addExternalLink(IModel<String> url, String linkText) {
        actions.add(new ExternalLinkAction(url, Model.of(linkText)));
    }

    public void addPageLink(final PageRef pageRef, IModel<String> linkTextModel) {
        addPageLink(pageRef, linkTextModel, null);
    }

    public void addPageLink(PageRef pageRef, IModel<String> linkTextModel, PopupSettings popupSettings) {
        if (accessController != null && accessController.canAccess(pageRef.getPageClass())) {
            actions.add(new PageLinkAction(pageRef, linkTextModel, popupSettings));
        }
    }

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
            item.add(new AttributeModifier("class", "pnl-DropDownMenu-separator"));
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
//        subMenu.label.add(new AttributeModifier("class", ""));

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
                        tag.put("class", tag.getAttribute("class") + " is-disabled force-disabled");
                    } else {
                        tag.put("class", "is-disabled force-disabled");
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
            link.setPopupSettings(action.popupSettings);

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

    public DropDownMenuPanel setStyle(ToolStyle style) {
        this.style = style;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DropDownMenuPanel setToolTip(IModel<String> model) {
        if (model instanceof IComponentAssignedModel) {
            this.toolTipModel = ((IComponentAssignedModel)model).wrapOnAssignment(this);
        } else{
            this.toolTipModel = model;
        }
        return this;
    }

}
