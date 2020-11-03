package edgedb.protocol.server.reader;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.protocol.common.Header;
import edgedb.protocol.common.HeaderReader;
import edgedb.protocol.server.ErrorResponse;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;

@Data
public class ErrorResponseReader extends BaseReader {

    public ErrorResponseReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public ErrorResponseReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    @Override
    public ErrorResponse read() throws IOException, EdgeDBInternalErrException {
        ErrorResponse error = new ErrorResponse();
        error.setMessageLength(readerHelper.readUint32());
        error.setSeverity(readerHelper.readUint8());

        error.setErrorCode(readerHelper.readUint32());

        error.setMessage(readerHelper.readString());

        short headerAttributeLength = readerHelper.readUint16();
        error.setHeaderAttributeLength(headerAttributeLength);

        Header[] headers = new Header[(int) headerAttributeLength];
        for (int i = 0; i < headerAttributeLength; i++) {
            HeaderReader headerReader = new HeaderReader(dataInputStream, readerHelper);
            headers[i] = headerReader.read();
        }
        error.setHeader(headers);
        return error;
    }
}
