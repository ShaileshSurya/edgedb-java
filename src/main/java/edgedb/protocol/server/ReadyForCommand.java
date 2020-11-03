package edgedb.protocol.server;


import edgedb.protocol.common.Header;
import lombok.Data;

import static edgedb.protocol.constants.MessageType.READY_FOR_COMMAND;

@Data
public class ReadyForCommand extends BaseServerProtocol {
    byte mType = READY_FOR_COMMAND;

    int messageLength;
    short headersLength;
    Header[] headers;
    byte transactionState;
}
