package edgedb.protocol.client.writerhelper;

import java.io.IOException;

public interface WriteHelp {
    public void writeUint8(int value) throws IOException;

    public void writeUint32(int value) throws IOException;

    public void writeUint16(int value) throws IOException;

    public void writeString(String str) throws IOException;

    public void writeByte(byte[] value) throws IOException;
}
