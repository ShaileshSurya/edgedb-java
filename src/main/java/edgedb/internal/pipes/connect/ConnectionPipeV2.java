package edgedb.internal.pipes.connect;

import edgedb.client.NonBlockingIOConnection;
import edgedb.exceptions.EdgeDBFailedToConnectServer;
import edgedb.internal.protocol.client.ClientHandshake;
import edgedb.internal.protocol.client.writer.ClientHandshakeWriterV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_CONNECT_EDGEDB_SERVER;
import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;

@Slf4j
@AllArgsConstructor
public class ConnectionPipeV2 {
    private NonBlockingIOConnection connection;

    public NonBlockingIOConnection open() throws Throwable {
        try {
            connection.connect();

            ClientHandshakeWriterV2 handshakeWriterV2 = new ClientHandshakeWriterV2(
                    connection.getClientChannel(), new ClientHandshake(connection));
            handshakeWriterV2.write();

            tryConnect();

            return connection;
        } catch (IOException e) {
            throw new EdgeDBFailedToConnectServer(FAILED_TO_CONNECT_EDGEDB_SERVER).initCause(e);
        }
    }

    public void tryConnect() throws IOException, InterruptedException {
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        int byteReceived;
        Thread.sleep(1000L);
        while ((byteReceived = connection.getClientChannel().read(readBuffer)) == -1){
            throw new SocketException("Connection Closed Prematurely");
        }
        log.debug("ReadBuffer :- {}", new String(readBuffer.array(),0,byteReceived));
        log.debug("98799999999999999999>>>>>>",String.valueOf(byteReceived));
    }
}
