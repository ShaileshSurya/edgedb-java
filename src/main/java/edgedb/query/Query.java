package edgedb.query;

import edgedb.resultset.ResultSet;

public interface Query {
    public String query();
    public String queryOne();
    public ResultSet execute();
}
