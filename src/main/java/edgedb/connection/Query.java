package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.exceptions.BaseException;
import edgedb.exceptions.EdgeDBException;
import edgedb.exceptions.clientexception.QueryException;

// should have some exception here.
public interface Query {
    public ResultSet query(String query) throws BaseException;
    public ResultSet queryOne(String query);
    void execute(String command) throws QueryException;
    public ResultSet queryJSON(String query);
}
