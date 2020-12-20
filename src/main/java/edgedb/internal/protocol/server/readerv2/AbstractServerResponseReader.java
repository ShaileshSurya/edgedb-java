package edgedb.internal.protocol.server.readerv2;

import lombok.AllArgsConstructor;

import java.nio.channels.SocketChannel;

@AllArgsConstructor
public abstract class AbstractServerResponseReader {
    SocketChannel channel;
}
