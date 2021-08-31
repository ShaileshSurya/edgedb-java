package edgedb.connection;

import edgedb.client.ResultSet;

public interface Query {
    public ResultSet query(String query);
    public ResultSet queryOne(String query);
    void execute(String command);
    public ResultSet queryJSON(String query);
}
