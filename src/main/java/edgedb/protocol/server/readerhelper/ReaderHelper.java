package edgedb.protocol.server.readerhelper;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.utility.TypeSizeHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;


@Data
@AllArgsConstructor
public class ReaderHelper implements Read {
    DataInputStream dataInputStream;
    private int currentReadCount;
    private ReaderHelper readerHelper;
    // initialize the message length with -1. Later set it to to appropriate message length
    private int messageLength;

    private static TypeSizeHelper typeSizeHelper;

    public ReaderHelper(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.currentReadCount = 0;
        this.messageLength = -1;
    }


    @Override
    public byte readUint8() throws IOException, OverReadException {
        checkReadCount();
        byte value = dataInputStream.readByte();
        currentReadCount += typeSizeHelper.getByteSize();
        return value;
    }

    @Override
    public int readUint32() throws IOException, OverReadException {
        checkReadCount();
        int value = dataInputStream.readInt();
        currentReadCount += typeSizeHelper.getIntSize();
        return value;
    }

    @Override
    public short readUint16() throws IOException, OverReadException {
        checkReadCount();
        short value = dataInputStream.readShort();
        currentReadCount += typeSizeHelper.getShortSize();
        return value;
    }

    @Override
    public String readString() throws IOException, OverReadException {
        checkReadCount();
        int length = dataInputStream.readInt();
        currentReadCount += typeSizeHelper.getIntSize();

        byte[] stringChar = new byte[length];
        dataInputStream.read(stringChar, 0, length);
        currentReadCount += length;
        return new String(stringChar);
    }

    public byte[] readByteArray() throws OverReadException, IOException {
        checkReadCount();
        int length = dataInputStream.readInt();
        currentReadCount += typeSizeHelper.getIntSize();

        byte[] array = new byte[length];
        dataInputStream.read(array, 0, length);
        currentReadCount += length;
        return array;
    }

    private void checkReadCount() throws OverReadException {
        if (messageLength > 0 && currentReadCount > messageLength) {
            throw new OverReadException();
        }
    }
}
