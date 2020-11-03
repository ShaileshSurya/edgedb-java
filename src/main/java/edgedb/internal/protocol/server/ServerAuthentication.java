package edgedb.internal.protocol.server;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.SERVER_AUTHENTICATION;

@Data
public class ServerAuthentication extends BaseServerProtocol {
    byte mType = SERVER_AUTHENTICATION;

    int messageLength;
    int authStatus;

    int methodsLength;
    String[] methods;

    byte[] saslData;
}
