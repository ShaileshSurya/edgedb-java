package edgedb.internal.protocol.server.readerv2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class AsyncClientReaderImpl implements BufferReader {

    AsynchronousSocketChannel channel;
    @Override
    public ByteBuffer read(ByteBuffer readInto) throws IOException {
        log.info("Trying to readInto buffer.");
        try {
            readInto.clear();

            channel.read(readInto, 1, TimeUnit.MINUTES, "Read operation ALFA",
                    new CompletionHandler() {
                        @Override
                        public void completed(Object result, Object attachment) {
                            System.out.println(attachment + " completed and " + result + " bytes are read. ");
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println(attachment + " failed with exception:");
                            exc.printStackTrace();
                        }
                    });

//            while (true) {
//                if (byteReceived.get() == -1) {
//                    break;
//                }
//                //log.info("Total byte Received {}", byteReceived);
//            }
            readInto.flip();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return readInto;
    }
}
