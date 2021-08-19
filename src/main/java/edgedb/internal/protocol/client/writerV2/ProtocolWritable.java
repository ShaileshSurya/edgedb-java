package edgedb.internal.protocol.client.writerV2;

public interface ProtocolWritable {
    public <T extends BufferWritable> void write(T protocol);
}
