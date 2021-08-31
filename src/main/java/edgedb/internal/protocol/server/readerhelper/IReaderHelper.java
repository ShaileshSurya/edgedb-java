package edgedb.internal.protocol.server.readerhelper;

public interface IReaderHelper {
    public void setMessageLength(int length);

    public byte readUint8();

    public int readUint32();

    public short readUint16();

    public String readString();

    public byte[] readUUID();

    public Long readUUIDLong();

    public byte[] readBytes();

    public byte readByte();

    public byte[] read(byte[] destination, int from, int to);
}
