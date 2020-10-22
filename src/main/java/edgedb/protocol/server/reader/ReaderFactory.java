package edgedb.protocol.server.reader;

import edgedb.exceptions.FailedToDecodeServerResponseException;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.io.IOException;

import static edgedb.protocol.constants.MessageType.SERVER_HANDSHAKE;
import static edgedb.protocol.constants.MessageType.AUTHENTICATION_OK;

@AllArgsConstructor
// Algebric data type
public class ReaderFactory {

    private DataInputStream dataInputStream;

    public Read getReader() throws IOException, FailedToDecodeServerResponseException {
        byte mType = dataInputStream.readByte();
        System.out.printf("mType %d >>>>> %c\n",(int)mType,(char)mType);
        switch (mType){
            case (int) SERVER_HANDSHAKE:
                return new ServerHandshakeReader();
            case (int) AUTHENTICATION_OK:
                return new AuthenticationOKReader();
            default:
                throw new FailedToDecodeServerResponseException();
        }
    }

}
