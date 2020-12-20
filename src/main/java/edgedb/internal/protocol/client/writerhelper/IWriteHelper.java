package edgedb.internal.protocol.client.writerhelper;

import java.io.IOException;

public interface IWriteHelper {
    public void writeUint8(int value) throws IOException;

    public void writeUint32(int value) throws IOException;

    public void writeUint16(int value) throws IOException;

    public void writeString(String str) throws IOException;

    public void writeBytes(byte[] value) throws IOException;
}
