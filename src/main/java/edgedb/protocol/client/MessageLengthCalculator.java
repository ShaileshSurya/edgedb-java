package edgedb.protocol.client;

public final class MessageLengthCalculator {

    public static final int calculate(int value) {
        return Integer.SIZE / Byte.SIZE;
    }

    public static final int calculate(byte value) {
        return Byte.SIZE/Byte.SIZE;
    }

    public static final int calculate(short value) {
        return Short.SIZE / Byte.SIZE;
    }

    // Size of String is encoded as 4 Byte length + Size of string
    public static final int calculate(String value) {
        if (value != null) {
            int lengthOfString = value.getBytes().length;
            return Integer.SIZE / Byte.SIZE + lengthOfString;
        }
        return 0;
    }

    public final int calculate(byte[] value) {
        if (value != null) {
            int lengthOfByte = value.length;
            return Integer.SIZE / Byte.SIZE + lengthOfByte;
        }
        return 0;
    }
}
