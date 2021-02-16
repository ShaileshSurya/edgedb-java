package edgedb.internal.protocol.typedescriptor;

import lombok.Data;

@Data
public class TupleTypeDescriptor {
    private char type =4;
    private byte[] id;
    private short elementCount;
    private short[] elementTypes;
}
