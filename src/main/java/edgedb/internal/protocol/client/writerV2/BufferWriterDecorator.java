package edgedb.internal.protocol.client.writerV2;

import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
public abstract class BufferWriterDecorator <T extends BufferWriter> implements BufferWriter{
    public T source;

    public BufferWriterDecorator(T source){
        this.source = source;
    }

    @Override
    public abstract ByteBuffer write(ByteBuffer destination) throws IOException;
}
