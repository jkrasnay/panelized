package ca.krasnay.panelized.datatable.sqlbuilder;

import ca.krasnay.sqlbuilder.AbstractSqlCreator;
import ca.krasnay.sqlbuilder.Predicate;

public class WordPrefixQuickFilter implements SelectCreatorQuickFilter {

    private String dbExpr;

    public WordPrefixQuickFilter(String dbExpr) {
        this.dbExpr = dbExpr;
    }

    @Override
    public Predicate createPredicate(final String quickFilterString) {

        return new Predicate() {

            private String p1;
            private String p2;

            @Override
            public void init(AbstractSqlCreator creator) {
                p1 = creator.allocateParameter();
                p2 = creator.allocateParameter();
                creator.setParameter(p1, quickFilterString.toLowerCase() + "%");
                creator.setParameter(p2, "% " + quickFilterString.toLowerCase() + "%");
            }

            @Override
            public String toSql() {
                return String.format("(lower(%s) like :%s or lower(%s) like :%s)", dbExpr, p1, dbExpr, p2);
            }

        };
    }

}
