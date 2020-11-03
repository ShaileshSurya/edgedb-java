package edgedb.internal.protocol.client;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.SYNC_MESSAGE;

@Data
public class SyncMessage extends BaseClientProtocol {
    byte mType = (int) SYNC_MESSAGE;
    int messageLength;

    public SyncMessage() {
        this.setMessageLength(calculateMessageLength());
    }

    @Override
    public int calculateMessageLength() {
        return new MessageLengthCalculator().calculate(messageLength);
    }
}
