package edgedb.internal.protocol;

import edgedb.internal.protocol.client.writerV2.BufferWritable;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class ConnectionParams implements BufferWritable, ClientProtocolBehaviour {
    String name;
    String value;

    @Override
    public int calculateMessageLength() {
        MessageLengthCalculator calculator = new MessageLengthCalculator();
        int length = 0;
        length += calculator.calculate(name);
        length += calculator.calculate(value);
        return length;
    }

    @Override
    public ByteBuffer write(ByteBuffer writeBuf) throws IOException {
        IWriteHelper helper = new BufferWriterHelper(writeBuf);
        return write(helper,writeBuf);
    }

    public ByteBuffer write(IWriteHelper helper,ByteBuffer writeBuf) throws IOException {
        helper.writeString(name);
        helper.writeString(value);
        return writeBuf;
    }
}
