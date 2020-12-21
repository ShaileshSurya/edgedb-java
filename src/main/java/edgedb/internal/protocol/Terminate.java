package edgedb.internal.protocol;

import edgedb.internal.protocol.client.writerV2.BufferWritable;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.MessageType.TERMINATE;

@Data
public class Terminate implements ClientProtocolBehaviour, BufferWritable {

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
