package edgedb.internal.protocol.server;

import edgedb.internal.protocol.common.Header;
import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.COMMAND_COMPLETE;

@Data
public class CommandComplete extends BaseServerProtocol {
    byte mType = COMMAND_COMPLETE;
    int messageLength;
    short headersLength;
    Header[] headers;
    String status;
}
