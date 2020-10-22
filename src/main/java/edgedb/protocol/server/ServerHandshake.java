package edgedb.protocol.server;

import edgedb.protocol.client.ClientProtocolExtension;
import lombok.Data;

@Data
public class ServerHandshake {
    byte mType = 'v';
    int messageLength;
    short majorVersion;
    short minorVersion;
    short protocolExtensionLength;
    ClientProtocolExtension[] protocolExtensions;

}
