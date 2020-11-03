package edgedb.protocol.client;

import edgedb.protocol.common.Header;
import lombok.Data;

@Data
public class Execute extends BaseClientProtocol {
    byte mType = (int) 'E';
    int messageLength;
    short headersLength;
    Header[] headers;
    byte[] statementName;
    byte[] arguments;

    public Execute() {
        this.headersLength = (short) 0;
        this.statementName = "".getBytes();
        this.arguments = "".getBytes();
        // Remove this hardcoded value.
        this.messageLength = 18;
    }

    @Override
    public int calculateMessageLength() {
        int length = 0;

        MessageLengthCalculator calculator = new MessageLengthCalculator();
        length += calculator.calculate(messageLength);
        length += calculator.calculate(headersLength);

        for (int i = 0; i < (int) headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }

        length += calculator.calculate(statementName);
        length += calculator.calculate(arguments);
        return length;
    }
}
