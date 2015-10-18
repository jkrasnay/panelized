package ca.krasnay.panelized;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel that contains a form with a file input field and a link. The file input
 * field is transparent and positioned over top of the link, such that when
 * the user thinks they're clicking the link they're really clicking the file
 * input field. When they select a file, the form is posted via AJAX and the
 * onSubmit method of this control is called with the resultant FileUpload object.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AjaxFileInputControl extends Panel {

    private FileUploadField fileUploadField;

    private List<FileUpload> fileUploads = new ArrayList<FileUpload>();

    private boolean required;

    public AjaxFileInputControl(String id, IModel<String> linkTextModel) {

        super(id);

        Form<Void> form = new Form<Void>("form");
        add(form);

        form.add(fileUploadField = new FileUploadField("fileInput", new PropertyModel<List<FileUpload>>(this, "fileUploads")) {
            @Override
            public boolean isRequired() {
                return AjaxFileInputControl.this.isRequired();
            }
        });

        fileUploadField.add(new AjaxFormSubmitBehavior("change") {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {

                FileUpload fileUpload = fileUploads != null && fileUploads.size() > 0 ? fileUploads.get(0) : null;

                if (fileUpload != null) {
                    onChange(target, fileUpload);
                }
            }

        });

        form.add(new AjaxLinkPanel("link", linkTextModel, new AjaxAction() {
            @Override
            public void invoke(AjaxRequestTarget target) {
                // Dummy link, should never happen since the file input field
                // is over top
            }
        }));

    }

    public boolean isRequired() {
        return required;
    }

    protected abstract void onChange(AjaxRequestTarget target, FileUpload fileUpload);

    public void setRequired(boolean required) {
        this.required = required;
    }

}
