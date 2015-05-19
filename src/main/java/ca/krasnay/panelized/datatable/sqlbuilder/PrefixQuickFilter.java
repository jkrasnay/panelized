package ca.krasnay.panelized.datatable.sqlbuilder;

import ca.krasnay.sqlbuilder.AbstractSqlCreator;
import ca.krasnay.sqlbuilder.Predicate;

public class PrefixQuickFilter implements SelectCreatorQuickFilter {

    private String dbExpr;

    public PrefixQuickFilter(String dbExpr) {
        this.dbExpr = dbExpr;
    }

    @Override
    public Predicate createPredicate(final String quickFilterString) {

        return new Predicate() {

            private String param;

            @Override
            public void init(AbstractSqlCreator creator) {
                param = creator.allocateParameter();
                creator.setParameter(param, quickFilterString.toLowerCase() + "%");
            }

            @Override
            public String toSql() {
                return String.format("lower(%s) like :%s", dbExpr, param);
            }

        };
    }

}
