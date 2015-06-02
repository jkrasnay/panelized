package ca.krasnay.panelized;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Action invoked by the ExportBehavior to download a binary file from the browser.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public interface ExportAction extends Serializable {

    /**
     * Returns the content type of the file being exported, for example "application/pdf".
     */
    public String getContentType();

    /**
     * Returns the file name to be used for the export.
     */
    public String getFileName();

    /**
     * Writes the content to be exported to the given OutputStream.
     */
    public void writeContent(OutputStream outputStream) throws IOException;

}
