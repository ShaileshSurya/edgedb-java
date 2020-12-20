package edgedb.internal.protocol.client;

import edgedb.internal.protocol.client.writerV2.BufferWriter;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class ConnectionParams extends BaseClientProtocol implements BufferWriter {
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
