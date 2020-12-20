package edgedb.internal.protocol.server.reader;

import edgedb.internal.protocol.server.BaseServerProtocol;

import java.nio.ByteBuffer;

public interface ProtocolReader {
    public <T extends BaseServerProtocol> T Read(ByteBuffer readBuffer);
}
