package edgedb.internal.protocol.client.writerhelper;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferWriterHelper implements IWriteHelper {
    ByteBuffer writeBuffer;

    public BufferWriterHelper(ByteBuffer writeBuffer){
        writeBuffer.clear();
        this.writeBuffer = writeBuffer;
    }

    @Override
    public void writeUint8(int value) throws IOException {
        writeBuffer.put((byte) value);
    }

    @Override
    public void writeUint32(int value) throws IOException {
        writeBuffer.putInt(value);
    }

    @Override
    public void writeUint16(int value) throws IOException {
        writeBuffer.putShort((short) value);
    }

    @Override
    public void writeString(String str) throws IOException {
        if (str != null) {
            writeBuffer.putInt(str.length());
            writeBuffer.put(str.getBytes());
        }
    }

    @Override
    public void writeBytes(byte[] value) throws IOException {
        if (value != null) {
            writeBuffer.putInt(value.length);
            writeBuffer.put(value);
        }
    }
}
