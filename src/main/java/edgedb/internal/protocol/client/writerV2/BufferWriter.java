package edgedb.internal.protocol.client.writerV2;

import edgedb.internal.protocol.client.writerhelper.IWriteHelper;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface BufferWriter {
    public ByteBuffer write(ByteBuffer destination) throws IOException;
    public ByteBuffer write(IWriteHelper helper,ByteBuffer destination) throws IOException;
}

