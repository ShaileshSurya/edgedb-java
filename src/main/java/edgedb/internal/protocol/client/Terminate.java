package edgedb.internal.protocol.client;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.TERMINATE;

@Data
public class Terminate extends BaseClientProtocol {

    byte mType = (int) TERMINATE;
    int messageLength;

    @Override
    public int calculateMessageLength() {
        return new MessageLengthCalculator().calculate(messageLength);
    }
}
