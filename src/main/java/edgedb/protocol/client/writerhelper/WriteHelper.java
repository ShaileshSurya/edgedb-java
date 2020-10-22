package edgedb.protocol.client.writerhelper;

import lombok.AllArgsConstructor;

import java.io.*;

@AllArgsConstructor
public class WriteHelper implements Write {
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
        dataOutputStream.writeInt(str.getBytes().length);
        dataOutputStream.write(str.getBytes(),0,str.getBytes().length);
    }
}
