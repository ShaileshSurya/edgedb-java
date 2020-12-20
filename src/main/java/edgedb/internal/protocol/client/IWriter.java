package edgedb.internal.protocol.client;

import java.nio.ByteBuffer;

public interface IWriter {
    public byte[] write(ByteBuffer writeBuf);
}
