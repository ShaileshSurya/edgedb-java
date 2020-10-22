package edgedb.protocol.client;

public class MessageLengthCalculator {

    public int calculate(int value) {
        return Integer.SIZE / Byte.SIZE;
    }

    public int calculate(byte value) {
        return Integer.SIZE / Byte.SIZE;
    }

    public int calculate(short value) {
        return Short.SIZE / Byte.SIZE;
    }

    // Size of String is encoded as 4 Byte length + Size of string
    public int calculate(String value) {
        int lengthOfString = value.getBytes().length;
        return Integer.SIZE / Byte.SIZE + lengthOfString;
    }
}
