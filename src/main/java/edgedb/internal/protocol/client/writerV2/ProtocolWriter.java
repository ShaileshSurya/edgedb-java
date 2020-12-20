package edgedb.internal.protocol.client.writerV2;

import java.io.IOException;

public interface ProtocolWriter {
    public <T extends BufferWriter> void write(T protocol) throws IOException;
}
