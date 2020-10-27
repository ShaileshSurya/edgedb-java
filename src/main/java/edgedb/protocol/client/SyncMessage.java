package edgedb.protocol.client;

import lombok.Data;

import static edgedb.protocol.constants.MessageType.SYNC_MESSAGE;

@Data
public class SyncMessage extends BaseClientProtocol{
    byte mType = (int)SYNC_MESSAGE;
    int messageLength;

    @Override
    public int calculateMessageLength() {
        return new MessageLengthCalculator().calculate(messageLength);
    }
}
