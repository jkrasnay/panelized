package ca.krasnay.panelized;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * FormPanel pre-populated with a BorderPanel, which includes a titlePanel and
 * button bars.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class BorderedFormPanel extends FormPanel {

    private BorderPanel border;

    private ButtonsPanel buttonsPanel;

    private Component feedbackPanel;

    private ButtonsPanel leftButtonsPanel;

    public BorderedFormPanel(String id, IModel<String> titleModel) {

        super(id);

        border = new BorderPanel(super.newPanelId());
        super.addPanel(border);
        border.setPadded(true);

        border.addHeaderLeftItem(new BorderTitlePanel("title", titleModel));

        feedbackPanel = createFeedbackPanel(border.newPanelId());
        if (feedbackPanel != null) {
            border.addPanel(feedbackPanel);
        }

//        formItems = new ContainerPanel(border.newPanelId());
//        border.addPanel(formItems);

        buttonsPanel = new ButtonsPanel("buttons");
        border.addFooterRightItem(buttonsPanel);

        leftButtonsPanel = new ButtonsPanel("leftButtons");
        border.addFooterLeftItem(leftButtonsPanel);

    }

    /**
     * Adds a button to the lower-right corner of the border.
     *
     * @param button
     *            Button to be added. If this is a submit button, be sure to
     *            override onError and call updateFeedback on this panel. Also,
     *            you must explicitly call "hide" on this panel to hide it.
     */
    public void addButton(Button button) {

        if (buttonsPanel.isEmpty()) {
            button.add(new AttributeAppender("class", Model.of("pnl-Button--default"), " "));
        }

        buttonsPanel.addButton(button);

    }

    public void addButton(IModel<String> buttonTextModel, final AjaxAction ajaxAction) {
        addButton(buttonTextModel, ajaxAction, true);
    }

    public void addButton(IModel<String> buttonTextModel, final AjaxAction ajaxAction, boolean defaultFormProcessing) {

        AjaxButton button = new AjaxButton(buttonsPanel.newButtonId(), buttonTextModel) {

            @Override
            public boolean isEnabled() {
                if (ajaxAction instanceof ConditionalAction) {
                    return ((ConditionalAction) ajaxAction).isActionEnabled();
                } else {
                    return super.isEnabled();
                }
            }

            @Override
            public boolean isVisible() {
                if (ajaxAction instanceof ConditionalAction) {
                    return ((ConditionalAction) ajaxAction).isActionVisible();
                } else {
                    return super.isEnabled();
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                updateFeedback(target);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                ajaxAction.invoke(target);
            }

            //
            // Decorate the AJAX call to save any TinyMCE editors back to their text areas
            //

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {

                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new AjaxCallListener() {
                    @Override
                    public CharSequence getBeforeHandler(Component component) {

                        // Do this with JS rather than iterating over the rich text controls on the server side,
                        // so it works when we dynamically create the rich text control after popup creation.

                        // Note that we use the border, not "this", since we actually move the border in
                        // the DOM when the popup is visible. Using "this" won't find the rich text areas.

                        // Note that we init rich text areas in EffReg.js, maybe we should do this there too

                        return String.format("$('#%s').find('.richTextArea').each(function () { var t = tinyMCE.get($(this).attr('id')); if (t) { t.save(); } });", border.getMarkupId());
                    }
                });

                attributes.setChannel(new AjaxChannel("PopupButton", AjaxChannel.Type.ACTIVE));

            }
        };

        button.setDefaultFormProcessing(defaultFormProcessing);

        addButton(button);

    }

    public void addCancelButton(IModel<String> buttonTextModel, final AjaxAction ajaxAction) {
        addButton(buttonTextModel, ajaxAction, true);
    }


    /**
     * Adds a button to the lower-left corner of the border.
     *
     * @param button
     *            Button to be added. If this is a submit button, be sure to
     *            override onError and call updateFeedback on this panel. Also,
     *            you must explicitly call "hide" on this panel to hide it.
     */
    public void addLeftButton(Button button) {
        leftButtonsPanel.addButton(button);
    }

    @Override
    public PanelContainer addPanel(Component panel) {
        return border.addPanel(panel);
    }

    /**
     * Creates the feedback panel for the dialog. By default creates a regular
     * Wicket FeedbackPanel. Subclasses may override this to create their own
     * feedback panel. May return null, in which case the dialog will not
     * display feedback messages.
     *
     * @param panelId
     *            ID with which to create the panel.
     */
    protected Component createFeedbackPanel(String panelId) {
        return new FeedbackPanel(panelId).setOutputMarkupId(true);
    }

    @Override
    public String newPanelId() {
        return border.newPanelId();
    }

    @Override
    public void removeAllPanels() {
        border.removeAllPanels();
    }

    /**
     * Updates this form's feedback panel on the Ajax response.
     */
    public void updateFeedback(AjaxRequestTarget target) {
        if (feedbackPanel != null) {
            target.add(feedbackPanel);
        }
    }

}
