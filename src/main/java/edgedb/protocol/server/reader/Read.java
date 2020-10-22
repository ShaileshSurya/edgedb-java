package edgedb.protocol.server.reader;

import edgedb.protocol.server.BaseServerProtocol;

public interface Read {
    public BaseServerProtocol read();
}
