package ca.krasnay.panelized.datatable.sqlbuilder;

import static ca.krasnay.sqlbuilder.Predicates.or;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

import ca.krasnay.panelized.datatable.filter.DataTableFilter;
import ca.krasnay.panelized.datatable.filter.FilterableDataProvider;
import ca.krasnay.panelized.datatable.filter.QuickFilterDataProvider;
import ca.krasnay.sqlbuilder.Dialect;
import ca.krasnay.sqlbuilder.PostgresqlDialect;
import ca.krasnay.sqlbuilder.Predicate;
import ca.krasnay.sqlbuilder.SelectCreator;

/**
 * DataProvider built around the SelectCreator class. Provides sorting and
 * filtering capabilities by modifying the query as needed.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class SelectCreatorDataProvider extends SortableDataProvider<RowMap, String> implements FilterableDataProvider, QuickFilterDataProvider, Iterable<RowMap> {

    private static final Logger log = LoggerFactory.getLogger(SelectCreatorDataProvider.class);

    private static final Dialect DIALECT = new PostgresqlDialect();

    private DataSource dataSource;

    private List<SelectCreatorQuickFilter> quickFilters = new ArrayList<>();

    private String quickFilterString;

    private SortParam<String> secondarySort;

    private SelectCreator selectCreator;

    private String uniqueColumn;

    private List<DataTableFilter> filters = new ArrayList<DataTableFilter>();

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

    public void addFilter(DataTableFilter filter) {
        if (filter instanceof SelectCreatorFilter) {
            filters.add(filter);
        } else {
            throw new RuntimeException("Filters added to SelectCreatorDataProvider must implement SelectCreatorFilter");
        }
    }

    public void addQuickFilter(SelectCreatorQuickFilter quickFilter) {
        quickFilters.add(quickFilter);
    }

    private void applyFilters(SelectCreator creator) {
        for (DataTableFilter filter : filters) {
            ((SelectCreatorFilter) filter).apply(creator);
        }
    }

    private void applyQuickFilters(SelectCreator creator) {

        if (hasQuickFilters() && quickFilterString != null) {

            List<Predicate> predicates = new ArrayList<>();

            for (SelectCreatorQuickFilter quickFilter : quickFilters) {
                Predicate predicate = quickFilter.createPredicate(quickFilterString);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }

            creator.where(or(predicates));

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

    @Override
    public List<DataTableFilter> getFilters() {
        return Collections.unmodifiableList(filters);
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

    public void removeFilter(DataTableFilter filter) {
        filters.remove(filter);
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

    @Override
    public Iterator<RowMap> iterator() {
        return iterator(0, Integer.MAX_VALUE);
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
