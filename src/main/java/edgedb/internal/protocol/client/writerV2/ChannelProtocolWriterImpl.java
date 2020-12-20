package edgedb.internal.protocol.client.writerV2;

import edgedb.client.SingletonBuffer;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@AllArgsConstructor
@Slf4j
public class ChannelProtocolWriterImpl implements ProtocolWriter {
    SocketChannel channel;

    @Override
    public <T extends BufferWriter> void write(T bufWriter) throws IOException {
        log.info("Trying to write to channel ..{}",bufWriter.toString());
        ByteBuffer writeBuffer = SingletonBuffer.getInstance().getBuffer();
        bufWriter.write(writeBuffer);
        writeBuffer.flip();
        channel.write(writeBuffer);
        log.info("Write to channel successful");
    }
}
