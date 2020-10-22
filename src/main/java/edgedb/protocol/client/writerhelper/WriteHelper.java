package edgedb.protocol.client.writerhelper;

import lombok.AllArgsConstructor;

import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
public class WriteHelper implements WriteHelp {
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
        dataOutputStream.write(str.getBytes(), 0, str.getBytes().length);
    }

    @Override
    public void writeByte(byte[] value) throws IOException {
        dataOutputStream.writeInt(value.length);
        dataOutputStream.write(value, 0, value.length);
    }
}
