package edgedb.internal.protocol;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.SERVER_KEY_DATA;

@Data
public class ServerKeyDataBehaviour implements ServerProtocolBehaviour {
    byte mType = SERVER_KEY_DATA;
    int messageLength;
    byte[] data;
}
