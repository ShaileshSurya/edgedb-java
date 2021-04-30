package edgedb.internal.protocol;

import edgedb.internal.protocol.client.writerV2.BufferWritable;
import edgedb.internal.protocol.client.writerhelper.*;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.client.ClientConstants.MAJOR_VERSION;
import static edgedb.client.ClientConstants.MINOR_VERSION;
import static edgedb.internal.protocol.constants.MessageType.CLIENT_HANDSHAKE;

@Data
@Slf4j
public class ClientHandshake implements BufferWritable, ClientProtocolBehaviour {

    byte mType = (int) CLIENT_HANDSHAKE;
    int messageLength;
    short majorVersion;
    short minorVersion;
    short connectionParamLength;
    ConnectionParams[] connectionParams;
    short protocolExtensionLength;
    ProtocolBehaviourExtension[] protocolExtensions;

    public ClientHandshake(String user, String database){
        // TODO: CHECK THIS LATER. ARE THESE USER PROVIDED
        majorVersion = (short) MAJOR_VERSION;
        minorVersion = (short) MINOR_VERSION;
        connectionParamLength = (short) 2;
        connectionParams = new ConnectionParams[]{
                new ConnectionParams("user", user),
                new ConnectionParams("database", "edgedb")
        };
        protocolExtensionLength = (short) 0;
        messageLength = calculateMessageLength();
    }


    public int calculateMessageLength() {
        log.debug("Starting to calculate length of message");
        int length = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();
        length += calculator.calculate(messageLength);
        length += calculator.calculate(majorVersion);
        length += calculator.calculate(minorVersion);
        length += calculator.calculate(connectionParamLength);

        for (int i = 0; i < connectionParamLength; i++) {
            length += connectionParams[i].calculateMessageLength();
        }

        length += calculator.calculate(protocolExtensionLength);

        for (int i = 0; i < protocolExtensionLength; i++) {
            length += protocolExtensions[i].calculateMessageLength();
        }

        log.debug("Calculated Length of message {}", length);
        return length;
    }


    public ByteBuffer write(ByteBuffer writeBuf) throws IOException {
        log.info("Client Handshake Buffer Writer");
        IWriteHelper helper = new BufferWriterHelper(writeBuf);
        helper.writeUint8(mType);
        helper.writeUint32(messageLength);
        helper.writeUint16(majorVersion);
        helper.writeUint16(minorVersion);
        helper.writeUint16(connectionParamLength);

        for(ConnectionParams connectionParam: connectionParams ){
            connectionParam.write(helper,writeBuf);
        }
        helper.writeUint16(protocolExtensionLength);
        return writeBuf;
    }

    @Override
    public ByteBuffer write(IWriteHelper helper, ByteBuffer destination) throws IOException {
        throw new UnsupportedOperationException();
    }
}
