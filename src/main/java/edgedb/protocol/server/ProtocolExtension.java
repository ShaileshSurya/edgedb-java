package edgedb.protocol.server;

import lombok.Data;

@Data
// No Protocol Extensions are currently Defined.
public class ProtocolExtension {
    String name;
    short headersLength;

    public int calculateMessageLength() {
        return 0;
    }
}
