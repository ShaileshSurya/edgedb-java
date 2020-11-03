package edgedb.internal.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.server.ProtocolExtension;
import edgedb.internal.protocol.server.ServerHandshake;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

@Data
@Slf4j
public class ServerHandshakeReader extends BaseReader {

    public ServerHandshakeReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public ServerHandshakeReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public ServerHandshake read() throws IOException {
        log.debug("Trying to read Server Handshake");
        ServerHandshake serverHandshake = new ServerHandshake();
        try {
            int messageLength = readerHelper.readUint32();
            serverHandshake.setMessageLength(messageLength);
            readerHelper.setMessageLength(messageLength);

            serverHandshake.setMajorVersion(readerHelper.readUint16());
            serverHandshake.setMinorVersion(readerHelper.readUint16());

            short protocolExtensionLength = readerHelper.readUint16();
            log.debug("Read protocolExtensionLength {}", protocolExtensionLength);
            serverHandshake.setProtocolExtensionLength(protocolExtensionLength);
            ProtocolExtension[] protocolExtensions = new ProtocolExtension[protocolExtensionLength];
            ProtocolExtensionReader peReader = new ProtocolExtensionReader(dataInputStream, readerHelper);
            for (int i = 0; i < protocolExtensionLength; i++) {
                protocolExtensions[i] = peReader.read();
            }
            log.debug("Completed reading Server Handshake");
            return serverHandshake;
        } catch (OverReadException e) {
            e.printStackTrace();
            return serverHandshake;
        } catch (IOException e) {
            throw e;
        }
    }
}
