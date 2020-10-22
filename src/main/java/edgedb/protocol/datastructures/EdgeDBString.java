package edgedb.protocol.datastructures;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class EdgeDBString {
    int stringLength;
    byte[] stringChars;

    public int getStringLength() {
        return stringLength;
    }

    public void setStringLength(int stringLength) {
        this.stringLength = stringLength;
    }

    public byte[] getStringChars() {
        return stringChars;
    }

    public void setStringChars(byte[] stringChars) {
        this.stringChars = stringChars;
    }

    public EdgeDBString(DataInputStream in) throws IOException {
        this.stringLength = in.readInt();
        stringChars = new byte[this.stringLength];
        in.read(stringChars,0,this.stringLength);
    }

    @Override
    public String toString() {
        return "EdgedbString{" +
                "stringLength=" + stringLength +
                ", stringChars=" + new String(stringChars) +
                '}';
    }
}
