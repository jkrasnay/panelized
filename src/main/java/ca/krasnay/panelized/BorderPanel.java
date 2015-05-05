package ca.krasnay.panelized;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel for adding a standard border around content. The border may contain a
 * header and/or footer, each with zero or more items floated left or right
 * within the panel.
 *
 * The preferred way to add child content to this panel is via the
 * {@link #addPanel(Panel)} method. However, this panel has a
 * &lt;wicket:child/&gt; element in the body section so it can be reasonably
 * used with markup inheritance.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class BorderPanel extends Panel implements PanelContainer {

    private String anchorName;

    private String cssClass;

    private RepeatingView headerLeftItemRepeater;

    private RepeatingView headerRightItemRepeater;

    private RepeatingView footerLeftItemRepeater;

    private RepeatingView footerRightItemRepeater;

    private RepeatingView panelRepeater;

    private boolean padded;

    private BorderTitlePanel titlePanel;

    public BorderPanel(String id) {
        this(id, null);
    }

    public BorderPanel(String id, IModel<?> model) {

        super(id, model);

        setOutputMarkupId(true);

        add(new WebMarkupContainer("anchor") {
            @Override
            public boolean isVisible() {
                return anchorName != null;
            }
        }.add(new AttributeModifier("name", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return anchorName;
            }
        })));

        WebMarkupContainer wrapper = new WebMarkupContainer("wrapper");
        add(wrapper);

        wrapper.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return getCssClass();
            }
        }, " "));

        wrapper.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return padded ? "pnl-Border--padded" : null;
            }
        }, " "));

        //
        // Header
        //

        WebMarkupContainer header = new WebMarkupContainer("header");
        wrapper.add(header);

        header.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                if (headerLeftItemRepeater.size() == 0 && headerRightItemRepeater.size() == 0) {
                    return "pnl-Border-hd--empty";
                } else {
                    return null;
                }
            }
        }, " "));

        headerLeftItemRepeater = new RepeatingView("leftItem");
        header.add(headerLeftItemRepeater);

        headerRightItemRepeater = new RepeatingView("rightItem");
        header.add(headerRightItemRepeater);

        //
        // Footer
        //

        WebMarkupContainer footer = new WebMarkupContainer("footer");
        wrapper.add(footer);

        footer.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                if (footerLeftItemRepeater.size() == 0 && footerRightItemRepeater.size() == 0) {
                    return "pnl-Border-ft--empty";
                } else {
                    return null;
                }
            }
        }, " "));

        footerLeftItemRepeater = new RepeatingView("leftItem");
        footer.add(footerLeftItemRepeater);

        footerRightItemRepeater = new RepeatingView("rightItem");
        footer.add(footerRightItemRepeater);

        //
        // Body Panels
        //

        panelRepeater = new RepeatingView("panel");
        wrapper.add(panelRepeater);

    }

    public BorderPanel addFooterLeftItem(Panel item) {
        footerLeftItemRepeater.add(item);
        return this;
    }

    public BorderPanel addFooterRightItem(Panel item) {
        footerRightItemRepeater.add(item);
        return this;
    }

    public BorderPanel addHeaderLeftItem(Panel item) {
        return addHeaderLeftItem(item, false);
    }

    public BorderPanel addHeaderLeftItem(Panel item, boolean clear) {
        if (clear) {
            item.add(new AttributeModifier("style", "clear:left"));
        }
        headerLeftItemRepeater.add(item);
        return this;
    }

    public BorderPanel addHeaderRightItem(Panel item) {
        headerRightItemRepeater.add(item);
        return this;
    }

    public BorderPanel addPanel(Component panel) {
        panelRepeater.add(panel);
        return this;
    }

    /**
     * Returns a CSS class to be appended to the 'class' attribute of the
     * border panel. Subclasses can override this if the desired CSS class
     * changed dynamically.
     */
    protected String getCssClass() {
        return cssClass;
    }

    public boolean isPadded() {
        return padded;
    }

    public String newPanelId() {
        return panelRepeater.newChildId();
    }

    /**
     * Removes all panels from the panel repeater.
     */
    public void removeAllPanels() {
        panelRepeater.removeAll();
    }

    /**
     * Sets the anchor name of for this border. If set to something non-null,
     * the panel will render an &lt;a&gt; tag with the name attribute set to
     * this value.
     */
    public BorderPanel setAnchorName(String anchorName) {
        this.anchorName = anchorName;
        return this;
    }

    /**
     * Sets the CSS class to be appended to the 'class' attribute of the
     * border panel. By default the CSS class is null.
     */
    public BorderPanel setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    /**
     * Sets whether the border is padded. Padded borders have a CSS class
     * appended that creates a small padding inside the border area. By default
     * borders are not padded.
     */
    public BorderPanel setPadded(boolean padded) {
        this.padded = padded;
        return this;
    }


    /**
     * Adds a BorderTitlePanel to the header of the border. If a title has already
     * been added, sets the model of that panel.
     *
     * @param titleModel
     *            Model returning the title of the border.
     */
    public BorderPanel setTitle(IModel<String> titleModel) {
        if (titlePanel == null) {
            addHeaderLeftItem(titlePanel = new BorderTitlePanel("title", titleModel));
        }
        titlePanel.setTitleModel(titleModel);
        return this;
    }

    /**
     * Adds a BorderTitlePanel to the header of the border.
     *
     * @param title
     *            Title of the border.
     */
    public BorderPanel setTitle(String title) {
        return setTitle(Model.of(title));
    }


}
