package edgedb.internal.protocol;

import edgedb.internal.protocol.client.writerV2.BufferWritable;
import edgedb.internal.protocol.client.writerhelper.*;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.MessageType.SYNC_MESSAGE;

@Data
@Slf4j
public class SyncMessage implements BufferWritable, ClientProtocolBehaviour {
    byte mType = (int) SYNC_MESSAGE;
    int messageLength;

    public SyncMessage() {
        this.setMessageLength(calculateMessageLength());
    }

    @Override
    public int calculateMessageLength() {
        return new MessageLengthCalculator().calculate(messageLength);
    }

    @Override
    public ByteBuffer write(ByteBuffer destination) throws IOException {
        IWriteHelper helper = new BufferWriterHelper(destination);
        return write(helper,destination);
    }

    public ByteBuffer write(IWriteHelper helper,ByteBuffer writeBuf) throws IOException {
        helper.writeUint8(mType);
        helper.writeUint32(messageLength);
        return writeBuf;
    }
}
