package edgedb.internal.protocol;

import lombok.Data;

import static edgedb.internal.protocol.constants.AuthenticationStatus.*;
import static edgedb.internal.protocol.constants.MessageType.SERVER_AUTHENTICATION;

@Data
public class ServerAuthenticationBehaviour implements ServerProtocolBehaviour {
    byte mType = SERVER_AUTHENTICATION;

    int messageLength;
    int authStatus;

    int methodsLength;
    String[] methods;

    byte[] saslData;

    public boolean isAuthenticationOkMessage(){
        return authStatus == AUTHENTICATION_OK;
    }

    public boolean isAuthenticationRequiredSASLMessage(){
        return authStatus == AUTHENTICATION_REQUIRED_SASL;
    }

    public boolean isAuthenticationSASLContinueMessage(){
        return authStatus == AUTHENTICATION_SASL_CONTINUE;
    }

    public boolean isAuthenticationSASLFinal(){
        return authStatus == AUTHENTICATION_SASL_FINAL;
    }
}




