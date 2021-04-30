package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.exceptions.*;

import java.io.IOException;

public interface Query {
    public ResultSet query(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException;
    public ResultSet queryOne(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException;
    public void execute(String query);
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException;
}
