package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.Header;
import edgedb.internal.protocol.ErrorResponse;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class ErrorResponseReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public ErrorResponse read(ByteBuffer buffer) {
        ErrorResponse error = new ErrorResponse();
        int messageLength = readerHelper.readUint32();
        error.setMessageLength(messageLength);
        readerHelper.setMessageLength(messageLength);

        error.setSeverity(readerHelper.readUint8());

        error.setErrorCode(readerHelper.readUint32());

        error.setMessage(readerHelper.readString());

        short headerAttributeLength = readerHelper.readUint16();
        error.setHeaderAttributeLength(headerAttributeLength);

        Header[] headers = new Header[(int) headerAttributeLength];
        HeaderReader headerReader = new HeaderReader(readerHelper);
        for (int i = 0; i < headerAttributeLength; i++) {
            headers[i] = headerReader.read(buffer);
        }
        error.setHeader(headers);
        return error;
    }

}
