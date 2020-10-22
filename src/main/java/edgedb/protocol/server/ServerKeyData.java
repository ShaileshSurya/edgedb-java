package edgedb.protocol.server;

import lombok.Data;

@Data
public class ServerKeyData {
    byte mType;
    int messageLength;
    byte data;
}
