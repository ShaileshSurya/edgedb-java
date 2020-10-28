package edgedb.protocol.server;

import lombok.Data;

import static edgedb.protocol.constants.MessageType.SERVER_KEY_DATA;

@Data
public class ServerKeyData extends BaseServerProtocol{
    byte mType = SERVER_KEY_DATA;
    int messageLength;
    byte[] data;
}
