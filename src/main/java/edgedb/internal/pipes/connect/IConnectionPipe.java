package edgedb.internal.pipes.connect;

import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;

import java.io.IOException;

public interface IConnectionPipe {
    public void connect(String user, String database) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException;
    public void terminate();
}
