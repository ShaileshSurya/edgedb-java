package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;

import java.nio.ByteBuffer;

public interface ProtocolReaderFactory {
    public ProtocolReader getProtocolReader(char mType, ByteBuffer buffer) throws EdgeDBInternalErrException;
}
