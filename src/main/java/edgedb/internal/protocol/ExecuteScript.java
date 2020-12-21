package edgedb.internal.protocol;

import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ExecuteScript implements ClientProtocolBehaviour {
    byte mType = (int) 'Q';
    int messageLength;
    short headersLength;
    Header[] headers;
    String script;

    @Override
    public int calculateMessageLength() {
        log.debug("Starting to calculate length of message");
        int length = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();
        length += calculator.calculate(messageLength);
        length += calculator.calculate(headersLength);

        for (int i = 0; i < headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }
        length += calculator.calculate(script);
        return length;
    }
}
