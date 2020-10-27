package edgedb.protocol.server;

import edgedb.protocol.client.ProtocolExtension;
import lombok.Data;

@Data
public class ServerHandshake {
    byte mType = 'v';
    int messageLength;
    short majorVersion;
    short minorVersion;
    short protocolExtensionLength;
    ProtocolExtension[] protocolExtensions;

}
