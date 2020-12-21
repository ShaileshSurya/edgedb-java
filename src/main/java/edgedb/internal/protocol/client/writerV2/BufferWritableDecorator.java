package edgedb.internal.protocol.client.writerV2;

import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
public abstract class BufferWritableDecorator<T extends BufferWritable> implements BufferWritable {
    public T source;

    public BufferWritableDecorator(T source){
        this.source = source;
    }

    @Override
    public abstract ByteBuffer write(ByteBuffer destination) throws IOException;
}
