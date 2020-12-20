package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.server.BaseServerProtocol;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ProtocolReader {
    public <T extends BaseServerProtocol> T read(ByteBuffer readBuffer) throws IOException;
}
