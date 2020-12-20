package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.ErrorResponse;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class ErrorResponseReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public ErrorResponse read(ByteBuffer buffer) throws IOException {
        ErrorResponse error = new ErrorResponse();
        error.setMessageLength(readerHelper.readUint32());
        error.setSeverity(readerHelper.readUint8());

        error.setErrorCode(readerHelper.readUint32());

        error.setMessage(readerHelper.readString());

        short headerAttributeLength = readerHelper.readUint16();
        error.setHeaderAttributeLength(headerAttributeLength);

        Header[] headers = new Header[(int) headerAttributeLength];
        for (int i = 0; i < headerAttributeLength; i++) {
            HeaderReader headerReader = new HeaderReader(readerHelper);
            headers[i] = headerReader.read(buffer);
        }
        error.setHeader(headers);
        return error;
    }

}
