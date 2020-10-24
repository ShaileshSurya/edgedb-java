package edgedb.protocol.client;

import edgedb.client.Connection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static edgedb.protocol.constants.MessageType.CLIENT_HANDSHAKE;

@Data
@Slf4j
public class ClientHandshake extends BaseClientProtocol {

    public final int MAJOR_VERSION = 0;
    public final int MINOR_VERSION = 8;

    byte mType = (int) CLIENT_HANDSHAKE;
    int messageLength;
    short majorVersion;
    short minorVersion;
    short connectionParamLength;
    ConnectionParams[] connectionParams;
    short protocolExtensionLength;
    ProtocolExtension[] protocolExtensions;

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
}
