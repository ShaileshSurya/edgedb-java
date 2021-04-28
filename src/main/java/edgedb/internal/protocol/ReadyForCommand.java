package edgedb.internal.protocol;


import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.READY_FOR_COMMAND;

@Data
public class ReadyForCommand implements ServerProtocolBehaviour {
    byte mType = READY_FOR_COMMAND;

    int messageLength;
    short headersLength;
    Header[] headers;
    byte transactionState;
}
