package edgedb.protocol.server.reader;

import edgedb.protocol.server.BaseServerProtocol;
import edgedb.protocol.server.ErrorResponse;

public class ErrorResponseReader implements Read{
    ErrorResponse errorResponse;

    @Override
    public BaseServerProtocol read() {
        return null;
    }
}
