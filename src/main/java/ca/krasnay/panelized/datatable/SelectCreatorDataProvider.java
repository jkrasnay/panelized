package ca.krasnay.panelized.datatable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ca.krasnay.sqlbuilder.Dialect;
import ca.krasnay.sqlbuilder.PostgresqlDialect;
import ca.krasnay.sqlbuilder.SelectCreator;
import ca.krasnay.sqlbuilder.UnionSelectCreator;

/**
 * DataProvider built around the SelectCreator class. Provides sorting and
 * filtering capabilities by modifying the query as needed.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class SelectCreatorDataProvider extends SortableDataProvider<RowMap, String> {

    private static final Logger log = LoggerFactory.getLogger(SelectCreatorDataProvider.class);

    private static final Dialect DIALECT = new PostgresqlDialect();

    private DataSource dataSource;

    private List<String> quickFilters = new ArrayList<String>();

    private String quickFilterString;

    private SortParam<String> secondarySort;

    private SelectCreator selectCreator;

    private String uniqueColumn;

    private List<SelectCreatorFilter> filters = new ArrayList<SelectCreatorFilter>();

    /**
     * Constructor.
     *
     * The caller must specify the name of a result set column that uniquely
     * identifies each row in the result set.This column is always added as the
     * last term of the ORDER BY clause. If we do not do this, paging along
     * result set rows will not proceed in a predictable order. See the
     * PostgreSQL documentation here:
     * http://www.postgresql.org/docs/8.3/interactive/queries-limit.html
     *
     * @param selectCreator
     *            SelectCreator to use.
     * @param uniqueColumn
     *            Name of the unique column described above
     * @param dataSource
     *            DataSource from which to perform the query.
     */
    public SelectCreatorDataProvider(SelectCreator selectCreator, String uniqueColumn, DataSource dataSource) {
        this.selectCreator = selectCreator;
        this.uniqueColumn = uniqueColumn;
        this.dataSource = dataSource;
    }

    public SelectCreatorDataProvider addFilter(SelectCreatorFilter filter) {
        filters.add(filter);
        return this;
    }

    /**
     * Adds a "quick filter" to this data provider. The given expression is a
     * boolean SQL expression that implements the filter. The filter
     * may contain one of the following substitution strings:
     *
     * <ul>
     * <li><tt>:filter</tt> - The text of the filter string entered by the user.
     * <li><tt>:prefixFilter</tt> - The text of the filter string entered by the user,
     * with '%' appended. This can be used to find strings that start with
     * the entered user string.
     * <li><tt>:wordPrefixFilter</tt> - Like <tt>:prefix</tt>, but with '% ' pre-pended,
     * such that it can be used to match words within a value.
     * </ul>
     *
     * Note that all substitution strings are converted to lowercase.
     *
     * Some examples are as follows:
     *
     * <li><tt>lower(name) like :prefixFilter</tt> - Matches any row where the name
     * column begins with the filter text.
     * <li><tt>lower(name) like :prefixFilter or lower(name) like :wordPrefixFilter</tt> -
     * Matches any row where the name column contains a word that starts with
     * the filter text. Note that addWordFilter method is a convenience method
     * that implements this kind of filter.
     * <li><tt>id = :filter</tt> - Matches rows where the id exactly matches
     * the entered value.
     * </ul>
     *
     * @param expr
     * @return
     */
    public SelectCreatorDataProvider addQuickFilter(String expr) {
        quickFilters.add(expr);
        return this;
    }

    /**
     * Convenience method that sets up a word prefix filter given a column name.
     * For example, given the column <tt>fullName</tt>, this method adds the
     * following quick filter:
     *
     * <pre>lower(fullName) like :prefixFilter or lower(fullName) like :wordPrefixFilter</pre>
     */
    public SelectCreatorDataProvider addWordFilter(String columnName) {
        quickFilters.add(
                String.format("lower(%s) like :prefixFilter or lower(%s) like :wordPrefixFilter",
                        columnName, columnName));
        return this;
    }

    private void applyFilters(SelectCreator creator) {
        for (SelectCreatorFilter filter : filters) {
            filter.apply(creator);
        }
    }

    private void applyQuickFilters(SelectCreator creator) {

        if (hasQuickFilter()) {

            StringBuilder sb = new StringBuilder();
            sb.append("(");

            boolean first = true;
            for (String quickFilter : quickFilters) {
                if (!first) {
                    sb.append(" or ");
                }
                sb.append(quickFilter);
                first = false;
            }
            sb.append(")");

            String whereClause = sb.toString();

            creator.where(whereClause);

            for (UnionSelectCreator usc : creator.getUnions()) {
                usc.where(whereClause);
            }

            String filterString = quickFilterString.toLowerCase();

            creator.setParameter("filter", filterString);
            creator.setParameter("prefixFilter", filterString.toLowerCase() + "%");
            creator.setParameter("wordPrefixFilter", "% " + filterString.toLowerCase() + "%");
        }
    }

    @Override
    public void detach() {
    }

    private int getCount(boolean quickFiltered) {

        SelectCreator creator = selectCreator.clone();

        applyFilters(creator);

        if (quickFiltered) {
            applyQuickFilters(creator);
        }

        JdbcTemplate t = new JdbcTemplate(getDataSource());

        List<Integer> results = t.query(creator.count(DIALECT), new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int index) throws SQLException {
                return rs.getInt(1);
            }
        });

        return results.get(0);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getQuickFilterString() {
        return quickFilterString;
    }

    public int getUnfilteredCount() {
        return getCount(false);
    }

    public SortParam<String> getSecondarySort() {
            return secondarySort;
    }

    private boolean hasQuickFilter() {
        return quickFilters != null && quickFilters.size() > 0 && quickFilterString != null;
    }

    public boolean isFiltered() {
        return hasQuickFilter();
    }

    @SuppressWarnings("unchecked")
    public Iterator<RowMap> iterator(long first, long count) {

        SelectCreator creator = selectCreator.clone();

        applyFilters(creator);
        applyQuickFilters(creator);

        if (getSort() != null) {
            creator.orderBy(getSort().getProperty(), getSort().isAscending());
        }

        creator.orderBy(uniqueColumn);

        log.debug("Executing query: " + creator);

        JdbcTemplate t = new JdbcTemplate(getDataSource());

        @SuppressWarnings("rawtypes")
        RowMapper rowMapper = new ColumnMapRowMapper() {
            @Override
            protected Map<String, Object> createColumnMap(int columnCount) {
                return new RowMap();
            }
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = super.mapRow(rs, rowNum);
                processRow((RowMap) row);
                return row;
            }
        };
        return t.query(creator.page(DIALECT, (int) count, (int) first), (RowMapper<RowMap>) rowMapper).iterator();

    }

    public IModel<RowMap> model(RowMap object) {
        return new Model<RowMap>(object);
    }

    public SelectCreatorDataProvider removeFilter(SelectCreatorFilter filter) {
        filters.remove(filter);
        return this;
    }

    public void setQuickFilterString(String s) {
        this.quickFilterString = s;
    }

    /**
     * Sets the secondary sorting of the data provider. This is null by default,
     * meaning the query results are sorted by the primary sort order (if any),
     * then by the unique ID. If a secondary sort is specified, the results are
     * sorted by the primary sort, then the secondary, then by the unique ID.
     */
    public void setSecondarySort(String property, boolean ascending) {
        this.secondarySort = new SortParam<String>(property, ascending);
    }

    public void setSecondarySort(SortParam<String> secondarySort) {
        this.secondarySort = secondarySort;
    }

    public long size() {
        return getCount(true);
    }

    public boolean hasQuickFilters() {
        return quickFilters.size() > 0;
    }

    /**
     * Hook method that subclasses can use to modify the row after it has been
     * loaded from the result set. By default does nothing.
     *
     * @param row
     *            RowMap representing the row.
     */
    protected void processRow(RowMap row) {

    }
}
