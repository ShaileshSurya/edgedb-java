package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.exceptions.OverReadException;
import edgedb.exceptions.clientexception.ClientException;
import edgedb.internal.protocol.ServerAuthenticationBehaviour;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.internal.protocol.constants.AuthenticationStatus.*;

@AllArgsConstructor
public class ServerAuthenticationReaderV2 implements ProtocolReader {
    IReaderHelper readerHelper;

    private ServerAuthenticationBehaviour readAuthenticationSASL(ServerAuthenticationBehaviour serverAuthentication) {
        int methodsLength = readerHelper.readUint32();
        serverAuthentication.setMethodsLength(methodsLength);
        String[] authMethods = new String[methodsLength];
        for (int i = 0; i < methodsLength; i++) {
            authMethods[i] = readerHelper.readString();
        }
        serverAuthentication.setMethods(authMethods);
        return serverAuthentication;
    }

    @Override
    public ServerAuthenticationBehaviour read(ByteBuffer readBuffer) {
        ServerAuthenticationBehaviour serverAuthentication = new ServerAuthenticationBehaviour();


        serverAuthentication.setMessageLength(readerHelper.readUint32());

        int authStatus = readerHelper.readUint32();
        serverAuthentication.setAuthStatus(authStatus);

        switch (authStatus) {
            case AUTHENTICATION_OK:
                return serverAuthentication;

            case AUTHENTICATION_REQUIRED_SASL:
                return readAuthenticationSASL(serverAuthentication);

            case AUTHENTICATION_SASL_CONTINUE:
            case AUTHENTICATION_SASL_FINAL:
                serverAuthentication.setSaslData(readerHelper.readBytes());
                return serverAuthentication;
            default:
                throw new ClientException(FAILED_TO_DECODE_SERVER_RESPONSE);
        }
    }
}
