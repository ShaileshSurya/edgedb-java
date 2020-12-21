package edgedb.internal.protocol.server.readerhelper;

import edgedb.exceptions.OverReadException;

import java.io.IOException;

public interface IReaderHelper {
    public void setMessageLength(int length);

    public byte readUint8() throws IOException, OverReadException;

    public int readUint32() throws IOException, OverReadException;

    public short readUint16() throws IOException, OverReadException;

    public String readString() throws IOException, OverReadException;

    public byte[] readUUID() throws OverReadException, IOException;

    public Long readUUIDLong() throws OverReadException, IOException;

    public byte[] readBytes() throws OverReadException, IOException;

    public byte readByte() throws OverReadException, IOException;

    public byte[] read(byte[] destination, int from, int to) throws OverReadException;
}
