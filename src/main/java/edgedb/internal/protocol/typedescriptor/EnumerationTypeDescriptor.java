package edgedb.internal.protocol.typedescriptor;

import lombok.Data;

@Data
public class EnumerationTypeDescriptor {
    private char type = 7;
    private byte[] id;
    private short memberCount;
    private String[] members;
}
