package edgedb.internal.protocol.client;

import edgedb.internal.protocol.client.writerV2.BufferWriter;
import edgedb.internal.protocol.client.writerhelper.BufferWriterHelper;
import edgedb.internal.protocol.client.writerhelper.IWriteHelper;
import edgedb.internal.protocol.common.Header;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.MessageType.PREPARE;

@Data
@Slf4j
public class Prepare extends BaseClientProtocol implements BufferWriter {
    byte mType = PREPARE;
    int messageLength;
    short headersLength;
    Header[] headers;
    byte ioFormat;
    byte expectedCardinality;
    byte[] statementName;
    String command;

    public Prepare(char IOFormat, char expectedCardinality, String command) {
        this.headersLength = (short) 0;
        this.ioFormat = (byte) IOFormat;
        this.expectedCardinality = (byte) expectedCardinality;
        this.statementName = "".getBytes();
        this.command = command;
        this.messageLength = calculateMessageLength();
    }

    @Override
    public int calculateMessageLength() {
        int length = 0;
        MessageLengthCalculator messageLengthCalculator = new MessageLengthCalculator();
        length += messageLengthCalculator.calculate(messageLength);

        length += messageLengthCalculator.calculate(headersLength);
        for (int i = 0; i < headersLength; i++) {
            length += headers[i].calculateMessageLength();
        }

        length += messageLengthCalculator.calculate(ioFormat);
        length += messageLengthCalculator.calculate(expectedCardinality);
        length += messageLengthCalculator.calculate(statementName);
        length += messageLengthCalculator.calculate(command);

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

        helper.writeUint8(ioFormat);
        helper.writeUint8(expectedCardinality);
        helper.writeBytes(statementName);
        helper.writeString(command);
        return destination;
    }
}
