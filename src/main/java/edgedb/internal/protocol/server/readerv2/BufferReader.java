package edgedb.internal.protocol.server.readerv2;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface BufferReader {
    public ByteBuffer read(ByteBuffer readInto);
}
