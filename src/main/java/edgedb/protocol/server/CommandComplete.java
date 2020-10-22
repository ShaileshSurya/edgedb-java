package edgedb.protocol.server;

import edgedb.protocol.client.Header;
import lombok.Data;

import static edgedb.protocol.constants.MessageType.COMMAND_COMPLETE;

@Data
public class CommandComplete extends BaseServerProtocol {
    byte mType = (int) COMMAND_COMPLETE;
    int messageLength;
    short headersLength;
    Header[] headers;
    String status;
}
