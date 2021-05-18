package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.exceptions.*;

import java.io.IOException;

public interface Query {
    public ResultSet query(String query);
    public ResultSet queryOne(String query);
    void execute(String command);
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException;
}
