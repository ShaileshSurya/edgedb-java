package edgedb.protocol.write;

public class Header {
    short code;
    byte[] value;

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
