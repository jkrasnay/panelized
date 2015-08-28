package ca.krasnay.panelized;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;

public class AjaxLinkPanel extends Panel {

    private boolean defaultButton;

    private ToolStyle style = ToolStyle.LINK;

    private IModel<String> toolTipModel;

    public AjaxLinkPanel(String panelId, IModel<String> labelModel, final AjaxAction action) {
        this(panelId, null, labelModel, action);
    }

    public AjaxLinkPanel(String panelId, String iconName, final AjaxAction action) {
        this(panelId, iconName, null, action);
    }

    public AjaxLinkPanel(String panelId, String iconName, IModel<String> labelModel, final AjaxAction action) {

        super(panelId);

        AjaxLink<Void> link = new AjaxLink<Void>("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                action.invoke(target);
            }
        };
        add(link);

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

        link.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return isEnabled() ? null : "is-disabled";
            }
        }, " "));

        link.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return defaultButton ? "pnl-Button--default" : null;
            }
        }, " "));

        link.add(new AttributeModifier("title", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return toolTipModel != null ? toolTipModel.getObject() : null;
            }
        }));

        link.add(new IconLabelPanel("label", iconName, labelModel));

    }

    public AjaxLinkPanel setDefaultButton(boolean defaultButton) {
        this.defaultButton = defaultButton;
        return this;
    }

    public AjaxLinkPanel setStyle(ToolStyle style) {
        this.style = style;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public AjaxLinkPanel setToolTip(IModel<String> model) {
        if (model instanceof IComponentAssignedModel) {
            this.toolTipModel = ((IComponentAssignedModel)model).wrapOnAssignment(this);
        } else{
            this.toolTipModel = model;
        }
        return this;
    }

}
