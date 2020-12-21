package edgedb.internal.protocol;

import lombok.Data;

@Data
// No Protocol Extensions are currently Defined.
public class ProtocolBehaviourExtension implements ServerProtocolBehaviour {
    String name;
    short headersLength;

    public int calculateMessageLength() {
        return 0;
    }
}
