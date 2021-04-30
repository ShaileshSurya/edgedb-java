package edgedb;

import edgedb.client.Client;
import edgedb.client.EdgeDBClientV2;
import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;

public class TestDBConnectionSingleton {

    private volatile static IConnection connection;
    private ByteBuffer buffer;

    private TestDBConnectionSingleton() {
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public static IConnection getInstance(ConnectionParams connectionParams, IConnection connectionType) throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException {
        if (connection == null) {
            Client client = new EdgeDBClientV2(connectionType);
            return client.getConnection(connectionParams);
        }
        return connection;
    }
}
