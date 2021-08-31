package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.ServerProtocolBehaviour;

import java.nio.ByteBuffer;
// TODO: Protocol's can be made to implement this and do the reading themselves without having to maintain separate reader
public interface ProtocolReader {
    public <T extends ServerProtocolBehaviour> T read(ByteBuffer readBuffer);
}
