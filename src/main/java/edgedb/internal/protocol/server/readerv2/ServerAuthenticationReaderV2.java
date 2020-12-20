package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.ServerAuthentication;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.internal.protocol.constants.AuthenticationStatus.*;

@AllArgsConstructor
public class ServerAuthenticationReaderV2 implements ProtocolReader {
    IReaderHelper readerHelper;

    private ServerAuthentication readAuthenticationSASL(ServerAuthentication serverAuthentication) throws IOException, OverReadException {
        int methodsLength = readerHelper.readUint32();
        serverAuthentication.setMethodsLength(methodsLength);
        String[] authMethods = new String[methodsLength];
        for (int i = 0; i < methodsLength; i++) {
            authMethods[i] = readerHelper.readString();
        }
        return serverAuthentication;
    }

    @Override
    public ServerAuthentication read(ByteBuffer readBuffer) throws IOException {
        ServerAuthentication serverAuthentication = new ServerAuthentication();

        try {
            serverAuthentication.setMessageLength(readerHelper.readUint32());

            int authStatus = readerHelper.readUint32();
            serverAuthentication.setAuthStatus(authStatus);

            switch (authStatus) {
                case AUTHENTICATION_OK:
                    return serverAuthentication;

                case AUTHENTICATION_SASL:
                    return readAuthenticationSASL(serverAuthentication);

                case AUTHENTICATION_SASL_CONTINUE:
                case AUTHENTICATION_SASL_FINAL:
                    serverAuthentication.setSaslData(readerHelper.readBytes());
                    return serverAuthentication;

                default:
                    throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
            }

        } catch (OverReadException e) {
            e.printStackTrace();
            return serverAuthentication;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (EdgeDBInternalErrException e) {
            e.printStackTrace();
        }
        return null;
    }
}
