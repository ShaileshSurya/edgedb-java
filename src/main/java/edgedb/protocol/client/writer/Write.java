package edgedb.protocol.client.writer;

import java.io.IOException;

public interface Write {
    public void write() throws IOException;

    public void writeAndFlush() throws IOException;
}
