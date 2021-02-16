package edgedb.internal.protocol.typedescriptor;

import lombok.Data;

@Data
public class ScalarTypeNameAnnotation {
    private char type = 0xff;
    private byte[] id;
    private String typeName;
}
