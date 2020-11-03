package edgedb.internal.protocol.utility;

public class TypeSizeHelper {

    private static int BYTE_SIZE = Byte.SIZE / Byte.SIZE;
    private static int INT_SIZE = Integer.SIZE / Byte.SIZE;
    private static int SHORT_SIZE = Short.SIZE / Byte.SIZE;

    public static final int getByteSize() {
        return BYTE_SIZE;
    }

    public static final int getIntSize() {
        return INT_SIZE;
    }

    public static final int getShortSize() {
        return SHORT_SIZE;
    }

}
