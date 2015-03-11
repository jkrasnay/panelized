package ca.krasnay.panelized.datatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import ca.krasnay.panelized.BorderPanel;

/**
 * Common base class for DataTablePanel and GroupingDataTablePanel.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public abstract class AbstractDataTablePanel<T> extends BorderPanel {

    private List<IColumn<T, String>> columns;

    private IDataProvider<T> dataProvider;

    private String rowIdProperty = "id";

    private String rowNameProperty = null; // "name";

    private List<IHeaderContributor> headerContributors = new ArrayList<IHeaderContributor>();

    public AbstractDataTablePanel(String id) {
        super(id);
    }

    public List<IColumn<T, String>> getColumns() {
        return columns;
    }

    /**
     * Adds an IHeaderContributor to be rendered by this component to the page
     * header. This is used by colums that need to hook into the component
     * initialization process.
     */
    public void addHeaderContributor(IHeaderContributor headerContributor) {
        headerContributors.add(headerContributor);
    }

    @Override
    protected String getCssClass() {
        return "bdr-dataTable";
    }

    public abstract long getCurrentPage();

    public final IDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public final long getRowCount() {
        return dataProvider.size();
    }


    /**
     * Returns the identifier of the row for the purposes of identifying the
     * row to actions. By default, retrieves the value from the row
     * corresponding to the property name returned by {@link #getRowIdProperty()}.
     */
    protected String getRowId(T row) {
        Object value = PropertyResolver.getValue(getRowIdProperty(), row);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    /**
     * Returns the name of the property in the row object that returns the row
     * ID for the purpose of identifying the row to actions. Used by the default
     * implementation of {@link #getRowId(IModel)}. By default returns "id".
     */
    public String getRowIdProperty() {
        return rowIdProperty;
    }

    /**
     * Returns the name of the row. This is used to identify which rows cannot
     * be acted upon by a particular multi action. By default, returns the
     * value from the row corresponding to the property name returned
     * by {@link #getRowNameProperty()}.
     */
    protected String getRowName(T row) {
        Object value = PropertyResolver.getValue(getRowNameProperty(), row);
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    protected String getRowNameProperty() {
        return rowNameProperty;
    }

    public abstract long getRowsPerPage();

    /**
     * Returns the RowStyle for the given row. By default returns null,
     * meaning that no special styling is applied to the row.
     */
//    protected RowStyle getRowStyle(IModel<T> rowModel) {
//        return null;
//    }

    /**
     * Initialize the given row container to populate the row ID and name
     * attributes and to apply the requested row style.
     */
    protected void initRowContainer(WebMarkupContainer rowContainer, T row) {

        String rowId = getRowId(row);
        if (rowId != null) {
            rowContainer.add(new AttributeModifier("data-row-id", rowId));
        }

        String rowName = getRowName(row);
        if (rowName != null) {
            rowContainer.add(new AttributeModifier("data-row-name", rowName));
        }

//        RowStyle rowStyle = getRowStyle(row);
//        if (rowStyle != null) {
//            rowContainer.add(new AttributeAppender("class", Model.of(rowStyle.name()), " "));
//        }

    }

    public abstract boolean isPaginated();

    /**
     * Returns true if this data table is re-orderable.
     * @return
     */
    protected boolean isReorderable() {
        return false;
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        for (IHeaderContributor contributor : headerContributors) {
            contributor.renderHead(response);
        }
    }

    public AbstractDataTablePanel<T> setColumns(IColumn<T, String>... columns) {
        this.columns = new ArrayList<IColumn<T, String>>(Arrays.asList(columns));
        return this;
    }

    public AbstractDataTablePanel<T> setColumns(List<IColumn<T, String>> columns) {
        this.columns = columns;
        return this;
    }

    public AbstractDataTablePanel<T> setDataProvider(IDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
        return this;
    }

    public AbstractDataTablePanel<T> setRowIdProperty(String rowIdProperty) {
        this.rowIdProperty = rowIdProperty;
        return this;
    }

    public AbstractDataTablePanel<T> setRowNameProperty(String rowNameProperty) {
        this.rowNameProperty = rowNameProperty;
        return this;
    }

}
