package ca.krasnay.panelized;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

/**
 * Like Wicket's TabbedPanel, but renders all tab panels at once and selects
 * between them on the client-side via Javascript. This simplifies form
 * component validation, which is only triggered when you submit the form.
 *
 * <p>Note that validation of components on tabbed panels is not yet handled well.
 * A good solution might be to filter messages to hide those corresponding to
 * hidden tabs. If there are no messages on the current tab but are some on
 * other tabs, switch to the next tab with an error and show those messages.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class ClientTabbedPanel extends Panel {

    private RepeatingView panelRepeater;

    private RepeatingView tabRepeater;

    private int selectedTabIndex = 0;

    public ClientTabbedPanel(String id, final List<ITab> tabs) {

        super(id);

        setOutputMarkupId(true);

        tabRepeater = new RepeatingView("tab");
        add(tabRepeater);

        panelRepeater = new RepeatingView("panel");
        add(panelRepeater);

        int index = 0;
        for (ITab tab : tabs) {

            final int tabIndex = index;

            //
            // The tab itself
            //

            WebMarkupContainer tabContainer = new WebMarkupContainer(tabRepeater.newChildId());
            tabRepeater.add(tabContainer);
            tabContainer.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return tabIndex == selectedTabIndex ? "selected" : null;
                }
            }, " "));

            tabContainer.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return tabIndex == tabs.size() - 1 ? "last" : null;
                }
            }, " "));

            AjaxLink<Void> link = new AjaxLink<Void>("link") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setSelectedTab(target, tabIndex);
                }
            };
            tabContainer.add(link);

            link.add(new Label("name", tab.getTitle()));


            //
            // The tab panel
            //

            WebMarkupContainer panelContainer = new WebMarkupContainer(panelRepeater.newChildId());
            panelRepeater.add(panelContainer);

            panelContainer.setOutputMarkupId(true);

            WebMarkupContainer panel = tab.getPanel("panel");
            panelContainer.add(panel);

            panel.add(new AttributeAppender("style", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return tabIndex == selectedTabIndex ? null : "display:none";
                }
            }, ";"));

            panel.add(new AttributeAppender("class", Model.of("tab-panel"), " "));

            index++;
        }
    }

    public void setSelectedTab(AjaxRequestTarget target, int selectedTabIndex) {

        this.selectedTabIndex = selectedTabIndex;

        StringBuilder js = new StringBuilder();

        js.append(String.format("$('#%s').find('.tab-row ul li').removeClass('selected').slice(%d,%d).addClass('selected');",
                getMarkupId(), selectedTabIndex, selectedTabIndex + 1));

        js.append(String.format("$('#%s').find('.tab-panel').css('display', 'none').slice(%d,%d).css('display', '');",
                getMarkupId(), selectedTabIndex, selectedTabIndex + 1));

        target.appendJavaScript(js);

    }
}
