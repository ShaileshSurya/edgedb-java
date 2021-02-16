package edgedb.internal.protocol.typedescriptor;

import lombok.Data;

@Data
// SetType is encoded as a single-dimensional array
public class SetTypeDescriptor {

    // Indicates that this is a Set value descriptor.
    private char type = 0;

    // Descriptor ID.
    private byte[] id;

    // Set element type descriptor index.
    private short typePosition;
}
