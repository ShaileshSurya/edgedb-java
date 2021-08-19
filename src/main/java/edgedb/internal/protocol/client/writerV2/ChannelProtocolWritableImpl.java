package edgedb.internal.protocol.client.writerV2;

import edgedb.exceptions.clientexception.ClientException;
import edgedb.internal.buffer.SingletonBuffer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@AllArgsConstructor
@Slf4j
public class ChannelProtocolWritableImpl implements ProtocolWritable {
    SocketChannel channel;

    @Override
    public <T extends BufferWritable> void write(T bufWriter) {
        try {
            log.info("Trying to write to channel ..{}",bufWriter.toString());
            ByteBuffer writeBuffer = SingletonBuffer.getInstance().getBuffer();
            bufWriter.write(writeBuffer);
            writeBuffer.flip();
            channel.write(writeBuffer);
            log.info("Write to channel successful");
        } catch (IOException e) {
            throw new ClientException("InternalServerError");
        }
    }
}
