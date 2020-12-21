package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.ProtocolBehaviourExtension;
import edgedb.internal.protocol.ServerHandshakeBehaviour;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@Slf4j
public class ServerHandshakeReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public ServerHandshakeBehaviour read(ByteBuffer buffer) throws IOException {
        log.debug("Trying to read Server Handshake");
        ServerHandshakeBehaviour serverHandshake = new ServerHandshakeBehaviour();
        try {
            int messageLength = readerHelper.readUint32();
            serverHandshake.setMessageLength(messageLength);
            readerHelper.setMessageLength(messageLength);

            serverHandshake.setMajorVersion(readerHelper.readUint16());
            serverHandshake.setMinorVersion(readerHelper.readUint16());

            short protocolExtensionLength = readerHelper.readUint16();
            log.debug("Read protocolExtensionLength {}", protocolExtensionLength);
            serverHandshake.setProtocolExtensionLength(protocolExtensionLength);
            ProtocolBehaviourExtension[] protocolExtensions = new ProtocolBehaviourExtension[protocolExtensionLength];
            ProtocolReader peReader = new ProtocolExtensionReaderV2(readerHelper);
            for (int i = 0; i < protocolExtensionLength; i++) {
                protocolExtensions[i] = peReader.read(buffer);
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
