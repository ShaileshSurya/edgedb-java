package edgedb.internal.protocol.server;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.SERVER_KEY_DATA;

@Data
public class ServerKeyData extends BaseServerProtocol {
    byte mType = SERVER_KEY_DATA;
    int messageLength;
    byte[] data;
}
