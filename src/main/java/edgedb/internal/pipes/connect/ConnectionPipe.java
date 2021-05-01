package edgedb.internal.pipes.connect;

import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.ClientHandshake;
import edgedb.internal.protocol.Terminate;
import edgedb.internal.protocol.client.writerV2.ProtocolWritable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ConnectionPipe implements IConnectionPipe {

    ProtocolWritable protocolWritable;
    @Override
    public void sendClientHandshake(ClientHandshake clientHandshake) throws IOException {
        log.info("Trying to write ClientHandshake");
        protocolWritable.write(clientHandshake);
    }

    @Override
    public void sendTerminate(Terminate terminate) throws IOException {
        log.info("Trying to write sendTerminate message");
        protocolWritable.write(terminate);
    }
}
