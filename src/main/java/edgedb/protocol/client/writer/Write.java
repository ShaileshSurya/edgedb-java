package edgedb.protocol.client.writer;

import java.io.IOException;

public interface Write {
    public void encode() throws IOException;
}
