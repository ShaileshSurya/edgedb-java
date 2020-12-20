package edgedb.connection;

import edgedb.connectionparams.ConnectionParams;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface IConnection extends IChannel, Query{
    public IConnection connect(ConnectionParams connectionParams) throws IOException;
    public void terminate() throws IOException;
}
