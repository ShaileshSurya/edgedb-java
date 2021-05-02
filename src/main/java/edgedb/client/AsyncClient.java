package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;

import java.util.concurrent.CompletableFuture;

public interface AsyncClient {
    <T> CompletableFuture<IConnection> getConnection(ConnectionParams connectionParams, SingleResultCallBack<T> singleResultCallBack);
    <T> CompletableFuture<Void> terminateConnection();
}
