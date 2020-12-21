package edgedb.internal.protocol;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.SERVER_AUTHENTICATION;

@Data
public class ServerAuthenticationBehaviour implements ServerProtocolBehaviour {
    byte mType = SERVER_AUTHENTICATION;

    int messageLength;
    int authStatus;

    int methodsLength;
    String[] methods;

    byte[] saslData;
}
