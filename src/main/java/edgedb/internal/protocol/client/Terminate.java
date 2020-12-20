package edgedb.internal.protocol.client;

import edgedb.internal.protocol.client.writerV2.BufferWriter;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.MessageType.TERMINATE;

@Data
public class Terminate extends BaseClientProtocol implements BufferWriter {

    byte mType = (int) TERMINATE;
    int messageLength;

    @Override
    public int calculateMessageLength() {
        return new MessageLengthCalculator().calculate(messageLength);
    }

    @Override
    public ByteBuffer write(ByteBuffer destination) throws IOException {
        IWriteHelper helper = new BufferWriterHelper(destination);
        helper.writeUint8(mType);
        helper.writeUint32(messageLength);
        return destination;
    }

    @Override
    public ByteBuffer write(IWriteHelper helper, ByteBuffer destination) throws IOException {
        return null;
    }
}
