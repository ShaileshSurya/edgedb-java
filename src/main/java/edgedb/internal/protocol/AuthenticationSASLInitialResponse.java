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
public class AuthenticationSASLInitialResponse implements BufferWritable, ClientProtocolBehaviour {
    byte mType = 'p';
    int messageLength;
    String method;
    byte[] saslData;

    public AuthenticationSASLInitialResponse(String method, byte[] saslData){
        this.method = method;
        this.saslData = saslData;
        messageLength = calculateMessageLength();
    }

    @Override
    public int calculateMessageLength() {
        log.debug("Starting to calculate length of message");
        int length = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();

        length += calculator.calculate(messageLength);
        length += calculator.calculate(method);
        length += calculator.calculate(saslData);

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
        helper.writeString(method);
        helper.writeBytes(saslData);
        return destination;
    }
}
