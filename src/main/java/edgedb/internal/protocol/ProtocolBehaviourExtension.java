package edgedb.internal.protocol;

import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;

@Data
// No Protocol Extensions are currently Defined.
public class ProtocolBehaviourExtension implements ClientProtocolBehaviour,ServerProtocolBehaviour {
    String name;
    short headersLength;
    Header[] headers;

    public int calculateMessageLength() {
        int length = 0;
        MessageLengthCalculator messageLengthCalculator = new MessageLengthCalculator();
        length += messageLengthCalculator.calculate(name);

        length += messageLengthCalculator.calculate(headersLength);
        for (int i = 0; i < headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }

        return length;
    }
}
