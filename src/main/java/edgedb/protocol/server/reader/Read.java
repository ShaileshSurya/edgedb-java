package edgedb.protocol.server.reader;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.protocol.server.AuthenticationOK;
import edgedb.protocol.server.BaseServerProtocol;

import java.io.IOException;

public interface Read<T extends BaseServerProtocol> {
    public T read() throws IOException, EdgeDBInternalErrException;
}
