package edgedb.internal.protocol.client.writerV2;

import edgedb.internal.buffer.SingletonBuffer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.Future;

@AllArgsConstructor
@Slf4j
public class AsyncSocketChannelProtocolWritableImpl implements ProtocolWritable{
    AsynchronousSocketChannel channel;

    @Override
    public <T extends BufferWritable> void write(T bufWriter) throws IOException {
        try {
            log.info("Trying to write to channel ..{}", bufWriter.toString());
            ByteBuffer writeBuffer = SingletonBuffer.getInstance().getBuffer();
            bufWriter.write(writeBuffer);
            writeBuffer.flip();
            Future<Integer> bytesWritten = channel.write(writeBuffer);
            log.info("Write to channel successful bytes written {}", bytesWritten.get());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
