package edgedb.internal.protocol;

import edgedb.internal.protocol.client.writerV2.BufferWritable;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.ServerProtocolBehaviour;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@Slf4j
public class Header implements BufferWritable, ServerProtocolBehaviour{
    short code;
    byte[] value;

    @Override
    public String toString() {
        return "Header{" +
                "code=" + code +
                ", value=" + new String(value) +
                '}';
    }

    public int calculateMessageLength() {
        int messageLength = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();

        messageLength += calculator.calculate(code);
        messageLength += calculator.calculate(value);

        return messageLength;
    }

    @Override
    public ByteBuffer write(ByteBuffer writeBuf) throws IOException {
        IWriteHelper helper = new BufferWriterHelper(writeBuf);
        return write(helper,writeBuf);
    }

    public ByteBuffer write(IWriteHelper helper,ByteBuffer writeBuf) throws IOException {
        helper.writeUint16(code);
        helper.writeBytes(value);
        return writeBuf;
    }
}
