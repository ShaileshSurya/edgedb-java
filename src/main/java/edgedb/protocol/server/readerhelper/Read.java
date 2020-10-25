package edgedb.protocol.server.readerhelper;

import edgedb.exceptions.OverReadException;

import java.io.IOException;

public interface Read {
    public byte readUint8() throws IOException, OverReadException;

    public int readUint32() throws IOException, OverReadException;

    public short readUint16() throws IOException, OverReadException;

    public String readString() throws IOException, OverReadException;

    public byte[] readUUID() throws OverReadException, IOException;
}
