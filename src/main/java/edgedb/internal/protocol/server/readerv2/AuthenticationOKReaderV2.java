package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.AuthenticationOK;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@Slf4j
public class AuthenticationOKReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;
    public AuthenticationOKReaderV2() {
       this.readerHelper = readerHelper;
    }

    @Override
    public AuthenticationOK read(ByteBuffer readBuffer) throws IOException {

        log.debug("Trying to read Authentication OK.");
        AuthenticationOK authenticationOK = new AuthenticationOK();
        try {
            authenticationOK.setMessageLength(readerHelper.readUint32());
            authenticationOK.setAuthStatus(readerHelper.readUint32());
            return authenticationOK;
        } catch (OverReadException e) {
            e.printStackTrace();
            return authenticationOK;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
