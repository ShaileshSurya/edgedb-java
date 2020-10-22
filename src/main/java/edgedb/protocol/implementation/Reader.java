package edgedb.protocol.implementation;

import com.github.edgedb.protocol.Interface.Read;


import java.io.DataInputStream;
import java.io.IOException;

public class Reader implements Read {
    DataInputStream dataInputStream;
    @Override
    public byte readUint8() throws IOException {
        return dataInputStream.readByte();
    }

    @Override
    public int readUint32() throws IOException {
        return dataInputStream.readInt();
    }

    @Override
    public short readUint16() throws IOException {
        return dataInputStream.readShort();
    }

    @Override
    public String readString() throws IOException {
        int length = dataInputStream.readInt();
        byte[] stringChar = new byte[length];
        dataInputStream.read(stringChar,0,length);
        return new String(stringChar);
    }
}
