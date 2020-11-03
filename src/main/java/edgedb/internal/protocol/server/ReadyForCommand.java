package edgedb.internal.protocol.server;


import edgedb.internal.protocol.common.Header;
import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.READY_FOR_COMMAND;

@Data
public class ReadyForCommand extends BaseServerProtocol {
    byte mType = READY_FOR_COMMAND;

    int messageLength;
    short headersLength;
    Header[] headers;
    byte transactionState;
}
