package edgedb.internal.protocol.typedescriptor;

public class TypeAnnotationDescriptor {
    // 0x80..0xfe from 128-254
    private char type;
    private byte[] id;
    private String annotation;
}
