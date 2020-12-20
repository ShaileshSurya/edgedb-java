package edgedb.internal.protocol.client.writerV2;

import edgedb.internal.protocol.client.SyncMessage;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import lombok.Data;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.nio.ByteBuffer;

@Data
public class SyncBufferWriterDecorator <T extends BufferWriter> extends BufferWriterDecorator {

    public SyncBufferWriterDecorator(BufferWriter source) {
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
