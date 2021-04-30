package edgedb.internal.protocol;

import edgedb.internal.protocol.client.writerV2.BufferWritable;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.utility.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@Slf4j
public class ExecuteScript implements BufferWritable, ClientProtocolBehaviour {
    byte mType = (int) 'Q';
    int messageLength;
    short headersLength;
    Header[] headers;
    String script;

    public ExecuteScript(String script) {
        this.script = script;
    }

    @Override
    public int calculateMessageLength() {
        log.debug("Starting to calculate length of message");
        int length = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();
        length += calculator.calculate(messageLength);
        length += calculator.calculate(headersLength);

        for (int i = 0; i < headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }
        length += calculator.calculate(script);
        return length;
    }

    @Override
    public ByteBuffer write(ByteBuffer destination) throws IOException {
        log.info("Client Handshake Buffer Writer");
        IWriteHelper helper = new BufferWriterHelper(destination);
        return write(helper, destination);
    }

    @Override
    public ByteBuffer write(IWriteHelper helper, ByteBuffer destination) throws IOException {
        helper.writeUint8(mType);
        helper.writeUint32(messageLength);
        helper.writeUint16(headersLength);

        for (int i = 0; i < (int) headersLength; i++) {
            headers[i].write(helper, destination);
        }
        helper.writeString(script);
        return destination;
    }
}
