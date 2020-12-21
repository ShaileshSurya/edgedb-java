package edgedb.internal.pipes.connect;

import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.ClientHandshake;
import edgedb.internal.protocol.Terminate;

import java.io.IOException;

public interface IConnectionPipe {
    public void sendClientHandshake(ClientHandshake clientHandshake) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException;
    public void sendTerminate(Terminate terminate) throws IOException;
}
