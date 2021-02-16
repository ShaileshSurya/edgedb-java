package edgedb.internal.protocol.typedescriptor;

public class ArrayTypeDescriptor {
    private char type = 6;
    private byte[] id;
    private short type_pos;
    private short dimensionCount;
    private int[] dimensions;
}
