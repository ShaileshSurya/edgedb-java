package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.AuthenticationOK;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Data
@Slf4j
public class AuthenticationOKReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;
    public AuthenticationOKReaderV2() {
       this.readerHelper = readerHelper;
    }

    @Override
    public AuthenticationOK read(ByteBuffer readBuffer) {

        log.debug("Trying to read Authentication OK.");
        AuthenticationOK authenticationOK = new AuthenticationOK();

            authenticationOK.setMessageLength(readerHelper.readUint32());
            authenticationOK.setAuthStatus(readerHelper.readUint32());
            return authenticationOK;

    }
}
