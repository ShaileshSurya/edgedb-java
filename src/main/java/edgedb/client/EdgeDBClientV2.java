package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;

import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class EdgeDBClientV2 implements Client{

    IConnection connection;
    ConnectionParams connectionParams;

    public EdgeDBClientV2(IConnection connection){
        this.connection = connection;
    }


    @Override
    public IConnection getConnection(String dsn) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {
        connectionParams = new ConnectionParams(dsn);
        connection = connection.createClientSocket(connectionParams);
        connection.initiateHandshake(connectionParams.getUser(),connectionParams.getDatabase());
        connection.handleHandshake();
        return connection;
    }

    @Override
    public void terminateConnection() throws IOException {
        connection.terminate();
    }

}