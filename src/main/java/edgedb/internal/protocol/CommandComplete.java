package edgedb.internal.protocol;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.COMMAND_COMPLETE;

@Data
public class CommandComplete implements ServerProtocolBehaviour {
    byte mType = COMMAND_COMPLETE;
    int messageLength;
    short headersLength;
    Header[] headers;
    String status;
}
