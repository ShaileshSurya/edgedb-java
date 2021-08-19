package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.clientexception.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;

@AllArgsConstructor
@Slf4j
public class BufferReaderImpl implements BufferReader {
    SocketChannel channel;
    @Override
    public ByteBuffer read(ByteBuffer readInto){
        log.info("Trying to readInto buffer.");

        try {
            readInto.clear();
            int byteReceived;
            // ByteBuffer sampleReadInto = ByteBuffer.allocate(BUFFER_SIZE);
            while ((byteReceived = channel.read(readInto)) == -1) ;
            readInto.flip();
            log.info("Total byte Received {}", byteReceived);
            return readInto;
        }catch (IOException e) {
            throw new ClientException("InternalServerError");
        }
    }
}
