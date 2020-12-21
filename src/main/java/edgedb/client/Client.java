package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;

import java.io.IOException;

public interface Client {
    public IConnection getConnection(String dsn) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException;
    public void terminateConnection() throws IOException;
}
