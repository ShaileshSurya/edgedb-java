package edgedb;

import edgedb.client.Client;
import edgedb.client.EdgeDBClientV2;
import edgedb.connection.*;
import edgedb.connectionparams.ConnectionParams;

import java.io.IOException;

public class TestDBConnectionSingleton {

    private volatile static TestDBConnectionSingleton uniqueConnection;
    private static IConnection connection;

    private TestDBConnectionSingleton(IConnection connection) {
        this.connection = connection;
    }

    public static TestDBConnectionSingleton getInstance(ConnectionParams connectionParams) throws IOException {
        if (uniqueConnection == null) {
            Client client = new EdgeDBClientV2(new BlockingConnection());
            uniqueConnection = new TestDBConnectionSingleton(client.getConnection(connectionParams));
        }
        return uniqueConnection;
    }

    public static IConnection getConnection() {
        return connection;
    }
}
