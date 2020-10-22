package edgedb.protocol.server.reader;

import edgedb.protocol.server.AuthenticationOK;
import edgedb.protocol.server.BaseServerProtocol;

public class AuthenticationOKReader implements Read {

    @Override
    public BaseServerProtocol read() {
        BaseServerProtocol authenticationOk = new AuthenticationOK();

        return authenticationOk;
    }
}
