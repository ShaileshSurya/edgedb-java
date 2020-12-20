package edgedb.internal.protocol.server.readerhelper;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.utility.TypeSizeHelper;
import lombok.AllArgsConstructor;

import java.nio.ByteBuffer;

@AllArgsConstructor
public class ChannelReaderHelperImpl implements IReaderHelper {
    ByteBuffer readBuffer;

    private int currentReadCount;
    private ReaderHelper readerHelper;
    private int messageLength;

    private static TypeSizeHelper typeSizeHelper;

    public ChannelReaderHelperImpl(ByteBuffer readBuffer) {
        this.readBuffer = readBuffer;
        this.currentReadCount = 0;
        this.messageLength = -1;
    }


    @Override
    public void setMessageLength(int length) {
        messageLength = length;
    }

    @Override
    public byte readUint8() throws OverReadException {
        checkReadCount();
        byte value = readBuffer.get();
        currentReadCount += typeSizeHelper.getByteSize();
        return value;
    }

    @Override
    public int readUint32() throws OverReadException {
        checkReadCount();
        int value = readBuffer.getInt();
        currentReadCount += typeSizeHelper.getByteSize();
        return value;
    }

    @Override
    public short readUint16() throws OverReadException {
        checkReadCount();
        short value = readBuffer.getShort();
        currentReadCount += typeSizeHelper.getByteSize();
        return value;
    }

    @Override
    public String readString() throws OverReadException {
        checkReadCount();
        int length = readBuffer.getInt();
        currentReadCount += typeSizeHelper.getIntSize();

        byte[] stringChar = new byte[length];
        readBuffer.get(stringChar, 0, length);
        currentReadCount += length;
        return new String(stringChar);
    }

    @Override
    public byte[] readUUID() throws OverReadException {
        checkReadCount();
        final int UUID_BYTE_ARRAY_LENGTH = 16;

        byte[] uuid = new byte[UUID_BYTE_ARRAY_LENGTH];
        readBuffer.get(uuid, 0, UUID_BYTE_ARRAY_LENGTH);
        currentReadCount += UUID_BYTE_ARRAY_LENGTH / typeSizeHelper.getByteSize();
        return uuid;
    }

    public Long readUUIDLong() throws OverReadException {
        checkReadCount();
        final int UUID_BYTE_ARRAY_LENGTH = 16;

        Long value = readBuffer.getLong();
        currentReadCount += UUID_BYTE_ARRAY_LENGTH / typeSizeHelper.getByteSize();

        return value;
    }

    public byte[] readBytes() throws OverReadException {
        checkReadCount();
        int length = readBuffer.getInt();
        currentReadCount += typeSizeHelper.getIntSize();

        byte[] array = new byte[length];
        readBuffer.get(array, 0, length);
        currentReadCount += length;
        return array;
    }

    public byte readByte() throws OverReadException {
        checkReadCount();
        byte value = readBuffer.get();
        currentReadCount += typeSizeHelper.getByteSize();
        return value;
    }

    public byte[] read(byte[] destination, int fromIndex, int toIndex) throws OverReadException {
        checkReadCount();
        ByteBuffer value = readBuffer.get(destination, fromIndex, toIndex);
        currentReadCount += toIndex - fromIndex;
        return new byte[value.remaining()];
    }


    // This can go into the abstract base class
    private void checkReadCount() throws OverReadException {
        if (messageLength > 0 && currentReadCount > messageLength) {
            throw new OverReadException();
        }
    }
}
