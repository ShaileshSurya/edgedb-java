package edgedb.connection;

import java.nio.channels.SocketChannel;

public interface IChannel {
    public SocketChannel getChannel();
}
