package edgedb.protocol.server;

import lombok.Data;

@Data
public class ServerHandshake extends BaseServerProtocol{
    byte mType = 'v';
    int messageLength;
    short majorVersion;
    short minorVersion;
    short protocolExtensionLength;
    ProtocolExtension[] protocolExtensions;

}
