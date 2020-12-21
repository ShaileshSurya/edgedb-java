package edgedb.internal.protocol.client.writerV2;

import java.io.IOException;

public interface ProtocolWritable {
    public <T extends BufferWritable> void write(T protocol) throws IOException;
}
