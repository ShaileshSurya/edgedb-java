package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import lombok.AllArgsConstructor;

import java.io.IOException;

import static edgedb.client.ClientConstants.MAJOR_VERSION;
import static edgedb.client.ClientConstants.MINOR_VERSION;

@AllArgsConstructor
public class EdgeDBClientV2 implements Client {

    IConnection connection;
    ConnectionParams connectionParams;

    public EdgeDBClientV2(IConnection connection) {
        this.connection = connection;
    }


//    @Override
//    public IConnection getConnection(String dsn) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {
//        connectionParams = new ConnectionParams();
//    }

    @Override
    public void terminateConnection() throws IOException {
        connection.terminate();
    }

    @Override
    public IConnection getConnection(ConnectionParams connectionParams) throws IOException {

        connection = connection.createClientSocket(connectionParams);
        connection.initiateHandshake(connectionParams.getUser(), connectionParams.getDatabase());
        connection.handleHandshake();
        return connection;
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

}
