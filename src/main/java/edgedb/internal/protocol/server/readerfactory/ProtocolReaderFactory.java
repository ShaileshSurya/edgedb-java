package edgedb.internal.protocol.server.readerfactory;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.server.readerv2.ProtocolReader;

import java.nio.ByteBuffer;

public interface ProtocolReaderFactory {
    public ProtocolReader getProtocolReader(char mType, ByteBuffer buffer) throws EdgeDBInternalErrException;
}
