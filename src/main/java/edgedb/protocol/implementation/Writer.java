package edgedb.protocol.implementation;

import com.github.edgedb.protocol.Interface.Write;


import java.io.DataOutputStream;
import java.io.IOException;

public class Writer implements Write {
    DataOutputStream dataOutputStream;


    @Override
    public void writeUint8(int value) throws IOException {
        dataOutputStream.writeByte(value);
    }

    @Override
    public void writeUint32(int value) throws IOException {
        dataOutputStream.writeInt(value);
    }

    @Override
    public void writeUint16(int value) throws IOException {
        dataOutputStream.writeShort(value);
    }

    @Override
    public void writeString(String str) throws IOException {
        int paramByteArrayLength = str.getBytes().length;
        dataOutputStream.writeInt(paramByteArrayLength);
        dataOutputStream.writeUTF(str);
    }
}
