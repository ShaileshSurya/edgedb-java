package edgedb.internal.protocol.client.writerV2;

import edgedb.internal.protocol.SyncMessage;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
public class SyncBufferWritableDecorator<T extends BufferWritable> extends BufferWritableDecorator {

    public SyncBufferWritableDecorator(BufferWritable source) {
        super(source);
    }

    @Override
    public ByteBuffer write(ByteBuffer destination) throws IOException {
        IWriteHelper helper = new BufferWriterHelper(destination);
        ByteBuffer buffer = this.source.write(helper,destination);
        buffer = new SyncMessage().write(helper,destination);
        return buffer;
    }

    @Override
    public ByteBuffer write(IWriteHelper helper, ByteBuffer destination) throws IOException {
        return null;
    }
}
