package edgedb.protocol.server;

import lombok.Data;

import static edgedb.protocol.constants.MessageType.SERVER_AUTHENTICATION;

@Data
public class ServerAuthentication {
    byte mType= SERVER_AUTHENTICATION;

    int messageLength;
    int authStatus;

    int methodsLength;
    String[] methods;

    byte[] saslData;
}
