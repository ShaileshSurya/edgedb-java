package edgedb.internal.protocol.client;

import edgedb.client.BaseConnectionV2;
import edgedb.client.Connection;
import edgedb.internal.protocol.client.writerV2.BufferWriter;
import edgedb.internal.protocol.client.writerhelper.*;
import edgedb.internal.protocol.server.ProtocolExtension;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.CommonConstants.*;
import static edgedb.internal.protocol.constants.MessageType.CLIENT_HANDSHAKE;

@Data
@Slf4j
public class ClientHandshake extends BaseClientProtocol implements BufferWriter {

    byte mType = (int) CLIENT_HANDSHAKE;
    int messageLength;
    short majorVersion;
    short minorVersion;
    short connectionParamLength;
    ConnectionParams[] connectionParams;
    short protocolExtensionLength;
    ProtocolExtension[] protocolExtensions;

    public ClientHandshake(String user, String database){
        // TODO: CHECK THIS LATER. ARE THESE USER PROVIDED
        majorVersion = (short) MAJOR_VERSION;
        minorVersion = (short) MINOR_VERSION;
        connectionParamLength = (short) 2;
        connectionParams = new ConnectionParams[]{
                new ConnectionParams("user", user),
                new ConnectionParams("database", database)
        };
        protocolExtensionLength = (short) 0;
        messageLength = calculateMessageLength();
    }


    public ClientHandshake(BaseConnectionV2 connection) {

        // TODO: CHECK THIS LATER. ARE THESE USER PROVIDED
        majorVersion = (short) MAJOR_VERSION;
        minorVersion = (short) MINOR_VERSION;
        connectionParamLength = (short) 2;
        connectionParams = new ConnectionParams[]{
                new ConnectionParams("user", connection.getUser()),
                new ConnectionParams("database", connection.getDatabase())
        };
        protocolExtensionLength = (short) 0;
        messageLength = calculateMessageLength();
    }

    public ClientHandshake(Connection connection) {

        // TODO: CHECK THIS LATER. ARE THESE USER PROVIDED
        majorVersion = (short) MAJOR_VERSION;
        minorVersion = (short) MINOR_VERSION;
        connectionParamLength = (short) 2;
        connectionParams = new ConnectionParams[]{
                new ConnectionParams("user", connection.getUser()),
                new ConnectionParams("database", connection.getDatabase())
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
        return null;
    }
}
