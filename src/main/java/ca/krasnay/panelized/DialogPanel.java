package ca.krasnay.panelized;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxChannel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Panel that pops over the page, with a standard border with titles and a
 * button bar. Typically, one populates this panel with form items (e.g.
 * TextFieldPanel), and adds a save button and a cancel button. To show the
 * panel in response to an Ajax request, call one of the "show" methods.
 *
 * <h2>Example</h2>
 *
 * <pre>
 * public class WidgetDialog extends DialogPanel {
 *
 *     public WidgetDialog(String id) {
 *         super(id);
 *     }
 *
 *     public show(AjaxRequestTarget target, String title, Widget widget,
 *                 AjaxAction saveAction, AjaxAction saveAndNewAction) {
 *
 *         // Sets a compound property model on the dialog, so that we can
 *         // use property names on field editors
 *         setModel(widget);
 *
 *         DialogFrame frame = buildFrame(title);
 *
 *         // Editor for the widget name
 *         frame.addPanel(new TextFieldPanel<String>("name"));
 *
 *         // Add other editors as required
 *
 *         frame.addSaveButton(saveAction);
 *         frame.addSaveAndNewButton(saveAndNewAction);
 *         frame.addCancelButton();
 *
 *         show(target);
 *
 *     }
 * }
 * </pre>
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class DialogPanel extends Panel implements IHeaderContributor {

    public enum Height {
        SHORT,
        MEDIUM,
        TALL;
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE;
    }


    public class DialogFrame implements Serializable {

        public void addBackButton(final AjaxAction ajaxAction) {
            addLeftButton(new AjaxButton(newButtonId(), Model.of("Back")) {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    ajaxAction.invoke(target);
                }

                @Override
                protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                    super.updateAjaxAttributes(attributes);
                    attributes.setChannel(new AjaxChannel("PopupButton", AjaxChannel.Type.ACTIVE));
                };

            }.setDefaultFormProcessing(false));
        }

        /**
         * Adds a button to the popup. You should *not* typically call this method;
         * instead, call one of the provided enhanced methods (addSaveButton,
         * addCancelButton, etc).
         *
         * @param button
         *            Button to be added. If this is a submit button, be sure to
         *            override onError and call updateFeedback on this panel. Also,
         *            you must explicitly call "hide" on this panel to hide it.
         */
        public void addButton(Button button) {

            if (buttonsPanel.isEmpty()) {
                button.add(new AttributeAppender("class", Model.of("btn-primary"), " "));
            }

            buttonsPanel.addButton(button);

        }

        public void addCloseButton(String label) {
            addCloseButton(Model.of(label));
        }

        public void addCloseButton(IModel<String> label) {
            addButton(new AjaxButton(newButtonId(), label) {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    hide(target);
                }

                @Override
                protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                    super.updateAjaxAttributes(attributes);
                    attributes.setChannel(new AjaxChannel("PopupButton", AjaxChannel.Type.ACTIVE));
                };

            }.setDefaultFormProcessing(false));
        }

        public void addCancelButton() {
            addCloseButton("Cancel");
        }

        public void addCloseButton() {
            addCloseButton("Close");
        }

        public void addPanel(Panel item) {
            formItems.addPanel(item);
        }

//        public void addExportButton(ExportAction exportAction) {
//
//            if (exportBehavior != null) {
//                throw new RuntimeException("You can add at most one export button to a popup.");
//            }
//
//            add(exportBehavior = new ExportBehavior(exportAction));
//
//            addSaveButton("Export", new AjaxAction() {
//                @Override
//                public void invoke(AjaxRequestTarget target) {
//                    exportBehavior.queueExport(target);
//                    hide(target);
//                }
//            });
//
//        }

        public void addLeftButton(Button button) {
            leftButtonsPanel.addButton(button);
        }

        public void addNextButton(AjaxAction ajaxAction) {
            addSaveButton("Next", ajaxAction);
        }

        public void addSaveButton(AjaxAction ajaxAction) {
            addSaveButton("Save", ajaxAction);
        }

        /**
         * Adds a button labelled Save and New that invokes the given action.
         *
         * @param ajaxAction
         *            Action to be invoked by the button. MAY BE NULL, in which
         *            case the button is simply not added. This solves a common
         *            use-case where the Save and New button is shown for a new
         *            action but not for an edit action.
         */
        public void addSaveAndNewButton(AjaxAction ajaxAction) {
            if (ajaxAction != null) {
                addSaveButton("Save and New", ajaxAction);
            }
        }

        public void addSaveButton(String buttonText, final AjaxAction ajaxAction) {
            addSaveButton(Model.of(buttonText), ajaxAction);
        }

        public void addSaveButton(IModel<String> buttonTextModel, final AjaxAction ajaxAction) {
            addButton(new AjaxButton(newButtonId(), buttonTextModel) {

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
            });
        }

        public String newButtonId() {
            return buttonsPanel.newButtonId();
        }

        public String newPanelId() {
            return formItems.newPanelId();
        }

    };


    /**
     * Repeater for direct children. There will only ever be at most one child,
     * a RawFormPanel. We do it this way in order to not have extra divs when
     * the popup is not showing.
     */
    private RepeatingView childRepeater;

