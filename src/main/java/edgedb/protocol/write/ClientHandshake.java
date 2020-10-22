package edgedb.protocol.write;
import edgedb.protocol.BaseProtocol;

public class ClientHandshake extends BaseProtocol {
    short connectionParamLength;
    ConnectionParams[] connectionParams;
    short protocolExtensionLength;
    ProtocolExtension [] protocolExtensions;
}
