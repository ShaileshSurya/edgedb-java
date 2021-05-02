package edgedb.connection;

import edgedb.client.SingleResultCallBack;
import edgedb.connectionparams.ConnectionParams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

public class AsyncConnection<T> {

    AsynchronousSocketChannel channel;

    public CompletableFuture<AsyncConnection> createClientSocket(ConnectionParams connectionParams, SingleResultCallBack<T> singleResultCallBack) {
        return null;
    }

    public void terminate() {

    }

    public void initiateHandshake(String user, String database) {

    }

    public void handleHandshake() {

    }

    public CompletableFuture<AsyncConnection> getConnection(ConnectionParams connectionParams, SingleResultCallBack<T> singleResultCallBack) {
        return (CompletableFuture<AsyncConnection>) CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return createAsyncClientSocket(connectionParams);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
        ).handle(
                (res, ex) -> res
        );
    }

    private void createAsyncClientSocket(ConnectionParams connectionParams) throws IOException, ExecutionException, InterruptedException {
        channel = AsynchronousSocketChannel.open();
        Future<Void> result = channel.connect(new InetSocketAddress(connectionParams.getHost(), connectionParams.getPort()));
        result.get();

    }
}
