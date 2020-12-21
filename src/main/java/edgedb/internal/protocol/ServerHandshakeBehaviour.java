package edgedb.internal.protocol;

import lombok.Data;

@Data
public class ServerHandshakeBehaviour implements ServerProtocolBehaviour {
    byte mType = 'v';
    int messageLength;
    short majorVersion;
    short minorVersion;
    short protocolExtensionLength;
    ProtocolBehaviourExtension[] protocolExtensions;

}
