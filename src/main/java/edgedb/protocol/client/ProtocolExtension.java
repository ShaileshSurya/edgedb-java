package edgedb.protocol.client;

import lombok.Data;

@Data
// No Protocol Extensions are currently Defined.
public class ProtocolExtension extends BaseClientProtocol {
    String name;
    short headersLength;

    @Override
    public int calculateMessageLength() {
        return 0;
    }
}
