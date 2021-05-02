package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;

import java.util.concurrent.CompletableFuture;

public class EdgeDBAsyncClient implements AsyncClient{
    @Override
    public <T> CompletableFuture<IConnection> getConnection(ConnectionParams connectionParams, SingleResultCallBack<T> singleResultCallBack) {
        return null;
    }

    @Override
    public <T> CompletableFuture<Void> terminateConnection() {
        return null;
    }
}
