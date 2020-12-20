package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.exceptions.*;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.PrepareComplete;

import java.io.IOException;

public class NonBlockingConnection extends BaseConnection {

    @Override
    protected IConnection getConnection() {
        return this;
    }

    @Override
    protected boolean isBlocking() {
        return false;
    }


    @Override
    public ResultSet query(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet queryOne(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet execute(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }
}
