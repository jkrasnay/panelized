package ca.krasnay.panelized;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Text field with a search icon. When the user presses Enter or clicks the search
 * icon the {@link #onSearch(AjaxRequestTarget, String)} method is called.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class SearchFieldPanel extends Panel {

    private AbstractDefaultAjaxBehavior ajaxBehavior;

    private TextField<String> textField;

    private int searchDelay;

    private String searchString;

    public SearchFieldPanel(String id) {

        super(id);

        add(textField = new TextField<String>("input", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return searchString;
            }
        }));

        textField.setOutputMarkupId(true);

        ajaxBehavior = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget target) {
                IRequestParameters req = RequestCycle.get().getRequest().getRequestParameters();
                searchString = req.getParameterValue("searchString").toString();
                onSearch(target, searchString);
            }
        };

        add(ajaxBehavior);

    }

    public String getCallbackUrl() {
        return ajaxBehavior.getCallbackUrl().toString();
    }

    @Override
    public void renderHead(IHeaderResponse response) {

        super.renderHead(response);

        StringBuilder options = new StringBuilder();
        options.append("{ ");
        if (searchDelay > 0) {
            options.append("searchDelay: " + searchDelay);
        }
        options.append("}");

        String script = String.format("Panelized.SearchField.init('#%s', '%s', %s);", textField.getMarkupId(), getCallbackUrl(), options);
        response.render(OnDomReadyHeaderItem.forScript(script));
    }

    /**
     * Called when the user initiates the search.
     *
     * @param target
     *            AjaxRequestTarget for the request. Used to refresh the UI
     *            elements that are sensitive to the search string.
     * @param searchString
     *            String that the user entered into the search field, or null if
     *            the user initiated a search with an empty field.
     */
    protected abstract void onSearch(AjaxRequestTarget target, String searchString);

    public SearchFieldPanel setSearchDelay(int searchDelay) {
        this.searchDelay = searchDelay;
        return this;
    }

}
