package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;

import java.io.IOException;

public interface Client {
    //IConnection getConnection(String dsn) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException;
    void terminateConnection() throws IOException;
    IConnection getConnection(ConnectionParams connectionParams) throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException;
    int getMajorVersion();
    int getMinorVersion();
}
