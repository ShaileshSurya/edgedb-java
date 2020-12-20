package edgedb.internal.protocol.client;

import edgedb.internal.protocol.client.writerV2.BufferWriter;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderWriter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@Slf4j
public class Execute extends BaseClientProtocol implements BufferWriter {
    byte mType = (int) 'E';
    int messageLength;
    short headersLength;
    Header[] headers;
    byte[] statementName;
    byte[] arguments;

    public Execute() {
        this.headersLength = (short) 0;
        this.statementName = "".getBytes();
        this.arguments = "".getBytes();
        // Remove this hardcoded value.
        this.messageLength = 18;
    }

    @Override
    public int calculateMessageLength() {
        int length = 0;

        MessageLengthCalculator calculator = new MessageLengthCalculator();
        length += calculator.calculate(messageLength);
        length += calculator.calculate(headersLength);

        for (int i = 0; i < (int) headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }

        length += calculator.calculate(statementName);
        length += calculator.calculate(arguments);
        return length;
    }

    @Override
    public ByteBuffer write(ByteBuffer destination) throws IOException {
        log.info("Client Handshake Buffer Writer");
        IWriteHelper helper = new BufferWriterHelper(destination);
        return write(helper,destination);
    }

    @Override
    public ByteBuffer write(IWriteHelper helper, ByteBuffer destination) throws IOException {
        helper.writeUint8(mType);
        helper.writeUint32(messageLength);

        helper.writeUint16(headersLength);

        for(int i=0; i<(int)headersLength; i++){
            headers[i].write(helper,destination);
        }

        helper.writeBytes("".getBytes());

        //TODO: this needs to be fixed
        helper.writeUint32(4);
        helper.writeUint32(0);
        return destination;
    }
}
