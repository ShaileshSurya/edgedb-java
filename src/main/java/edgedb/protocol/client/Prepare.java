package edgedb.protocol.client;

import edgedb.protocol.common.Header;
import lombok.Data;

import static edgedb.protocol.constants.MessageType.PREPARE;

@Data
public class Prepare extends BaseClientProtocol {
    byte mType = PREPARE;
    int messageLength;
    short headersLength;
    Header[] headers;
    byte ioFormat;
    byte expectedCardinality;
    byte[] statementName;
    String command;

    public Prepare(char IOFormat, char expectedCardinality, String command) {
        this.headersLength = (short) 0;
        this.ioFormat = (byte) IOFormat;
        this.expectedCardinality = (byte) expectedCardinality;
        this.statementName = "".getBytes();
        this.command = command;
        this.messageLength = calculateMessageLength();
    }

    @Override
    public int calculateMessageLength() {
        int length = 0;
        MessageLengthCalculator messageLengthCalculator = new MessageLengthCalculator();
        length += messageLengthCalculator.calculate(messageLength);

        length += messageLengthCalculator.calculate(headersLength);
        for (int i = 0; i < headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }

        length += messageLengthCalculator.calculate(ioFormat);
        length += messageLengthCalculator.calculate(expectedCardinality);
        length += messageLengthCalculator.calculate(statementName);
        length += messageLengthCalculator.calculate(command);

        return length;
    }
}
