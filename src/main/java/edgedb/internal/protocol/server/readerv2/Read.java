package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.server.BaseServerProtocol;

import java.io.IOException;

public interface Read<T extends BaseServerProtocol> {
    public T read() throws IOException, EdgeDBInternalErrException;
}