//    private WebMarkupContainer container;

    private FormPanel formPanel;

    private BorderPanel border;

    private String cssClass;

    private Component feedbackPanel;

    private ContainerPanel formItems;

    private ButtonsPanel buttonsPanel;

    private ButtonsPanel leftButtonsPanel;

    private Size size = Size.LARGE;

    private Height height = Height.SHORT;

//    private ExportBehavior exportBehavior;

    private boolean isShowing;

//    private boolean showCloseIcon = true;


    /**
     * Constructor.
     *
     * @param id ID of the panel;
     */
    public DialogPanel(String id) {

        super(id);

        setOutputMarkupId(true);

        childRepeater = new RepeatingView("child");
        add(childRepeater);

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                String result = "popUpForm popUp-" + size + " popUp-height-" + height;
                String cssClass = getCssClass();
                if (cssClass != null) {
                    result += " " + cssClass;
                }
                return result;
            }
        }, " "));

    }


    public DialogFrame buildFrame(String title) {

        destroyFrame();

        formPanel = new FormPanel(childRepeater.newChildId()) {
            protected void onValidate() {
                DialogPanel.this.onValidate();
            };
        };
        childRepeater.add(formPanel);

        border = new BorderPanel(formPanel.newPanelId());
        formPanel.addPanel(border);

        border.addHeaderLeftItem(new BorderTitlePanel("title", Model.of(title)));

        feedbackPanel = createFeedbackPanel(border.newPanelId());
        if (feedbackPanel != null) {
            border.addPanel(feedbackPanel);
        }

        formItems = new ContainerPanel(border.newPanelId());
        border.addPanel(formItems);

        buttonsPanel = new ButtonsPanel("buttons");
        border.addFooterRightItem(buttonsPanel);

        leftButtonsPanel = new ButtonsPanel("leftButtons");
        border.addFooterLeftItem(leftButtonsPanel);

        return new DialogFrame();

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

    private void destroyFrame() {

//        if (exportBehavior != null) {
//            remove(exportBehavior);
//            exportBehavior = null;
//        }

        childRepeater.removeAll();

    }

    public String getCssClass() {
        return cssClass;
    }

    private Form<Void> getForm() {
        return formPanel.getForm();
    }

    public final void hide(AjaxRequestTarget target) {
        hide(target, false);
    }

    public final void hide(AjaxRequestTarget target, boolean keepOverlay) {

        isShowing = false;

        getForm().setEnabled(false);

        target.prependJavaScript(String.format("$('#%s').effRegModal('hide', %s)",
                getMarkupId(), keepOverlay));

        destroyFrame();

        target.add(this);

    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * Called as part of the form's validation process. By default, looks for
     * child components implementing the {@link FormValidatable} interface and
     * validating the returned form validators. Subclasses can also override
     * this method and do their own form validation directly.
     */
    private void onValidate() {

        validate();

        getForm().visitChildren(Component.class, new IVisitor<Component, Void>() {
            @Override
            public void component(Component component, IVisit<Void> visit) {
                if (component instanceof FormValidatable) {
                    IFormValidator childValidator = ((FormValidatable) component).getFormValidator();
                    if (childValidator != null) {
                        // TODO replicate functionality from Form.validateFormValidator
                        // (or just pass the child validator to that method)
                        // (since its protected final, we'd have to provide a wrapper method)
                        childValidator.validate(getForm());
                    }
                }
            }
        });

    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * Sets the desired height of the popup. Note that this is a minimum height:
     * the popup will grow if its contents become larger than this.
     */
    public DialogPanel setHeight(Height height) {
        this.height = height;
        return this;
    }

    /**
     * Sets the model object for the form. The model object is wrapped in a
     * CompoundPropertyModel. Typically this is used so you can add form fields
     * that are wired to model object fields by their IDs.
     */
    protected void setModel(Serializable modelObject) {
        setDefaultModel(new CompoundPropertyModel<Object>(modelObject));
    }

    public DialogPanel setMultiPart(boolean multiPart) {
        getForm().setMultiPart(multiPart);
        return this;
    }

    public DialogPanel setSize(Size size) {
        this.size = size;
        return this;
    }

    /**
     * Shows this pop-up. If there is already a pop-up showing, this one will be
     * layered over top. If you want to replace the current popup, use
     * {@link #replace(AjaxRequestTarget)}.
     *
     * The dialog must be built before calling this method, by calling
     * {@link #buildFrame(String)} and adding the desired components to the
     * frame. Typically this is done in an overloaded "save" method that passes
     * in any data required to build the dialog.
     */
    public final void show(AjaxRequestTarget target) {

        getForm().setEnabled(true);

        target.appendJavaScript(String.format("$('#%s').effRegModal('show')",
                JavaScriptUtils.escapeQuotes(getMarkupId())));

        target.appendJavaScript(String.format("$('#%s').effRegModal('fixNestedForm')",
                getMarkupId()));

        // TODO do dragging w/o JQuery UI
//        target.appendJavaScript(String.format("$('#%s').draggable({ handle: '.bdr-hd' })",
//                formPanel.getMarkupId()));

        target.add(this);

        isShowing = true;

    }

    private void updateFeedback(AjaxRequestTarget target) {
        if (feedbackPanel != null) {
            target.add(feedbackPanel);
        }
    }

    /**
     * Callback called during form validations so that subclasses can do their
     * own dialog-level validations. By default does nothing.
     */
    protected void validate() {

    }

}
