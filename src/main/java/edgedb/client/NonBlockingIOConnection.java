package edgedb.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Slf4j
@Data
public class NonBlockingIOConnection extends BaseConnectionV2 {

    SocketChannel clientChannel;

    protected NonBlockingIOConnection(String dsn) {
        super(dsn);
    }


    public void connect() throws IOException {

        Selector selector = Selector.open();

        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);

        if (!clientChannel.connect(new InetSocketAddress(this.getHost(), this.getPort()))) {
            while (!clientChannel.finishConnect());
        }
    }


    public void write(ByteBuffer writeBuffer) {

    }

    @Override
    public void terminate() {

    }
}
