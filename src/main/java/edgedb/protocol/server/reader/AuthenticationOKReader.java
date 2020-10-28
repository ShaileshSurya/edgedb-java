package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.AuthenticationOK;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

@Data
@Slf4j
public class AuthenticationOKReader extends BaseReader {

    public AuthenticationOKReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public AuthenticationOKReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public AuthenticationOK read() throws IOException {
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
